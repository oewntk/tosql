/*
 * Copyright (c) 2021-2024. Bernard Bou.
 */
package org.oewntk.sql.out

import org.oewntk.model.*
import org.oewntk.sql.out.BuiltIn.generateAdjectivePositionTypes
import org.oewntk.sql.out.BuiltIn.generateDomains
import org.oewntk.sql.out.BuiltIn.generatePoses
import org.oewntk.sql.out.BuiltIn.generateRelationTypes
import org.oewntk.sql.out.Lexes.generateCasedWords
import org.oewntk.sql.out.Lexes.generateLexes
import org.oewntk.sql.out.Lexes.generateLexesMorphs
import org.oewntk.sql.out.Lexes.generateLexesPronunciations
import org.oewntk.sql.out.Lexes.generateMorphs
import org.oewntk.sql.out.Lexes.generatePronunciations
import org.oewntk.sql.out.Lexes.generateWords
import org.oewntk.sql.out.SchemaGenerator.Companion.schema
import org.oewntk.sql.out.Senses.generateSenseRelations
import org.oewntk.sql.out.Senses.generateSenses
import org.oewntk.sql.out.Senses.generateSensesAdjPositions
import org.oewntk.sql.out.Senses.generateSensesSamples
import org.oewntk.sql.out.Senses.generateSensesVerbFrames
import org.oewntk.sql.out.Senses.generateSensesWords
import org.oewntk.sql.out.SourcesGenerator.sources
import org.oewntk.sql.out.Synsets.generateSynsetIlis
import org.oewntk.sql.out.Synsets.generateSynsetRelations
import org.oewntk.sql.out.Synsets.generateSynsetSamples
import org.oewntk.sql.out.Synsets.generateSynsetUsages
import org.oewntk.sql.out.Synsets.generateSynsetWikidatas
import org.oewntk.sql.out.Synsets.generateSynsets
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.PrintStream
import java.nio.charset.StandardCharsets
import java.util.function.Consumer

/**
 * Main class that generates the core WN database in the SQL format
 *
 * @property outDir output directory
 *
 * @author Bernard Bou
 * @see "https://sqlunet.sourceforge.net/schema.html"
 */
