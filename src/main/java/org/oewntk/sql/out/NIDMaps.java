/*
 * Copyright (c) $originalComment.match("Copyright \(c\) (\d+)", 1, "-")2021. Bernard Bou.
 */

package org.oewntk.sql.out;

import java.io.PrintStream;
import java.util.List;
import java.util.Map;

import org.oewntk.model.Lex;
import org.oewntk.model.Synset;

public class NIDMaps
{
	private NIDMaps()
	{
	}

	static <T> int lookup(Map<T, Integer> map, T key)
	{
		try
		{
			return map.get(key);
		}
		catch (Exception e)
		{
			System.err.printf("lookup of <%s> failed%n", key);
			throw e;
		}
	}

	static <T> String lookupNullable(Map<T, Integer> map, T key)
	{
		Integer value = map.get(key);
		if(value == null)
			return "NULL";
		return value.toString();
	}

	// P R I N T

	public static void printWords(final PrintStream ps, final Map<String, List<Lex>> lexesByLemma)
	{
		Map<String, Integer> wordToNID = Lexes.makeWordNIDs(lexesByLemma);
		print(ps, wordToNID);
	}

	public static void printCasedWords(final PrintStream ps, final Map<String, List<Lex>> lexesByLemma)
	{
		Map<String, Integer> casedToNID = Lexes.makeCasedWordNIDs(lexesByLemma);
		print(ps, casedToNID);
	}

	public static void printMorphs(final PrintStream ps, final Map<String, List<Lex>> lexesByLemma)
	{
		Map<String, Integer> morphToNID = Lexes.makeMorphs(lexesByLemma);
		print(ps, morphToNID);
	}

	public static void printPronunciations(final PrintStream ps, final Map<String, List<Lex>> lexesByLemma)
	{
		Map<String, Integer> pronunciationValueToNID = Lexes.makePronunciations(lexesByLemma);
		print(ps, pronunciationValueToNID);
	}

	public static void printSynsets(final PrintStream ps, final Map<String, Synset> synsetsById)
	{
		Map<String, Integer> synsetIdToNID = Synsets.makeSynsetNIDs(synsetsById);
		print(ps, synsetIdToNID);
	}

	private static void print(final PrintStream ps, final Map<String, Integer> toNID)
	{
		toNID.keySet().stream().sorted().forEach(k -> ps.printf("%s %d%n", k, toNID.get(k)));
	}
}
