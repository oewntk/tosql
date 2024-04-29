/*
 * Copyright (c) $originalComment.match("Copyright \(c\) (\d+)", 1, "-")2021. Bernard Bou.
 */
package org.oewntk.sql.out

import org.oewntk.sql.out.Printers.printInsert
import java.io.PrintStream

/**
 * Utilities
 */
object Utils {

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
        toString: (Pair<Int, T>) -> String,
    ) {

        // make object-to-nid map
        val seq = byNid.entries.asSequence()
            .map { Pair(it.key, it.value) }
            .sortedBy { it.first }

        // insert map
        printInsert(ps, table, columns, seq, toString, false)
    }

    // escape

    /**
     * Escape string for it to be handled by SQL
     *
     * @param str string
     * @return SQL escaped string
     */
    fun escape(str: String): String {
        return str.replace("'", "''")
    }
}
