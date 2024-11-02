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
@Suppress("KotlinConstantConditions")
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
            synset.relations!!.keys
                .asSequence()
                .onEach { require(BuiltIn.OEWN_RELATION_TYPES.containsKey(it)) { it } } // relation type
                .flatMap {
                    val relation: Relation = it
                    val relationNID: Int = BuiltIn.OEWN_RELATION_TYPES[it]!! // relation NID
                    synset.relations!![it]!!
                        .asSequence() // sequence of synset2 ids
                        .map { synset2Id -> (relation to relationNID) to synset2Id }
                } // sequence of ((relation, relationNID), synset2Id_1) ((relation, relationNID, synset2Id_2) ...
                .sortedWith(
                    Comparator
                        .comparingInt { data: Pair<Pair<Relation, Int>, SynsetId> -> data.first.second } // relationNID
                        .thenComparing { data -> data.second } //  synset2Id
                )
        }

        val toSqlRows = { synset: Synset ->
            val synset1NID = NIDMaps.lookup(synsetIdToNIDMap, synset.synsetId)
            toTargetData(synset) // sequence of ((relation, relationNID), synset2Id_1) ((relation, relationNID, synset2Id_2) ...
                .map {
                    val relationNID: Int = BuiltIn.OEWN_RELATION_TYPES[it.first.first]!! // relation
                    val synset2NID = NIDMaps.lookup(synsetIdToNIDMap, it.second)
                    "$synset1NID,$synset2NID,$relationNID"
                }
                .toList()
        }

        if (!Printers.WITH_COMMENT) {
            printInserts(ps, Names.SEMRELATIONS.TABLE, columns, synsetSeq, toSqlRows, false)
        } else {
            val toSqlRowsWithComments = { synset: Synset ->
                val rows = toSqlRows.invoke(synset)
                val comments = toTargetData(synset) // sequence of ((relation, relationNID), synset2Id_1) ((relation, relationNID, synset2Id_2) ...
                    .map { "${synset.synsetId} -${it.first.first}-> ${it.second}" }
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
    fun generateSynsetSamples(ps: PrintStream, synsets: Collection<Synset>, synsetIdToNIDMap: Map<String, Int>) {

        // sequence of synsets
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
            val synsetNID1 = NIDMaps.lookup(synsetIdToNIDMap, synset.synsetId)
            synset.examples!!
                .map {
                    val text = escape(it.first)
                    val source = if (it.second == null) "NULL" else "'${escape(it.second!!)}'"
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
    fun generateSynsetUsages(ps: PrintStream, synsets: Collection<Synset>, synsetIdToNIDMap: Map<String, Int>) {

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
            Names.USAGES.usage,
        ).joinToString(",")
        val toSqlRows = { synset: Synset ->
            val synsetNID1 = NIDMaps.lookup(synsetIdToNIDMap, synset.synsetId)
            synset.usages!!
                .map {
                    val usage = escape(it)
                    "$synsetNID1,NULL,NULL,'$usage'"
                }
                .toList()
        }
        printInserts(ps, Names.SAMPLES.TABLE, columns, synsetSeq, toSqlRows, true)
    }

    /**
     * Generate ilis table
     *
     * @param ps               print stream
     * @param synsets          synsets
     * @param synsetIdToNIDMap id-to-nid map
     */
    fun generateSynsetIlis(ps: PrintStream, synsets: Collection<Synset>, synsetIdToNIDMap: Map<String, Int>) {

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
            val synsetNID1 = NIDMaps.lookup(synsetIdToNIDMap, synset.synsetId)
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
    fun generateSynsetWikidatas(ps: PrintStream, synsets: Collection<Synset>, synsetIdToNIDMap: Map<String, Int>) {

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
            val synsetNID1 = NIDMaps.lookup(synsetIdToNIDMap, synset.synsetId)
            "$synsetNID1,'${synset.wikidata}'"
        }
        printInsert(ps, Names.ILIS.TABLE, columns, synsetSeq, toSqlRows, false)
    }
}
