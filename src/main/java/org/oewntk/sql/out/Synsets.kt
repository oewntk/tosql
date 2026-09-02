/*
 * Copyright (c) 2024. Bernard Bou.
 */
package org.oewntk.sql.out

import org.oewntk.model.*
import org.oewntk.model.NIDs.lookup
import org.oewntk.model.NIDs.lookupLC
import org.oewntk.model.NIDs.makeSynsetNIDs
import org.oewntk.sql.out.Printers.printInsert
import org.oewntk.sql.out.Printers.printInsertWithComment
import org.oewntk.sql.out.Printers.printInserts
import org.oewntk.sql.out.Printers.printInsertsWithComment
import org.oewntk.sql.out.Utils.escape
import java.io.PrintStream

/**
 * Process synsets
 */
object Synsets {

    /**
     * Generate synsets table
     *
     * @param ps      print stream
     * @param synsets synsets
     * @return synsets id-to-nid map
     */
    fun generateSynsets(ps: PrintStream, synsets: Collection<Synset>): Map<SynsetId, Int> {
        // make synsetId-to-nid map
        val synsetIdToNID = makeSynsetNIDs(synsets)
        val resolver = { synset: Synset -> lookup(synsetIdToNID, synset.synsetId) }

        // insert map
        val columns = listOf(
            Names.SYNSETS.synsetid,
            Names.SYNSETS.posid,
            Names.SYNSETS.domainid,
            Names.SYNSETS.definition
        ).joinToString(",")

        val toSqlRow = { synset: Synset ->
            val type = synset.type
            val definition = synset.definition
            val domain = synset.lexfile
            val lexdomainId = BuiltIn.LEXFILE_NIDS[domain]!!
            "'${type.value}',$lexdomainId,'${escape(definition!!)}'"
        }
        if (!Printers.WITH_COMMENT) {
            printInsert(ps, Names.SYNSETS.TABLE, columns, synsets, resolver, toSqlRow)
        } else {
            val toSqlRowWithComment = { synset: Synset -> toSqlRow.invoke(synset) to synset.synsetId.id }
            printInsertWithComment(ps, Names.SYNSETS.TABLE, columns, synsets, resolver, toSqlRowWithComment)
        }
        return synsetIdToNID
    }

    private data class RelationData(
        val relation: Relation,
        val relationNid: Int,
        val targetSynsetId: SynsetId,
        val targetLexId: LexId?
    )

    /**
     * Generate synset relations table
     *
     * @param ps               print stream
     * @param synsets          synsets
     * @param synsetIdToNIDMap id-to-nid map
     */
    fun generateSynsetRelations(
        ps: PrintStream,
        synsets: Collection<Synset>,
        senseResolver: (SenseKey) -> Sense,
        synsetResolver: (SynsetId) -> Synset,
        synsetIdToNIDMap: Map<SynsetId, Int>,
        lexIdToNIDMap: Map<LexId, Int>,
        wordIdToNIDMap: Map<Lemma, Int>,
    ) {

        // synset sequence
        val synsetSeq = synsets
            .asSequence()
            .filter { !it.relations.isNullOrEmpty() }
            .sortedBy(Synset::synsetId)

        // insert
        val columns = listOf(
            Names.SEMRELATIONS.synset1id,
            Names.SEMRELATIONS.synset2id,
            Names.SEMRELATIONS.lu2id,
            Names.SEMRELATIONS.word2id,
            Names.SEMRELATIONS.relationid
        ).joinToString(",")

        val toTargetData = { synset: Synset ->
            synset.relations!!.keys
                .asSequence()
                .onEach { require(BuiltIn.OEWN_RELATION_TYPES.containsKey(it.id)) { it } } // relation type
                .flatMap {
                    val relation: Relation = it
                    val relationNID: Int = BuiltIn.OEWN_RELATION_TYPES[it.id]!! // relation NID
                    synset.relations!![it]!!
                        .asSequence() // sequence of target ids
                        .map { targetId ->
                            if (targetId.targetsSynset) {
                                val synset2 = synsetResolver(targetId.synsetId)
                                RelationData(relation, relationNID, synset2.synsetId, null)
                            } else {
                                val sense2 = senseResolver(targetId.senseKey)
                                RelationData(relation, relationNID, sense2.synsetId, sense2.lexId)
                            }
                        }
                } // sequence of ((relation, relationNID), synset2Id_1) ((relation, relationNID, synset2Id_2) ...
                .sortedWith(
                    Comparator
                        .comparingInt { data: Synsets.RelationData -> data.relationNid }
                        .thenComparing { data -> data.targetSynsetId }
                )
        }

        val toSqlRows = { synset: Synset ->
            val synset1NID = lookup(synsetIdToNIDMap, synset.synsetId)
            toTargetData(synset) // sequence of ((relation, relationNID), synset2Id_1) ((relation, relationNID, synset2Id_2) ...
                .map { data ->
                    val lu2NID = if (data.targetLexId != null) lookup(lexIdToNIDMap, data.targetLexId) else "NULL"
                    val word2NID = if (data.targetLexId != null) lookupLC(wordIdToNIDMap, data.targetLexId.lemma.lCLemma) else "NULL"
                    val synset2NID = lookup(synsetIdToNIDMap, data.targetSynsetId)
                    val relationNID: Int = BuiltIn.OEWN_RELATION_TYPES[data.relation.id]!! // relation
                    "$synset1NID,$synset2NID,$lu2NID,$word2NID,$relationNID"
                }
                .toList()
        }

        if (!Printers.WITH_COMMENT) {
            printInserts(ps, Names.SEMRELATIONS.TABLE, columns, synsetSeq, toSqlRows, false)
        } else {
            val toSqlRowsWithComments = { synset: Synset ->
                val rows = toSqlRows.invoke(synset)

                val comments = toTargetData(synset) // sequence of ((relation, relationNID), synset2Id_1) ((relation, relationNID, synset2Id_2) ...
                    .map {
                        val word2 = it.targetLexId?.lemma
                        "${synset.synsetId} -${it.relation}-> ${it.targetSynsetId}${if (word2 != null) " '$word2'" else ""}"
                    }
                rows
                    .asSequence()
                    .zip(comments)
            }
            printInsertsWithComment(ps, Names.SEMRELATIONS.TABLE, columns, synsetSeq, toSqlRowsWithComments, false)
        }
    }

