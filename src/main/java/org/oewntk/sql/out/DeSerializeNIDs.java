/*
 * Copyright (c) 2021. Bernard Bou.
 */

package org.oewntk.sql.out;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Deserialize ID to Numeric IDs maps
 */
public class DeSerializeNIDs
{
	/**
	 * Deserialize id-to_nid maps
	 *
	 * @param inDir input directory
	 * @return id-to-nid map indexed by name
	 * @throws IOException            io exception
	 * @throws ClassNotFoundException class not found exception
	 */
	public static Map<String, Map<String, Integer>> deserializeNIDs(final File inDir) throws IOException, ClassNotFoundException
	{
		Map<String, Map<String, Integer>> maps = new HashMap<>();
		try (InputStream is = new FileInputStream(new File(inDir, SerializeNIDs.NID_PREFIX + Names.WORDS.FILE)))
		{
			var m = DeSerializeNIDs.deSerializeNIDs(is);
			maps.put(Names.WORDS.FILE, m);
		}
		try (InputStream is = new FileInputStream(new File(inDir, SerializeNIDs.NID_PREFIX + Names.CASEDWORDS.FILE)))
		{
			var m = DeSerializeNIDs.deSerializeNIDs(is);
			maps.put(Names.CASEDWORDS.FILE, m);
		}
		try (InputStream is = new FileInputStream(new File(inDir, SerializeNIDs.NID_PREFIX + Names.MORPHS.FILE)))
		{
			var m = DeSerializeNIDs.deSerializeNIDs(is);
			maps.put(Names.MORPHS.FILE, m);
		}
		try (InputStream is = new FileInputStream(new File(inDir, SerializeNIDs.NID_PREFIX + Names.PRONUNCIATIONS.FILE)))
		{
			var m = DeSerializeNIDs.deSerializeNIDs(is);
			maps.put(Names.PRONUNCIATIONS.FILE, m);
		}
		try (InputStream is = new FileInputStream(new File(inDir, SerializeNIDs.NID_PREFIX + Names.SENSES.FILE)))
		{
			var m = DeSerializeNIDs.deSerializeNIDs(is);
			maps.put(Names.SENSES.FILE, m);
		}
		try (InputStream is = new FileInputStream(new File(inDir, SerializeNIDs.NID_PREFIX + Names.SYNSETS.FILE)))
		{
			var m = DeSerializeNIDs.deSerializeNIDs(is);
			maps.put(Names.SYNSETS.FILE, m);
		}
		return maps;
	}

	/**
	 * Deserialize id-to_nid map
	 *
	 * @param is input stream
	 * @return id-to-nid map
	 * @throws IOException            io exception
	 * @throws ClassNotFoundException class not found exception
	 */
	public static Map<String, Integer> deSerializeNIDs(final InputStream is) throws IOException, ClassNotFoundException
	{
		return (Map<String, Integer>) deSerialize(is);
	}

	/**
	 * Deserialize object
	 *
	 * @param is input stream
	 * @return object
	 * @throws IOException            io exception
	 * @throws ClassNotFoundException class not found exception
	 */
	private static Object deSerialize(final InputStream is) throws IOException, ClassNotFoundException
	{
		try (ObjectInputStream ois = new ObjectInputStream(is))
		{
			return ois.readObject();
		}
	}

	/**
	 * Main
	 *
	 * @param args command-line arguments
	 * @throws IOException            io exception
	 * @throws ClassNotFoundException class not found exception
	 */
	static public void main(String[] args) throws IOException, ClassNotFoundException
	{
		File inDir = new File(args[0]);
		if (!inDir.isDirectory())
		{
			System.exit(1);
		}

		Map<String, Map<String, Integer>> maps = deserializeNIDs(inDir);
		System.out.printf("%s %d%n", Names.WORDS.FILE, maps.get(Names.WORDS.FILE).size());
		System.out.printf("%s %d%n", Names.CASEDWORDS.FILE, maps.get(Names.CASEDWORDS.FILE).size());
		System.out.printf("%s %d%n", Names.MORPHS.FILE, maps.get(Names.MORPHS.FILE).size());
		System.out.printf("%s %d%n", Names.PRONUNCIATIONS.FILE, maps.get(Names.PRONUNCIATIONS.FILE).size());
		System.out.printf("%s %d%n", Names.SYNSETS.FILE, maps.get(Names.SYNSETS.FILE).size());
	}
}
