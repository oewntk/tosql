/*
 * Copyright (c) 2024. Bernard Bou.
 */
package org.oewntk.sql.out

import org.oewntk.model.Synset
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
        return synsets.asSequence()
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
        val toString = { synset: Synset ->
            val type = synset.type
            val definition = synset.definition
            val domain = synset.lexfile
            val lexdomainId = BuiltIn.LEXFILE_NIDS[domain]!!
            "'$type',$lexdomainId,'${escape(definition!!)}'"
        }
        if (!Printers.WITH_COMMENT) {
            printInsert(ps, Names.SYNSETS.TABLE, columns, synsets, { it.synsetId }, synsetIdToNID, toString)
        } else {
            val toStrings = { synset: Synset ->
                arrayOf(toString.invoke(synset), synset.synsetId)
            }
            printInsertWithComment(
                ps,
                Names.SYNSETS.TABLE,
                columns,
                synsets,
                { it.synsetId },
                synsetIdToNID,
                toStrings
            )
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
        val toString = { synset: Synset ->
            val result = ArrayList<String>()
            val synset1Id = synset.synsetId
            val synset1NID = NIDMaps.lookup(synsetIdToNIDMap, synset1Id)
            if (synset.relations != null) {
                for (relation in synset.relations!!.keys) {
                    require(BuiltIn.OEWN_RELATION_TYPES.containsKey(relation)) { relation }
                    val relationId = BuiltIn.OEWN_RELATION_TYPES[relation]!!
                    for (synset2Id in synset.relations!![relation]!!) {
                        val synset2NID = NIDMaps.lookup(synsetIdToNIDMap, synset2Id)
                        result.add("$synset1NID,$synset2NID,$relationId")
                    }
                }
            }
            result
        }
        if (!Printers.WITH_COMMENT) {
            printInserts(ps, Names.SEMRELATIONS.TABLE, columns, synsetSeq, toString, false)
        } else {
            val toStrings = { synset: Synset ->
                val result = ArrayList<Array<String>>()
                val data = toString.invoke(synset)
                val synset1Id = synset.synsetId
                if (synset.relations != null) {
                    var i = 0
                    for (relation in synset.relations!!.keys) {
                        for (synsetId2 in synset.relations!![relation]!!) {
                            result.add(
                                arrayOf(
                                    data[i],
                                    "$synset1Id -$relation-> $synsetId2"
                                )
                            )
                            i++
                        }
                    }
                }
                result
            }
            printInsertsWithComment(ps, Names.SEMRELATIONS.TABLE, columns, synsetSeq, toStrings, false)
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
        val columns = listOf(Names.SAMPLES.sampleid, Names.SAMPLES.synsetid, Names.SAMPLES.sample).joinToString(",")
        val toString = { synset: Synset ->
            val strings = ArrayList<String>()
            val synsetId1 = synset.synsetId
            val synsetNID1 = NIDMaps.lookup(synsetIdToNIDMap, synsetId1)
            for (example in synset.examples!!) {
                strings.add("$synsetNID1,'${escape(example)}'")
            }
            strings
        }
        printInserts(ps, Names.SAMPLES.TABLE, columns, synsetSeq, toString, true)
    }
}
