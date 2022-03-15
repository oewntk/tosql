/*
 * Copyright (c) 2021. Bernard Bou.
 */

package org.oewntk.sql;

import org.junit.BeforeClass;
import org.junit.Test;
import org.oewntk.model.Tracing;
import org.oewntk.sql.out.Variables;

import java.io.PrintStream;
import java.util.ResourceBundle;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

public class TestSqlVariables
{
	private static final PrintStream ps = !System.getProperties().containsKey("SILENT") ? Tracing.psInfo : Tracing.psNull;

	private final String[] wellFormedInputs = new String[]{ //
			"${senses.file} ${senses_vtemplates.templateid} a ${senses_vframes.file} b ${lexes_pronunciations.wordid} c", //
	};

	private final String[] illFormedInputs = new String[]{ //
			"${senses.file} a $ { }", //
			"${senses.file} b ${var4} ", //
			"${senses.file} c ${senses._file_} ", //
	};

	static Variables variables;

	@BeforeClass
	public static void init()
	{
		ResourceBundle bundle = ResourceBundle.getBundle("Names");
		variables = new Variables(bundle);
	}

	@Test
	public void wellFormedVarSubstitution()
	{
		for (var input : wellFormedInputs)
		{
			assertNotNull(input);
			try
			{
				String output = variables.varSubstitution(input, false);
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
				String output = variables.varSubstitution(input, false);
				fail("Not expected to succeed " + input + " yields " + output);
			}
			catch (IllegalArgumentException e)
			{
				// Tracing.psErr.println(e);
			}
		}
	}
}
