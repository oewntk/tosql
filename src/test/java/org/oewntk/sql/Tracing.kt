/*
 * Copyright (c) 2024. Bernard Bou.
 */
package org.oewntk.sql

import java.io.OutputStream
import java.io.PrintStream

/**
 * Heap Memory utilities
 *
 * @author Bernard Bou
 */
object Tracing {

    val psInfo: PrintStream = System.out

    @Suppress("unused")
    val psErr: PrintStream = System.err

    val psNull: PrintStream = PrintStream(object : OutputStream(
    ) {
        override fun write(i: Int) {
            // do nothing
        }
    })
}
