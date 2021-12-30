/*
 * Copyright (c) $originalComment.match("Copyright \(c\) (\d+)", 1, "-")2021. Bernard Bou.
 */

package org.oewntk.sql.out;

import org.oewntk.model.Key;
import org.oewntk.model.KeyF;
import org.oewntk.model.Lex;

import java.io.PrintStream;
import java.util.AbstractMap.SimpleEntry;
import java.util.Collection;
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
			int nid = NIDMaps.lookup(objectToNID, k);
			ps.printf("%n(%d,%s)", nid, s);
		});
		ps.println(";");
	}

	public static <T> void printInsert(final PrintStream ps, final String table, final String columns, final Collection<T> objects, final Function<T, String> toId, final Map<String, Integer> objectIdToNID, final Function<T, String> toString)
	{
		ps.printf("INSERT INTO %s (%s) VALUES", table, columns);

		final int[] i = {1};  // used as a final int holder
		objects.stream() //
				.map(object -> new SimpleEntry<>(object, NIDMaps.lookup(objectIdToNID, toId.apply(object)))) //
				.sorted(Map.Entry.comparingByValue()) //
				.forEach(entry -> {
					if (i[0]++ != 1)
					{
						ps.print(',');
					}
					String s = toString.apply(entry.getKey());
					ps.printf("%n(%d,%s)", entry.getValue(), s);
				});
		ps.println(";");
	}

	public static <T> void printInsertWithComment(final PrintStream ps, final String table, final String columns, final Collection<T> objects, final Function<T, String> toId, final Map<String, Integer> objectIdToNID, final Function<T, String[]> toStringWithComments)
	{
		ps.printf("INSERT INTO %s (%s) VALUES", table, columns);

		final int[] i = {1};  // used as a final int holder
		objects.stream() //
				.map(object -> new SimpleEntry<>(object, NIDMaps.lookup(objectIdToNID, toId.apply(object)))) //
				.sorted(Map.Entry.comparingByValue()) //
				.forEach(entry -> {
					if (i[0]++ != 1)
					{
						ps.print(',');
					}
					String[] s = toStringWithComments.apply(entry.getKey());
					ps.printf("%n(%d,%s) /* %s */", entry.getValue(), s[0], s[1]);
				});
		ps.println(";");
	}

	// ley key map

	public static void printInsert(final PrintStream ps, final String table, final String columns, final Collection<Lex> lexes, final Map<Key, Integer> lexKeyToNID, final Function<Lex, String> toString)
	{
		ps.printf("INSERT INTO %s (%s) VALUES", table, columns);

		final int[] i = {1};  // used as a final int holder
		lexes.stream() //
				.map(lex -> new SimpleEntry<>(lex, NIDMaps.lookup(lexKeyToNID, KeyF.F_W_P_A.Mono.of_t(lex)))) //
				.sorted(Map.Entry.comparingByValue()) //
				.forEach(e -> {
					if (i[0]++ != 1)
					{
						ps.print(',');
					}
					Lex lex = e.getKey();
					Integer v = e.getValue();
					String s = toString.apply(lex);
					ps.printf("%n(%d,%s)", v, s);
				});
		ps.println(";");
	}

	public static void printInsertWithComment(final PrintStream ps, final String table, final String columns, final Collection<Lex> lexes, final Map<Key, Integer> lexKeyToNID, final Function<Lex, String[]> toStringWithComments)
	{
		ps.printf("INSERT INTO %s (%s) VALUES", table, columns);

		final int[] i = {1};  // used as a final int holder
		lexes.stream() //
				.map(lex -> new SimpleEntry<>(lex, NIDMaps.lookup(lexKeyToNID, KeyF.F_W_P_A.Mono.of_t(lex)))) //
				.sorted(Map.Entry.comparingByValue()) //
				.forEach(e -> {
					if (i[0]++ != 1)
					{
						ps.print(',');
					}
					Lex lex = e.getKey();
					Integer v = e.getValue();
					String[] s = toStringWithComments.apply(lex);
					ps.printf("%n(%d,%s) /* %s */", v, s[0], s[1]);
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

	public static <T extends Comparable<T>> void printInsert(final PrintStream ps, final String table, final String columns, final String format, final Map<String, T> mapper)
	{
		ps.printf("INSERT INTO %s (%s) VALUES", table, columns);

		final int[] i = {1};  // used only as an int holder
		mapper.entrySet().stream() //
				.sorted(Map.Entry.comparingByValue())  //
				.forEach(e -> {
					if (i[0]++ != 1)
					{
						ps.print(',');
					}
					String k = e.getKey();
					T val = e.getValue();
					ps.printf(format, val, Utils.escape(k));
				});
		ps.println(";");
	}

	public static <T extends Comparable<T>> void printInsert2(final PrintStream ps, final String table, final String columns, final String format, final Map<Object[], T> mapper)
	{
		ps.printf("INSERT INTO %s (%s) VALUES", table, columns);

		final int[] i = {1};  // used it only as an int holder
		mapper.entrySet().stream() //
				.sorted(Map.Entry.comparingByValue())  //
				.forEach(e -> {
					if (i[0]++ != 1)
					{
						ps.print(',');
					}
					Object[] k = e.getKey();
					T v = e.getValue();
					ps.printf(format, v, k[0], k[1]);
				});
		ps.println(";");
	}

	public static <T extends Comparable<T>> void printInsert3(final PrintStream ps, final String table, final String columns, final String format, final Map<Object[], T> mapper)
	{
		ps.printf("INSERT INTO %s (%s) VALUES", table, columns);

		final int[] i = {1};  // used it only as an int holder
		mapper.entrySet().stream() //
				.sorted(Map.Entry.comparingByValue()) //
				.forEach(e -> {
					if (i[0]++ != 1)
					{
						ps.print(',');
					}
					Object[] k = e.getKey();
					T v = e.getValue();
					ps.printf(format, v, k[0], k[1], k[2]);
				});
		ps.println(";");
	}
}
