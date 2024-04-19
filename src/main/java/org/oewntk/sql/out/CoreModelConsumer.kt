/*
 * Copyright (c) 2021-2021. Bernard Bou.
 */

package org.oewntk.sql.out;

import org.oewntk.model.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Main class that generates the core WN database in the SQL format
 *
 * @author Bernard Bou
 * @see "https://sqlunet.sourceforge.net/schema.html"
 */
public class CoreModelConsumer implements Consumer<CoreModel>
{
	/**
	 * Output dir
	 */
	protected final File outDir;

	/**
	 * NID maps
	 */
	protected Map<Key, Integer> lexKeyToNID = null;
	protected Map<String, Integer> wordToNID = null;
	protected Map<String, Integer> casedWordToNID = null;
	protected Map<String, Integer> synsetIdToNID = null;

	/**
	 * Constructor
	 *
	 * @param outDir output directory
	 */
	public CoreModelConsumer(final File outDir)
	{
		this.outDir = outDir;
	}

	@Override
	public void accept(final CoreModel model)
	{
		Tracing.psInfo.printf("[CoreModel] %s%n", model.getSource());

		try
		{
			lexes(outDir, model.lexes);
			synsets(outDir, model.synsets);
			senses(outDir, model.senses, model.getSensesById());
			builtins(outDir);
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace(Tracing.psErr);
		}
	}

	/**
	 * Consume lexes
	 *
	 * @param outDir out dir
	 * @param lexes  lexes
	 * @throws FileNotFoundException file not found exception
	 */
	private void lexes(final File outDir, final Collection<Lex> lexes) throws FileNotFoundException
	{
		try (PrintStream ps = new PrintStream(new FileOutputStream(new File(outDir, makeFilename(Names.WORDS.FILE))), true, StandardCharsets.UTF_8))
		{
			wordToNID = Lexes.generateWords(ps, lexes);
		}
		try (PrintStream ps = new PrintStream(new FileOutputStream(new File(outDir, makeFilename(Names.CASEDWORDS.FILE))), true, StandardCharsets.UTF_8))
		{
			casedWordToNID = Lexes.generateCasedWords(ps, lexes, wordToNID);
		}
		try (PrintStream ps = new PrintStream(new FileOutputStream(new File(outDir, makeFilename(Names.LEXES.FILE))), true, StandardCharsets.UTF_8))
		{
			lexKeyToNID = Lexes.generateLexes(ps, lexes, wordToNID, casedWordToNID);
		}
		Map<String, Integer> morphToNID;
		try (PrintStream ps = new PrintStream(new FileOutputStream(new File(outDir, makeFilename(Names.MORPHS.FILE))), true, StandardCharsets.UTF_8))
		{
			morphToNID = Lexes.generateMorphs(ps, lexes);
		}
		try (PrintStream ps = new PrintStream(new FileOutputStream(new File(outDir, makeFilename(Names.LEXES_MORPHS.FILE))), true, StandardCharsets.UTF_8))
		{
			Lexes.generateLexesMorphs(ps, lexes, lexKeyToNID, wordToNID, morphToNID);
		}
		Map<String, Integer> pronunciationToNID;
		try (PrintStream ps = new PrintStream(new FileOutputStream(new File(outDir, makeFilename(Names.PRONUNCIATIONS.FILE))), true, StandardCharsets.UTF_8))
		{
			pronunciationToNID = Lexes.generatePronunciations(ps, lexes);
		}
		try (PrintStream ps = new PrintStream(new FileOutputStream(new File(outDir, makeFilename(Names.LEXES_PRONUNCIATIONS.FILE))), true, StandardCharsets.UTF_8))
		{
			Lexes.generateLexesPronunciations(ps, lexes, lexKeyToNID, wordToNID, pronunciationToNID);
		}
	}

