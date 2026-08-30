/*
 * Copyright (c) 2024. Bernard Bou.
 */

package org.oewntk.sql.out

import org.oewntk.model.*
import org.oewntk.model.NIDs.lookup
import org.oewntk.model.NIDs.lookupLC
import org.oewntk.model.NIDs.lookupNullable
import org.oewntk.model.NIDs.makeSenseNIDs
import org.oewntk.sql.out.Printers.printInsert
import org.oewntk.sql.out.Printers.printInsertWithComment
import org.oewntk.sql.out.Printers.printInserts
import org.oewntk.sql.out.Printers.printInsertsWithComment
import org.oewntk.sql.out.Utils.escape
import java.io.PrintStream

/**
 * Process senses
 */
object Senses {

    /**
     * Generate senses table
     *
     * @param ps                  print stream
     * @param senses              senses
     * @param synsetIdToNIDMap    id-to-nid map for synsets
     * @param lexIdToNIDMap      key-to-nid map for lexes
     * @param wordIdToNIDMap      id-to-nid map for words
     * @param casedWordIdToNIDMap id-to-nid map for cased words
     * @return senses id-to-nid map
     */
    fun generateSenses(
        ps: PrintStream,
        senses: Collection<Sense>,
        synsetIdToNIDMap: Map<SynsetId, Int>,
        lexIdToNIDMap: Map<LexId, Int>,
        wordIdToNIDMap: Map<Lemma, Int>,
        casedWordIdToNIDMap: Map<LowerCasedLemma, Int>,
    ): Map<String, Int> {

        // make sensekey␣lemma-to-nid map
        val idToNID = makeSenseNIDs(senses)

        // insert map
        val columns = listOf(
            Names.SENSES.senseid,
            Names.SENSES.sensekey,
            Names.SENSES.sensenum,
            Names.SENSES.synsetid,
            Names.SENSES.luid,
            Names.SENSES.wordid,
            Names.SENSES.casedwordid,
            Names.SENSES.lexid,
            Names.SENSES.tagcount
        ).joinToString(",")
        val toSqlRow = { sense: Sense ->
            val casedWord = sense.lemma
            val word = sense.lCLemma
            val synsetId = sense.synsetId
            val lexId = sense.lexId
            val sensekey = sense.senseKey
            val senseNum = sense.indexInLex + 1
            val lexid = sense.findLexid()
            val tagCount = sense.tagCount
            val wordNID = lookupLC(wordIdToNIDMap, word)
            val synsetNID = lookup(synsetIdToNIDMap, synsetId)
            val lexNID = lookup(lexIdToNIDMap, lexId)
            val casedWordNID = lookupNullable(casedWordIdToNIDMap, casedWord)
            val tagCnt = tagCount?.toString() ?: "NULL"
            "'${escape(sensekey.id)}',$senseNum,$synsetNID,$lexNID,$wordNID,$casedWordNID,$lexid,$tagCnt"
        }
        if (!Printers.WITH_COMMENT) {
            printInsert(ps, Names.SENSES.TABLE, columns, senses, Sense::uniqueId, idToNID, toSqlRow)
        } else {
            val toSqlRowWithComment = { sense: Sense -> toSqlRow.invoke(sense) to "${sense.senseKey} ${sense.synsetId} '${sense.lemma}'" }
            printInsertWithComment(ps, Names.SENSES.TABLE, columns, senses, Sense::uniqueId, idToNID, toSqlRowWithComment)
        }
        return idToNID
    }

