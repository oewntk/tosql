/*
 * Copyright (c) $originalComment.match("Copyright \(c\) (\d+)", 1, "-")2021. Bernard Bou.
 */

package org.oewntk.sql.out;

import org.oewntk.model.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Locale;
import java.util.Map;

public class NIDMaps
{
	private NIDMaps()
	{
	}

	static <T> int lookup(final Map<T, Integer> map, T key)
	{
		try
		{
			int nid = map.get(key);
			assert nid != 0;
			return nid;
		}
		catch (Exception e)
		{
			Tracing.psErr.printf("lookup of <%s> failed%n", key);
			throw e;
		}
	}

	public static int lookup(final Map<Key, Integer> map, final Key key)
	{
		try
		{
			int nid = map.get(key);
			assert nid != 0;
			return nid;
		}
		catch (Exception e)
		{
			Tracing.psErr.printf("lookup of <%s> failed%n", key);
			throw e;
		}
	}

	static int lookupLC(Map<String, Integer> map, String key)
	{
		assert key.equals(key.toLowerCase(Locale.ENGLISH));
		return lookup(map, key);
	}

	static <T> String lookupNullable(Map<T, Integer> map, T key)
	{
		Integer value = map.get(key);
		if (value == null)
		{
			return "NULL";
		}
		return value.toString();
	}

	// P R I N T

	public static void printWords(final PrintStream ps, final Collection<Lex> lexes)
	{
		Map<String, Integer> wordToNID = Lexes.makeWordNIDs(lexes);
		print(ps, wordToNID);
	}

	public static void printCasedWords(final PrintStream ps, final Collection<Lex> lexes)
	{
		Map<String, Integer> casedToNID = Lexes.makeCasedWordNIDs(lexes);
		print(ps, casedToNID);
	}

	public static void printMorphs(final PrintStream ps, final Collection<Lex> lexes)
	{
		Map<String, Integer> morphToNID = Lexes.makeMorphs(lexes);
		print(ps, morphToNID);
	}

	public static void printPronunciations(final PrintStream ps, final Collection<Lex> lexes)
	{
		Map<String, Integer> pronunciationValueToNID = Lexes.makePronunciations(lexes);
		print(ps, pronunciationValueToNID);
	}

	public static void printSynsets(final PrintStream ps, final Collection<Synset> synsets)
	{
		Map<String, Integer> synsetIdToNID = Synsets.makeSynsetNIDs(synsets);
		print(ps, synsetIdToNID);
	}

	private static void printSenses(final PrintStream ps, final Collection<Sense> senses)
	{
		Map<String, Integer> synsetIdToNID = Senses.makeSenseNIDs(senses);
		print(ps, synsetIdToNID);
	}

	private static void print(final PrintStream ps, final Map<String, Integer> toNID)
	{
		toNID.keySet().stream().sorted().forEach(k -> ps.printf("%s %d%n", k, toNID.get(k)));
	}

	public static void printMaps(final CoreModel model, final File outDir) throws IOException
	{
		try (PrintStream ps = new PrintStream(new FileOutputStream(new File(outDir, Names.WORDS.FILE)), true, StandardCharsets.UTF_8))
		{
			NIDMaps.printWords(ps, model.lexes);
		}
		try (PrintStream ps = new PrintStream(new FileOutputStream(new File(outDir, Names.CASEDWORDS.FILE)), true, StandardCharsets.UTF_8))
		{
			NIDMaps.printCasedWords(ps, model.lexes);
		}
		try (PrintStream ps = new PrintStream(new FileOutputStream(new File(outDir, Names.MORPHS.FILE)), true, StandardCharsets.UTF_8))
		{
			NIDMaps.printMorphs(ps, model.lexes);
		}
		try (PrintStream ps = new PrintStream(new FileOutputStream(new File(outDir, Names.PRONUNCIATIONS.FILE)), true, StandardCharsets.UTF_8))
		{
			NIDMaps.printPronunciations(ps, model.lexes);
		}
		try (PrintStream ps = new PrintStream(new FileOutputStream(new File(outDir, Names.SYNSETS.FILE)), true, StandardCharsets.UTF_8))
		{
			NIDMaps.printSynsets(ps, model.synsets);
		}
		try (PrintStream ps = new PrintStream(new FileOutputStream(new File(outDir, Names.SENSES.FILE)), true, StandardCharsets.UTF_8))
		{
			NIDMaps.printSenses(ps, model.senses);
		}
	}
}
