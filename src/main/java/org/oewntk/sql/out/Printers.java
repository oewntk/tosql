/*
 * Copyright (c) $originalComment.match("Copyright \(c\) (\d+)", 1, "-")2021. Bernard Bou.
 */

package org.oewntk.sql.out;

import java.io.PrintStream;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

public class Printers
{
	private Printers()
	{
	}

	static public final boolean withComment = true;

	// from maps

	public static <T> void printInsert(final PrintStream ps, final String table, final String columns, final Map<T, Integer> objectToNID, final Function<T, String> toString)
	{
		ps.printf("INSERT INTO %s (%s) VALUES", table, columns);

		final int[] i = {1};  // used as a final int holder
		objectToNID.keySet().forEach(k -> {
			String s = toString.apply(k);
			if (i[0]++ != 1)
			{
				ps.print(',');
			}
			ps.printf("%n(%d,%s)", NIDMaps.lookup(objectToNID, k), s);
		});
		ps.println(";");
	}

	public static <T> void printInsertWithComment(final PrintStream ps, final String table, final String columns, final Map<T, Integer> objectToNID, final Function<T, String[]> toStringWithComments)
	{
		ps.printf("INSERT INTO %s (%s) VALUES", table, columns);

		final int[] i = {1};  // used as a final int holder
		objectToNID.keySet().forEach(k -> {
			String[] s = toStringWithComments.apply(k);
			if (i[0]++ != 1)
			{
				ps.print(',');
			}
			ps.printf("%n(%d,%s) /* %s */", NIDMaps.lookup(objectToNID, k), s[0], s[1]);
		});
		ps.println(";");
	}

	public static <T> void printInsert(final PrintStream ps, final String table, final String columns, final Map<String, T> objectsById, final Map<String, Integer> objectToNID, final Function<T, String> toString)
	{
		ps.printf("INSERT INTO %s (%s) VALUES", table, columns);

		final int[] i = {1};  // used as a final int holder
		objectToNID.keySet().forEach(k -> {
			if (i[0]++ != 1)
			{
				ps.print(',');
			}
			T object = objectsById.get(k);
			String s = toString.apply(object);
			ps.printf("%n(%d,%s)", NIDMaps.lookup(objectToNID, k), s);
		});
		ps.println(";");
	}

	public static <T> void printInsertWithComment(final PrintStream ps, final String table, final String columns, final Map<String, T> objectsById, final Map<String, Integer> objectToNID, final Function<T, String[]> toStringWithComments)
	{
		ps.printf("INSERT INTO %s (%s) VALUES", table, columns);

		final int[] i = {1};  // used as a final int holder
		objectToNID.keySet().forEach(k -> {
			if (i[0]++ != 1)
			{
				ps.print(',');
			}
			T object = objectsById.get(k);
			String[] s = toStringWithComments.apply(object);
			ps.printf("%n(%d,%s) /* %s */", NIDMaps.lookup(objectToNID, k), s[0], s[1]);
		});
		ps.println(";");
	}

	// from streams

	public static <T> void printInsert(final PrintStream ps, final String table, final String columns, final Stream<T> stream, final Function<T, String> toString, final boolean withNumber)
	{
		ps.printf("INSERT INTO %s (%s) VALUES", table, columns);

		final int[] i = {1};  // used as a final int holder
		stream.forEach(object -> {
			if (i[0] != 1)
			{
				ps.print(',');
			}
			String s = toString.apply(object);
			if (withNumber)
			{
				ps.printf("%n(%d,%s)", i[0], s);
			}
			else
			{
				ps.printf("%n(%s)", s);
			}
			i[0]++;
		});
		ps.println(";");
	}

	public static <T> void printInsertWithComment(final PrintStream ps, final String table, final String columns, final Stream<T> stream, final Function<T, String[]> toStringWithComment, final boolean withNumber)
	{
		ps.printf("INSERT INTO %s (%s) VALUES", table, columns);

		final int[] i = {1};  // used as a final int holder
		stream.forEach(object -> {
			if (i[0] != 1)
			{
				ps.print(',');
			}
			String[] s = toStringWithComment.apply(object);
			if (withNumber)
			{
				ps.printf("%n(%d,%s) /* %s */", i[0], s[0], s[1]);
			}
			else
			{
				ps.printf("%n(%s) /* %s */", s[0], s[1]);
			}
			i[0]++;
		});
		ps.println(";");
	}

	public static <T> void printInserts(final PrintStream ps, final String table, final String columns, final Stream<T> stream, final Function<T, List<String>> toStrings, final boolean withNumber)
	{
		ps.printf("INSERT INTO %s (%s) VALUES", table, columns);

		final int[] i = {1};  // used as a final int holder
		stream.forEach(object -> {
			List<String> ss = toStrings.apply(object);
			for (String s : ss)
			{
				if (i[0] != 1)
				{
					ps.print(',');
				}
				if (withNumber)
				{
					ps.printf("%n(%d,%s)", i[0], s);
				}
				else
				{
					ps.printf("%n(%s)", s);
				}
				i[0]++;
			}
		});
		ps.println(";");
	}

	public static <T> void printInsertsWithComment(final PrintStream ps, final String table, final String columns, final Stream<T> stream, final Function<T, List<String[]>> toStringsWithComments, final boolean withNumber)
	{
		ps.printf("INSERT INTO %s (%s) VALUES", table, columns);

		final int[] i = {1};  // used as a final int holder
		stream.forEach(object -> {
			List<String[]> ss = toStringsWithComments.apply(object);
			for (String[] s : ss)
			{
				if (i[0] != 1)
				{
					ps.print(',');
				}
				if (withNumber)
				{
					ps.printf("%n(%d,%s) /* %s */", i[0], s[0], s[1]);
				}
				else
				{
					ps.printf("%n(%s) /* %s */", s[0], s[1]);
				}
				i[0]++;
			}
		});
		ps.println(";");
	}

	// to table

	public static <T> void printInsert(final PrintStream ps, final String table, final String columns, final String format, final Map<String, T> mapper)
	{
		ps.printf("INSERT INTO %s (%s) VALUES", table, columns);

		final int[] i = {1};  // I am using it only as an int holder
		mapper.keySet().forEach(k -> {
			if (i[0]++ != 1)
			{
				ps.print(',');
			}
			T val = mapper.get(k);
			ps.printf(format, val, Utils.escape(k));
		});
		ps.println(";");
	}

	public static <T> void printInsert2(final PrintStream ps, final String table, final String columns, final String format, final Map<Object[], T> mapper)
	{
		ps.printf("INSERT INTO %s (%s) VALUES", table, columns);

		final int[] i = {1};  // I am using it only as an int holder
		mapper.keySet().forEach(k -> {
			if (i[0]++ != 1)
			{
				ps.print(',');
			}
			T val = mapper.get(k);
			ps.printf(format, val, k[0], k[1]);
		});
		ps.println(";");
	}

	public static <T> void printInsert3(final PrintStream ps, final String table, final String columns, final String format, final Map<Object[], T> mapper)
	{
		ps.printf("INSERT INTO %s (%s) VALUES", table, columns);

		final int[] i = {1};  // I am using it only as an int holder
		mapper.keySet().forEach(k -> {
			if (i[0]++ != 1)
			{
				ps.print(',');
			}
			T val = mapper.get(k);
			ps.printf(format, val, k[0], k[1], k[2]);
		});
		ps.println(";");
	}
}
