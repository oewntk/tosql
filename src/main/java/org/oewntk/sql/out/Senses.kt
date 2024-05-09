/*
 * Copyright (c) 2024. Bernard Bou.
 */
package org.oewntk.sql.out

import org.oewntk.model.Key
import org.oewntk.model.Key.KeyLCP.Companion.of_t
import org.oewntk.model.Sense
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
     * Make id function, this adds the case-sensitive lemma to make it unique
     */
    private val makeId = { sense: Sense -> sense.senseKey + ' ' + sense.lex.lemma.replace(' ', '_') }

    /**
     * Make sense id-to-nid map
     *
     * @param senses senses
     * @return id-to-nid map
     */
    fun makeSenseNIDs(senses: Collection<Sense>): Map<String, Int> {
        return senses
            .asSequence()
            .map(makeId)
            .distinct()
            .sorted()
            .withIndex()
            .associate { it.value to it.index + 1 }
    }

    /**
     * Generate senses table
     *
     * @param ps                  print stream
     * @param senses              senses
     * @param synsetIdToNIDMap    id-to-nid map for synsets
     * @param lexKeyToNIDMap      key-to-nid map for lexes
     * @param wordIdToNIDMap      id-to-nid map for words
     * @param casedWordIdToNIDMap id-to-nid map for cased words
     * @return senses id-to-nid map
     */
    fun generateSenses(
        ps: PrintStream,
        senses: Collection<Sense>,
        synsetIdToNIDMap: Map<String, Int>,
        lexKeyToNIDMap: Map<Key, Int>,
        wordIdToNIDMap: Map<String, Int>,
        casedWordIdToNIDMap: Map<String, Int>,
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
            val lex = sense.lex
            val casedWord = lex.lemma
            val word = lex.lCLemma
            val synsetId = sense.synsetId
            val sensekey = sense.senseKey
            val senseNum = sense.lexIndex + 1
            val lexid = sense.findLexid()
            val tagCount = sense.tagCount
            val wordNID = NIDMaps.lookupLC(wordIdToNIDMap, word)
            val synsetNID = NIDMaps.lookup(synsetIdToNIDMap, synsetId)
            val lexNID = NIDMaps.lookup(lexKeyToNIDMap, of_t(lex))
            val casedWordNID = NIDMaps.lookupNullable(casedWordIdToNIDMap, casedWord)
            val tagCnt = tagCount?.count?.toString() ?: "NULL"
            "'${escape(sensekey)}',$senseNum,$synsetNID,$lexNID,$wordNID,$casedWordNID,$lexid,$tagCnt"
        }
        if (!Printers.WITH_COMMENT) {
            printInsert(ps, Names.SENSES.TABLE, columns, senses, makeId, idToNID, toSqlRow)
        } else {
            val toSqlRowWithComment = { sense: Sense -> toSqlRow.invoke(sense) to "${sense.senseKey} ${sense.synsetId} '${sense.lex.lemma}'" }
            printInsertWithComment(ps, Names.SENSES.TABLE, columns, senses, makeId, idToNID, toSqlRowWithComment)
        }
        return idToNID
    }

    /**
     * Generate sense relations
     *
     * @param ps               print stream
     * @param senses           senses
     * @param sensesById       senses by id
     * @param synsetIdToNIDMap id-to-nid map for synsets
     * @param lexKeyToNIDMap   key-to-nid map for lexes
     * @param wordIdToNIDMap   id-to-nid map for words
     */
    fun generateSenseRelations(
        ps: PrintStream,
        senses: Collection<Sense>,
        sensesById: Map<String, Sense>,
        synsetIdToNIDMap: Map<String, Int>,
        lexKeyToNIDMap: Map<Key, Int>,
        wordIdToNIDMap: Map<String, Int>,
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
        val toSqlRows = { sense: Sense ->
            val rows = ArrayList<String>()
            val synsetId1 = sense.synsetId
            val lex1 = sense.lex
            val word1 = lex1.lCLemma
            val lu1NID = NIDMaps.lookup(lexKeyToNIDMap, of_t(lex1))
            val wordNID1 = NIDMaps.lookupLC(wordIdToNIDMap, word1)
            val synsetNID1 = NIDMaps.lookup(synsetIdToNIDMap, synsetId1)
            if (sense.relations != null) {
                for (relation in sense.relations!!.keys) {
                    require(BuiltIn.OEWN_RELATION_TYPES.containsKey(relation)) { relation }
                    val relationId = BuiltIn.OEWN_RELATION_TYPES[relation]!!
                    for (senseId2 in sense.relations!![relation]!!) {
                        val sense2 = sensesById[senseId2]
                        val synsetId2 = sense2!!.synsetId
                        val lex2 = sense2.lex
                        val word2 = lex2.lCLemma
                        val lu2NID = NIDMaps.lookup(lexKeyToNIDMap, of_t(lex2))
                        val wordNID2 = NIDMaps.lookupLC(wordIdToNIDMap, word2)
                        val synsetNID2 = NIDMaps.lookup(synsetIdToNIDMap, synsetId2)
                        rows.add("$synsetNID1,$lu1NID,$wordNID1,$synsetNID2,$lu2NID,$wordNID2,$relationId")
                    }
                }
            }
            rows
        }
        if (!Printers.WITH_COMMENT) {
            printInserts(ps, Names.LEXRELATIONS.TABLE, columns, senseSeq, toSqlRows, false)
        } else {
            val toSqlRowsWithComments = { sense: Sense ->
                val data = toSqlRows.invoke(sense)

                val result = ArrayList<Pair<String, String>>()
                val synsetId1 = sense.synsetId
                val lex1 = sense.lex
                val casedword1 = lex1.lemma

                if (sense.relations != null) {
                    var i = 0
                    for (relation in sense.relations!!.keys) {
                        for (senseId2 in sense.relations!![relation]!!) {
                            val sense2 = sensesById[senseId2]
                            val synsetId2 = sense2!!.synsetId
                            val lex2 = sense2.lex
                            val casedword2 = lex2.lemma
                            result.add(data[i] to "$synsetId1 '$casedword1' -$relation-> $synsetId2 '$casedword2'")
                            i++
                        }
                    }
                }
                result.asSequence()
            }
            printInsertsWithComment(ps, Names.LEXRELATIONS.TABLE, columns, senseSeq, toSqlRowsWithComments, false)
        }
    }

    /**
     * Generate senses to adj position
     *
     * @param ps               print stream
     * @param senses           senses
     * @param synsetIdToNIDMap id-to-nid map for synsets
     * @param lexKeyToNIDMap   key-to-nid map for lexes
     * @param wordIdToNIDMap   id-to-nid map for words
     */
    fun generateSensesAdjPositions(
        ps: PrintStream,
        senses: Collection<Sense>,
        synsetIdToNIDMap: Map<String, Int>,
        lexKeyToNIDMap: Map<Key, Int>,
        wordIdToNIDMap: Map<String, Int>,
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
            val lex = sense.lex
            val word = lex.lCLemma
            val synsetNID = NIDMaps.lookup(synsetIdToNIDMap, synsetId)
            val luNID = NIDMaps.lookup(lexKeyToNIDMap, of_t(lex))
            val wordNID = NIDMaps.lookupLC(wordIdToNIDMap, word)
            "$synsetNID,$luNID,$wordNID,'${sense.adjPosition}'"
        }
        if (!Printers.WITH_COMMENT) {
            printInsert(ps, Names.SENSES_ADJPOSITIONS.TABLE, columns, senseSeq, toSqlRow, false)
        } else {
            val toSqlRowWithComment = { sense: Sense -> toSqlRow.invoke(sense) to sense.senseKey }
            printInsertWithComment(ps, Names.SENSES_ADJPOSITIONS.TABLE, columns, senseSeq, toSqlRowWithComment, false)
        }
    }

    /**
     * Generate senses to verb frames
     *
     * @param ps               print stream
     * @param senses           senses
     * @param synsetIdToNIDMap id-to-nid map for synsets
     * @param lexKeyToNIDMap   key-to-nid map for lexes
     * @param wordIdToNIDMap   id-to-nid map for words
     */
    fun generateSensesVerbFrames(
        ps: PrintStream,
        senses: Collection<Sense>,
        synsetIdToNIDMap: Map<String, Int>,
        lexKeyToNIDMap: Map<Key, Int>,
        wordIdToNIDMap: Map<String, Int>,
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
            val synsetId = sense.synsetId
            val word = sense.lCLemma
            val synsetNID = NIDMaps.lookup(synsetIdToNIDMap, synsetId)
            val wordNID = NIDMaps.lookupLC(wordIdToNIDMap, word)
            val lex = sense.lex
            val luNID = NIDMaps.lookup(lexKeyToNIDMap, of_t(lex))

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
                val comments = generateSequence { sense.senseKey }
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
     * @param sensesById       senses by id
     * @param synsetIdToNIDMap id-to-nid map for synsets
     * @param lexKeyToNIDMap   key-to-nid map for lexes
     * @param wordIdToNIDMap   id-to-nid map for words
     */
    fun generateSensesVerbTemplates(
        ps: PrintStream,
        sensesById: Map<String, Sense>,
        synsetIdToNIDMap: Map<String, Int>,
        lexKeyToNIDMap: Map<Key, Int>,
        wordIdToNIDMap: Map<String, Int>,
    ) {
        // sequence of senses
        val senseSeq = sensesById.values
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
            val strings = ArrayList<String>()
            val synsetId = sense.synsetId
            val word = sense.lCLemma
            val synsetNID = NIDMaps.lookup(synsetIdToNIDMap, synsetId)
            val wordNID = NIDMaps.lookupLC(wordIdToNIDMap, word)
            val lex = sense.lex
            val luNID = NIDMaps.lookup(lexKeyToNIDMap, of_t(lex))
            sense.verbTemplates!!
                .map { "$synsetNID,$luNID,$wordNID,$it" }
                .toList()
        }
        if (!Printers.WITH_COMMENT) {
            printInserts(ps, Names.SENSES_VTEMPLATES.TABLE, columns, senseSeq, toSqlRows, false)
        } else {
            val toSqlRowsWithComments = { sense: Sense ->
                val rows = toSqlRows.invoke(sense)
                val comments = generateSequence { sense.senseKey }
                rows
                    .asSequence()
                    .zip(comments)
            }
            printInsertsWithComment(ps, Names.SENSES_VTEMPLATES.TABLE, columns, senseSeq, toSqlRowsWithComments, false)
        }
    }
}