	/**
	 * Consume synsets
	 *
	 * @param outDir  out dir
	 * @param synsets synsets
	 * @throws FileNotFoundException file not found exception
	 */
	private void synsets(final File outDir, final Collection<Synset> synsets) throws FileNotFoundException
	{
		try (PrintStream ps = new PrintStream(new FileOutputStream(new File(outDir, makeFilename(Names.SYNSETS.FILE))), true, StandardCharsets.UTF_8))
		{
			synsetIdToNID = Synsets.generateSynsets(ps, synsets);
		}
		try (PrintStream ps = new PrintStream(new FileOutputStream(new File(outDir, makeFilename(Names.SAMPLES.FILE))), true, StandardCharsets.UTF_8))
		{
			Synsets.generateSamples(ps, synsets, synsetIdToNID);
		}
		try (PrintStream ps = new PrintStream(new FileOutputStream(new File(outDir, makeFilename(Names.SEMRELATIONS.FILE))), true, StandardCharsets.UTF_8))
		{
			Synsets.generateSynsetRelations(ps, synsets, synsetIdToNID);
		}
	}

	/**
	 * Consume senses
	 *
	 * @param outDir     out dir
	 * @param senses     senses
	 * @param sensesById senses mapped by sensekeys
	 * @throws FileNotFoundException file not found exception
	 */
	private void senses(final File outDir, final Collection<Sense> senses, final Map<String, Sense> sensesById) throws FileNotFoundException
	{
		try (PrintStream ps = new PrintStream(new FileOutputStream(new File(outDir, makeFilename(Names.SENSES.FILE))), true, StandardCharsets.UTF_8))
		{
			/* Map<String, Integer> idToNID =*/
			Senses.generateSenses(ps, senses, synsetIdToNID, lexKeyToNID, wordToNID, casedWordToNID);
		}
		try (PrintStream ps = new PrintStream(new FileOutputStream(new File(outDir, makeFilename(Names.LEXRELATIONS.FILE))), true, StandardCharsets.UTF_8))
		{
			Senses.generateSenseRelations(ps, senses, sensesById, synsetIdToNID, lexKeyToNID, wordToNID);
		}
		try (PrintStream ps = new PrintStream(new FileOutputStream(new File(outDir, makeFilename(Names.SENSES_VFRAMES.FILE))), true, StandardCharsets.UTF_8))
		{
			Senses.generateSensesVerbFrames(ps, senses, synsetIdToNID, lexKeyToNID, wordToNID);
		}
		try (PrintStream ps = new PrintStream(new FileOutputStream(new File(outDir, makeFilename(Names.SENSES_ADJPOSITIONS.FILE))), true, StandardCharsets.UTF_8))
		{
			Senses.generateSensesAdjPositions(ps, senses, synsetIdToNID, lexKeyToNID, wordToNID);
		}
	}

	/**
	 * Consume builtins
	 *
	 * @param outDir out dir
	 * @throws FileNotFoundException file not found exception
	 */
	public static void builtins(final File outDir) throws FileNotFoundException
	{
		try (PrintStream ps = new PrintStream(new FileOutputStream(new File(outDir, makeFilename(Names.DOMAINS.TABLE))), true, StandardCharsets.UTF_8))
		{
			BuiltIn.generateDomains(ps);
		}
		try (PrintStream ps = new PrintStream(new FileOutputStream(new File(outDir, makeFilename(Names.POSES.FILE))), true, StandardCharsets.UTF_8))
		{
			BuiltIn.generatePosTypes(ps);
		}
		try (PrintStream ps = new PrintStream(new FileOutputStream(new File(outDir, makeFilename(Names.ADJPOSITIONS.FILE))), true, StandardCharsets.UTF_8))
		{
			BuiltIn.generateAdjectivePositionTypes(ps);
		}
		try (PrintStream ps = new PrintStream(new FileOutputStream(new File(outDir, makeFilename(Names.RELS.FILE))), true, StandardCharsets.UTF_8))
		{
			BuiltIn.generateRelationTypes(ps);
		}
	}

	/**
	 * Make SQL filename
	 *
	 * @param name name
	 * @return filename
	 */
	static protected String makeFilename(String name)
	{
		String fileName = name + ".sql";
		Tracing.psInfo.println(fileName);
		return fileName;
	}
}
