/*
 * Copyright (c) 2021-2021. Bernard Bou.
 */

package org.oewntk.sql.out;

import org.oewntk.model.Model;
import org.oewntk.model.Sense;
import org.oewntk.model.VerbTemplate;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
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
	 * Logging info
	 */
	private final PrintStream ps;
	public ModelConsumer(File outDir, PrintStream ps)
	{
		this.outDir = outDir;
		this.ps = ps;
	}

	@Override
	public void accept(final Model model)
	{
		CoreModelConsumer coreConsumer = new CoreModelConsumer(outDir, ps);
		coreConsumer.accept(model);

		try
		{
			templates(outDir, coreConsumer, model.getSensesById(), model.getVerbTemplatesById());
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
	}

	private void templates(final File outDir, final CoreModelConsumer coreConsumer, final Map<String, Sense> sensesById, final Map<Integer, VerbTemplate> verbTemplatesById) throws FileNotFoundException
	{
		try (PrintStream ps = new PrintStream(new FileOutputStream(new File(outDir, CoreModelConsumer.makeFilename(Names.SENSES_VTEMPLATES.FILE))), true, StandardCharsets.UTF_8))
		{
			Senses.generateVerbTemplates(ps, sensesById, coreConsumer.synsetIdToNID, coreConsumer.lexToNID, coreConsumer.wordToNID);
		}
		final Function<Map.Entry<Integer, VerbTemplate>, String> toString = entry -> String.format("%d, '%s'", entry.getKey(), Utils.escape(entry.getValue().getTemplate()));
		try (PrintStream ps = new PrintStream(new FileOutputStream(new File(outDir, CoreModelConsumer.makeFilename(Names.VTEMPLATES.FILE))), true, StandardCharsets.UTF_8))
		{
			Utils.generateTable(ps, Names.VTEMPLATES.TABLE, String.join(",", Names.VTEMPLATES.templateid, Names.VTEMPLATES.template), verbTemplatesById, toString);
		}
	}
}
