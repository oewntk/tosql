/*
 * Copyright (c) $originalComment.match("Copyright \(c\) (\d+)", 1, "-")2021. Bernard Bou.
 */
package org.oewntk.sql.out

import org.oewntk.model.Key
import org.oewntk.model.Key.W_P_A.Companion.of_t
import org.oewntk.model.KeyF
import org.oewntk.model.Lex
import org.oewntk.model.Pronunciation
import java.io.PrintStream
import java.util.*
import java.util.function.Function

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
	 * @param casedwordToNID id-to-nid map for cased words
	 * @return lex_key-to-nid map
	 */
	@JvmStatic
	fun generateLexes(
		ps: PrintStream,
		lexes: Collection<Lex>,
		wordToNID: Map<String, Int>,
		casedwordToNID: Map<String, Int>
	): Map<out Key, Int> {

		// stream of lex key
		val lexKeyStream = lexes.stream().map { of_t(it) }

		// make lex key-to-nid map
		val lexKeyToNID = Utils.makeNIDMap(lexKeyStream)

		// insert map
		val columns =
			java.lang.String.join(",", Names.LEXES.luid, Names.LEXES.posid, Names.LEXES.wordid, Names.LEXES.casedwordid)
		val toString = { lex: Lex ->
			val word = lex.lCLemma
			val wordNID = NIDMaps.lookupLC(wordToNID, word)
			val casedWordNID = NIDMaps.lookupNullable(casedwordToNID, lex.lemma)
			val type = lex.type
			String.format("'%c',%d,%s", type, wordNID, casedWordNID)
		}
		if (!Printers.WITH_COMMENT) {
			Printers.printInsert(ps, Names.LEXES.TABLE, columns, lexes, lexKeyToNID, toString)
		} else {
			val toStrings = { lex: Lex ->
				val casedWord = lex.lemma
				val type = lex.type
				arrayOf(
					toString.invoke(lex),
					String.format("%c '%s'", type, casedWord),
				)
			}
			Printers.printInsertWithComment(ps, Names.LEXES.TABLE, columns, lexes, lexKeyToNID, toStrings)
		}
		return lexKeyToNID
	}

	// words
	/**
	 * Generate words table
	 *
	 * @param ps    print stream
	 * @param lexes lexes
	 * @return word-to-nid map
	 */
	@JvmStatic
	fun generateWords(ps: PrintStream, lexes: Collection<Lex>): Map<String, Int> {
		// make word-to-nid map
		val wordToNID = makeWordNIDs(lexes)

		// insert map
		val columns = java.lang.String.join(",", Names.WORDS.wordid, Names.WORDS.word)
		val toString = { word: String -> String.format("'%s'", Utils.escape(word)) }
		Printers.printInsert(ps, Names.WORDS.TABLE, columns, wordToNID, toString)

		return wordToNID
	}

	/**
	 * Make word-to-nid map
	 *
	 * @param lexes lexes
	 * @return word-to-nid map
	 */
	@JvmStatic
	fun makeWordNIDs(lexes: Collection<Lex>): Map<String, Int> {
		// stream of words
		val wordStream = lexes.stream()
			.map(Lex::lCLemma).distinct()

		// make word-to-nid map
		val map = Utils.makeNIDMap(wordStream)
		assert(map.values.stream().noneMatch { i: Int -> i == 0 })
		return map
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
	@JvmStatic
	fun generateCasedWords(
		ps: PrintStream,
		lexes: Collection<Lex>,
		wordIdToNID: Map<String, Int>
	): Map<String, Int> {

		// make casedword-to-nid map
		val casedWordToNID = makeCasedWordNIDs(lexes)

		// insert map
		val columns = java.lang.String.join(
			",",
			Names.CASEDWORDS.casedwordid,
			Names.CASEDWORDS.casedword,
			Names.CASEDWORDS.wordid
		)
		val toString = Function { casedWord: String ->
			String.format(
				"'%s',%d",
				Utils.escape(casedWord),
				NIDMaps.lookupLC(wordIdToNID, casedWord.lowercase())
			)
		}
		Printers.printInsert(ps, Names.CASEDWORDS.TABLE, columns, casedWordToNID, toString)

		return casedWordToNID
	}

	/**
	 * Make cased_word-to-nid map
	 *
	 * @param lexes lexes
	 * @return cased_word-to-nid map
	 */
	@JvmStatic
	fun makeCasedWordNIDs(lexes: Collection<Lex>): Map<String, Int> {

		// stream of cased words
		val casedWordStream = lexes.stream()
			.filter(Lex::isCased)
			.map { lex: Lex -> lex.lemma }.distinct()

		// make casedword-to-nid map
		val map = Utils.makeNIDMap(casedWordStream)
		assert(map.values.stream().noneMatch { i: Int -> i == 0 })
		return map
	}

	// morphs
	/**
	 * Generate morphs table
	 *
	 * @param ps    print stream
	 * @param lexes lexes
	 * @return morph-to-nid map
	 */
	@JvmStatic
	fun generateMorphs(ps: PrintStream, lexes: Collection<Lex>): Map<String, Int> {

		// make morph-to-nid map
		val morphToNID = makeMorphNIDs(lexes)

		// insert map
		val columns = java.lang.String.join(",", Names.MORPHS.morphid, Names.MORPHS.morph)
		val toString = { morph: String -> String.format("'%s'", Utils.escape(morph)) }
		Printers.printInsert(ps, Names.MORPHS.TABLE, columns, morphToNID, toString)

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
	@JvmStatic
	fun generateLexesMorphs(
		ps: PrintStream,
		lexes: Collection<Lex>,
		lexKeyToNID: Map<out Key, Int>,
		wordToNID: Map<String, Int>,
		morphToNID: Map<String, Int>
	) {
		// stream of lexes
		val lexStream = lexes.stream()
			.filter { lex: Lex -> lex.forms != null && lex.forms!!.isNotEmpty() }
			.sorted(Comparator.comparing { lex: Lex -> lex.lemma })

		// insert map
		val columns = java.lang.String.join(
			",",
			Names.LEXES_MORPHS.morphid,
			Names.LEXES_MORPHS.luid,
			Names.LEXES_MORPHS.wordid,
			Names.LEXES_MORPHS.posid
		)
		val toString = { lex: Lex ->
			val strings = ArrayList<String>()
			val word = lex.lCLemma
			val wordNID = NIDMaps.lookupLC(wordToNID, word)
			val lexNID = NIDMaps.lookup(lexKeyToNID, KeyF.F_W_P_A.Mono.of_t(lex))
			val type = lex.type
			for (morph in lex.forms!!) {
				val morphNID = NIDMaps.lookup(morphToNID, morph)
				strings.add(String.format("%d,%d,%d,'%c'", morphNID, lexNID, wordNID, type))
			}
			strings
		}
		if (!Printers.WITH_COMMENT) {
			Printers.printInserts(ps, Names.LEXES_MORPHS.TABLE, columns, lexStream, toString, false)
		} else {
			val toStrings = { lex: Lex ->
				val strings = toString.invoke(lex)
				val stringsWithComment = ArrayList<Array<String>>()
				val casedWord = lex.lemma
				val type = lex.type
				for ((i, morph) in lex.forms!!.withIndex()) {
					stringsWithComment.add(
						arrayOf(
							strings[i],
							String.format("'%s' '%s' %c", morph, casedWord, type)
						)
					)
				}
				stringsWithComment
			}
			Printers.printInsertsWithComment(ps, Names.LEXES_MORPHS.TABLE, columns, lexStream, toStrings, false)
		}
	}

	/**
	 * Make morphs nid map
	 *
	 * @param lexes lexes
	 * @return morph-to-nid map
	 */
	@JvmStatic
	fun makeMorphNIDs(lexes: Collection<Lex>): Map<String, Int> {
		// stream of morphs
		val morphStream = lexes.stream()
			.filter { lex: Lex -> lex.forms != null && lex.forms!!.isNotEmpty() }
			.flatMap { lex: Lex -> Arrays.stream(lex.forms) }
			.sorted()
			.distinct()

		// make morph-to-nid map
		return Utils.makeNIDMap(morphStream)
	}

	// pronunciations

	/**
	 * Generate pronunciations table
	 *
	 * @param ps    print stream
	 * @param lexes lexes
	 * @return pronunciation-to-nid
	 */
	@JvmStatic
	fun generatePronunciations(ps: PrintStream, lexes: Collection<Lex>): Map<String, Int> {

		// make pronunciation_value-to-nid map
		val pronunciationValueToNID = makePronunciationNIDs(lexes)

		// insert map
		val columns =
			java.lang.String.join(",", Names.PRONUNCIATIONS.pronunciationid, Names.PRONUNCIATIONS.pronunciation)
		val toString = { pronunciationValue: String -> String.format("'%s'", Utils.escape(pronunciationValue)) }
		Printers.printInsert(ps, Names.PRONUNCIATIONS.TABLE, columns, pronunciationValueToNID, toString)

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
	@JvmStatic
	fun generateLexesPronunciations(
		ps: PrintStream,
		lexes: Collection<Lex>,
		lexKeyToNID: Map<out Key, Int>,
		wordToNID: Map<String, Int>,
		pronunciationToNID: Map<String, Int>
	) {
		// stream of lexes
		val lexStream = lexes.stream()
			.filter { lex: Lex -> lex.pronunciations != null && lex.pronunciations!!.isNotEmpty() }
			.sorted(Comparator.comparing { lex: Lex -> lex.lemma })

		// insert map
		val columns = java.lang.String.join(
			",",
			Names.LEXES_PRONUNCIATIONS.pronunciationid,
			Names.LEXES_PRONUNCIATIONS.variety,
			Names.LEXES_PRONUNCIATIONS.luid,
			Names.LEXES_PRONUNCIATIONS.wordid,
			Names.LEXES_PRONUNCIATIONS.posid
		)
		val toString = { lex: Lex ->
			val strings = ArrayList<String>()
			val word = lex.lCLemma
			val wordNID = NIDMaps.lookupLC(wordToNID, word)
			val lexNID = NIDMaps.lookup(lexKeyToNID, KeyF.F_W_P_A.Mono.of_t(lex))
			val type = lex.type
			for (pronunciation in lex.pronunciations!!) {
				val variety = pronunciation.variety
				val value = pronunciation.value
				val pronunciationNID = NIDMaps.lookup(pronunciationToNID, value)
				strings.add(
					String.format(
						"%d,%s,%d,%d,'%c'",
						pronunciationNID,
						if (variety == null) "NULL" else "'$variety'",
						lexNID,
						wordNID,
						type
					)
				)
			}
			strings
		}
		if (!Printers.WITH_COMMENT) {
			Printers.printInserts(ps, Names.LEXES_PRONUNCIATIONS.TABLE, columns, lexStream, toString, false)
		} else {
			val toStrings = { lex: Lex ->
				val strings = toString.invoke(lex)
				val stringsWithComment = ArrayList<Array<String>>()
				val casedWord = lex.lemma
				val type = lex.type
				for ((i, pronunciation) in lex.pronunciations!!.withIndex()) {
					val variety = pronunciation.variety
					val value = pronunciation.value
					stringsWithComment.add(
						arrayOf(
							strings[i],
							String.format(
								"%s%s '%s' %c",
								value,
								if (variety == null) "" else " [$variety]",
								casedWord,
								type
							),
						)
					)
				}
				stringsWithComment
			}
			Printers.printInsertsWithComment(ps, Names.LEXES_PRONUNCIATIONS.TABLE, columns, lexStream, toStrings, false)
		}
	}

	/**
	 * Make pronunciation values nid map
	 *
	 * @param lexes lexes
	 * @return pronunciation-to-nid map
	 */
	@JvmStatic
	fun makePronunciationNIDs(lexes: Collection<Lex>): Map<String, Int> {
		// stream of pronunciation values
		val pronunciationValueStream = lexes.stream()
			.filter { lex: Lex -> lex.pronunciations != null && lex.pronunciations!!.isNotEmpty() }
			.flatMap { lex: Lex -> Arrays.stream(lex.pronunciations) }
			.map { p: Pronunciation -> p.value }
			.sorted()
			.distinct()

		// make pronunciation_value-to-nid map
		return Utils.makeNIDMap(pronunciationValueStream)
	}
}
