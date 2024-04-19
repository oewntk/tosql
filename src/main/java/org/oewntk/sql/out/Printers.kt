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

/**
 * Insert printers
 */
public class Printers
{
	private Printers()
	{
	}

	static public final boolean withComment = true;

	// from maps

	/**
	 * Print inserts for objects mapped by nids
	 *
	 * @param ps          print stream
	 * @param table       table name
	 * @param columns     column names
	 * @param objectToNID objects-to-nid map
	 * @param toString    stringifier of objects
	 * @param <T>         type of objects
	 */
	public static <T> void printInsert(final PrintStream ps, final String table, final String columns, final Map<T, Integer> objectToNID, final Function<T, String> toString)
	{
		if (objectToNID.isEmpty())
		{
			ps.print("-- NONE");
		}
		else
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
	}

	/**
	 * Print inserts for collection
	 *
	 * @param ps            print stream
	 * @param table         table name
	 * @param columns       column names
	 * @param objects       collection of objects
	 * @param toId          id extractor
	 * @param objectIdToNID id-to-nid map
	 * @param toString      stringifier of objects
	 * @param <T>           type of objects
	 */
	public static <T> void printInsert(final PrintStream ps, final String table, final String columns, final Collection<T> objects, final Function<T, String> toId, final Map<String, Integer> objectIdToNID, final Function<T, String> toString)
	{
		if (objects.isEmpty())
		{
			ps.print("-- NONE");
		}
		else
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
	}

	/**
	 * Print inserts for collection with comments
	 *
	 * @param ps                   print stream
	 * @param table                table name
	 * @param columns              column names
	 * @param objects              collection of objects
	 * @param toId                 id extractor
	 * @param objectIdToNID        id-to-nid map
	 * @param toStringWithComments double stringifier of objects, two strings are produced: [0] insert values , [1] comment
	 * @param <T>                  type of objects
	 */
	public static <T> void printInsertWithComment(final PrintStream ps, final String table, final String columns, final Collection<T> objects, final Function<T, String> toId, final Map<String, Integer> objectIdToNID, final Function<T, String[]> toStringWithComments)
	{
		if (objects.isEmpty())
		{
			ps.print("-- NONE");
		}
		else
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
	}

	// lexes

	/**
	 * Print inserts from lexes
	 *
	 * @param ps          print stream
	 * @param table       table name
	 * @param columns     column names
	 * @param lexes       lexes
	 * @param lexKeyToNID lex_key-to-nid map
	 * @param toString    stringifier of objects
	 */
	public static void printInsert(final PrintStream ps, final String table, final String columns, final Collection<Lex> lexes, final Map<Key, Integer> lexKeyToNID, final Function<Lex, String> toString)
	{
		if (lexes.isEmpty())
		{
			ps.print("-- NONE");
		}
		else
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
	}

	/**
	 * Print inserts from lexes with comments
	 *
	 * @param ps                   print stream
	 * @param table                table name
	 * @param columns              column names
	 * @param lexes                lexes
	 * @param lexKeyToNID          lex_key-to-nid map
	 * @param toStringWithComments double stringifier of objects, two strings are produced: [0] insert values , [1] comment
	 */
	public static void printInsertWithComment(final PrintStream ps, final String table, final String columns, final Collection<Lex> lexes, final Map<Key, Integer> lexKeyToNID, final Function<Lex, String[]> toStringWithComments)
	{
		if (lexes.isEmpty())
		{
			ps.print("-- NONE");
		}
		else
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
	}

	// from streams

	/**
	 * Print inserts from stream
	 *
	 * @param ps         print stream
	 * @param table      table name
	 * @param columns    column names
	 * @param stream     stream of objects
	 * @param toString   stringifier of objects
	 * @param withNumber whether to number objects
	 * @param <T>        type of objects in stream
	 */
	public static <T> void printInsert(final PrintStream ps, final String table, final String columns, final Stream<T> stream, final Function<T, String> toString, final boolean withNumber)
	{
		final int[] i = {1};  // used as a final int holder
		stream.forEach(object -> {
			if (i[0] == 1)
			{
				ps.printf("INSERT INTO %s (%s) VALUES", table, columns);
			}
			else
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

	/**
	 * Print inserts from stream with comments
	 *
	 * @param ps                  print stream
	 * @param table               table name
	 * @param columns             column names
	 * @param stream              stream of objects
	 * @param toStringWithComment double stringifier of objects, two strings are produced: [0] insert values , [1] comment
	 * @param withNumber          whether to number objects
	 * @param <T>                 type of objects in stream
	 */
	public static <T> void printInsertWithComment(final PrintStream ps, final String table, final String columns, final Stream<T> stream, final Function<T, String[]> toStringWithComment, final boolean withNumber)
	{
		final int[] i = {1};  // used as a final int holder
		stream.forEach(object -> {
			if (i[0] == 1)
			{
				ps.printf("INSERT INTO %s (%s) VALUES", table, columns);
			}
			else
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

	/**
	 * Print inserts from stream
	 *
	 * @param ps         print stream
	 * @param table      table name
	 * @param columns    column names
	 * @param stream     stream of objects
	 * @param toStrings  stringifier for multiple values
	 * @param withNumber whether to number objects
	 * @param <T>        type of objects in stream
	 */
	public static <T> void printInserts(final PrintStream ps, final String table, final String columns, final Stream<T> stream, final Function<T, List<String>> toStrings, final boolean withNumber)
	{
		final int[] i = {1};  // used as a final int holder
		stream.forEach(object -> {
			List<String> ss = toStrings.apply(object);
			for (String s : ss)
			{
				if (i[0] == 1)
				{
					ps.printf("INSERT INTO %s (%s) VALUES", table, columns);
				}
				else
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

	/**
	 * Print inserts from stream with comments
	 *
	 * @param ps                    print stream
	 * @param table                 table name
	 * @param columns               column names
	 * @param stream                stream of objects
	 * @param toStringsWithComments double stringifier of objects, two strings are produced: [0] insert values , [1] comment
	 * @param withNumber            whether to number objects
	 * @param <T>                   type of objects in stream
	 */
	public static <T> void printInsertsWithComment(final PrintStream ps, final String table, final String columns, final Stream<T> stream, final Function<T, List<String[]>> toStringsWithComments, final boolean withNumber)
	{
		final int[] i = {1};  // used as a final int holder
		stream.forEach(object -> {
			List<String[]> ss = toStringsWithComments.apply(object);
			for (String[] s : ss)
			{
				if (i[0] == 1)
				{
					ps.printf("INSERT INTO %s (%s) VALUES", table, columns);
				}
				else
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

	/**
	 * Print inserts, single value
	 *
	 * @param ps      print stream
	 * @param table   table name
	 * @param columns column names
	 * @param format  value format
	 * @param mapper  objects by id
	 * @param <T>     type of objects
	 */
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

	/**
	 * Print inserts, 2 values
	 *
	 * @param ps      print stream
	 * @param table   table name
	 * @param columns column names
	 * @param format  value format
	 * @param mapper  object arrays by id
	 * @param <T>     type of objects
	 */
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

	/**
	 * Print inserts, 3 values
	 *
	 * @param ps      print stream
	 * @param table   table name
	 * @param columns column names
	 * @param format  value format
	 * @param mapper  object arrays by id
	 * @param <T>     type of objects
	 */
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
