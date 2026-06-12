/*
 * Copyright (c) 2021-2024. Bernard Bou.
 */
package org.oewntk.sql.out

import java.io.File
import java.io.FileOutputStream
import java.io.IOException

/**
 * Main class that generates the sources data
 *
 * @author Bernard Bou
 * @see "https://sqlunet.sourceforge.net/schema.html"
 */
object SourcesGenerator {

    /**
     * Generate sources
     *
     * @param outDir out dir
     * @throws IOException io exception
     */
    @Throws(IOException::class)
    fun sources(outDir: File) {
        if (!outDir.exists()) {
            outDir.mkdirs()
        }
        val url = checkNotNull(SourcesGenerator::class.java.getResource("/wn/sqltemplates/data/sources.sql"))
        url.openStream()
            .use {
                FileOutputStream(File(outDir, "sources.sql"))
                    .use { os ->
                        it.transferTo(os)
                    }
            }
    }

    /**
     * Main entry point
     *
     * @param args command-line arguments
     * @throws IOException io exception
     */
    @Throws(IOException::class)
    @JvmStatic
    fun main(args: Array<String>) {
        sources(File(args[0]))
    }
}
