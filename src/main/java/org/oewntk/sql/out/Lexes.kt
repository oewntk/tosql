/*
 * Copyright (c) 2024. Bernard Bou.
 */
package org.oewntk.sql.out

import org.oewntk.model.*
import java.io.PrintStream

/**
 * Process lexes
 */
object Lexes {

    // lexes

    /**
     * Make lex-to-NID map
     */
    fun makeLexesNIDs(
        lexes: Collection<Lex>,
    ): Map<Key, Int> {
        return lexes
            .asSequence()
            .map { Key.KeyLCP.of_t(it) }
            .sorted()
            .withIndex()
            .associate { it.value to it.index + 1 } // map(of_t(lex), nid)
    }

    /**
     * Generate lexes table
     *
     * @param ps             print stream
     * @param lexes          lexes
     * @param wordToNID      id-to-nid map for words
     * @param casedwordToNID id-to-nid map for cased words
     * @return lex_key-to-nid map
     */
    fun generateLexes(
        ps: PrintStream,
        lexes: Collection<Lex>,
        wordToNID: Map<String, Int>,
        casedwordToNID: Map<String, Int>,
    ): Map<Key, Int> {

        // lex key to NID
        val lexKeyToNID: Map<Key, Int> = makeLexesNIDs(lexes)

        // insert map
        val columns = listOf(
            Names.LEXES.luid,
            Names.LEXES.posid,
            Names.LEXES.wordid,
            Names.LEXES.casedwordid
        ).joinToString(",")
        val toSqlRow = { lex: Lex ->
            val word = lex.lCLemma
            val wordNID = NIDMaps.lookupLC(wordToNID, word)
            val casedWordNID = NIDMaps.lookupNullable(casedwordToNID, lex.lemma)
            val type = lex.type
            "'$type',$wordNID,$casedWordNID"
        }
        if (!Printers.WITH_COMMENT) {
            Printers.printInsert(ps, Names.LEXES.TABLE, columns, lexes, lexKeyToNID, toSqlRow)
        } else {
            val toSqlRowWithComment = { lex: Lex -> toSqlRow.invoke(lex) to "${lex.type} '${lex.lemma}'" }
            Printers.printInsertWithComment(ps, Names.LEXES.TABLE, columns, lexes, lexKeyToNID, toSqlRowWithComment)
        }
        return lexKeyToNID
    }

    // words

    /**
     * Make word-to-NID map
     *
     * @param lexes lexes
     * @return word-to-nid map
     */
    fun makeWordNIDs(lexes: Collection<Lex>): Map<String, Int> {
        // stream of words
        val map = lexes
            .asSequence()
            .map(Lex::lCLemma)
            .distinct()
            .sorted()
            .withIndex()
            .associate { it.value to it.index + 1 }
        assert(map.values.none { it == 0 })
        return map
    }

    /**
     * Generate words table
     *
     * @param ps    print stream
     * @param lexes lexes
     * @return word-to-nid map
     */
    fun generateWords(ps: PrintStream, lexes: Collection<Lex>): Map<String, Int> {
        // make word-to-nid map
        val wordToNID = makeWordNIDs(lexes)

        // insert map
        val columns = listOf(
            Names.WORDS.wordid,
            Names.WORDS.word
        ).joinToString(",")
        val toSqlRow = { lemma: Lemma -> "'${Utils.escape(lemma)}'" }
        Printers.printInsert(ps, Names.WORDS.TABLE, columns, wordToNID, toSqlRow)

        return wordToNID
    }

    // cased words

