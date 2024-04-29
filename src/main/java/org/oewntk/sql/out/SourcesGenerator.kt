/*
 * Copyright (c) 2021-2021. Bernard Bou.
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
class SourcesGenerator {

    /**
     * Generate sources
     *
     * @param args command-line arguments
     * @throws IOException io exception
     */
    @Throws(IOException::class)
    private fun sources(args: Array<String>) {
        val arg1 = args[0]
        val outdir = File(arg1)
        if (!outdir.exists()) {
            // System.err.println("Output to new dir " + arg1);
            outdir.mkdirs()
        }
        val url = checkNotNull(SourcesGenerator::class.java.getResource("/wn/sqltemplates/data/sources.sql"))
        url.openStream().use {
            FileOutputStream(File(outdir, "sources.sql")).use { os ->
                it.transferTo(os)
            }
        }
    }

    companion object {

        /**
         * Main entry point
         *
         * @param args command-line arguments
         * @throws IOException io exception
         */
        @Throws(IOException::class)
        @JvmStatic
        fun main(args: Array<String>) {
            SourcesGenerator().sources(args)
        }
    }
}
