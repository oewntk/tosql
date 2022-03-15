/*
 * Copyright (c) 2021-2021. Bernard Bou.
 */

package org.oewntk.sql.out;

import java.io.*;
import java.net.URL;

/**
 * Main class that generates the sources data
 *
 * @author Bernard Bou
 * @see "https://sqlunet.sourceforge.net/schema.html"
 */
public class SourcesGenerator
{
	/**
	 * Main entry point
	 *
	 * @param args command-line arguments
	 * @throws IOException io exception
	 */
	public static void main(String[] args) throws IOException
	{
		new SourcesGenerator().sources(args);
	}

	/**
	 * Generate sources
	 *
	 * @param args command-line arguments
	 * @throws IOException io exception
	 */
	private void sources(final String[] args) throws IOException
	{
		String arg1 = args[0];
		File outdir = new File(arg1);
		if (!outdir.exists())
		{
			// System.err.println("Output to new dir " + arg1);
			//noinspection ResultOfMethodCallIgnored
			outdir.mkdirs();
		}
		final URL url = SourcesGenerator.class.getResource("/wn/sqltemplates/data/sources.sql");
		assert url != null;
		try (InputStream is = url.openStream(); OutputStream os = new FileOutputStream(new File(outdir, "sources.sql")))
		{
			is.transferTo(os);
		}
	}
}
