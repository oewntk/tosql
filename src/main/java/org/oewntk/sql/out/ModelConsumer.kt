/*
 * Copyright (c) 2021-2024. Bernard Bou.
 */
package org.oewntk.sql.out

import org.oewntk.model.Model
import org.oewntk.model.Sense
import org.oewntk.model.VerbFrame
import org.oewntk.model.VerbTemplate
import org.oewntk.sql.out.CoreModelConsumer.Companion.makeFilename
import org.oewntk.sql.out.Senses.generateSensesVerbTemplates
import org.oewntk.sql.out.Utils.escape
import org.oewntk.sql.out.Utils.generateTable
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.PrintStream
import java.nio.charset.StandardCharsets
import java.util.function.Consumer

/**
 * Main class that generates the WN database in the SQL format
 *
 * @param outDir output directory
 *
 * @author Bernard Bou
 * @see "https://sqlunet.sourceforge.net/schema.html"
 */
class ModelConsumer(
    private val outDir: File,
) : Consumer<Model> {

    override fun accept(model: Model) {
        Tracing.psInfo.printf("[Model] %s%n", model.sources.contentToString())

        // core
        val coreConsumer = CoreModelConsumer(outDir)
        coreConsumer.accept(model)

        // verb frames
        try {
            frames(outDir, model.verbFrames)
        } catch (e: FileNotFoundException) {
            e.printStackTrace(Tracing.psErr)
        }

        // verb templates
        try {
            templates(outDir, coreConsumer, model.sensesById!!, model.verbTemplatesById!!)
        } catch (e: FileNotFoundException) {
            e.printStackTrace(Tracing.psErr)
        }
    }

    /**
     * Consume frames
     *
     * @param outDir     out dir
     * @param verbFrames verb frames
     * @throws FileNotFoundException file not found exception
     */
    @Throws(FileNotFoundException::class)
    private fun frames(outDir: File, verbFrames: Collection<VerbFrame>) {
        PrintStream(
            FileOutputStream(File(outDir, makeFilename(Names.VFRAMES.FILE))),
            true,
            StandardCharsets.UTF_8
        ).use { 
            VerbFrames.generateVerbFrames(it, verbFrames)
        }
    }

    /**
     * Consume templates
     *
     * @param outDir            out dir
     * @param coreConsumer      core consumer
     * @param sensesById        senses by id
     * @param verbTemplatesById verb templates by id
     * @throws FileNotFoundException file not found exception
     */
    @Throws(FileNotFoundException::class)
    private fun templates(
        outDir: File,
        coreConsumer: CoreModelConsumer,
        sensesById: Map<String, Sense>,
        verbTemplatesById: Map<Int, VerbTemplate>,
    ) {
        PrintStream(
            FileOutputStream(File(outDir, makeFilename(Names.SENSES_VTEMPLATES.FILE))),
            true,
            StandardCharsets.UTF_8
        ).use { 
            generateSensesVerbTemplates(
                it,
                sensesById!!,
                coreConsumer.synsetIdToNID!!,
                coreConsumer.lexKeyToNID!!,
                coreConsumer.wordToNID!!
            )
        }
        val toString = { entry: Pair<Int, VerbTemplate> ->
            "${entry.first}, '${escape(entry.second.template)}'"
        }

        PrintStream(
            FileOutputStream(File(outDir, makeFilename(Names.VTEMPLATES.FILE))),
            true,
            StandardCharsets.UTF_8
        ).use { 
            generateTable(
                it,
                Names.VTEMPLATES.TABLE,
                listOf(Names.VTEMPLATES.templateid, Names.VTEMPLATES.template).joinToString(","),
                verbTemplatesById,
                toString
            )
        }
    }
}
