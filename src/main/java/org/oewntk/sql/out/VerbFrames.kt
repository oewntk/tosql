/*
 * Copyright (c) 2022. Bernard Bou.
 */
package org.oewntk.sql.out

import org.oewntk.model.VerbFrame
import org.oewntk.sql.out.Printers.printInsert
import java.io.PrintStream
import java.util.AbstractMap.SimpleEntry
import java.util.stream.Collectors
import java.util.stream.Stream

/**
 * Process verb frames
 */
object VerbFrames {

	// name, frameid
	private val VERBFRAME_VALUES = arrayOf(
		arrayOf("vii", 1),
		arrayOf("via", 2),
		arrayOf("nonreferential", 3),
		arrayOf("vii-pp", 4),
		arrayOf("vtii-adj", 5),
		arrayOf("vii-adj", 6),
		arrayOf("via-adj", 7),
		arrayOf("vtai", 8),
		arrayOf("vtaa", 9),
		arrayOf("vtia", 10),
		arrayOf("vtii", 11),
		arrayOf("vii-to", 12),
		arrayOf("via-on-inanim", 13),
		arrayOf("ditransitive", 14),
		arrayOf("vtai-to", 15),
		arrayOf("vtai-from", 16),
		arrayOf("vtaa-with", 17),
		arrayOf("vtaa-of", 18),
		arrayOf("vtai-on", 19),
		arrayOf("vtaa-pp", 20),
		arrayOf("vtai-pp", 21),
		arrayOf("via-pp", 22),
		arrayOf("vibody", 23),
		arrayOf("vtaa-to-inf", 24),
		arrayOf("vtaa-inf", 25),
		arrayOf("via-that", 26),
		arrayOf("via-to", 27),
		arrayOf("via-to-inf", 28),
		arrayOf("via-whether-inf", 29),
		arrayOf("vtaa-into-ger", 30),
		arrayOf("vtai-with", 31),
		arrayOf("via-inf", 32),
		arrayOf("via-ger", 33),
		arrayOf("nonreferential-sent", 34),
		arrayOf("vii-inf", 35),
		arrayOf("via-at", 36),
		arrayOf("via-for", 37),
		arrayOf("via-on-anim", 38),
		arrayOf("via-out-of", 39),
	)

	/**
	 * Map frame id (via, ...) to numeric id
	 */
	val VERB_FRAME_ID_TO_NIDS: Map<String, Int> = Stream.of(*VERBFRAME_VALUES)
		.collect(Collectors.toMap(
		    { it[0] as String },
			{ it[1] as Int })
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
		val i = intArrayOf(0)
		val table = verbFrames.stream()
			.peek { ++i[0] }
			.map { vf: VerbFrame -> SimpleEntry(vf.frame, getNID(vf, i[0])) }
			.collect(
				Collectors.toMap(
					{ it.key },
					{ it.value })
			)

		printInsert<Int>(
			ps,
			Names.VFRAMES.TABLE,
			java.lang.String.join(",", Names.VFRAMES.frameid, Names.VFRAMES.frame),
			"%n(%d,'%s')",
			table
		)
	}
}
