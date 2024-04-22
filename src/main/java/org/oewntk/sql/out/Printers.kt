/*
 * Copyright (c) $originalComment.match("Copyright \(c\) (\d+)", 1, "-")2021. Bernard Bou.
 */
package org.oewntk.sql.out

import org.oewntk.model.Key
import org.oewntk.model.KeyF
import org.oewntk.model.Lex
import java.io.PrintStream
import java.util.function.Consumer
import java.util.function.Function
import java.util.stream.Stream
import kotlin.streams.toList

/**
 * Insert printers
 */
object Printers {
	const val WITH_COMMENT: Boolean = true

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
	fun <T> printInsert(
		ps: PrintStream,
		table: String,
		columns: String,
		objectToNID: Map<T, Int>,
		toString: Function<T, String>
	) {
		if (objectToNID.isEmpty()) {
			ps.print("-- NONE")
		} else {
			ps.printf("INSERT INTO %s (%s) VALUES", table, columns)
			val i = intArrayOf(1) // used as a final int holder
			objectToNID.keys.forEach(Consumer { k: T ->
				val s = toString.apply(k)
				if (i[0]++ != 1) {
					ps.print(',')
				}
				val nid = NIDMaps.lookup(objectToNID, k)
				ps.printf("%n(%d,%s)", nid, s)
			})
			ps.println(";")
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
	@JvmStatic
	fun <T> printInsert(
		ps: PrintStream,
		table: String,
		columns: String,
		objects: Collection<T>,
		toId: (T) -> String,
		objectIdToNID: Map<String, Int>,
		toString: (T) -> String
	) {
		if (objects.isEmpty()) {
			ps.print("-- NONE")
		} else {
			ps.printf("INSERT INTO %s (%s) VALUES", table, columns)
			val i = intArrayOf(1) // used as a final int holder
			objects.stream()
				.map { it to NIDMaps.lookup(objectIdToNID, toId.invoke(it)) }
				.toList()
				.sortedBy { it.second }
				.forEach {
					if ((i[0]++) > 1) {
						ps.print(',')
					}
					val s = toString.invoke(it.first)
					ps.printf("%n(%d,%s)", it.second, s)
				}
			ps.println(";")
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
	fun <T> printInsertWithComment(
		ps: PrintStream,
		table: String?,
		columns: String?,
		objects: Collection<T>,
		toId: (T) -> String,
		objectIdToNID: Map<String, Int>,
		toStringWithComments: (T) -> Array<String>
	) {
		if (objects.isEmpty()) {
			ps.print("-- NONE")
		} else {
			ps.printf("INSERT INTO %s (%s) VALUES", table, columns)
			val i = intArrayOf(1) // used as a final int holder
			objects.stream()
				.map { it to NIDMaps.lookup(objectIdToNID, toId.invoke(it)) }
				.toList()
				.sortedBy { it.second }
				.forEach {
					if (i[0]++ != 1) {
						ps.print(',')
					}
					val s = toStringWithComments.invoke(it.first)
					ps.printf("%n(%d,%s) /* %s */", it.second, s[0], s[1])
				}
			ps.println(";")
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
	fun printInsert(
		ps: PrintStream,
		table: String,
		columns: String,
		lexes: Collection<Lex>,
		lexKeyToNID: Map<out Key, Int>,
		toString: (Lex) -> String
	) {
		if (lexes.isEmpty()) {
			ps.print("-- NONE")
		} else {
			ps.printf("INSERT INTO %s (%s) VALUES", table, columns)
			val i = intArrayOf(1) // used as a final int holder
			lexes.stream()
				.map { it to NIDMaps.lookup(lexKeyToNID, KeyF.F_W_P_A.Mono.of(Lex::lemma, Lex::type, it)) }
				.toList()
				.sortedBy { it.second }
				.forEach {
					if (i[0]++ != 1) {
						ps.print(',')
					}
					val lex = it.first
					val v = it.second
					val s = toString.invoke(lex)
					ps.printf("%n(%d,%s)", v, s)
				}
			ps.println(";")
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
	fun printInsertWithComment(
		ps: PrintStream,
		table: String,
		columns: String,
		lexes: Collection<Lex>,
		lexKeyToNID: Map<out Key, Int>,
		toStringWithComments: (Lex) -> Array<String>
	) {
		if (lexes.isEmpty()) {
			ps.print("-- NONE")
		} else {
			ps.printf("INSERT INTO %s (%s) VALUES", table, columns)
			val i = intArrayOf(1) // used as a final int holder
			lexes.stream()
				.map { it to NIDMaps.lookup(lexKeyToNID, KeyF.F_W_P_A.Mono.of(Lex::lemma, Lex::type, it)) }
				.toList()
				.sortedBy { it.second }
				.forEach {
					if (i[0]++ != 1) {
						ps.print(',')
					}
					val lex = it.first
					val v = it.second
					val s = toStringWithComments.invoke(lex)
					ps.printf("%n(%d,%s) /* %s */", v, s[0], s[1])
				}
			ps.println(";")
		}
	}

	// from sequences

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
	@JvmStatic
	fun <T> printInsert(
		ps: PrintStream,
		table: String,
		columns: String,
		seq: Sequence<T>,
		toString: (T) -> String,
		withNumber: Boolean
	) {
		val i = intArrayOf(1) // used as a final int holder
		seq.forEach {
			if (i[0] == 1) {
				ps.printf("INSERT INTO %s (%s) VALUES", table, columns)
			} else {
				ps.print(',')
			}
			val s = toString.invoke(it)
			if (withNumber) {
				ps.printf("%n(%d,%s)", i[0], s)
			} else {
				ps.printf("%n(%s)", s)
			}
			i[0]++
		}
		ps.println(";")
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
	@JvmStatic
	fun <T> printInsert(
		ps: PrintStream,
		table: String,
		columns: String,
		stream: Stream<T>,
		toString: (T) -> String,
		withNumber: Boolean
	) {
		val i = intArrayOf(1) // used as a final int holder
		stream.forEach {
			if (i[0] == 1) {
				ps.printf("INSERT INTO %s (%s) VALUES", table, columns)
			} else {
				ps.print(',')
			}
			val s = toString.invoke(it)
			if (withNumber) {
				ps.printf("%n(%d,%s)", i[0], s)
			} else {
				ps.printf("%n(%s)", s)
			}
			i[0]++
		}
		ps.println(";")
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
	@JvmStatic
	fun <T> printInsertWithComment(
		ps: PrintStream,
		table: String,
		columns: String,
		stream: Stream<T>,
		toStringWithComment: (T) -> Array<String>,
		withNumber: Boolean
	) {
		val i = intArrayOf(1) // used as a final int holder
		stream.forEach {
			if (i[0] == 1) {
				ps.printf("INSERT INTO %s (%s) VALUES", table, columns)
			} else {
				ps.print(',')
			}
			val s = toStringWithComment.invoke(it)
			if (withNumber) {
				ps.printf("%n(%d,%s) /* %s */", i[0], s[0], s[1])
			} else {
				ps.printf("%n(%s) /* %s */", s[0], s[1])
			}
			i[0]++
		}
		ps.println(";")
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
	@JvmStatic
	fun <T> printInserts(
		ps: PrintStream,
		table: String,
		columns: String,
		stream: Stream<T>,
		toStrings: (T) -> List<String>,
		withNumber: Boolean
	) {
		val i = intArrayOf(1) // used as a final int holder
		stream.forEach {
			val ss = toStrings.invoke(it)
			for (s in ss) {
				if (i[0] == 1) {
					ps.printf("INSERT INTO %s (%s) VALUES", table, columns)
				} else {
					ps.print(',')
				}
				if (withNumber) {
					ps.printf("%n(%d,%s)", i[0], s)
				} else {
					ps.printf("%n(%s)", s)
				}
				i[0]++
			}
		}
		ps.println(";")
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
	@JvmStatic
	fun <T> printInsertsWithComment(
		ps: PrintStream,
		table: String,
		columns: String,
		stream: Stream<T>,
		toStringsWithComments: (T) -> List<Array<String>>,
		withNumber: Boolean
	) {
		val i = intArrayOf(1) // used as a final int holder
		stream.forEach {
			val ss = toStringsWithComments.invoke(it)
			for (s in ss) {
				if (i[0] == 1) {
					ps.printf("INSERT INTO %s (%s) VALUES", table, columns)
				} else {
					ps.print(',')
				}
				if (withNumber) {
					ps.printf("%n(%d,%s) /* %s */", i[0], s[0], s[1])
				} else {
					ps.printf("%n(%s) /* %s */", s[0], s[1])
				}
				i[0]++
			}
		}
		ps.println(";")
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
	 * @param T       type of objects
	 */
	@JvmStatic
	fun <T : Comparable<T>> printInsert(
		ps: PrintStream,
		table: String,
		columns: String,
		format: String,
		mapper: Map<String, T>
	) {
		ps.printf("INSERT INTO %s (%s) VALUES", table, columns)

		val i = intArrayOf(1) // used only as an int holder
		mapper.entries.stream()
			.toList()
			.sortedBy { it.value }
			.forEach {
				if (i[0]++ != 1) {
					ps.print(',')
				}
				val k = it.key
				val v = it.value
				ps.printf(format, v, Utils.escape(k))
			}
		ps.println(";")
	}

	/**
	 * Print inserts, 2 values
	 *
	 * @param ps      print stream
	 * @param table   table name
	 * @param columns column names
	 * @param format  value format
	 * @param mapper  object arrays by id
	 * @param T       type of objects
	 */
	fun <T : Comparable<T>?> printInsert2(
		ps: PrintStream,
		table: String,
		columns: String,
		format: String,
		mapper: Map<Array<Any>, T>
	) {
		ps.printf("INSERT INTO %s (%s) VALUES", table, columns)

		val i = intArrayOf(1) // used it only as an int holder
		mapper.entries.stream()
			.toList()
			.sortedBy { it.value }
			.forEach {
				if (i[0]++ != 1) {
					ps.print(',')
				}
				val k = it.key
				val v = it.value
				ps.printf(format, v, k[0], k[1])
			}
		ps.println(";")
	}

	/**
	 * Print inserts, 3 values
	 *
	 * @param ps      print stream
	 * @param table   table name
	 * @param columns column names
	 * @param format  value format
	 * @param mapper  object arrays by id
	 * @param T       type of objects
	 */
	fun <T : Comparable<T>?> printInsert3(
		ps: PrintStream,
		table: String,
		columns: String,
		format: String,
		mapper: Map<Array<String>, T>
	) {
		ps.printf("INSERT INTO %s (%s) VALUES", table, columns)

		val i = intArrayOf(1) // used it only as an int holder
		mapper.entries
			.toList()
			.sortedBy { it.value }
			.forEach {
				if (i[0]++ != 1) {
					ps.print(',')
				}
				val k = it.key
				val v = it.value
				ps.printf(format, v, k[0], k[1], k[2])
			}
		ps.println(";")
	}
}
