/*
 * Copyright (c) 2021-2021. Bernard Bou.
 */

package org.oewntk.sql.out;

import org.oewntk.sql.out.Variables;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

/**
 * Main class that generates the SQL schema
 *
 * @author Bernard Bou
 * @see "https://sqlunet.sourceforge.net/schema.html"
 */
public class SchemaGenerator
{
	/**
	 * Main entry point
	 *
	 * @param args command-line arguments [-compat:lexid] [-compat:pointer] yamlDir [outputDir]
	 * @throws IOException io
	 */
	public static void main(String[] args) throws IOException
	{
		File output = null;

		// Output
		String arg1 = args[0];
		if (! "-".equals(arg1))
		{
			if (arg1.endsWith(".sql"))
			{
				System.err.println("Output to file " + arg1);
				output = new File(arg1);
				if (output.exists())
				{
					System.err.println("Overwrite " + output.getAbsolutePath());
					System.exit(1);
				}
				//noinspection ResultOfMethodCallIgnored
				output.createNewFile();
			}
			else
			{
				output = new File(arg1);
				if (!output.exists() || !output.isDirectory())
				{
					// System.err.println("Output to new dir " + arg1);
					//noinspection ResultOfMethodCallIgnored
					output.mkdirs();
				}
			}
		}

		// Input
		String[] inputs = Arrays.copyOfRange(args, 1, args.length);

		// Single output if console or file
		if (output == null || output.isFile())
		{
			try (PrintStream ps = output == null ? System.out : new PrintStream(output))
			{
				for (var input : inputs)
				{
					// System.err.println(arg);
					File file = new File(input);
					Variables.varSubstitutionInFile(file, ps, true);
				}
			}
		}
		// Multiple outputs if output is directory
		else if (output.isDirectory())
		{
			for (var input : inputs)
			{
				File file = new File(input);
				Path path = Paths.get(file.getAbsolutePath());
				String inputDir = path.getParent().toString();

				String name = file.getName();
				if (output.getAbsolutePath().equals(inputDir))
				{
					name = "@" + name;
				}
				File output2 = new File(output, name);
				try (PrintStream ps = new PrintStream(output2))
				{
					Variables.varSubstitutionInFile(file, ps, true);
				}
			}
		}
		else
		{
			System.err.println("Internal error");
		}
	}
}
