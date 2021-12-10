/*
 * Copyright (c) $originalComment.match("Copyright \(c\) (\d+)", 1, "-")2021. Bernard Bou.
 */

package org.oewntk.sql.out;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import org.oewntk.model.CoreModel;
import org.oewntk.model.Lex;
import org.oewntk.model.Synset;

public class SerializeNIDs
{
	static final String NID_PREFIX = "nid_";

	public static void serializeWordNIDs(final OutputStream os, final Map<String, List<Lex>> lexesByLemma) throws IOException
	{
		Map<String, Integer> wordToNID = Lexes.makeWordNIDs(lexesByLemma);
		serialize(os, wordToNID);
	}

	public static void serializeCasedWordNIDs(final OutputStream os, final Map<String, List<Lex>> lexesByLemma) throws IOException
	{
		Map<String, Integer> casedToNID = Lexes.makeCasedWordNIDs(lexesByLemma);
		serialize(os, casedToNID);
	}

	public static void serializeMorphNIDs(final OutputStream os, final Map<String, List<Lex>> lexesByLemma) throws IOException
	{
		Map<String, Integer> morphToNID = Lexes.makeMorphs(lexesByLemma);
		serialize(os, morphToNID);
	}

	public static void serializePronunciationNIDs(final OutputStream os, final Map<String, List<Lex>> lexesByLemma) throws IOException
	{
		Map<String, Integer> pronunciationValueToNID = Lexes.makeMorphs(lexesByLemma);
		serialize(os, pronunciationValueToNID);
	}

	public static void serializeSynsetNIDs(final OutputStream os, final Map<String, Synset> synsetsById) throws IOException
	{
		Map<String, Integer> synsetIdToNID = Synsets.makeSynsetNIDs(synsetsById);
		serialize(os, synsetIdToNID);
	}

	private static void serialize(final OutputStream os, final Object object) throws IOException
	{
		try (ObjectOutputStream oos = new ObjectOutputStream(os))
		{
			oos.writeObject(object);
		}
	}

	static public void serializeNIDs(final CoreModel model, final File outDir) throws IOException
	{
		try (OutputStream os = new FileOutputStream(new File(outDir, NID_PREFIX + Names.WORDS.FILE)))
		{
			serializeWordNIDs(os, model.lexesByLemma);
		}
		try (OutputStream os = new FileOutputStream(new File(outDir, NID_PREFIX + Names.CASEDWORDS.FILE)))
		{
			serializeCasedWordNIDs(os, model.lexesByLemma);
		}
		try (OutputStream os = new FileOutputStream(new File(outDir, NID_PREFIX + Names.MORPHS.FILE)))
		{
			serializeMorphNIDs(os, model.lexesByLemma);
		}
		try (OutputStream os = new FileOutputStream(new File(outDir, NID_PREFIX + Names.PRONUNCIATIONS.FILE)))
		{
			serializePronunciationNIDs(os, model.lexesByLemma);
		}
		try (OutputStream os = new FileOutputStream(new File(outDir, NID_PREFIX + Names.SYNSETS.FILE)))
		{
			serializeSynsetNIDs(os, model.synsetsById);
		}
	}
}
