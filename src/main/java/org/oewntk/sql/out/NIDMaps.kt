/*
 * Copyright (c) 2024. Bernard Bou.
 */

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

    // L O O K U P

    /**
     * Lookup of id of type K
     *
     * @param map map of K-integer pairs
     * @param key key
     * @param K type of key
     * @return nid
     */
    fun <K> lookup(map: Map<K, Int>, key: K): Int {
        try {
            val nid = map[key]!!
            assert(nid != 0)
            return nid
        } catch (e: Exception) {
            Tracing.psErr.println("lookup of <$key> failed")
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
     * @param K type of key
     * @return nid or "NULL"
     */
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
        toNID.keys
            .sorted()
            .forEach { ps.println("$it ${toNID[it]}") }
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
        PrintStream(FileOutputStream(File(outDir, "${Names.WORDS.FILE}.map")), true, StandardCharsets.UTF_8)
            .use {
                printWords(it, model.lexes)
            }
        PrintStream(FileOutputStream(File(outDir, "${Names.CASEDWORDS.FILE}.map")), true, StandardCharsets.UTF_8)
            .use {
                printCasedWords(it, model.lexes)
            }
        PrintStream(FileOutputStream(File(outDir, "${Names.MORPHS.FILE}.map")), true, StandardCharsets.UTF_8)
            .use {
                printMorphs(it, model.lexes)
            }
        PrintStream(FileOutputStream(File(outDir, "${Names.PRONUNCIATIONS.FILE}.map")), true, StandardCharsets.UTF_8)
            .use {
                printPronunciations(it, model.lexes)
            }
        PrintStream(FileOutputStream(File(outDir, "${Names.SYNSETS.FILE}.map")), true, StandardCharsets.UTF_8)
            .use {
                printSynsets(it, model.synsets)
            }
        PrintStream(FileOutputStream(File(outDir, "${Names.SENSES.FILE}.map")), true, StandardCharsets.UTF_8)
            .use {
                printSenses(it, model.senses)
            }
    }
}