    /**
     * Make cased_word-to-NID map
     *
     * @param lexes lexes
     * @return cased_word-to-nid map
     */
    fun makeCasedWordNIDs(lexes: Collection<Lex>): Map<String, Int> {
        val map = lexes
            .asSequence()
            .filter(Lex::isCased)
            .map { it.lemma }
            .distinct()
            .sorted()
            .withIndex()
            .associate { it.value to it.index + 1 }
        assert(map.values.none { it == 0 })
        return map
    }

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
        wordIdToNID: Map<String, Int>,
    ): Map<String, Int> {

        // make casedword-to-nid map
        val casedWordToNID = makeCasedWordNIDs(lexes)

        // insert map
        val columns = listOf(
            Names.CASEDWORDS.casedwordid,
            Names.CASEDWORDS.casedword,
            Names.CASEDWORDS.wordid
        ).joinToString(",")
        val toSqlRow = { casedWord: Lemma ->
            val nid = NIDMaps.lookupLC(wordIdToNID, casedWord.lowercase())
            "'${Utils.escape(casedWord)}',$nid"
        }
        Printers.printInsert(ps, Names.CASEDWORDS.TABLE, columns, casedWordToNID, toSqlRow)

        return casedWordToNID
    }

    // morphs

    /**
     * Make morphs-to-NID map
     *
     * @param lexes lexes
     * @return morph-to-nid map
     */
    fun makeMorphNIDs(lexes: Collection<Lex>): Map<String, Int> {
        return lexes
            .asSequence()
            .filter { it.forms != null && it.forms!!.isNotEmpty() }
            .flatMap { it.forms!!.asSequence() }
            .sorted()
            .distinct()
            .withIndex()
            .associate { it.value to it.index + 1 }
    }

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
     * @param lexKeyToNID lex_key-to-nid map
     * @param wordToNID   word-to-nid map
     * @param morphToNID  morph-to-nid map
     */
    fun generateLexesMorphs(
        ps: PrintStream,
        lexes: Collection<Lex>,
        lexKeyToNID: Map<Key, Int>,
        wordToNID: Map<String, Int>,
        morphToNID: Map<String, Int>,
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
            val wordNID = NIDMaps.lookupLC(wordToNID, lex.lCLemma)
            val lexNID = NIDMaps.lookup(lexKeyToNID, Key.KeyLCP.of_t(lex))
            lex.forms!!
                .map {
                    val morphNID = NIDMaps.lookup(morphToNID, it)
                    "$morphNID,$lexNID,$wordNID,'${lex.type}'"
                }
        }
        if (!Printers.WITH_COMMENT) {
            Printers.printInserts(ps, Names.LEXES_MORPHS.TABLE, columns, lexSeq, toSqlRows, false)
        } else {
            val toSqlRowsWithComments = { lex: Lex ->
                val rows = toSqlRows.invoke(lex)
                val comments = lex.forms!!
                    .asSequence()
                    .map { "'$it' '${lex.lemma}' ${lex.type}" }
                rows
                    .asSequence()
                    .zip(comments)
            }
            Printers.printInsertsWithComment(ps, Names.LEXES_MORPHS.TABLE, columns, lexSeq, toSqlRowsWithComments, false)
        }
    }

// pronunciations

    /**
     * Make pronunciation(values)-to-NID map
     *
     * @param lexes lexes
     * @return pronunciation-to-nid map
     */
    fun makePronunciationNIDs(lexes: Collection<Lex>): Map<String, Int> {
        return lexes
            .asSequence()
            .filter { it.pronunciations != null && it.pronunciations!!.isNotEmpty() }
            .flatMap { it.pronunciations!!.asSequence() }
            .map { it.value }
            .sorted()
            .distinct()
            .withIndex()
            .associate { it.value to it.index + 1 }
    }

    /**
     * Generate pronunciations table
     *
     * @param ps    print stream
     * @param lexes lexes
     * @return pronunciation-to-nid
     */
    fun generatePronunciations(ps: PrintStream, lexes: Collection<Lex>): Map<String, Int> {

        // make pronunciation_value-to-nid map
        val pronunciationValueToNID = makePronunciationNIDs(lexes)

        // insert map
        val columns = listOf(
            Names.PRONUNCIATIONS.pronunciationid,
            Names.PRONUNCIATIONS.pronunciation
        ).joinToString(",")
        val toSqlRow = { pronunciationValue: PronunciationValue -> "'${Utils.escape(pronunciationValue)}'" }
        Printers.printInsert(ps, Names.PRONUNCIATIONS.TABLE, columns, pronunciationValueToNID, toSqlRow)

        return pronunciationValueToNID
    }

    /**
     * Generate lexes-pronunciations mappings
     *
     * @param ps                 print stream
     * @param lexes              lexes
     * @param lexKeyToNID        lex_key-to-nid map
     * @param wordToNID          word-to-nid map
     * @param pronunciationToNID pronunciation-to-nid
     */
    fun generateLexesPronunciations(
        ps: PrintStream,
        lexes: Collection<Lex>,
        lexKeyToNID: Map<Key, Int>,
        wordToNID: Map<String, Int>,
        pronunciationToNID: Map<String, Int>,
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
            val wordNID = NIDMaps.lookupLC(wordToNID, lex.lCLemma)
            val lexNID = NIDMaps.lookup(lexKeyToNID, Key.KeyLCP.of_t(lex))
            lex.pronunciations!!
                .map {
                    val variety = if (it.variety == null) "NULL" else "'${it.variety}'"
                    val pronunciationNID = NIDMaps.lookup(pronunciationToNID, it.value)
                    "$pronunciationNID,$variety,$lexNID,$wordNID,'${lex.type}'"
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
                        "${it.value}$variety '${lex.lemma}' ${lex.type}"
                    }
                rows
                    .asSequence()
                    .zip(comments)
            }
            Printers.printInsertsWithComment(ps, Names.LEXES_PRONUNCIATIONS.TABLE, columns, lexSeq, toSqlRowsWithComments, false)
        }
    }
}
