/*
 * Copyright (c) $originalComment.match("Copyright \(c\) (\d+)", 1, "-")2021. Bernard Bou.
 */

package org.oewntk.sql.out;

import java.io.PrintStream;
import java.util.AbstractMap;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

public class Utils
{
	// map factory

	public static <T> Map<T, Integer> makeMap(final Stream<T> stream)
	{
		//final AtomicInteger index = new AtomicInteger();
		//index.set(0);
		final int[] i = { 0 };
		//noinspection UnnecessaryLocalVariable
		Map<T, Integer> map = stream //
				.sequential() //
				.peek(e -> i[0]++) //
				.map(item -> new SimpleEntry<>(item, i[0] /* index.addAndGet(1) */)) //
				.collect(toMap(SimpleEntry::getKey, SimpleEntry::getValue, (e1, e2) -> 0, TreeMap::new));
		// map.forEach((k, v) -> System.out.printf("%s %s%n", k, v));
		return map;
	}

	public static <T extends Comparable<T>> Map<T, Integer> makeSortedMap(final Stream<T> stream)
	{
		//final AtomicInteger index = new AtomicInteger();
		//index.set(0);
		final int[] i = { 0 };
		//noinspection UnnecessaryLocalVariable
		Map<T, Integer> map = stream //
				.sequential() //
				.peek(e -> i[0]++) //
				.map(item -> new AbstractMap.SimpleEntry<>(item, i[0] /* index.addAndGet(1) */)) //
				.collect(toMap(SimpleEntry::getKey, SimpleEntry::getValue, (existing, replacement) -> {
					if (existing.equals(replacement))
					{
						throw new IllegalArgumentException(existing + "," + replacement);
					}
					return existing;
				}, TreeMap::new));
		// map.forEach((k, v) -> System.out.printf("%s %s%n", k, v));
		return map;
	}

	public static <T> void generateTable(final PrintStream ps, final String table, final String columns, final Map<Integer, T> byId,
			final Function<Entry<Integer, T>, String> toString)
	{
		// make object-to-nid map
		Stream<Entry<Integer, T>> stream = byId.entrySet().stream();

		// insert map
		Printers.printInsert(ps, table, columns, stream, toString, false);
	}

	// escape

	public static String escape(final String str)
	{
		return str.replace("'", "''");
	}
}
