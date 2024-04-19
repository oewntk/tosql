package org.oewntk.sql.out

import org.oewntk.model.*
import org.oewntk.sql.out.Lexes.makeCasedWordNIDs
import org.oewntk.sql.out.Lexes.makeMorphNIDs
import org.oewntk.sql.out.Lexes.makePronunciationNIDs
import org.oewntk.sql.out.Lexes.makeWordNIDs
import org.oewntk.sql.out.Senses.makeSenseNIDs
import org.oewntk.sql.out.Synsets.makeSynsetNIDs
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.PrintStream
import java.nio.charset.StandardCharsets

/**
 * Lookup of ID/KEY-to-NID maps and printing them
 */
object NIDMaps {
	/**
	 * Lookup of id of type K
	 *
	 * @param map map of K-integer pairs
	 * @param key key
	 * @param <K> type of key
	 * @return nid
	</K> */
	fun <K> lookup(map: Map<K, Int>, key: K): Int {
		try {
			val nid = map[key]!!
			assert(nid != 0)
			return nid
		} catch (e: Exception) {
			Tracing.psErr.printf("lookup of <%s> failed%n", key)
			throw e
		}
	}

	/**
	 * Lookup of key
	 *
	 * @param map map of key-integer pairs
	 * @param key key
	 * @return nid
	 */
	fun lookup(map: Map<out Key, Int>, key: Key): Int {
		try {
			val nid = map[key]!!
			assert(nid != 0)
			return nid
		} catch (e: Exception) {
			Tracing.psErr.printf("lookup of <%s> failed%n", key)
			throw e
		}
	}

	/**
	 * Lookup of lower-cased key
	 *
	 * @param map map
	 * @param key key, already lower-cased
	 * @return nid
	 */
	fun lookupLC(map: Map<String, Int>, key: String): Int {
		assert(key == key.lowercase())
		return lookup(map, key)
	}

	/**
	 * Look up
	 *
	 * @param map map
	 * @param key key
	 * @param <K> type of key
	 * @return nid or "NULL"
	</K> */
	fun <K> lookupNullable(map: Map<K, Int>, key: K): String {
		val value = map[key] ?: return "NULL"
		return value.toString()
	}

	// P R I N T

	/**
	 * Print words id-to-nid map
	 *
	 * @param ps    print stream
	 * @param lexes lexes
	 */
	private fun printWords(ps: PrintStream, lexes: Collection<Lex>) {
		val wordToNID = makeWordNIDs(lexes)
		print(ps, wordToNID)
	}

	/**
	 * Print cased words id-to-nid map
	 *
	 * @param ps    print stream
	 * @param lexes lexes
	 */
	private fun printCasedWords(ps: PrintStream, lexes: Collection<Lex>) {
		val casedToNID = makeCasedWordNIDs(lexes)
		print(ps, casedToNID)
	}

	/**
	 * Print morphs id-to-nid map
	 *
	 * @param ps    print stream
	 * @param lexes lexes
	 */
	private fun printMorphs(ps: PrintStream, lexes: Collection<Lex>) {
		val morphToNID = makeMorphNIDs(lexes)
		print(ps, morphToNID)
	}

	/**
	 * Print pronunciations id-to-nid map
	 *
	 * @param ps    print stream
	 * @param lexes lexes
	 */
	private fun printPronunciations(ps: PrintStream, lexes: Collection<Lex>) {
		val pronunciationValueToNID = makePronunciationNIDs(lexes)
		print(ps, pronunciationValueToNID)
	}

	/**
	 * Print synsets id-to-nid map
	 *
	 * @param ps      print stream
	 * @param synsets synsets
	 */
	private fun printSynsets(ps: PrintStream, synsets: Collection<Synset>) {
		val synsetIdToNID = makeSynsetNIDs(synsets)
		print(ps, synsetIdToNID)
	}

	/**
	 * Print sense id-to-nid map
	 *
	 * @param ps     print stream
	 * @param senses senses
	 */
	private fun printSenses(ps: PrintStream, senses: Collection<Sense>) {
		val synsetIdToNID = makeSenseNIDs(senses)
		print(ps, synsetIdToNID)
	}

	/**
	 * Print id-to-nid map
	 *
	 * @param ps    print stream
	 * @param toNID od-to-nid map
	 */
	private fun print(ps: PrintStream, toNID: Map<String, Int>) {
		toNID.keys.stream().sorted().forEach { k: String -> ps.printf("%s %d%n", k, toNID[k]) }
	}

	/**
	 * Print all id-to-nid maps for a model
	 *
	 * @param model  model
	 * @param outDir out dir
	 * @throws IOException io exception
	 */
	@Throws(IOException::class)
	fun printMaps(model: CoreModel, outDir: File) {
		PrintStream(FileOutputStream(File(outDir, Names.WORDS.FILE)), true, StandardCharsets.UTF_8).use { ps ->
			printWords(ps, model.lexes)
		}
		PrintStream(FileOutputStream(File(outDir, Names.CASEDWORDS.FILE)), true, StandardCharsets.UTF_8).use { ps ->
			printCasedWords(ps, model.lexes)
		}
		PrintStream(FileOutputStream(File(outDir, Names.MORPHS.FILE)), true, StandardCharsets.UTF_8).use { ps ->
			printMorphs(ps, model.lexes)
		}
		PrintStream(FileOutputStream(File(outDir, Names.PRONUNCIATIONS.FILE)), true, StandardCharsets.UTF_8).use { ps ->
			printPronunciations(ps, model.lexes)
		}
		PrintStream(FileOutputStream(File(outDir, Names.SYNSETS.FILE)), true, StandardCharsets.UTF_8).use { ps ->
			printSynsets(ps, model.synsets)
		}
		PrintStream(FileOutputStream(File(outDir, Names.SENSES.FILE)), true, StandardCharsets.UTF_8).use { ps ->
			printSenses(ps, model.senses)
		}
	}
}
