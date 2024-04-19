/*
 * Copyright (c) $originalComment.match("Copyright \(c\) (\d+)", 1, "-")2021. Bernard Bou.
 */
package org.oewntk.sql.out

import org.oewntk.sql.out.Printers.printInsert
import java.io.PrintStream
import java.util.*
import java.util.AbstractMap.SimpleEntry
import java.util.function.BinaryOperator
import java.util.stream.Collectors
import java.util.stream.Stream

/**
 * Utilities
 */
object Utils {

	/**
	 * Merging function, keep existing element against replacement
	 */
	private val mergingKeepFunction = BinaryOperator { existing: Int, replacement: Int ->
		require(existing != replacement) { "$existing,$replacement" }
		existing
	}

	/**
	 * Merging function, keep replacement element against replacement
	 */
	private val mergingReplaceFunction = BinaryOperator { existing: Int, replacement: Int ->
		require(existing != replacement) { "$existing,$replacement" }
		replacement
	}

	// map factory

	/**
	 * Make NID
	 * To be used with objects that support equal and comparable
	 *
	 * @param stream must be distinct
	 * @param T type of stream elements
	 * @return map if object-to-NID pairs
	 */
	@JvmStatic
	fun <T> makeNIDMap(stream: Stream<T>): Map<T, Int> {
		// val index = AtomicInteger()
		// index.set(0);
		var i = 0
		return stream
			.sequential()
			.sorted()
			.peek { ++i }
			.map { SimpleEntry(it, i /* index.addAndGet(1) */) }
			.collect(
				Collectors.toMap(
					{ it.key },
					{ it.value },
					{ existing, replacement -> throw IllegalArgumentException("$existing,$replacement") },
					{ TreeMap() })
			)
	}

	/**
	 * Generate table
	 *
	 * @param ps       print stream
	 * @param table    table name
	 * @param columns  column name
	 * @param byNid    nid-to-value map, values mapped by nid
	 * @param toString nid-to-value pair stringifier
	 * @param T        type of values
	 */
	fun <T> generateTable(
		ps: PrintStream,
		table: String,
		columns: String,
		byNid: Map<Int, T>,
		toString: (Map.Entry<Int, T>) -> String
	) {

		// make object-to-nid map
		val stream = byNid.entries.stream()
			.sorted(Comparator.comparingInt { it.key })

		// insert map
		printInsert(ps, table, columns, stream, toString, false)
	}

	// escape

	/**
	 * Escape string for it to be handled by SQL
	 *
	 * @param str string
	 * @return SQL escaped string
	 */
	@JvmStatic
	fun escape(str: String): String {
		return str.replace("'", "''")
	}
}
