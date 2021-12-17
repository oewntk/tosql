/*
 * Copyright (c) 2021-2021. Bernard Bou.
 */

package org.oewntk.sql.out;

import org.oewntk.model.CoreModel;
import org.oewntk.model.Lex;
import org.oewntk.model.Sense;
import org.oewntk.model.Synset;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Main class that generates the WN database in the SQL format
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
	protected Map<Lex, Integer> lexToNID = null;
	protected Map<String, Integer> wordToNID = null;
	protected Map<String, Integer> casedWordToNID = null;
	protected Map<String, Integer> synsetIdToNID = null;

	/**
	 * Constructor
	 *
	 * @param outDir output directory
	 */
	public CoreModelConsumer(File outDir)
	{
		this.outDir = outDir;
	}

	@Override
	public void accept(final CoreModel model)
	{
		try
		{
			lexes(outDir, model.getLexesByLemma());
			synsets(outDir, model.getSynsetsById());
			senses(outDir, model.getSensesById());
			builtins(outDir);
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
	}

	private void lexes(final File outDir, final Map<String, List<Lex>> lexesByLemma) throws FileNotFoundException
	{
		try (PrintStream ps = new PrintStream(new FileOutputStream(new File(outDir, makeFilename(Names.WORDS.FILE))), true, StandardCharsets.UTF_8))
		{
			wordToNID = Lexes.generateWords(ps, lexesByLemma);
		}
		try (PrintStream ps = new PrintStream(new FileOutputStream(new File(outDir, makeFilename(Names.CASEDWORDS.FILE))), true, StandardCharsets.UTF_8))
		{
			casedWordToNID = Lexes.generateCasedWords(ps, lexesByLemma, wordToNID);
		}
		try (PrintStream ps = new PrintStream(new FileOutputStream(new File(outDir, makeFilename(Names.LEXES.FILE))), true, StandardCharsets.UTF_8))
		{
			lexToNID = Lexes.generateLexes(ps, lexesByLemma, wordToNID, casedWordToNID);
		}
		Map<String, Integer> morphToNID = null;
		try (PrintStream ps = new PrintStream(new FileOutputStream(new File(outDir, makeFilename(Names.MORPHS.FILE))), true, StandardCharsets.UTF_8))
		{
			morphToNID = Lexes.generateMorphs(ps, lexesByLemma);
		}
		try (PrintStream ps = new PrintStream(new FileOutputStream(new File(outDir, makeFilename(Names.LEXES_MORPHS.FILE))), true, StandardCharsets.UTF_8))
		{
			Lexes.generateMorphMaps(ps, lexesByLemma, lexToNID, wordToNID, morphToNID);
		}
		Map<String, Integer> pronunciationToNID = null;
		try (PrintStream ps = new PrintStream(new FileOutputStream(new File(outDir, makeFilename(Names.PRONUNCIATIONS.FILE))), true, StandardCharsets.UTF_8))
		{
			pronunciationToNID = Lexes.generatePronunciations(ps, lexesByLemma);
		}
		try (PrintStream ps = new PrintStream(new FileOutputStream(new File(outDir, makeFilename(Names.LEXES_PRONUNCIATIONS.FILE))), true, StandardCharsets.UTF_8))
		{
			Lexes.generatePronunciationMaps(ps, lexesByLemma, lexToNID, wordToNID, pronunciationToNID);
		}
	}

	private void synsets(final File outDir, final Map<String, Synset> synsetsById) throws FileNotFoundException
	{
		try (PrintStream ps = new PrintStream(new FileOutputStream(new File(outDir, makeFilename(Names.SYNSETS.FILE))), true, StandardCharsets.UTF_8))
		{
			synsetIdToNID = Synsets.generateSynsets(ps, synsetsById);
		}
		try (PrintStream ps = new PrintStream(new FileOutputStream(new File(outDir, makeFilename(Names.SAMPLES.FILE))), true, StandardCharsets.UTF_8))
		{
			Synsets.generateSamples(ps, synsetsById, synsetIdToNID);
		}
		try (PrintStream ps = new PrintStream(new FileOutputStream(new File(outDir, makeFilename(Names.SYNSETS_SYNSETS.FILE))), true, StandardCharsets.UTF_8))
		{
			Synsets.generateSynsetRelations(ps, synsetsById, synsetIdToNID);
		}
	}

	private void senses(final File outDir, final Map<String, Sense> sensesById) throws FileNotFoundException
	{
		try (PrintStream ps = new PrintStream(new FileOutputStream(new File(outDir, makeFilename(Names.SENSES.FILE))), true, StandardCharsets.UTF_8))
		{
			Map<String, Integer> sensekeyToNID = Senses.generateSenses(ps, sensesById, synsetIdToNID, lexToNID, wordToNID, casedWordToNID);
		}
		try (PrintStream ps = new PrintStream(new FileOutputStream(new File(outDir, makeFilename(Names.SENSES_SENSES.FILE))), true, StandardCharsets.UTF_8))
		{
			Senses.generateSenseRelations(ps, sensesById, synsetIdToNID, lexToNID, wordToNID);
		}
		try (PrintStream ps = new PrintStream(new FileOutputStream(new File(outDir, makeFilename(Names.SENSES_VFRAMES.FILE))), true, StandardCharsets.UTF_8))
		{
			Senses.generateVerbFrames(ps, sensesById, synsetIdToNID, lexToNID, wordToNID);
		}
		try (PrintStream ps = new PrintStream(new FileOutputStream(new File(outDir, makeFilename(Names.SENSES_ADJPOSITIONS.FILE))), true, StandardCharsets.UTF_8))
		{
			Senses.generateAdjPositions(ps, sensesById, synsetIdToNID, lexToNID, wordToNID);
		}
	}

	public static void builtins(final File outDir) throws FileNotFoundException
	{
		try (PrintStream ps = new PrintStream(new FileOutputStream(new File(outDir, makeFilename(Names.DOMAINS.TABLE))), true, StandardCharsets.UTF_8))
		{
			BuiltIn.generateDomains(ps);
		}
		try (PrintStream ps = new PrintStream(new FileOutputStream(new File(outDir, makeFilename(Names.VFRAMES.FILE))), true, StandardCharsets.UTF_8))
		{
			BuiltIn.generateVerbFrames(ps);
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

	static protected String makeFilename(String name)
	{
		String fileName = name + ".sql";
		Tracing.psInfo.println(fileName);
		return fileName;
	}
}
