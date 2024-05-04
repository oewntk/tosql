/*
 * Copyright (c) 2021-2024. Bernard Bou.
 */
package org.oewntk.sql.out

import java.io.*
import kotlin.system.exitProcess

/**
 * Deserialize ID to Numeric IDs maps
 */
object DeSerializeNIDs {

    /**
     * Deserialize id-to_nid maps
     *
     * @param inDir input directory
     * @return id-to-nid map indexed by name
     * @throws IOException            io exception
     * @throws ClassNotFoundException class not found exception
     */
    @Throws(IOException::class, ClassNotFoundException::class)
    fun deserializeNIDs(inDir: File): Map<String, Map<String, Int>> {

        val maps: MutableMap<String, Map<String, Int>> = HashMap()
        FileInputStream(File(inDir, SerializeNIDs.NID_PREFIX + Names.WORDS.FILE)).use {
            val m = deSerializeNIDs(it)
            maps.put(Names.WORDS.FILE, m)
        }
        FileInputStream(File(inDir, SerializeNIDs.NID_PREFIX + Names.CASEDWORDS.FILE)).use {
            val m = deSerializeNIDs(it)
            maps.put(Names.CASEDWORDS.FILE, m)
        }
        FileInputStream(File(inDir, SerializeNIDs.NID_PREFIX + Names.MORPHS.FILE)).use {
            val m = deSerializeNIDs(it)
            maps.put(Names.MORPHS.FILE, m)
        }
        FileInputStream(File(inDir, SerializeNIDs.NID_PREFIX + Names.PRONUNCIATIONS.FILE)).use {
            val m = deSerializeNIDs(it)
            maps.put(Names.PRONUNCIATIONS.FILE, m)
        }
        FileInputStream(File(inDir, SerializeNIDs.NID_PREFIX + Names.SENSES.FILE)).use {
            val m = deSerializeNIDs(it)
            maps.put(Names.SENSES.FILE, m)
        }
        FileInputStream(File(inDir, SerializeNIDs.NID_PREFIX + Names.SYNSETS.FILE)).use {
            val m = deSerializeNIDs(it)
            maps.put(Names.SYNSETS.FILE, m)
        }
        return maps
    }

    /**
     * Deserialize id-to_nid map
     *
     * @param `is` input stream
     * @return id-to-nid map
     * @throws IOException            io exception
     * @throws ClassNotFoundException class not found exception
     */
    @Throws(IOException::class, ClassNotFoundException::class)
    @Suppress("UNCHECKED_CAST")
    fun deSerializeNIDs(inStream: InputStream): Map<String, Int> {
        return deSerialize(inStream) as Map<String, Int>
    }

    /**
     * Deserialize object
     *
     * @param `is` input stream
     * @return object
     * @throws IOException            io exception
     * @throws ClassNotFoundException class not found exception
     */
    @Throws(IOException::class, ClassNotFoundException::class)
    private fun deSerialize(inStream: InputStream): Any {
        ObjectInputStream(inStream).use { return it.readObject() }
    }

    /**
     * Main
     *
     * @param args command-line arguments
     * @throws IOException            io exception
     * @throws ClassNotFoundException class not found exception
     */
    @Throws(IOException::class, ClassNotFoundException::class)
    @JvmStatic
    fun main(args: Array<String>) {
        val inDir = File(args[0])
        if (!inDir.isDirectory) {
            exitProcess(1)
        }

        val maps = deserializeNIDs(inDir)
        println("${Names.WORDS.FILE} ${maps[Names.WORDS.FILE]!!.size}")
        println("${Names.CASEDWORDS.FILE} ${maps[Names.CASEDWORDS.FILE]!!.size}")
        println("${Names.MORPHS.FILE} ${maps[Names.MORPHS.FILE]!!.size}")
        println("${Names.PRONUNCIATIONS.FILE} ${maps[Names.PRONUNCIATIONS.FILE]!!.size}")
        println("${Names.SENSES.FILE} ${maps[Names.SENSES.FILE]!!.size}")
        println("${Names.SYNSETS.FILE} ${maps[Names.SYNSETS.FILE]!!.size}")
    }
}