    /**
     * Generate samples table
     *
     * @param ps               print stream
     * @param synsets          synsets
     * @param synsetIdToNIDMap id-to-nid map
     */
    fun generateSynsetSamples(ps: PrintStream, synsets: Collection<Synset>, synsetIdToNIDMap: Map<SynsetId, Int>) {

        // sequence of synsets
        // val synsetCountWithExamples = synsets.count { !it.examples.isNullOrEmpty() }
        // val exampleCount = synsets.asSequence().filter { !it.examples.isNullOrEmpty() }.flatMap { it.examples!! }.count()

        val synsetSeq = synsets
            .asSequence()
            .filter { !it.examples.isNullOrEmpty() }
            .sortedBy(Synset::synsetId)

        // insert
        val columns = listOf(
            Names.SAMPLES.sampleid,
            Names.SAMPLES.synsetid,
            Names.SAMPLES.luid,
            Names.SAMPLES.wordid,
            Names.SAMPLES.sample,
            Names.SAMPLES.source
        ).joinToString(",")
        val toSqlRows = { synset: Synset ->
            val synsetNID1 = lookup(synsetIdToNIDMap, synset.synsetId)
            synset.examples!!
                .map {
                    val text = escape(it.text)
                    val source = if (it.source == null) "NULL" else "'${escape(it.source!!)}'"
                    "$synsetNID1,NULL,NULL,'$text',$source"
                }
                .toList()
        }
        printInserts(ps, Names.SAMPLES.TABLE, columns, synsetSeq, toSqlRows, true)
    }

    /**
     * Generate usages table
     *
     * @param ps               print stream
     * @param synsets          synsets
     * @param synsetIdToNIDMap id-to-nid map
     */
    fun generateSynsetUsages(ps: PrintStream, synsets: Collection<Synset>, synsetIdToNIDMap: Map<SynsetId, Int>) {

        // sequence of synsets
        val synsetSeq = synsets
            .asSequence()
            .filter { !it.usages.isNullOrEmpty() }
            .sortedBy(Synset::synsetId)

        // insert
        val columns = listOf(
            Names.USAGES.usageid,
            Names.USAGES.synsetid,
            Names.USAGES.luid,
            Names.USAGES.wordid,
            Names.USAGES.usagenote,
        ).joinToString(",")
        val toSqlRows = { synset: Synset ->
            val synsetNID1 = lookup(synsetIdToNIDMap, synset.synsetId)
            synset.usages!!
                .map {
                    val usage = escape(it)
                    "$synsetNID1,NULL,NULL,'$usage'"
                }
                .toList()
        }
        printInserts(ps, Names.USAGES.TABLE, columns, synsetSeq, toSqlRows, true)
    }

    /**
     * Generate ilis table
     *
     * @param ps               print stream
     * @param synsets          synsets
     * @param synsetIdToNIDMap id-to-nid map
     */
    fun generateSynsetIlis(ps: PrintStream, synsets: Collection<Synset>, synsetIdToNIDMap: Map<SynsetId, Int>) {

        // sequence of synsets
        val synsetSeq = synsets
            .asSequence()
            .filter { !it.ili.isNullOrEmpty() }
            .sortedBy(Synset::synsetId)

        // insert
        val columns = listOf(
            Names.ILIS.synsetid,
            Names.ILIS.ili,
        ).joinToString(",")
        val toSqlRows = { synset: Synset ->
            val synsetNID1 = lookup(synsetIdToNIDMap, synset.synsetId)
            "$synsetNID1,'${synset.ili}'"
        }
        printInsert(ps, Names.ILIS.TABLE, columns, synsetSeq, toSqlRows, false)
    }

    /**
     * Generate wikidatas table
     *
     * @param ps               print stream
     * @param synsets          synsets
     * @param synsetIdToNIDMap id-to-nid map
     */
    fun generateSynsetWikidatas(ps: PrintStream, synsets: Collection<Synset>, synsetIdToNIDMap: Map<SynsetId, Int>) {

        // sequence of synsets
        val synsetSeq = synsets
            .asSequence()
            .filter { !it.wikidata.isNullOrEmpty() }
            .sortedBy(Synset::synsetId)

        // insert
        val columns = listOf(
            Names.WIKIDATAS.synsetid,
            Names.WIKIDATAS.wikidata,
        ).joinToString(",")
        val toSqlRows = { synset: Synset ->
            val synsetNID1 = lookup(synsetIdToNIDMap, synset.synsetId)
            synset.wikidata!!.map {
                "$synsetNID1,'${it}'"
            }
        }
        printInserts(ps, Names.WIKIDATAS.TABLE, columns, synsetSeq, toSqlRows, false)
    }
}
