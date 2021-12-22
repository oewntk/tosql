/*
 * Copyright (c) $originalComment.match("Copyright \(c\) (\d+)", 1, "-")2021. Bernard Bou.
 */

package org.oewntk.sql.out;

import org.oewntk.model.Lex;
import org.oewntk.model.Pronunciation;

import java.io.PrintStream;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

public class Lexes
{
	private Lexes()
	{
	}

	public static Map<Lex, Integer> generateLexes(final PrintStream ps, final Collection<Lex> lexes, final Map<String, Integer> wordIdToNID, final Map<String, Integer> casedwordIdToNID)
	{
		// stream of lexes
		Stream<Lex> lexStream = lexes.stream();

		// make lex-to-nid map
		Map<Lex, Integer> lexToNID = Utils.makeNIDIdentityMap(lexStream, Lex.comparatorByKeyOEWN);

		// insert map
		final String columns = String.join(",", Names.LEXES.luid, Names.LEXES.posid, Names.LEXES.wordid, Names.LEXES.casedwordid);
		final Function<Lex, String> toString = lex -> {

			String word = lex.getLCLemma();
			int wordNID = NIDMaps.lookupLC(wordIdToNID, word);
			String casedWordNID = NIDMaps.lookupNullable(casedwordIdToNID, lex.getLemma());
			char type = lex.getType();
			return String.format("'%c',%d,%s", type, wordNID, casedWordNID);
		};
		if (!Printers.withComment)
		{
			Printers.printInsert(ps, Names.LEXES.TABLE, columns, lexToNID, Lex.comparatorByKeyOEWN, toString);
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
			Printers.printInsertWithComment(ps, Names.LEXES.TABLE, columns, lexToNID, Lex.comparatorByKeyOEWN, toStrings);
		}
		return lexToNID;
	}

	public static Map<String, Integer> makeWordNIDs(final Collection<Lex> lexes)
	{
		// stream of words
		Stream<String> wordStream = lexes.stream() //
				.map(Lex::getLCLemma)
				.distinct();

		// make word-to-nid map
		var map = Utils.makeNIDMap(wordStream);
		assert map.values().stream().noneMatch(i -> i == 0);
		return map;
	}

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

	public static Map<String, Integer> makeCasedWordNIDs(final Collection<Lex> lexes)
	{
		// stream of cased words
		Stream<String> casedWordStream = lexes.stream() //
				.filter(Lex::isCased) //
				.map(Lex::getLemma)
				.distinct();

		// make casedword-to-nid map
		var map = Utils.makeNIDMap(casedWordStream);
		assert map.values().stream().noneMatch(i -> i == 0);
		return map;
	}

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

	public static Map<String, Integer> makeMorphs(final Collection<Lex> lexes)
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

	public static Map<String, Integer> generateMorphs(final PrintStream ps, final Collection<Lex> lexes)
	{
		// make morph-to-nid map
		Map<String, Integer> morphToNID = makeMorphs(lexes);

		// insert map
		final String columns = String.join(",", Names.MORPHS.morphid, Names.MORPHS.morph);
		final Function<String, String> toString = morph -> String.format("'%s'", Utils.escape(morph));
		Printers.printInsert(ps, Names.MORPHS.TABLE, columns, morphToNID, toString);

		return morphToNID;
	}

	public static void generateMorphMaps(final PrintStream ps, final Collection<Lex> lexes, final Map<Lex, Integer> lexToNID, final Map<String, Integer> wordIdToNID, final Map<String, Integer> morphIdToNID)
	{
		// stream of lexes
		Stream<Lex> lexStream = lexes.stream() //
				.filter(lex -> lex.getForms() != null && lex.getForms().length > 0);

		// insert map
		final String columns = String.join(",", Names.LEXES_MORPHS.morphid, Names.LEXES_MORPHS.luid, Names.LEXES_MORPHS.wordid, Names.LEXES_MORPHS.posid);
		final Function<Lex, List<String>> toString = lex -> {

			var strings = new ArrayList<String>();
			String word = lex.getLCLemma();
			int wordNID = NIDMaps.lookupLC(wordIdToNID, word);
			int lexNID = NIDMaps.lookup(lexToNID, lex);
			char type = lex.getType();
			for (String morph : lex.getForms())
			{
				int morphNID = NIDMaps.lookup(morphIdToNID, morph);
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

	public static Map<String, Integer> makePronunciations(final Collection<Lex> lexes)
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

	public static Map<String, Integer> generatePronunciations(final PrintStream ps, final Collection<Lex> lexes)
	{
		// make pronunciation_value-to-nid map
		Map<String, Integer> pronunciationValueToNID = makePronunciations(lexes);

		// insert map
		final String columns = String.join(",", Names.PRONUNCIATIONS.pronunciationid, Names.PRONUNCIATIONS.pronunciation);
		final Function<String, String> toString = pronunciationValue -> String.format("'%s'", Utils.escape(pronunciationValue));
		Printers.printInsert(ps, Names.PRONUNCIATIONS.TABLE, columns, pronunciationValueToNID, toString);

		return pronunciationValueToNID;
	}

	public static void generatePronunciationMaps(final PrintStream ps, final Collection<Lex> lexes, final Map<Lex, Integer> lexToNID, final Map<String, Integer> wordIdToNID, final Map<String, Integer> pronunciationIdToNID)
	{
		// stream of lexes
		Stream<Lex> lexStream = lexes.stream() //
				.filter(lex -> lex.getPronunciations() != null && lex.getPronunciations().length > 0);

		// insert map
		final String columns = String.join(",", Names.LEXES_PRONUNCIATIONS.pronunciationid, Names.LEXES_PRONUNCIATIONS.variety, Names.LEXES_PRONUNCIATIONS.luid, Names.LEXES_PRONUNCIATIONS.wordid, Names.LEXES_PRONUNCIATIONS.posid);
		final Function<Lex, List<String>> toString = lex -> {

			var strings = new ArrayList<String>();
			String word = lex.getLCLemma();
			int wordNID = NIDMaps.lookupLC(wordIdToNID, word);
			int lexNID = NIDMaps.lookup(lexToNID, lex);
			char type = lex.getType();
			for (Pronunciation pronunciation : lex.getPronunciations())
			{
				String variety = pronunciation.getVariety();
				String value = pronunciation.getValue();
				int pronunciationNID = NIDMaps.lookup(pronunciationIdToNID, value);
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
}
