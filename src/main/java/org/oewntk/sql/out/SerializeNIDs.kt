/*
 * Copyright (c) $originalComment.match("Copyright \(c\) (\d+)", 1, "-")2021. Bernard Bou.
 */

package org.oewntk.sql.out;

import org.oewntk.model.CoreModel;
import org.oewntk.model.Lex;
import org.oewntk.model.Sense;
import org.oewntk.model.Synset;

import java.io.*;
import java.util.AbstractMap.SimpleEntry;
import java.util.Collection;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

/**
 * Serialize ID to Numeric IDs maps
 */
public class SerializeNIDs
{
	static final String NID_PREFIX = "nid_";

	private static final String SENSEKEYS_WORDS_SYNSETS_FILE = "sensekeys_words_synsets";

	/**
	 * Serialize words id-to-nid map
	 *
	 * @param os    output stream
	 * @param lexes lexes
	 * @throws IOException io exception
	 */
	public static void serializeWordNIDs(final OutputStream os, final Collection<Lex> lexes) throws IOException
	{
		Map<String, Integer> wordToNID = Lexes.makeWordNIDs(lexes);
		serialize(os, wordToNID);
	}

	/**
	 * Serialize cased words id-to-nid map
	 *
	 * @param os    output stream
	 * @param lexes lexes
	 * @throws IOException io exception
	 */
	public static void serializeCasedWordNIDs(final OutputStream os, final Collection<Lex> lexes) throws IOException
	{
		Map<String, Integer> casedToNID = Lexes.makeCasedWordNIDs(lexes);
		serialize(os, casedToNID);
	}

	/**
	 * Serialize morphs id-to-nid map
	 *
	 * @param os    output stream
	 * @param lexes lexes
	 * @throws IOException io exception
	 */
	public static void serializeMorphNIDs(final OutputStream os, final Collection<Lex> lexes) throws IOException
	{
		Map<String, Integer> morphToNID = Lexes.makeMorphNIDs(lexes);
		serialize(os, morphToNID);
	}

	/**
	 * Serialize pronunciations id-to-nid map
	 *
	 * @param os    output stream
	 * @param lexes lexes
	 * @throws IOException io exception
	 */
	public static void serializePronunciationNIDs(final OutputStream os, final Collection<Lex> lexes) throws IOException
	{
		Map<String, Integer> pronunciationValueToNID = Lexes.makeMorphNIDs(lexes);
		serialize(os, pronunciationValueToNID);
	}

	/**
	 * Serialize senses id-to-nid map
	 *
	 * @param os     output stream
	 * @param senses senses
	 * @throws IOException io exception
	 */
	private static void serializeSensesNIDs(final OutputStream os, final Collection<Sense> senses) throws IOException
	{
		Map<String, Integer> senseToNID = Senses.makeSenseNIDs(senses);
		serialize(os, senseToNID);
	}

	/**
	 * Serialize id-to-nid map
	 *
	 * @param os      output stream
	 * @param synsets synsets
	 * @throws IOException io exception
	 */
	public static void serializeSynsetNIDs(final OutputStream os, final Collection<Synset> synsets) throws IOException
	{
		Map<String, Integer> synsetIdToNID = Synsets.makeSynsetNIDs(synsets);
		serialize(os, synsetIdToNID);
	}

	/**
	 * Serialize object
	 *
	 * @param os     output stream
	 * @param object object
	 * @throws IOException io exception
	 */
	private static void serialize(final OutputStream os, final Object object) throws IOException
	{
		try (ObjectOutputStream oos = new ObjectOutputStream(os))
		{
			oos.writeObject(object);
		}
	}

	/**
	 * Serialize sensekey to wordnid-synsetnid
	 *
	 * @param os    output stream
	 * @param model model
	 * @throws IOException io exception
	 */
	private static void serializeSensekeysWordsSynsetsNIDs(final OutputStream os, final CoreModel model) throws IOException
	{
		Map<String, Integer> wordToNID = Lexes.makeWordNIDs(model.lexes);
		Map<String, Integer> synsetIdToNID = Synsets.makeSynsetNIDs(model.synsets);
		var m = model.senses.stream() //
				.map(s -> new SimpleEntry<>(s.getSenseKey(), new SimpleEntry<>(wordToNID.get(s.getLCLemma()), synsetIdToNID.get(s.synsetId)))) //
				.collect(toMap(SimpleEntry::getKey, SimpleEntry::getValue, (e,n)->{if (!e.equals(n)) System.err.printf("existing %s -> new %s%n", e, n); return e;}));
		serialize(os, m);
	}

	/**
	 * Serialize all id-to-nid maps
	 *
	 * @param model  model
	 * @param outDir output dir
	 * @throws IOException io exception
	 */
	static public void serializeNIDs(final CoreModel model, final File outDir) throws IOException
	{
		try (OutputStream os = new FileOutputStream(new File(outDir, NID_PREFIX + Names.WORDS.FILE + ".ser")))
		{
			serializeWordNIDs(os, model.lexes);
		}
		try (OutputStream os = new FileOutputStream(new File(outDir, NID_PREFIX + Names.CASEDWORDS.FILE + ".ser")))
		{
			serializeCasedWordNIDs(os, model.lexes);
		}
		try (OutputStream os = new FileOutputStream(new File(outDir, NID_PREFIX + Names.MORPHS.FILE + ".ser")))
		{
			serializeMorphNIDs(os, model.lexes);
		}
		try (OutputStream os = new FileOutputStream(new File(outDir, NID_PREFIX + Names.PRONUNCIATIONS.FILE + ".ser")))
		{
			serializePronunciationNIDs(os, model.lexes);
		}
		try (OutputStream os = new FileOutputStream(new File(outDir, NID_PREFIX + Names.SENSES.FILE + ".ser")))
		{
			serializeSensesNIDs(os, model.senses);
		}
		try (OutputStream os = new FileOutputStream(new File(outDir, NID_PREFIX + Names.SYNSETS.FILE + ".ser")))
		{
			serializeSynsetNIDs(os, model.synsets);
		}
		try (OutputStream os = new FileOutputStream(new File(outDir, SENSEKEYS_WORDS_SYNSETS_FILE + ".ser")))
		{
			serializeSensekeysWordsSynsetsNIDs(os, model);
		}
	}
}