    /**
     * Generate sense relations
     *
     * @param ps               print stream
     * @param senses           senses
     * @param senseResolver    sense resolver
     * @param synsetIdToNIDMap id-to-nid map for synsets
     * @param lexIdToNIDMap    id-to-nid map for lexes
     * @param wordIdToNIDMap   id-to-nid map for words
     */
    fun generateSenseRelations(
        ps: PrintStream,
        senses: Collection<Sense>,
        senseResolver: (SenseKey) -> Sense,
        synsetIdToNIDMap: Map<SynsetId, Int>,
        lexIdToNIDMap: Map<LexId, Int>,
        wordIdToNIDMap: Map<Lemma, Int>,
    ) {
        // sequence of senses
        val senseSeq = senses
            .asSequence()
            .filter { !it.relations.isNullOrEmpty() }
            .sortedBy(Sense::senseKey)

        // insert map
        val columns = listOf(
            Names.LEXRELATIONS.synset1id,
            Names.LEXRELATIONS.lu1id,
            Names.LEXRELATIONS.word1id,
            Names.LEXRELATIONS.synset2id,
            Names.LEXRELATIONS.lu2id,
            Names.LEXRELATIONS.word2id,
            Names.LEXRELATIONS.relationid
        ).joinToString(",")

        val toTargetData = { sense: Sense ->
            sense.relations!!.keys
                .asSequence()
                .onEach { require(BuiltIn.OEWN_RELATION_TYPES.containsKey(it.id)) { it } } // relation type
                .flatMap {
                    val relation: Relation = it
                    val relationNID: Int = BuiltIn.OEWN_RELATION_TYPES[it.id]!! // relation NID
                    sense.relations!![it]!!
                        .asSequence() // sequence of target ids
                        .map { targetId ->
                            val sense2 = senseResolver(targetId)
                            (relation to relationNID) to sense2
                        }
                } // sequence of ((relation, relationNID), sense2_1) ((relation, relationNID), sense2_2) ...
                .sortedWith(
                    Comparator
                        .comparingInt { data: Pair<Pair<Relation, Int>, Sense> -> data.first.second } // relation NID
                        .thenComparing { data -> data.second.lemma } //  lemma2
                        .thenComparing { data -> data.second.senseKey } //  sensekey2
                )
        }

        val toSqlRows = { sense: Sense ->
            val lu1NID = lookup(lexIdToNIDMap, sense.lexId)
            val word1NID = lookupLC(wordIdToNIDMap, sense.lCLemma)
            val synset1NID = lookup(synsetIdToNIDMap, sense.synsetId)
            toTargetData(sense) // sequence of ((relation, relationNID), sense2_1) ((relation, relationNID), sense2_2) ...
                .map {
                    val sense2 = it.second
                    val word2 = sense2.lCLemma
                    val synsetId2 = sense2.synsetId

                    val lu2NID = lookup(lexIdToNIDMap, sense2.lexId)
                    val word2NID = lookupLC(wordIdToNIDMap, word2)
                    val synset2NID = lookup(synsetIdToNIDMap, synsetId2)
                    val relationNID: Int = BuiltIn.OEWN_RELATION_TYPES[it.first.first.id]!! // relation
                    "$synset1NID,$lu1NID,$word1NID,$synset2NID,$lu2NID,$word2NID,$relationNID"
                }
                .toList()
        }

        if (!Printers.WITH_COMMENT) {
            printInserts(ps, Names.LEXRELATIONS.TABLE, columns, senseSeq, toSqlRows, false)
        } else {
            val toSqlRowsWithComments = { sense: Sense ->
                val rows = toSqlRows.invoke(sense)

                val synsetId1 = sense.synsetId
                val word1 = sense.lemma
                val comments = toTargetData(sense) // sequence of ((relation, relationNID), sense2_1) ((relation, relationNID), sense2_2) ...
                    .map {
                        val relation = it.first.first
                        val sense2 = it.second
                        val word2 = sense2.lCLemma
                        val synsetId2 = sense2.synsetId
                        "$synsetId1 '$word1' -$relation-> $synsetId2 '$word2'"
                    }
                rows
                    .asSequence()
                    .zip(comments)
            }
            printInsertsWithComment(ps, Names.LEXRELATIONS.TABLE, columns, senseSeq, toSqlRowsWithComments, false)
        }
    }

