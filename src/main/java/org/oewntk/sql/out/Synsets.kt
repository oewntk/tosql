/*
 * Copyright (c) 2024. Bernard Bou.
 */
package org.oewntk.sql.out

import org.oewntk.model.Relation
import org.oewntk.model.Synset
import org.oewntk.model.SynsetId
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
     * Make synset id-to-nid map
     *
     * @param synsets synsets
     * @return id-to-nid map
     */
    fun makeSynsetNIDs(synsets: Collection<Synset>): Map<String, Int> {
        return synsets
            .asSequence()
            .map { s: Synset -> s.synsetId }
            .sorted()
            .withIndex()
            .associate { it.value to it.index + 1 }
    }

    /**
     * Generate synsets table
     *
     * @param ps      print stream
     * @param synsets synsets
     * @return synsets id-to-nid map
     */
    fun generateSynsets(ps: PrintStream, synsets: Collection<Synset>): Map<String, Int> {
        // make synsetId-to-nid map
        val synsetIdToNID = makeSynsetNIDs(synsets)

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
            "'$type',$lexdomainId,'${escape(definition!!)}'"
        }
        if (!Printers.WITH_COMMENT) {
            printInsert(ps, Names.SYNSETS.TABLE, columns, synsets, { it.synsetId }, synsetIdToNID, toSqlRow)
        } else {
            val toSqlRowWithComment = { synset: Synset -> toSqlRow.invoke(synset) to synset.synsetId }
            printInsertWithComment(ps, Names.SYNSETS.TABLE, columns, synsets, { it.synsetId }, synsetIdToNID, toSqlRowWithComment)
        }
        return synsetIdToNID
    }

    /**
     * Generate synset relations table
     *
     * @param ps               print stream
     * @param synsets          synsets
     * @param synsetIdToNIDMap id-to-nid map
     */
    fun generateSynsetRelations(ps: PrintStream, synsets: Collection<Synset>, synsetIdToNIDMap: Map<String, Int>) {

        // synset sequence
        val synsetSeq = synsets
            .asSequence()
            .filter { !it.relations.isNullOrEmpty() }
            .sortedBy(Synset::synsetId)

        // insert
        val columns = listOf(
            Names.SEMRELATIONS.synset1id,
            Names.SEMRELATIONS.synset2id,
            Names.SEMRELATIONS.relationid
        ).joinToString(",")

        val toTargetData = { synset: Synset ->
            synset.relations?.keys
                ?.asSequence()
                ?.onEach { require(BuiltIn.OEWN_RELATION_TYPES.containsKey(it)) { it } } // relation type
                ?.flatMap {
                    val relation: Relation = it
                    val relationNID: Int = BuiltIn.OEWN_RELATION_TYPES[it]!! // relation NID
                    synset.relations!![it]!!
                        .asSequence() // sequence of synset2 ids
                        .map { synset2Id -> Triple(relation, relationNID, synset2Id) }
                } // sequence of (relation, relationNID, synset2Id1) (relation, relationNID, synset2Id2) ...
                ?.sortedWith(
                    Comparator
                        .comparing(Triple<*, Int, SynsetId>::second)
                        .thenComparing(Triple<*, *, SynsetId>::third)
                )
        }

        val toSqlRows = { synset: Synset ->
            val synset1Id = synset.synsetId
            val synset1NID = NIDMaps.lookup(synsetIdToNIDMap, synset1Id)
            toTargetData(synset) // sequence of (relation, relationNID, synset2Id1) (relation, relationNID, synset2Id2) ...
                ?.map {
                    val relationNID: Int = BuiltIn.OEWN_RELATION_TYPES[it.first]!! // relation type id
                    val synset2NID = NIDMaps.lookup(synsetIdToNIDMap, it.third)
                    "$synset1NID,$synset2NID,$relationNID"
                }
                ?.toList()!!
        }

        if (!Printers.WITH_COMMENT) {
            printInserts(ps, Names.SEMRELATIONS.TABLE, columns, synsetSeq, toSqlRows, false)
        } else {
            val toSqlRowsWithComments = { synset: Synset ->
                val rows = toSqlRows.invoke(synset)
                val comments = toTargetData(synset) // sequence of (relation, relationNID, synset2Id1) (relation, relationNID, synset2Id2) ...
                    ?.map { "${synset.synsetId} -${it.first}-> ${it.third}" }
                rows
                    .asSequence()
                    .zip(comments!!)
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
    fun generateSamples(ps: PrintStream, synsets: Collection<Synset>, synsetIdToNIDMap: Map<String, Int>) {

        // sequence of synsets
        val synsetSeq = synsets
            .asSequence()
            .filter { !it.examples.isNullOrEmpty() }
            .sortedBy(Synset::synsetId)

        // insert
        val columns = listOf(
            Names.SAMPLES.sampleid,
            Names.SAMPLES.synsetid,
            Names.SAMPLES.sample
        ).joinToString(",")
        val toSqlRows = { synset: Synset ->
            val synsetId1 = synset.synsetId
            val synsetNID1 = NIDMaps.lookup(synsetIdToNIDMap, synsetId1)
            synset.examples!!
                .map { "$synsetNID1,'${escape(it)}'" }
                .toList()
        }
        printInserts(ps, Names.SAMPLES.TABLE, columns, synsetSeq, toSqlRows, true)
    }
}
