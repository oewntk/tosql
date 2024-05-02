/*
 * Copyright (c) 2024. Bernard Bou.
 */
package org.oewntk.sql.out

import org.oewntk.model.CoreModel
import org.oewntk.model.Lex
import org.oewntk.model.Sense
import org.oewntk.model.Synset
import org.oewntk.sql.out.Lexes.makeCasedWordNIDs
import org.oewntk.sql.out.Lexes.makeMorphNIDs
import org.oewntk.sql.out.Lexes.makeWordNIDs
import org.oewntk.sql.out.Senses.makeSenseNIDs
import org.oewntk.sql.out.Synsets.makeSynsetNIDs
import java.io.*

/**
 * Serialize ID to Numeric IDs maps
 */
object SerializeNIDs {

    const val NID_PREFIX: String = "nid_"

    private const val SENSEKEYS_WORDS_SYNSETS_FILE = "sensekeys_words_synsets"

    /**
     * Serialize words id-to-nid map
     *
     * @param os    output stream
     * @param lexes lexes
     * @throws IOException io exception
     */
    @Throws(IOException::class)
    fun serializeWordNIDs(os: OutputStream, lexes: Collection<Lex>) {
        val wordToNID = makeWordNIDs(lexes)
        serialize(os, wordToNID)
    }

    /**
     * Serialize cased words id-to-nid map
     *
     * @param os    output stream
     * @param lexes lexes
     * @throws IOException io exception
     */
    @Throws(IOException::class)
    fun serializeCasedWordNIDs(os: OutputStream, lexes: Collection<Lex>) {
        val casedToNID = makeCasedWordNIDs(lexes)
        serialize(os, casedToNID)
    }

    /**
     * Serialize morphs id-to-nid map
     *
     * @param os    output stream
     * @param lexes lexes
     * @throws IOException io exception
     */
    @Throws(IOException::class)
    fun serializeMorphNIDs(os: OutputStream, lexes: Collection<Lex>) {
        val morphToNID = makeMorphNIDs(lexes)
        serialize(os, morphToNID)
    }

    /**
     * Serialize pronunciations id-to-nid map
     *
     * @param os    output stream
     * @param lexes lexes
     * @throws IOException io exception
     */
    @Throws(IOException::class)
    fun serializePronunciationNIDs(os: OutputStream, lexes: Collection<Lex>) {
        val pronunciationValueToNID = makeMorphNIDs(lexes)
        serialize(os, pronunciationValueToNID)
    }

    /**
     * Serialize senses id-to-nid map
     *
     * @param os     output stream
     * @param senses senses
     * @throws IOException io exception
     */
    @Throws(IOException::class)
    private fun serializeSensesNIDs(os: OutputStream, senses: Collection<Sense>) {
        val senseToNID = makeSenseNIDs(senses)
        serialize(os, senseToNID)
    }

    /**
     * Serialize id-to-nid map
     *
     * @param os      output stream
     * @param synsets synsets
     * @throws IOException io exception
     */
    @Throws(IOException::class)
    fun serializeSynsetNIDs(os: OutputStream, synsets: Collection<Synset>) {
        val synsetIdToNID = makeSynsetNIDs(synsets)
        serialize(os, synsetIdToNID)
    }

    /**
     * Serialize object
     *
     * @param os     output stream
     * @param object object
     * @throws IOException io exception
     */
    @Throws(IOException::class)
    private fun serialize(os: OutputStream, `object`: Any) {
        ObjectOutputStream(os).use { it.writeObject(`object`) }
    }

    /**
     * Serialize sensekey to wordnid-synsetnid
     *
     * @param os    output stream
     * @param model model
     * @throws IOException io exception
     */
    @Throws(IOException::class)
    private fun serializeSensekeysWordsSynsetsNIDs(os: OutputStream, model: CoreModel) {
        val wordToNID = makeWordNIDs(model.lexes)
        val synsetIdToNID = makeSynsetNIDs(model.synsets)
        val m = model.senses
            .asSequence()
            .map { it.senseKey to (wordToNID[it.lCLemma] to synsetIdToNID[it.synsetId]) }
            .groupBy { it.first }
            .mapValues { (_, values) ->
                values
                    .reduce { e, n ->
                        if (e != n) System.err.println("existing $e -> $n")
                        e
                    }
            }
        serialize(os, m)
    }

    /**
     * Serialize all id-to-nid maps
     *
     * @param model  model
     * @param outDir output dir
     * @throws IOException io exception
     */
    @Throws(IOException::class)
    fun serializeNIDs(model: CoreModel, outDir: File) {
        FileOutputStream(File(outDir, "$NID_PREFIX${Names.WORDS.FILE}.ser")).use {
            serializeWordNIDs(it, model.lexes)
        }
        FileOutputStream(File(outDir, "$NID_PREFIX${Names.CASEDWORDS.FILE}.ser")).use {
            serializeCasedWordNIDs(it, model.lexes)
        }
        FileOutputStream(File(outDir, "$NID_PREFIX${Names.MORPHS.FILE}.ser")).use {
            serializeMorphNIDs(it, model.lexes)
        }
        FileOutputStream(File(outDir, "$NID_PREFIX${Names.PRONUNCIATIONS.FILE}.ser")).use {
            serializePronunciationNIDs(it, model.lexes)
        }
        FileOutputStream(File(outDir, "$NID_PREFIX${Names.SENSES.FILE}.ser")).use {
            serializeSensesNIDs(it, model.senses)
        }
        FileOutputStream(File(outDir, "$NID_PREFIX${Names.SYNSETS.FILE}.ser")).use {
            serializeSynsetNIDs(it, model.synsets)
        }
        FileOutputStream(File(outDir, "$SENSEKEYS_WORDS_SYNSETS_FILE.ser")).use {
            serializeSensekeysWordsSynsetsNIDs(it, model)
        }
    }
}
