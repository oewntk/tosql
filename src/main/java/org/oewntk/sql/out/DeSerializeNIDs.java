/*
 * Copyright (c) 2021. Bernard Bou.
 */

package org.oewntk.sql.out;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.Map;

public class DeSerializeNIDs
{
	public static Map<String, Map<String, Integer>> deserializeNIDs(final File inDir) throws IOException, ClassNotFoundException
	{
		Map<String, Map<String, Integer>> maps = new HashMap<>();
		try (InputStream os = new FileInputStream(new File(inDir, SerializeNIDs.NID_PREFIX + Names.WORDS.FILE)))
		{
			var m = DeSerializeNIDs.deSerializeNIDs(os);
			maps.put(Names.WORDS.FILE, m);
		}
		try (InputStream os = new FileInputStream(new File(inDir, SerializeNIDs.NID_PREFIX + Names.CASEDWORDS.FILE)))
		{
			var m = DeSerializeNIDs.deSerializeNIDs(os);
			maps.put(Names.CASEDWORDS.FILE, m);
		}
		try (InputStream os = new FileInputStream(new File(inDir, SerializeNIDs.NID_PREFIX + Names.MORPHS.FILE)))
		{
			var m = DeSerializeNIDs.deSerializeNIDs(os);
			maps.put(Names.MORPHS.FILE, m);
		}
		try (InputStream os = new FileInputStream(new File(inDir, SerializeNIDs.NID_PREFIX + Names.PRONUNCIATIONS.FILE)))
		{
			var m = DeSerializeNIDs.deSerializeNIDs(os);
			maps.put(Names.PRONUNCIATIONS.FILE, m);
		}
		try (InputStream os = new FileInputStream(new File(inDir, SerializeNIDs.NID_PREFIX + Names.SYNSETS.FILE)))
		{
			var m = DeSerializeNIDs.deSerializeNIDs(os);
			maps.put(Names.SYNSETS.FILE, m);
		}
		return maps;
	}

	public static Map<String, Integer> deSerializeNIDs(final InputStream is) throws IOException, ClassNotFoundException
	{
		return (Map<String, Integer>) deSerialize(is);
	}

	private static Object deSerialize(final InputStream is) throws IOException, ClassNotFoundException
	{
		try (ObjectInputStream ois = new ObjectInputStream(is))
		{
			return ois.readObject();
		}
	}
}
