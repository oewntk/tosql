/*
 * Copyright (c) 2024. Bernard Bou.
 */
package org.oewntk.sql.out

import org.oewntk.model.*
import org.oewntk.model.NIDs.lookup
import org.oewntk.model.NIDs.lookupLC
import org.oewntk.model.NIDs.lookupNullable
import org.oewntk.model.NIDs.makeCasedWordNIDs
import org.oewntk.model.NIDs.makeLexesNIDs
import org.oewntk.model.NIDs.makeMorphNIDs
import org.oewntk.model.NIDs.makePronunciationNIDs
import org.oewntk.model.NIDs.makeWordNIDs
import java.io.PrintStream
import java.util.*

/**
 * Process lexes
 */
object Lexes {

    // lexes

    /**
     * Generate lexes table
     *
     * @param ps             print stream
     * @param lexes          lexes
     * @param wordToNID      id-to-nid map for words
     * @param casedWordToNID id-to-nid map for cased words
     * @return lex_key-to-nid map
     */
    fun generateLexes(
        ps: PrintStream,
        lexes: Collection<Lex>,
        wordToNID: Map<Lemma, Int>,
        casedWordToNID: Map<Lemma, Int>,
    ): Map<LexId, Int> {

        // lex key to NID
        val lexIdToNID: Map<LexId, Int> = makeLexesNIDs(lexes)

        // insert map
        val columns = listOf(
            Names.LEXES.luid,
            Names.LEXES.posid,
            Names.LEXES.wordid,
            Names.LEXES.casedwordid
        ).joinToString(",")
        val toSqlRow = { lex: Lex ->
            val word = lex.lCLemma
            val wordNID = lookupLC(wordToNID, word)
            val casedWordNID = lookupNullable(casedWordToNID, lex.lemma)
            val pos = lex.partOfSpeech
            "'${pos.value}',$wordNID,$casedWordNID"
        }
        if (!Printers.WITH_COMMENT) {
            Printers.printInsert(ps, Names.LEXES.TABLE, columns, lexes, lexIdToNID, toSqlRow)
        } else {
            val toSqlRowWithComment = { lex: Lex -> toSqlRow.invoke(lex) to "${lex.partOfSpeech.value} '${lex.lemma}'" }
            Printers.printInsertWithComment(ps, Names.LEXES.TABLE, columns, lexes, lexIdToNID, toSqlRowWithComment)
        }
        return lexIdToNID
    }

    // words

    /**
     * Generate words table
     *
     * @param ps    print stream
     * @param lexes lexes
     * @return word-to-nid map
     */
    fun generateWords(ps: PrintStream, lexes: Collection<Lex>): Map<Lemma, Int> {
        // make word-to-nid map
        val wordToNID = makeWordNIDs(lexes)

        // insert map
        val columns = listOf(
            Names.WORDS.wordid,
            Names.WORDS.word
        ).joinToString(",")
        val toSqlRow = { lemma: Lemma -> "'${Utils.escape(lemma.form)}'" }
        Printers.printInsert(ps, Names.WORDS.TABLE, columns, wordToNID, toSqlRow)

        return wordToNID
    }

    // cased words

    /**
     * Generate cased word table
     *
     * @param ps          print stream
     * @param lexes       lexes
     * @param wordIdToNID word-to-nid map
     * @return cased_word-to-nid map
     */
    fun generateCasedWords(
        ps: PrintStream,
        lexes: Collection<Lex>,
        wordIdToNID: Map<Lemma, Int>,
    ): Map<Lemma, Int> {

        // make casedword-to-nid map
        val casedWordToNID = makeCasedWordNIDs(lexes)

        // insert map
        val columns = listOf(
            Names.CASEDWORDS.casedwordid,
            Names.CASEDWORDS.casedword,
            Names.CASEDWORDS.wordid
        ).joinToString(",")
        val toSqlRow = { casedWord: Lemma ->
            val nid = lookupLC(wordIdToNID, casedWord.lCLemma)
            "'${Utils.escape(casedWord.form)}',$nid"
        }
        Printers.printInsert(ps, Names.CASEDWORDS.TABLE, columns, casedWordToNID, toSqlRow)

        return casedWordToNID
    }

    // morphs

    /**
     * Generate morphs table
     *
     * @param ps    print stream
     * @param lexes lexes
     * @return morph-to-nid map
     */
    fun generateMorphs(ps: PrintStream, lexes: Collection<Lex>): Map<String, Int> {

        // make morph-to-nid map
        val morphToNID = makeMorphNIDs(lexes)

        // insert map
        val columns = listOf(
            Names.MORPHS.morphid,
            Names.MORPHS.morph
        ).joinToString(",")
        val toSqlRow = { morph: Morph -> "'${Utils.escape(morph)}'" }
        Printers.printInsert(ps, Names.MORPHS.TABLE, columns, morphToNID, toSqlRow)

        return morphToNID
    }

