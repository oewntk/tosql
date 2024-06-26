/*
 * Copyright (c) 2024. Bernard Bou.
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
     * @param toRow    nid-to-value pair stringifier
     * @param T        type of values
     */
    fun <T> generateTable(
        ps: PrintStream,
        table: String,
        columns: String,
        byNid: Map<Int, T>,
        toRow: (Pair<Int, T>) -> String,
    ) {

        // make object-to-nid map
        val seq = byNid.entries
            .asSequence()
            .map { Pair(it.key, it.value) }
            .sortedBy { it.first }

        // insert map
        printInsert(ps, table, columns, seq, toRow, false)
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
