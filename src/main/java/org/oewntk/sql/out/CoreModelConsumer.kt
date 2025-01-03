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
import org.oewntk.sql.out.Senses.generateSenseRelations
import org.oewntk.sql.out.Senses.generateSenses
import org.oewntk.sql.out.Senses.generateSensesAdjPositions
import org.oewntk.sql.out.Senses.generateSensesSamples
import org.oewntk.sql.out.Senses.generateSensesVerbFrames
import org.oewntk.sql.out.Synsets.generateSynsetIlis
import org.oewntk.sql.out.Synsets.generateSynsetSamples
import org.oewntk.sql.out.Synsets.generateSynsetRelations
import org.oewntk.sql.out.Synsets.generateSynsets
import org.oewntk.sql.out.Synsets.generateSynsetUsages
import org.oewntk.sql.out.Synsets.generateSynsetWikidatas
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
) : Consumer<CoreModel> {

    /**
     * NID maps
     */
    var lexKeyToNID: Map<Key, Int>? = null

    /**
     * Word to NID map
     */
    var wordToNID: Map<String, Int>? = null

    /**
     * Cased word to NID map
     */
    private var casedWordToNID: Map<String, Int>? = null

    /**
     * Synset to NID map
     */
    var synsetIdToNID: Map<String, Int>? = null

    /**
     * Accept model
     *
     * @param model model
     */
    override fun accept(model: CoreModel) {
        Tracing.psInfo.println("[CoreModel] ${model.source}")

        try {
            lexes(outDir, model.lexes)
            synsets(outDir, model.synsets)
            senses(outDir, model.senses, model.sensesById!!)
            builtins(outDir)
        } catch (e: FileNotFoundException) {
            e.printStackTrace(Tracing.psErr)
        }
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
        PrintStream(FileOutputStream(File(outDir, makeFilename(Names.WORDS.FILE))), true, StandardCharsets.UTF_8)
            .use {
                wordToNID = generateWords(it, lexes)
            }
        PrintStream(FileOutputStream(File(outDir, makeFilename(Names.CASEDWORDS.FILE))), true, StandardCharsets.UTF_8)
            .use {
                casedWordToNID = generateCasedWords(it, lexes, wordToNID!!)
            }
        PrintStream(FileOutputStream(File(outDir, makeFilename(Names.LEXES.FILE))), true, StandardCharsets.UTF_8)
            .use {
                lexKeyToNID = generateLexes(it, lexes, wordToNID!!, casedWordToNID!!)
            }
        var morphToNID: Map<String, Int>
        PrintStream(FileOutputStream(File(outDir, makeFilename(Names.MORPHS.FILE))), true, StandardCharsets.UTF_8)
            .use {
                morphToNID = generateMorphs(it, lexes)
            }
        PrintStream(FileOutputStream(File(outDir, makeFilename(Names.LEXES_MORPHS.FILE))), true, StandardCharsets.UTF_8)
            .use {
                generateLexesMorphs(it, lexes, lexKeyToNID!!, wordToNID!!, morphToNID)
            }
        var pronunciationToNID: Map<String, Int>
        PrintStream(FileOutputStream(File(outDir, makeFilename(Names.PRONUNCIATIONS.FILE))), true, StandardCharsets.UTF_8)
            .use {
                pronunciationToNID = generatePronunciations(it, lexes)
            }
        PrintStream(FileOutputStream(File(outDir, makeFilename(Names.LEXES_PRONUNCIATIONS.FILE))), true, StandardCharsets.UTF_8)
            .use {
                generateLexesPronunciations(it, lexes, lexKeyToNID!!, wordToNID!!, pronunciationToNID)
            }
    }

    /**
     * Consume synsets
     *
     * @param outDir  out dir
     * @param synsets synsets
     * @throws FileNotFoundException file not found exception
     */
    @Throws(FileNotFoundException::class)
    private fun synsets(outDir: File, synsets: Collection<Synset>) {
        PrintStream(FileOutputStream(File(outDir, makeFilename(Names.SYNSETS.FILE))), true, StandardCharsets.UTF_8)
            .use {
                synsetIdToNID = generateSynsets(it, synsets)
            }
        // synsets are generated first, so do not append
        PrintStream(FileOutputStream(File(outDir, makeFilename(Names.SAMPLES.FILE)), false), true, StandardCharsets.UTF_8)
            .use {
                generateSynsetSamples(it, synsets, synsetIdToNID!!)
            }
        PrintStream(FileOutputStream(File(outDir, makeFilename(Names.USAGES.FILE))), true, StandardCharsets.UTF_8)
            .use {
                generateSynsetUsages(it, synsets, synsetIdToNID!!)
            }
        PrintStream(FileOutputStream(File(outDir, makeFilename(Names.SEMRELATIONS.FILE))), true, StandardCharsets.UTF_8)
            .use {
                generateSynsetRelations(it, synsets, synsetIdToNID!!)
            }
        PrintStream(FileOutputStream(File(outDir, makeFilename(Names.ILIS.FILE))), true, StandardCharsets.UTF_8)
            .use {
                generateSynsetIlis(it, synsets, synsetIdToNID!!)
            }
        PrintStream(FileOutputStream(File(outDir, makeFilename(Names.WIKIDATAS.FILE))), true, StandardCharsets.UTF_8)
            .use {
                generateSynsetWikidatas(it, synsets, synsetIdToNID!!)
            }
    }

    /**
     * Consume senses
     *
     * @param outDir     out dir
     * @param senses     senses
     * @param sensesById senses mapped by sensekeys
     * @throws FileNotFoundException file not found exception
     */
    @Throws(FileNotFoundException::class)
    private fun senses(outDir: File, senses: Collection<Sense>, sensesById: Map<String, Sense>) {
        PrintStream(FileOutputStream(File(outDir, makeFilename(Names.SENSES.FILE))), true, StandardCharsets.UTF_8)
            .use {
                generateSenses(it, senses, synsetIdToNID!!, lexKeyToNID!!, wordToNID!!, casedWordToNID!!)
            }
        PrintStream(FileOutputStream(File(outDir, makeFilename(Names.LEXRELATIONS.FILE))), true, StandardCharsets.UTF_8)
            .use {
                generateSenseRelations(it, senses, sensesById, synsetIdToNID!!, lexKeyToNID!!, wordToNID!!)
            }
        // senses are generated second, so append
        PrintStream(FileOutputStream(File(outDir, makeFilename(Names.SAMPLES.FILE)), true), true, StandardCharsets.UTF_8)
            .use {
                generateSensesSamples(it, senses, synsetIdToNID!!, lexKeyToNID!!, wordToNID!!)
            }
        PrintStream(FileOutputStream(File(outDir, makeFilename(Names.SENSES_VFRAMES.FILE))), true, StandardCharsets.UTF_8)
            .use {
                generateSensesVerbFrames(it, senses, synsetIdToNID!!, lexKeyToNID!!, wordToNID!!)
            }
        PrintStream(FileOutputStream(File(outDir, makeFilename(Names.SENSES_ADJPOSITIONS.FILE))), true, StandardCharsets.UTF_8)
            .use {
                generateSensesAdjPositions(it, senses, synsetIdToNID!!, lexKeyToNID!!, wordToNID!!)
            }
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
            PrintStream(FileOutputStream(File(outDir, makeFilename(Names.DOMAINS.TABLE))), true, StandardCharsets.UTF_8)
                .use {
                    generateDomains(it)
                }
            PrintStream(FileOutputStream(File(outDir, makeFilename(Names.POSES.FILE))), true, StandardCharsets.UTF_8)
                .use {
                    generatePoses(it)
                }
            PrintStream(FileOutputStream(File(outDir, makeFilename(Names.ADJPOSITIONS.FILE))), true, StandardCharsets.UTF_8)
                .use {
                    generateAdjectivePositionTypes(it)
                }
            PrintStream(FileOutputStream(File(outDir, makeFilename(Names.RELS.FILE))), true, StandardCharsets.UTF_8)
                .use {
                    generateRelationTypes(it)
                }
        }

        /**
         * Make SQL filename
         *
         * @param name name
         * @return filename
         */
        fun makeFilename(name: String): String {
            val fileName = "$name.sql"
            Tracing.psInfo.println(fileName)
            return fileName
        }
    }
}