    /**
     * Generate lexes-pronunciations mappings
     *
     * @param ps          print stream
     * @param lexes       lexes
     * @param lexIdToNID  lexId-to-nid map
     * @param wordToNID   word-to-nid map
     * @param morphToNID  morph-to-nid map
     */
    fun generateLexesMorphs(
        ps: PrintStream,
        lexes: Collection<Lex>,
        lexIdToNID: Map<LexId, Int>,
        wordToNID: Map<Lemma, Int>,
        morphToNID: Map<Morph, Int>,
    ) {
        // stream of lexes
        val lexSeq = lexes
            .asSequence()
            .filter { it.forms != null && it.forms!!.isNotEmpty() }
            .sortedBy { it.lemma }

        // insert map
        val columns = listOf(
            Names.LEXES_MORPHS.morphid,
            Names.LEXES_MORPHS.luid,
            Names.LEXES_MORPHS.wordid,
            Names.LEXES_MORPHS.posid
        ).joinToString(",")
        val toSqlRows = { lex: Lex ->
            val wordNID = lookupLC(wordToNID, lex.lCLemma)
            val lexNID = lookup(lexIdToNID, lex.key)
            lex.forms!!
                .map {
                    val morphNID = lookup(morphToNID, it)
                    "$morphNID,$lexNID,$wordNID,'${lex.partOfSpeech.value}'"
                }
        }
        if (!Printers.WITH_COMMENT) {
            Printers.printInserts(ps, Names.LEXES_MORPHS.TABLE, columns, lexSeq, toSqlRows, false)
        } else {
            val toSqlRowsWithComments = { lex: Lex ->
                val rows = toSqlRows.invoke(lex)
                val comments = lex.forms!!
                    .asSequence()
                    .map { "'$it' '${lex.lemma}' ${lex.partOfSpeech.value}" }
                rows
                    .asSequence()
                    .zip(comments)
            }
            Printers.printInsertsWithComment(ps, Names.LEXES_MORPHS.TABLE, columns, lexSeq, toSqlRowsWithComments, false)
        }
    }

// pronunciations

    /**
     * Generate pronunciations table
     *
     * @param ps    print stream
     * @param lexes lexes
     * @return pronunciation-to-nid
     */
    fun generatePronunciations(ps: PrintStream, lexes: Collection<Lex>): Map<PronunciationValue, Int> {

        // make pronunciation_value-to-nid map
        val pronunciationValueToNID = makePronunciationNIDs(lexes)

        // insert map
        val columns = listOf(
            Names.PRONUNCIATIONS.pronunciationid,
            Names.PRONUNCIATIONS.pronunciation
        ).joinToString(",")
        val toSqlRow = { pronunciationValue: PronunciationValue -> "'${Utils.escape(pronunciationValue.ipa)}'" }
        Printers.printInsert(ps, Names.PRONUNCIATIONS.TABLE, columns, pronunciationValueToNID, toSqlRow)

        return pronunciationValueToNID
    }

    /**
     * Generate lexes-pronunciations mappings
     *
     * @param ps                 print stream
     * @param lexes              lexes
     * @param lexIdToNID         lexId-to-nid map
     * @param wordToNID          word-to-nid map
     * @param pronunciationToNID pronunciation-to-nid
     */
    fun generateLexesPronunciations(
        ps: PrintStream,
        lexes: Collection<Lex>,
        lexIdToNID: Map<LexId, Int>,
        wordToNID: Map<Lemma, Int>,
        pronunciationToNID: Map<PronunciationValue, Int>,
    ) {
        // stream of lexes
        val lexSeq = lexes
            .asSequence()
            .filter { it.pronunciations != null && it.pronunciations!!.isNotEmpty() }
            .sortedBy { it.lemma }

        // insert map
        val columns = listOf(
            Names.LEXES_PRONUNCIATIONS.pronunciationid,
            Names.LEXES_PRONUNCIATIONS.variety,
            Names.LEXES_PRONUNCIATIONS.luid,
            Names.LEXES_PRONUNCIATIONS.wordid,
            Names.LEXES_PRONUNCIATIONS.posid
        ).joinToString(",")
        val toSqlRows = { lex: Lex ->
            val wordNID = lookupLC(wordToNID, lex.lCLemma)
            val lexNID = lookup(lexIdToNID, lex.key)
            lex.pronunciations!!
                .map {
                    val variety = if (it.variety == null) "NULL" else "'${it.variety}'"
                    val pronunciationNID = lookup(pronunciationToNID, it.value)
                    "$pronunciationNID,$variety,$lexNID,$wordNID,'${lex.partOfSpeech.value}'"
                }
                .toList()
        }
        if (!Printers.WITH_COMMENT) {
            Printers.printInserts(ps, Names.LEXES_PRONUNCIATIONS.TABLE, columns, lexSeq, toSqlRows, false)
        } else {
            val toSqlRowsWithComments = { lex: Lex ->
                val rows = toSqlRows.invoke(lex)
                val comments = lex.pronunciations!!
                    .asSequence()
                    .map {
                        val variety = if (it.variety == null) "" else " [${it.variety}]"
                        "${it.value}$variety '${lex.lemma}' ${lex.partOfSpeech.value}"
                    }
                rows
                    .asSequence()
                    .zip(comments)
            }
            Printers.printInsertsWithComment(ps, Names.LEXES_PRONUNCIATIONS.TABLE, columns, lexSeq, toSqlRowsWithComments, false)
        }
    }
}