class CoreModelConsumer(
    private val outDir: File,
    private val withSchema: Boolean = true,
    private val withSources: Boolean = true,
    private val compatSchema: Boolean = false,
    private val verbose: Boolean = false,
) : Consumer<CoreModel> {

    /**
     * NID maps
     */
    var lexIdToNID: Map<LexId, Int>? = null

    /**
     * Word to NID map
     */
    var wordToNID: Map<Lemma, Int>? = null

    /**
     * Cased word to NID map
     */
    private var casedWordToNID: Map<Lemma, Int>? = null

    /**
     * Synset to NID map
     */
    var synsetIdToNID: Map<SynsetId, Int>? = null

    /**
     * Accept model
     *
     * @param model model
     */
    override fun accept(model: CoreModel) {
        if (verbose) Tracing.psInfo.println("[CoreModel] ${model.source}")
        if (!outDir.exists()) {
            outDir.mkdirs()
        }
        val dataDir = File(outDir, "data")
        if (!dataDir.exists()) {
            dataDir.mkdirs()
        }

        try {
            lexes(dataDir, model.lexes)
            synsets(dataDir, model.synsets, model.synsetResolver, model.senseResolver)
            senses(dataDir, model.senses, model.senseResolver, model.synsetResolver)
            builtins(dataDir)
            if (withSources) sources(outDir)
            if (withSchema) schema(outDir.absolutePath, compat = compatSchema)
        } catch (e: FileNotFoundException) {
            e.printStackTrace(Tracing.psErr)
        }
    }

    private fun <T, R> generate(outDir: File, outFile: String, append: Boolean = false, what: T, generator: (ps: PrintStream, what: T) -> R): R {
        val fileName = makeFilename(outFile)
        if (verbose) Tracing.psInfo.println("-$fileName")
        var r: R? = null
        PrintStream(FileOutputStream(File(outDir, fileName), append), true, StandardCharsets.UTF_8)
            .use { ps -> r = generator(ps, what) }
        return r!!
    }

    /**
     * Consume lexes
     *
     * @param outDir out dir
     * @param lexes  lexes
     * @throws FileNotFoundException file not found exception
     */
    @Throws(FileNotFoundException::class)
    private fun lexes(outDir: File, lexes: Collection<Lex>) {
        wordToNID = generate(outDir, Names.WORDS.FILE, append = false, lexes, ::generateWords)
        casedWordToNID = generate(outDir, Names.CASEDWORDS.FILE, append = false, lexes) { ps, lexes -> generateCasedWords(ps, lexes, wordToNID!!) }
        lexIdToNID = generate(outDir, Names.LEXES.FILE, append = false, lexes) { ps, lexes -> generateLexes(ps, lexes, wordToNID!!, casedWordToNID!!) }
        val morphToNID = generate(outDir, Names.MORPHS.FILE, append = false, lexes, ::generateMorphs)
        generate(outDir, Names.LEXES_MORPHS.FILE, append = false, lexes) { ps, lexes -> generateLexesMorphs(ps, lexes, lexIdToNID!!, wordToNID!!, morphToNID) }
        val pronunciationToNID = generate(outDir, Names.PRONUNCIATIONS.FILE, append = false, lexes, ::generatePronunciations)
        generate(outDir, Names.LEXES_PRONUNCIATIONS.FILE, append = false, lexes) { ps, lexes -> generateLexesPronunciations(ps, lexes, lexIdToNID!!, wordToNID!!, pronunciationToNID) }
    }

    /**
     * Consume synsets
     *
     * @param outDir  out dir
     * @param synsets synsets
     * @throws FileNotFoundException file not found exception
     */
    @Throws(FileNotFoundException::class)
    private fun synsets(outDir: File, synsets: Collection<Synset>, synsetResolver: (SynsetId) -> Synset, senseResolver: (SenseKey) -> Sense) {
        synsetIdToNID = generate(outDir, Names.SYNSETS.FILE, append = false, synsets, ::generateSynsets)
        // synsets are generated first, so do not append
        generate(outDir, Names.SAMPLES.FILE, append = false, synsets) { ps, synsets -> generateSynsetSamples(ps, synsets, synsetIdToNID!!) }
        generate(outDir, Names.USAGES.FILE, append = false, synsets) { ps, synsets -> generateSynsetUsages(ps, synsets, synsetIdToNID!!) }
        generate(outDir, Names.ILIS.FILE, append = false, synsets) { ps, synsets -> generateSynsetIlis(ps, synsets, synsetIdToNID!!) }
        generate(outDir, Names.WIKIDATAS.FILE, append = false, synsets) { ps, synsets -> generateSynsetWikidatas(ps, synsets, synsetIdToNID!!) }
        generate(outDir, Names.SEMRELATIONS.FILE, append = false, synsets) { ps, synsets ->
            generateSynsetRelations(ps, synsets,
                senseResolver, synsetResolver,
                synsetIdToNID!!, lexIdToNID!!, wordToNID!!) }
    }

    /**
     * Consume senses
     *
     * @param outDir     out dir
     * @param senses     senses
     * @param senseResolver sense resolver from sensekey
     * @throws FileNotFoundException file not found exception
     */
    @Throws(FileNotFoundException::class)
    private fun senses(outDir: File, senses: Collection<Sense>, senseResolver: (SenseKey) -> Sense, synsetResolver: (SynsetId) -> Synset) {
        generate(outDir, Names.SENSES.FILE, append = false, senses) { ps, senses -> generateSenses(ps, senses, synsetIdToNID!!, lexIdToNID!!, wordToNID!!, casedWordToNID!!) }
        generate(outDir, Names.SENSES_WORDS.FILE, append = false, senses) { ps, senses -> generateSensesWords(ps, senses, synsetIdToNID!!, lexIdToNID!!, wordToNID!!, casedWordToNID!!) }
        generate(outDir, Names.SAMPLES.FILE, append = true, senses) { ps, senses -> generateSensesSamples(ps, senses, synsetIdToNID!!, lexIdToNID!!, wordToNID!!) }
        generate(outDir, Names.SENSES_VFRAMES.FILE, append = false, senses) { ps, senses -> generateSensesVerbFrames(ps, senses, synsetIdToNID!!, lexIdToNID!!, wordToNID!!) }
        generate(outDir, Names.SENSES_ADJPOSITIONS.FILE, append = false, senses) { ps, senses -> generateSensesAdjPositions(ps, senses, synsetIdToNID!!, lexIdToNID!!, wordToNID!!) }
        generate(outDir, Names.LEXRELATIONS.FILE, append = false, senses) { ps, senses ->
            generateSenseRelations(ps, senses,
                senseResolver, synsetResolver,
                synsetIdToNID!!, lexIdToNID!!, wordToNID!!) }
    }

    companion object {

        /**
         * Consume builtins
         *
         * @param outDir out dir
         * @throws FileNotFoundException file not found exception
         */
        @Throws(FileNotFoundException::class)
        fun builtins(outDir: File) {

            fun generate(outDir: File, outFile: String, append: Boolean = false, generator: (ps: PrintStream) -> Unit) {
                PrintStream(FileOutputStream(File(outDir, makeFilename(outFile)), false), true, StandardCharsets.UTF_8)
                    .use { ps -> generator(ps) }
            }

            generate(outDir, Names.DOMAINS.FILE, append = false, ::generateDomains)
            generate(outDir, Names.POSES.FILE, append = false, ::generatePoses)
            generate(outDir, Names.ADJPOSITIONS.FILE, append = false, ::generateAdjectivePositionTypes)
            generate(outDir, Names.RELS.FILE, append = false, ::generateRelationTypes)
        }

        /**
         * Make SQL filename
         *
         * @param name name
         * @return filename
         */
        fun makeFilename(name: String): String = "$name.sql"
    }
}
