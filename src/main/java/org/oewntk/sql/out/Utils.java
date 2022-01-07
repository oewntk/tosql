/*
 * Copyright (c) $originalComment.match("Copyright \(c\) (\d+)", 1, "-")2021. Bernard Bou.
 */

package org.oewntk.sql.out;

import java.io.PrintStream;
import java.util.AbstractMap.SimpleEntry;
import java.util.Comparator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

/**
 * Utilities
 */
public class Utils
{
	/**
	 * Merging function, keep existing element against replacement
	 */
	private static final BinaryOperator<Integer> mergingKeepFunction = (existing, replacement) -> {
		if (existing.equals(replacement))
		{
			throw new IllegalArgumentException(existing + "," + replacement);
		}
		return existing;
	};

	/**
	 * Merging function, keep replacement element against replacement
	 */
	private static final BinaryOperator<Integer> mergingReplaceFunction = (existing, replacement) -> {
		if (existing.equals(replacement))
		{
			throw new IllegalArgumentException(existing + "," + replacement);
		}
		return replacement;
	};

	// map factory

	/**
	 * Make NID
	 * To be used with objects that support equal and comparable
	 *
	 * @param stream must be distinct
	 * @param <T>    type of stream elements
	 * @return map if object-to-NID pairs
	 */
	public static <T> Map<T, Integer> makeNIDMap(final Stream<T> stream)
	{
		//final AtomicInteger index = new AtomicInteger();
		//index.set(0);
		final int[] i = {0};
		return stream //
				.sequential() //
				.sorted() //
				.peek(e -> ++i[0]) //
				.map(item -> new SimpleEntry<>(item, i[0] /* index.addAndGet(1) */)) //
				.collect(toMap(SimpleEntry::getKey, SimpleEntry::getValue, (existing, replacement) -> {
					throw new IllegalArgumentException(existing + "," + replacement);
				}, TreeMap::new));
	}

	/**
	 * Generate table
	 *
	 * @param ps       print stream
	 * @param table    table name
	 * @param columns  column name
	 * @param byNid    nid-to-value map, values mapped by nid
	 * @param toString nid-to-value pair stringifier
	 * @param <T>      type of values
	 */
	public static <T> void generateTable(final PrintStream ps, final String table, final String columns, final Map<Integer, T> byNid, final Function<Entry<Integer, T>, String> toString)
	{
		// make object-to-nid map
		Stream<Entry<Integer, T>> stream = byNid.entrySet().stream() //
				.sorted(Comparator.comparingInt(Entry::getKey));

		// insert map
		Printers.printInsert(ps, table, columns, stream, toString, false);
	}

	// escape

	/**
	 * Escape string for it to be handled by SQL
	 *
	 * @param str string
	 * @return SQL escaped string
	 */
	public static String escape(final String str)
	{
		return str.replace("'", "''");
	}
}