    /**
     * Generate sense samples
     *
     * @param ps               print stream
     * @param senses           senses
     * @param synsetIdToNIDMap id-to-nid map for synsets
     * @param lexIdToNIDMap    id-to-nid map for lexes
     * @param wordIdToNIDMap   id-to-nid map for words
     */
    fun generateSensesSamples(
        ps: PrintStream,
        senses: Collection<Sense>,
        synsetIdToNIDMap: Map<SynsetId, Int>,
        lexIdToNIDMap: Map<LexId, Int>,
        wordIdToNIDMap: Map<Lemma, Int>,
    ) {
        // val sensesCountWithExamples = senses.count { !it.examples.isNullOrEmpty() }
        // val exampleCount = senses.asSequence().filter { !it.examples.isNullOrEmpty() }.flatMap { it.examples!! }.count()

        // sequence of senses
        val senseSeq = senses
            .asSequence()
            .filter { !it.examples.isNullOrEmpty() }
            .sortedBy(Sense::senseKey)

        // insert
        val columns = listOf(
            Names.SAMPLES.sampleid,
            Names.SAMPLES.synsetid,
            Names.SAMPLES.luid,
            Names.SAMPLES.wordid,
            Names.SAMPLES.sample,
            Names.SAMPLES.source
        ).joinToString(",")
        val toSqlRows = { sense: Sense ->
            val synsetNID1 = lookup(synsetIdToNIDMap, sense.synsetId)
            val lexNID1 = lookup(lexIdToNIDMap, sense.lexId)
            val wordNID1 = lookup(wordIdToNIDMap, sense.lCLemma)
            sense.examples!!
                .map {
                    val text = escape(it.text)
                    val source = if (it.source == null) "NULL" else "'${escape(it.source!!)}'"
                    "$synsetNID1,$lexNID1,$wordNID1,'$text',$source"
                }
                .toList()
        }
        printInserts(ps, Names.SAMPLES.TABLE, columns, senseSeq, toSqlRows, true)
    }

    /**
     * Generate senses to adj position
     *
     * @param ps               print stream
     * @param senses           senses
     * @param synsetIdToNIDMap id-to-nid map for synsets
     * @param lexIdToNIDMap    id-to-nid map for lexes
     * @param wordIdToNIDMap   id-to-nid map for words
     */
    fun generateSensesAdjPositions(
        ps: PrintStream,
        senses: Collection<Sense>,
        synsetIdToNIDMap: Map<SynsetId, Int>,
        lexIdToNIDMap: Map<LexId, Int>,
        wordIdToNIDMap: Map<Lemma, Int>,
    ) {
        // sequence of senses
        val senseSeq = senses
            .asSequence()
            .filter { it.adjPosition != null }
            .sortedBy(Sense::senseKey)

        // insert map
        val columns = listOf(
            Names.SENSES_ADJPOSITIONS.synsetid,
            Names.SENSES_ADJPOSITIONS.luid,
            Names.SENSES_ADJPOSITIONS.wordid,
            Names.SENSES_ADJPOSITIONS.positionid
        ).joinToString(",")
        val toSqlRow = { sense: Sense ->
            val synsetId = sense.synsetId
            val word = sense.lCLemma
            val synsetNID = lookup(synsetIdToNIDMap, synsetId)
            val luNID = lookup(lexIdToNIDMap, sense.lexId)
            val wordNID = lookupLC(wordIdToNIDMap, word)
            "$synsetNID,$luNID,$wordNID,'${sense.adjPosition}'"
        }
        if (!Printers.WITH_COMMENT) {
            printInsert(ps, Names.SENSES_ADJPOSITIONS.TABLE, columns, senseSeq, toSqlRow, false)
        } else {
            val toSqlRowWithComment = { sense: Sense -> toSqlRow.invoke(sense) to sense.senseKey.id }
            printInsertWithComment(ps, Names.SENSES_ADJPOSITIONS.TABLE, columns, senseSeq, toSqlRowWithComment, false)
        }
    }

