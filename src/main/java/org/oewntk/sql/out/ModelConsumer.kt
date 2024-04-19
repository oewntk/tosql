/*
 * Copyright (c) 2021-2021. Bernard Bou.
 */

package org.oewntk.sql.out;

import org.oewntk.model.Model;
import org.oewntk.model.Sense;
import org.oewntk.model.VerbFrame;
import org.oewntk.model.VerbTemplate;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Main class that generates the WN database in the SQL format
 *
 * @author Bernard Bou
 * @see "https://sqlunet.sourceforge.net/schema.html"
 */
public class ModelConsumer implements Consumer<Model>
{
	/**
	 * Output dir
	 */
	private final File outDir;

	/**
	 * Constructor
	 *
	 * @param outDir output directory
	 */
	public ModelConsumer(File outDir)
	{
		this.outDir = outDir;
	}

	@Override
	public void accept(final Model model)
	{
		Tracing.psInfo.printf("[Model] %s%n", Arrays.toString(model.getSources()));

		// core
		CoreModelConsumer coreConsumer = new CoreModelConsumer(outDir);
		coreConsumer.accept(model);

		// verb frames
		try
		{
			frames(outDir, model.verbFrames);
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace(Tracing.psErr);
		}

		// verb templates
		try
		{
			templates(outDir, coreConsumer, model.getSensesById(), model.getVerbTemplatesById());
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace(Tracing.psErr);
		}
	}

	/**
	 * Consume frames
	 *
	 * @param outDir     out dir
	 * @param verbFrames verb frames
	 * @throws FileNotFoundException file not found exception
	 */
	private void frames(final File outDir, final Collection<VerbFrame> verbFrames) throws FileNotFoundException
	{
		try (PrintStream ps = new PrintStream(new FileOutputStream(new File(outDir, CoreModelConsumer.makeFilename(Names.VFRAMES.FILE))), true, StandardCharsets.UTF_8))
		{
			VerbFrames.generateVerbFrames(ps, verbFrames);
		}
	}

	/**
	 * Consume templates
	 *
	 * @param outDir            out dir
	 * @param coreConsumer      core consumer
	 * @param sensesById        senses by id
	 * @param verbTemplatesById verb templates by id
	 * @throws FileNotFoundException file not found exception
	 */
	private void templates(final File outDir, final CoreModelConsumer coreConsumer, final Map<String, Sense> sensesById, final Map<Integer, VerbTemplate> verbTemplatesById) throws FileNotFoundException
	{
		try (PrintStream ps = new PrintStream(new FileOutputStream(new File(outDir, CoreModelConsumer.makeFilename(Names.SENSES_VTEMPLATES.FILE))), true, StandardCharsets.UTF_8))
		{
			Senses.generateSensesVerbTemplates(ps, sensesById, coreConsumer.synsetIdToNID, coreConsumer.lexKeyToNID, coreConsumer.wordToNID);
		}

		final Function<Map.Entry<Integer, VerbTemplate>, String> toString = entry -> String.format("%d, '%s'", entry.getKey(), Utils.escape(entry.getValue().template));
		try (PrintStream ps = new PrintStream(new FileOutputStream(new File(outDir, CoreModelConsumer.makeFilename(Names.VTEMPLATES.FILE))), true, StandardCharsets.UTF_8))
		{
			Utils.generateTable(ps, Names.VTEMPLATES.TABLE, String.join(",", Names.VTEMPLATES.templateid, Names.VTEMPLATES.template), verbTemplatesById, toString);
		}
	}
}
