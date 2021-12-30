/*
 * Copyright (c) 2021-2021. Bernard Bou.
 */

package org.oewntk.sql.out;

import org.oewntk.sql.out.CoreModelConsumer;

import java.io.File;
import java.io.IOException;

/**
 * Main class that generates the WN database in the WNDB format as per wndb(5WN)
 *
 * @author Bernard Bou
 * @see "https://wordnet.princeton.edu/documentation/wndb5wn"
 */
public class GrindBuiltIn
{
	/**
	 * Main entry point
	 *
	 * @param args command-line arguments [-compat:lexid] [-compat:pointer] yamlDir [outputDir]
	 * @throws IOException io
	 */
	public static void main(String[] args) throws IOException
	{
		// Output
		File outDir = new File(args[0]);
		if (!outDir.exists())
		{
			//noinspection ResultOfMethodCallIgnored
			outDir.mkdirs();
		}

		System.out.println("[Output] " + outDir.getAbsolutePath());

		// Process
		CoreModelConsumer.builtins(outDir);
	}
}
