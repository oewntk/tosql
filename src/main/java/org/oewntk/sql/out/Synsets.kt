/*
 * Copyright (c) $originalComment.match("Copyright \(c\) (\d+)", 1, "-")2021. Bernard Bou.
 */
package org.oewntk.sql.out

import org.oewntk.model.Synset
import org.oewntk.sql.out.Printers.printInsert
import org.oewntk.sql.out.Printers.printInsertWithComment
import org.oewntk.sql.out.Printers.printInserts
import org.oewntk.sql.out.Printers.printInsertsWithComment
import org.oewntk.sql.out.Utils.escape
import org.oewntk.sql.out.Utils.makeNIDMap
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
	@JvmStatic
	fun makeSynsetNIDs(synsets: Collection<Synset>): Map<String, Int> {
		// stream of synsetIds
		val synsetIdStream = synsets.stream()
			.map { s: Synset -> s.synsetId }
		return makeNIDMap(synsetIdStream)
	}

	/**
	 * Generate synsets table
	 *
	 * @param ps      print stream
	 * @param synsets synsets
	 * @return synsets id-to-nid map
	 */
	@JvmStatic
	fun generateSynsets(ps: PrintStream, synsets: Collection<Synset>): Map<String, Int> {
		// make synsetId-to-nid map
		val synsetIdToNID = makeSynsetNIDs(synsets)

		// insert map
		val columns = java.lang.String.join(
			",",
			Names.SYNSETS.synsetid,
			Names.SYNSETS.posid,
			Names.SYNSETS.domainid,
			Names.SYNSETS.definition
		)
		val toString = { synset: Synset ->
			val type = synset.type
			val definition = synset.definition
			val domain = synset.lexfile
			val lexdomainId = BuiltIn.LEXFILE_NIDS[domain]!!
			String.format("'%c',%d,'%s'", type, lexdomainId, escape(definition!!))
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
	@JvmStatic
	fun generateSynsetRelations(ps: PrintStream, synsets: Collection<Synset>, synsetIdToNIDMap: Map<String, Int>) {
		// synset stream
		val synsetStream = synsets.stream()
			.filter { !it.relations.isNullOrEmpty() }
			.sorted(Comparator.comparing { it.synsetId })

		// insert
		val columns = java.lang.String.join(
			",",
			Names.SEMRELATIONS.synset1id,
			Names.SEMRELATIONS.synset2id,
			Names.SEMRELATIONS.relationid
		)
		val toString = { synset: Synset ->
			val strings = ArrayList<String>()
			val synset1Id = synset.synsetId
			val synset1NID = NIDMaps.lookup(synsetIdToNIDMap, synset1Id)
			val relations: Map<String, Set<String>>? = synset.relations
			for (relation in relations!!.keys) {
				require(BuiltIn.OEWN_RELATION_TYPES.containsKey(relation)) { relation }
				val relationId = BuiltIn.OEWN_RELATION_TYPES[relation]!!
				for (synset2Id in relations[relation]!!) {
					val synset2NID = NIDMaps.lookup(synsetIdToNIDMap, synset2Id)
					strings.add(String.format("%d,%d,%d", synset1NID, synset2NID, relationId))
				}
			}
			strings
		}
		if (!Printers.WITH_COMMENT) {
			printInserts(ps, Names.SEMRELATIONS.TABLE, columns, synsetStream, toString, false)
		} else {
			val toStrings = { synset: Synset ->
				val strings = toString.invoke(synset)
				val stringsWithComment = ArrayList<Array<String>>()
				val synset1Id = synset.synsetId
				val relations: Map<String, Set<String>>? = synset.relations
				var i = 0
				for (relation in relations!!.keys) {
					for (synsetId2 in relations[relation]!!) {
						stringsWithComment.add(
							arrayOf(
								strings[i],
								String.format("%s -%s-> %s", synset1Id, relation, synsetId2),
							)
						)
						i++
					}
				}
				stringsWithComment
			}
			printInsertsWithComment(ps, Names.SEMRELATIONS.TABLE, columns, synsetStream, toStrings, false)
		}
	}

	/**
	 * Generate samples table
	 *
	 * @param ps               print stream
	 * @param synsets          synsets
	 * @param synsetIdToNIDMap id-to-nid map
	 */
	@JvmStatic
	fun generateSamples(ps: PrintStream, synsets: Collection<Synset>, synsetIdToNIDMap: Map<String, Int>) {
		// stream of synsets
		val synsetStream = synsets.stream()
			.filter { !it.examples.isNullOrEmpty() }
			.sorted(Comparator.comparing { it.synsetId })

		// insert
		val columns = java.lang.String.join(",", Names.SAMPLES.sampleid, Names.SAMPLES.synsetid, Names.SAMPLES.sample)
		val toString = { synset: Synset ->
			val strings = ArrayList<String>()
			val synsetId1 = synset.synsetId
			val synsetNID1 = NIDMaps.lookup(synsetIdToNIDMap, synsetId1)
			val examples = synset.examples
			for (example in examples!!) {
				strings.add(String.format("%d,'%s'", synsetNID1, escape(example)))
			}
			strings
		}
		printInserts(ps, Names.SAMPLES.TABLE, columns, synsetStream, toString, true)
	}
}
