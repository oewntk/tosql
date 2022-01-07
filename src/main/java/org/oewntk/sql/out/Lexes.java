/*
 * Copyright (c) $originalComment.match("Copyright \(c\) (\d+)", 1, "-")2021. Bernard Bou.
 */

package org.oewntk.sql.out;

import org.oewntk.model.Key;
import org.oewntk.model.KeyF;
import org.oewntk.model.Lex;
import org.oewntk.model.Pronunciation;

import java.io.PrintStream;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Process lexes
 */
public class Lexes
{
	private Lexes()
	{
	}

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
	public static Map<Key, Integer> generateLexes(final PrintStream ps, final Collection<Lex> lexes, final Map<String, Integer> wordToNID, final Map<String, Integer> casedwordToNID)
	{
		// stream of lex key
		Stream<Key> lexKeyStream = lexes.stream().map(Key.W_P_A::of_t);

		// make lex key-to-nid map
		Map<Key, Integer> lexKeyToNID = Utils.makeNIDMap(lexKeyStream);

		// insert map
		final String columns = String.join(",", Names.LEXES.luid, Names.LEXES.posid, Names.LEXES.wordid, Names.LEXES.casedwordid);
		final Function<Lex, String> toString = lex -> {

			String word = lex.getLCLemma();
			int wordNID = NIDMaps.lookupLC(wordToNID, word);
			String casedWordNID = NIDMaps.lookupNullable(casedwordToNID, lex.getLemma());
			char type = lex.getType();
			return String.format("'%c',%d,%s", type, wordNID, casedWordNID);
		};
		if (!Printers.withComment)
		{
			Printers.printInsert(ps, Names.LEXES.TABLE, columns, lexes, lexKeyToNID, toString);
		}
		else
		{
			final Function<Lex, String[]> toStrings = lex -> {

				String casedWord = lex.getLemma();
				char type = lex.getType();
				return new String[]{ //
						toString.apply(lex), //
						String.format("%c '%s'", type, casedWord), //
				};
			};
			Printers.printInsertWithComment(ps, Names.LEXES.TABLE, columns, lexes, lexKeyToNID, toStrings);
		}
		return lexKeyToNID;
	}

	// words

	/**
	 * Generate words table
	 *
	 * @param ps    print stream
	 * @param lexes lexes
	 * @return word-to-nid map
	 */
	public static Map<String, Integer> generateWords(final PrintStream ps, final Collection<Lex> lexes)
	{
		// make word-to-nid map
		Map<String, Integer> wordToNID = makeWordNIDs(lexes);

		// insert map
		final String columns = String.join(",", Names.WORDS.wordid, Names.WORDS.word);
		final Function<String, String> toString = word -> String.format("'%s'", Utils.escape(word));
		Printers.printInsert(ps, Names.WORDS.TABLE, columns, wordToNID, toString);

		return wordToNID;
	}

