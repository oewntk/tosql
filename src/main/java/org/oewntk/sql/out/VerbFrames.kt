/*
 * Copyright (c) 2022-2024. Bernard Bou.
 */
package org.oewntk.sql.out

import org.oewntk.model.VerbFrame
import org.oewntk.sql.out.Printers.printInsert
import java.io.PrintStream

/**
 * Process verb frames
 */
object VerbFrames {

    // name, frameid

    /**
     * VerbFrame name-to-NID map (via, ...)
     */
    val VERB_FRAME_ID_TO_NIDS = mapOf(
        "vii" to 1,
        "via" to 2,
        "nonreferential" to 3,
        "vii-pp" to 4,
        "vtii-adj" to 5,
        "vii-adj" to 6,
        "via-adj" to 7,
        "vtai" to 8,
        "vtaa" to 9,
        "vtia" to 10,
        "vtii" to 11,
        "vii-to" to 12,
        "via-on-inanim" to 13,
        "ditransitive" to 14,
        "vtai-to" to 15,
        "vtai-from" to 16,
        "vtaa-with" to 17,
        "vtaa-of" to 18,
        "vtai-on" to 19,
        "vtaa-pp" to 20,
        "vtai-pp" to 21,
        "via-pp" to 22,
        "vibody" to 23,
        "vtaa-to-inf" to 24,
        "vtaa-inf" to 25,
        "via-that" to 26,
        "via-to" to 27,
        "via-to-inf" to 28,
        "via-whether-inf" to 29,
        "vtaa-into-ger" to 30,
        "vtai-with" to 31,
        "via-inf" to 32,
        "via-ger" to 33,
        "nonreferential-sent" to 34,
        "vii-inf" to 35,
        "via-at" to 36,
        "via-for" to 37,
        "via-on-anim" to 38,
        "via-out-of" to 39,
    )

    /**
     * Get nid
     *
     * @param vf    verb frame
     * @param index index
     * @return nod
     */
    private fun getNID(vf: VerbFrame, index: Int): Int {
        val id = vf.id
        val nid = VERB_FRAME_ID_TO_NIDS[id]
        if (nid != null) {
            return nid
        }
        return 100 + index
    }

    /**
     * Generate verb frame table
     *
     * @param ps         print stm
     * @param verbFrames verb frames
     */
    fun generateVerbFrames(ps: PrintStream, verbFrames: Collection<VerbFrame>) {
        val table = verbFrames
            .withIndex()
            .associate { it.value.frame to getNID(it.value, it.index) }
        printInsert(
            ps,
            Names.VFRAMES.TABLE,
            listOf(
                Names.VFRAMES.frameid,
                Names.VFRAMES.frame
            ).joinToString(","),
            "(%d,'%s')",
            table
        )
    }
}
