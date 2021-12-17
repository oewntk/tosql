/*
 * Copyright (c) 2021. Bernard Bou.
 */

package org.oewntk.sql;

import org.junit.Test;
import org.oewntk.sql.out.Variables;

import java.io.OutputStream;
import java.io.PrintStream;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

public class TestSqlVariables
{
	private static final PrintStream ps = !System.getProperties().containsKey("SILENT") ? System.out : new PrintStream(new OutputStream()
	{
		public void write(int b)
		{
			//DO NOTHING
		}
	});

	private final String[] wellFormedInputs = new String[]{ //
			"${senses.file} ${senses_vtemplates.templateid} a ${senses_vframes.file} b ${lexes_pronunciations.wordid} c", //
	};

	private final String[] illFormedInputs = new String[]{ //
			"${senses.file} a $ { }", //
			"${senses.file} b ${var4} ", //
			"${senses.file} c ${senses._file_} ", //
	};

	@Test
	public void wellFormedVarSubstitution()
	{
		for (var input : wellFormedInputs)
		{
			assertNotNull(input);
			try
			{
				String output = Variables.varSubstitution(input);
				ps.println(output);
			}
			catch (IllegalArgumentException e)
			{
				fail("Not expected to fail " + input);
			}
		}
	}

	@Test
	public void illFormedVarSubstitution()
	{
		for (var input : illFormedInputs)
		{
			assertNotNull(input);
			try
			{
				String output = Variables.varSubstitution(input);
				fail("Not expected to succeed " + input + " yields " + output);
			}
			catch (IllegalArgumentException e)
			{
				// Tracing.psErr.println(e);
			}
		}
	}
}