	/**
	 * Make word-to-nid map
	 *
	 * @param lexes lexes
	 * @return word-to-nid map
	 */
	public static Map<String, Integer> makeWordNIDs(final Collection<Lex> lexes)
	{
		// stream of words
		Stream<String> wordStream = lexes.stream() //
				.map(Lex::getLCLemma).distinct();

		// make word-to-nid map
		var map = Utils.makeNIDMap(wordStream);
		assert map.values().stream().noneMatch(i -> i == 0);
		return map;
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
	public static Map<String, Integer> generateCasedWords(final PrintStream ps, final Collection<Lex> lexes, final Map<String, Integer> wordIdToNID)
	{
		// make casedword-to-nid map
		Map<String, Integer> casedWordToNID = makeCasedWordNIDs(lexes);

		// insert map
		final String columns = String.join(",", Names.CASEDWORDS.casedwordid, Names.CASEDWORDS.casedword, Names.CASEDWORDS.wordid);
		final Function<String, String> toString = casedWord -> String.format("'%s',%d", Utils.escape(casedWord), NIDMaps.lookupLC(wordIdToNID, casedWord.toLowerCase(Locale.ENGLISH)));
		Printers.printInsert(ps, Names.CASEDWORDS.TABLE, columns, casedWordToNID, toString);

		return casedWordToNID;
	}

	/**
	 * Make cased_word-to-nid map
	 *
	 * @param lexes lexes
	 * @return cased_word-to-nid map
	 */
	public static Map<String, Integer> makeCasedWordNIDs(final Collection<Lex> lexes)
	{
		// stream of cased words
		Stream<String> casedWordStream = lexes.stream() //
				.filter(Lex::isCased) //
				.map(Lex::getLemma).distinct();

		// make casedword-to-nid map
		var map = Utils.makeNIDMap(casedWordStream);
		assert map.values().stream().noneMatch(i -> i == 0);
		return map;
	}

	// morphs

	/**
	 * Generate morphs table
	 *
	 * @param ps    print stream
	 * @param lexes lexes
	 * @return morph-to-nid map
	 */
	public static Map<String, Integer> generateMorphs(final PrintStream ps, final Collection<Lex> lexes)
	{
		// make morph-to-nid map
		Map<String, Integer> morphToNID = makeMorphNIDs(lexes);

		// insert map
		final String columns = String.join(",", Names.MORPHS.morphid, Names.MORPHS.morph);
		final Function<String, String> toString = morph -> String.format("'%s'", Utils.escape(morph));
		Printers.printInsert(ps, Names.MORPHS.TABLE, columns, morphToNID, toString);

		return morphToNID;
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
	public static void generateLexMorphMappings(final PrintStream ps, final Collection<Lex> lexes, final Map<Key, Integer> lexKeyToNID, final Map<String, Integer> wordToNID, final Map<String, Integer> morphToNID)
	{
		// stream of lexes
		Stream<Lex> lexStream = lexes.stream() //
				.filter(lex -> lex.getForms() != null && lex.getForms().length > 0) //
				.sorted(Comparator.comparing(Lex::getLemma));

		// insert map
		final String columns = String.join(",", Names.LEXES_MORPHS.morphid, Names.LEXES_MORPHS.luid, Names.LEXES_MORPHS.wordid, Names.LEXES_MORPHS.posid);
		final Function<Lex, List<String>> toString = lex -> {

			var strings = new ArrayList<String>();
			String word = lex.getLCLemma();
			int wordNID = NIDMaps.lookupLC(wordToNID, word);
			int lexNID = NIDMaps.lookup(lexKeyToNID, KeyF.F_W_P_A.Mono.of_t(lex));
			char type = lex.getType();
			for (String morph : lex.getForms())
			{
				int morphNID = NIDMaps.lookup(morphToNID, morph);
				strings.add(String.format("%d,%d,%d,'%c'", morphNID, lexNID, wordNID, type));
			}
			return strings;
		};
		if (!Printers.withComment)
		{
			Printers.printInserts(ps, Names.LEXES_MORPHS.TABLE, columns, lexStream, toString, false);
		}
		else
		{
			final Function<Lex, List<String[]>> toStrings = lex -> {

				var strings = toString.apply(lex);
				var stringsWithComment = new ArrayList<String[]>();
				String casedWord = lex.getLemma();
				char type = lex.getType();
				int i = 0;
				for (String morph : lex.getForms())
				{
					stringsWithComment.add(new String[]{ //
							strings.get(i), //
							String.format("'%s' '%s' %c", morph, casedWord, type)});
					i++;
				}
				return stringsWithComment;
			};
			Printers.printInsertsWithComment(ps, Names.LEXES_MORPHS.TABLE, columns, lexStream, toStrings, false);
		}
	}

	/**
	 * Make morphs nid map
	 *
	 * @param lexes lexes
	 * @return morph-to-nid map
	 */
	public static Map<String, Integer> makeMorphNIDs(final Collection<Lex> lexes)
	{
		// stream of morphs
		Stream<String> morphStream = lexes.stream() //
				.filter(lex -> lex.getForms() != null && lex.getForms().length > 0) //
				.flatMap(lex -> Arrays.stream(lex.getForms())) //
				.sorted() //
				.distinct();

		// make morph-to-nid map
		return Utils.makeNIDMap(morphStream);
	}

	// pronunciations

	/**
	 * Generate pronunciations table
	 *
	 * @param ps    print stream
	 * @param lexes lexes
	 * @return pronunciation-to-nid
	 */
	public static Map<String, Integer> generatePronunciations(final PrintStream ps, final Collection<Lex> lexes)
	{
		// make pronunciation_value-to-nid map
		Map<String, Integer> pronunciationValueToNID = makePronunciationNIDs(lexes);

		// insert map
		final String columns = String.join(",", Names.PRONUNCIATIONS.pronunciationid, Names.PRONUNCIATIONS.pronunciation);
		final Function<String, String> toString = pronunciationValue -> String.format("'%s'", Utils.escape(pronunciationValue));
		Printers.printInsert(ps, Names.PRONUNCIATIONS.TABLE, columns, pronunciationValueToNID, toString);

		return pronunciationValueToNID;
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
	public static void generateLexPronunciationMappings(final PrintStream ps, final Collection<Lex> lexes, final Map<Key, Integer> lexKeyToNID, final Map<String, Integer> wordToNID, final Map<String, Integer> pronunciationToNID)
	{
		// stream of lexes
		Stream<Lex> lexStream = lexes.stream() //
				.filter(lex -> lex.getPronunciations() != null && lex.getPronunciations().length > 0) //
				.sorted(Comparator.comparing(Lex::getLemma));

		// insert map
		final String columns = String.join(",", Names.LEXES_PRONUNCIATIONS.pronunciationid, Names.LEXES_PRONUNCIATIONS.variety, Names.LEXES_PRONUNCIATIONS.luid, Names.LEXES_PRONUNCIATIONS.wordid, Names.LEXES_PRONUNCIATIONS.posid);
		final Function<Lex, List<String>> toString = lex -> {

			var strings = new ArrayList<String>();
			String word = lex.getLCLemma();
			int wordNID = NIDMaps.lookupLC(wordToNID, word);
			int lexNID = NIDMaps.lookup(lexKeyToNID, KeyF.F_W_P_A.Mono.of_t(lex));
			char type = lex.getType();
			for (Pronunciation pronunciation : lex.getPronunciations())
			{
				String variety = pronunciation.getVariety();
				String value = pronunciation.getValue();
				int pronunciationNID = NIDMaps.lookup(pronunciationToNID, value);
				strings.add(String.format("%d,%s,%d,%d,'%c'", pronunciationNID, variety == null ? "NULL" : "'" + variety + "'", lexNID, wordNID, type));
			}
			return strings;
		};
		if (!Printers.withComment)
		{
			Printers.printInserts(ps, Names.LEXES_PRONUNCIATIONS.TABLE, columns, lexStream, toString, false);
		}
		else
		{
			final Function<Lex, List<String[]>> toStrings = lex -> {

				var strings = toString.apply(lex);
				var stringsWithComment = new ArrayList<String[]>();
				String casedWord = lex.getLemma();
				char type = lex.getType();
				int i = 0;
				for (Pronunciation pronunciation : lex.getPronunciations())
				{
					String variety = pronunciation.getVariety();
					String value = pronunciation.getValue();
					stringsWithComment.add(new String[]{ //
							strings.get(i), //
							String.format("%s%s '%s' %c", value, variety == null ? "" : " [" + variety + "]", casedWord, type),});
					i++;
				}
				return stringsWithComment;
			};
			Printers.printInsertsWithComment(ps, Names.LEXES_PRONUNCIATIONS.TABLE, columns, lexStream, toStrings, false);
		}
	}

	/**
	 * Make pronunciation values nid map
	 *
	 * @param lexes lexes
	 * @return pronunciation-to-nid map
	 */
	public static Map<String, Integer> makePronunciationNIDs(final Collection<Lex> lexes)
	{
		// stream of pronunciation values
		Stream<String> pronunciationValueStream = lexes.stream() //
				.filter(lex -> lex.getPronunciations() != null && lex.getPronunciations().length > 0) //
				.flatMap(lex -> Arrays.stream(lex.getPronunciations())) //
				.map(Pronunciation::getValue) //
				.sorted() //
				.distinct();

		// make pronunciation_value-to-nid map
		return Utils.makeNIDMap(pronunciationValueStream);
	}
}