    /**
     * Generate senses to verb frames
     *
     * @param ps               print stream
     * @param senses           senses
     * @param synsetIdToNIDMap id-to-nid map for synsets
     * @param lexIdToNIDMap    id-to-nid map for lexes
     * @param wordIdToNIDMap   id-to-nid map for words
     */
    fun generateSensesVerbFrames(
        ps: PrintStream,
        senses: Collection<Sense>,
        synsetIdToNIDMap: Map<SynsetId, Int>,
        lexIdToNIDMap: Map<LexId, Int>,
        wordIdToNIDMap: Map<Lemma, Int>,
    ) {
        // sequence of senses
        val senseSeq = senses
            .asSequence()
            .filter { !it.verbFrames.isNullOrEmpty() }
            .sortedBy(Sense::senseKey)

        // insert map
        val columns = listOf(
            Names.SENSES_VFRAMES.synsetid,
            Names.SENSES_VFRAMES.luid,
            Names.SENSES_VFRAMES.wordid,
            Names.SENSES_VFRAMES.frameid
        ).joinToString(",")

        val toSqlRows = { sense: Sense ->
            val synsetNID = lookup(synsetIdToNIDMap, sense.synsetId)
            val wordNID = lookupLC(wordIdToNIDMap, sense.lCLemma)
            val luNID = lookup(lexIdToNIDMap, sense.lexId)
            sense.verbFrames!!
                .map {
                    val verbFrameNID = VerbFrames.VERB_FRAME_ID_TO_NIDS[it]!!
                    "$synsetNID,$luNID,$wordNID,$verbFrameNID"
                }
                .toList()
        }
        if (!Printers.WITH_COMMENT) {
            printInserts(ps, Names.SENSES_VFRAMES.TABLE, columns, senseSeq, toSqlRows, false)
        } else {
            val toSqlRowsWithComments = { sense: Sense ->
                val rows = toSqlRows.invoke(sense)
                val comments = generateSequence { sense.senseKey.id }
                rows
                    .asSequence()
                    .zip(comments)
            }
            printInsertsWithComment(ps, Names.SENSES_VFRAMES.TABLE, columns, senseSeq, toSqlRowsWithComments, false)
        }
    }

    /**
     * Generate senses to verb templates
     *
     * @param ps               print stream
     * @param senses           senses
     * @param synsetIdToNIDMap id-to-nid map for synsets
     * @param lexIdToNIDMap    id-to-nid map for lexes
     * @param wordIdToNIDMap   id-to-nid map for words
     */
    fun generateSensesVerbTemplates(
        ps: PrintStream,
        senses: Collection<Sense>,
        synsetIdToNIDMap: Map<SynsetId, Int>,
        lexIdToNIDMap: Map<LexId, Int>,
        wordIdToNIDMap: Map<Lemma, Int>,
    ) {
        // sequence of senses
        val senseSeq = senses
            .asSequence()
            .filter { !it.verbTemplates.isNullOrEmpty() }
            .sortedBy(Sense::senseKey)

        // insert map
        val columns = arrayOf(
            Names.SENSES_VTEMPLATES.synsetid,
            Names.SENSES_VTEMPLATES.luid,
            Names.SENSES_VTEMPLATES.wordid,
            Names.SENSES_VTEMPLATES.templateid
        ).joinToString(",")

        val toSqlRows = { sense: Sense ->
            val synsetNID = lookup(synsetIdToNIDMap, sense.synsetId)
            val wordNID = lookupLC(wordIdToNIDMap, sense.lCLemma)
            val luNID = lookup(lexIdToNIDMap, sense.lexId)
            sense.verbTemplates!!
                .map { "$synsetNID,$luNID,$wordNID,$it" }
                .toList()
        }
        if (!Printers.WITH_COMMENT) {
            printInserts(ps, Names.SENSES_VTEMPLATES.TABLE, columns, senseSeq, toSqlRows, false)
        } else {
            val toSqlRowsWithComments = { sense: Sense ->
                val rows = toSqlRows.invoke(sense)
                val comments = generateSequence { sense.senseKey.id }
                rows
                    .asSequence()
                    .zip(comments)
            }
            printInsertsWithComment(ps, Names.SENSES_VTEMPLATES.TABLE, columns, senseSeq, toSqlRowsWithComments, false)
        }
    }
}
