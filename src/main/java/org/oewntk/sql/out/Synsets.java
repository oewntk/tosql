/*
 * Copyright (c) $originalComment.match("Copyright \(c\) (\d+)", 1, "-")2021. Bernard Bou.
 */

package org.oewntk.sql.out;

import org.oewntk.model.Synset;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

public class Synsets
{
	private Synsets()
	{
	}

	public static Map<String, Integer> makeSynsetNIDs(final Map<String, Synset> synsetsById)
	{
		// stream of synsetIds
		Stream<String> synsetIdStream = synsetsById.keySet() //
				.stream() //
				.sorted();
		return Utils.makeMap(synsetIdStream);
	}

	public static Map<String, Integer> generateSynsets(final PrintStream ps, final Map<String, Synset> synsetsById)
	{
		// make synsetId-to-nid map
		Map<String, Integer> synsetIdToNID = makeSynsetNIDs(synsetsById);

		// insert map
		final String columns = String.join(",", Names.SYNSETS.synsetid, Names.SYNSETS.posid, Names.SYNSETS.domainid, Names.SYNSETS.definition);
		Function<Synset, String> toString = synset -> {

			char type = synset.getType();
			String definition = synset.getDefinition();
			String domain = synset.getLexfile();
			int lexdomainId = BuiltIn.LEXFILENIDS.get(domain);
			return String.format("'%c',%d,'%s'", type, lexdomainId, Utils.escape(definition));
		};
		Printers.printInsert(ps, Names.SYNSETS.TABLE, columns, synsetsById, synsetIdToNID, toString);

		return synsetIdToNID;
	}

	public static void generateSynsetRelations(final PrintStream ps, final Map<String, Synset> synsetsById, final Map<String, Integer> synsetIdToNIDMap)
	{
		// synset stream
		Stream<Synset> synsetStream = synsetsById.values() //
				.stream() //
				.filter(synset -> {
					var relations = synset.getRelations();
					return relations != null && relations.size() > 0;
				});

		// insert
		final String columns = String.join(",", Names.SYNSETS_SYNSETS.synset1id, Names.SYNSETS_SYNSETS.synset2id, Names.SYNSETS_SYNSETS.relationid);
		Function<Synset, List<String>> toString = (synset) -> {

			var strings = new ArrayList<String>();
			String synset1Id = synset.getSynsetId();
			int synset1NID = NIDMaps.lookup(synsetIdToNIDMap, synset1Id);
			var relations = synset.getRelations();
			for (String relation : relations.keySet())
			{
				if (!BuiltIn.OEWN_RELATIONTYPES.containsKey(relation))
				{
					throw new IllegalArgumentException(relation);
				}
				int relationId = BuiltIn.OEWN_RELATIONTYPES.get(relation);
				for (String synset2Id : relations.get(relation))
				{
					int synset2NID = NIDMaps.lookup(synsetIdToNIDMap, synset2Id);
					strings.add(String.format("%d,%d,%d", synset1NID, synset2NID, relationId));
				}
			}
			return strings;
		};
		if (!Printers.withComment)
		{
			Printers.printInserts(ps, Names.SYNSETS_SYNSETS.TABLE, columns, synsetStream, toString, false);
		}
		else
		{
			Function<Synset, List<String[]>> toStrings = (synset) -> {

				var strings = toString.apply(synset);
				var stringsWithComment = new ArrayList<String[]>();
				String synset1Id = synset.getSynsetId();
				var relations = synset.getRelations();
				int i = 0;
				for (String relation : relations.keySet())
				{
					for (String synsetId2 : relations.get(relation))
					{
						stringsWithComment.add(new String[]{ //
								strings.get(i), //
								String.format("%s -%s-> %s", synset1Id, relation, synsetId2),});
						i++;
					}
				}
				return stringsWithComment;
			};
			Printers.printInsertsWithComment(ps, Names.SYNSETS_SYNSETS.TABLE, columns, synsetStream, toStrings, false);
		}
	}

	public static void generateSamples(final PrintStream ps, final Map<String, Synset> synsetsById, final Map<String, Integer> synsetIdToNIDMap)
	{
		// stream of synsets
		Stream<Synset> synsetStream = synsetsById.values() //
				.stream() //
				.filter(synset -> {
					var examples = synset.getExamples();
					return examples != null && examples.length > 0;
				});

		// insert
		final String columns = String.join(",", Names.SAMPLES.sampleid, Names.SAMPLES.synsetid, Names.SAMPLES.sample);
		Function<Synset, List<String>> toString = (synset) -> {

			var strings = new ArrayList<String>();
			String synsetId1 = synset.getSynsetId();
			int synsetNID1 = NIDMaps.lookup(synsetIdToNIDMap, synsetId1);
			var examples = synset.getExamples();
			for (String example : examples)
			{
				strings.add(String.format("%d,'%s'", synsetNID1, Utils.escape(example)));
			}
			return strings;
		};
		Printers.printInserts(ps, Names.SAMPLES.TABLE, columns, synsetStream, toString, true);
	}
}
