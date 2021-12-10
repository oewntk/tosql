/*
 * Copyright (c) 2021. Bernard Bou.
 */

package org.oewntk.sql;

import org.junit.BeforeClass;
import org.junit.Test;
import org.oewntk.sql.out.Names;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ResourceBundle;
import java.util.TreeSet;

import static org.junit.Assert.assertEquals;

public class TestSqlBundle
{
	private static final PrintStream ps = !System.getProperties().containsKey("SILENT") ? System.out : new PrintStream(new OutputStream()
	{
		public void write(int b)
		{
			//DO NOTHING
		}
	});

	private static ResourceBundle bundle;

	private static ResourceBundle bundleCompat;

	@BeforeClass
	public static void init()
	{
		bundle = ResourceBundle.getBundle("Names");
		bundleCompat = ResourceBundle.getBundle("NamesCompat");
	}

	@Test
	public void testBundleKeys()
	{
		assertEquals(bundle.keySet(), bundle.keySet());
	}

	@Test
	public void testBundleValues()
	{
		for (String key : new TreeSet<>(bundle.keySet()))
		{
			String value = bundle.getString(key);
			String valueCompat = bundleCompat.getString(key);
			if (!value.equals(valueCompat))
			{
				ps.printf("[%s] %s <> %s%n", key, value, valueCompat);
			}
		}
	}

	@Test
	public void testClasses()
	{
		assertEquals(Names.WORDS.FILE, "words");
		assertEquals(Names.SENSES.FILE, "senses");
		assertEquals(Names.SYNSETS.FILE, "synsets");
	}
}
