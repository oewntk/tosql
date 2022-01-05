/*
 * Copyright (c) 2022. Bernard Bou.
 */

package org.oewntk.sql.out;

import org.oewntk.model.VerbFrame;

import java.io.PrintStream;
import java.util.AbstractMap.SimpleEntry;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

public class VerbFrames
{
	// name, frameid
	public static final Object[][] VERBFRAME_VALUES = new Object[][]{ //
			{"vii", 1}, //
			{"via", 2}, //
			{"nonreferential", 3}, //
			{"vii-pp", 4}, //
			{"vtii-adj", 5}, //
			{"vii-adj", 6}, //
			{"via-adj", 7}, //
			{"vtai", 8}, //
			{"vtaa", 9}, //
			{"vtia", 10}, //
			{"vtii", 11}, //
			{"vii-to", 12}, //
			{"via-on-inanim", 13}, //
			{"ditransitive", 14}, //
			{"vtai-to", 15}, //
			{"vtai-from", 16}, //
			{"vtaa-with", 17}, //
			{"vtaa-of", 18}, //
			{"vtai-on", 19}, //
			{"vtaa-pp", 20}, //
			{"vtai-pp", 21}, //
			{"via-pp", 22}, //
			{"vibody", 23}, //
			{"vtaa-to-inf", 24}, //
			{"vtaa-inf", 25}, //
			{"via-that", 26}, //
			{"via-to", 27}, //
			{"via-to-inf", 28}, //
			{"via-whether-inf", 29}, //
			{"vtaa-into-ger", 30}, //
			{"vtai-with", 31}, //
			{"via-inf", 32}, //
			{"via-ger", 33}, //
			{"nonreferential-sent", 34}, //
			{"vii-inf", 35}, //
			{"via-at", 36}, //
			{"via-for", 37}, //
			{"via-on-anim", 38}, //
			{"via-out-of", 39}, //
	};

	/**
	 * Map frame id (via, ...) to numeric id
	 */
	public static final Map<String, Integer> VERB_FRAME_ID_TO_NIDS = Stream.of(VERBFRAME_VALUES).collect(toMap(data -> (String) data[0], data -> (Integer) data[2]));

	public static void generateVerbFrames(final PrintStream ps, final Collection<VerbFrame> verbFrames)
	{
		int[] i = {0};
		var table = verbFrames.stream() //
				.peek(vf -> ++i[0]) //
				.map(vf -> new SimpleEntry<>(vf.getFrame(), getNID(vf, i[0]))) //
				.collect(Collectors.toMap(SimpleEntry::getKey, SimpleEntry::getValue));

		Printers.printInsert(ps, Names.VFRAMES.TABLE, String.join(",", Names.VFRAMES.frameid, Names.VFRAMES.frame), "%n(%d,'%s')", table);
	}

	private static int getNID(VerbFrame vf, int index)
	{
		String id = vf.getId();
		Integer nid = VERB_FRAME_ID_TO_NIDS.get(id);
		if (nid != null)
		{
			return nid;
		}
		return 100 + index;
	}
}
