/*
 * Copyright (c) 2021-2021. Bernard Bou.
 */
package org.oewntk.sql.out

import org.oewntk.sql.out.CoreModelConsumer.Companion.builtins
import java.io.File
import java.io.IOException

/**
 * Main class that generates the WN database in the WNDB format as per wndb(5WN)
 *
 * @author Bernard Bou
 */
object GrindBuiltIn {

    /**
     * Main entry point
     *
     * @param args command-line arguments
     * @throws IOException io exception
     */
    @Throws(IOException::class)
    @JvmStatic
    fun main(args: Array<String>) {

        // Output
        val outDir = File(args[0])
        if (!outDir.exists()) {
            outDir.mkdirs()
        }
        println("[Output] " + outDir.absolutePath)

        // Process
        builtins(outDir)
    }
}
