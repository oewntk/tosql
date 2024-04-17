/*
 * Copyright (c) $originalComment.match("Copyright \(c\) (\d+)", 1, "-")2021. Bernard Bou.
 */

package org.oewntk.sql.out;

import org.oewntk.model.Synset;

import java.io.PrintStream;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Process synsets
 */
public class Synsets
{
	private Synsets()
	{
	}

	/**
	 * Make synset id-to-nid map
	 *
	 * @param synsets synsets
	 * @return id-to-nid map
	 */
	public static Map<String, Integer> makeSynsetNIDs(final Collection<Synset> synsets)
	{
		// stream of synsetIds
		Stream<String> synsetIdStream = synsets.stream() //
				.map(Synset::getSynsetId);
		return Utils.makeNIDMap(synsetIdStream);
	}

	/**
	 * Generate synsets table
	 *
	 * @param ps      print stream
	 * @param synsets synsets
	 * @return synsets id-to-nid map
	 */
	public static Map<String, Integer> generateSynsets(final PrintStream ps, final Collection<Synset> synsets)
	{
		// make synsetId-to-nid map
		Map<String, Integer> synsetIdToNID = makeSynsetNIDs(synsets);

		// insert map
		final String columns = String.join(",", Names.SYNSETS.synsetid, Names.SYNSETS.posid, Names.SYNSETS.domainid, Names.SYNSETS.definition);
		Function<Synset, String> toString = synset -> {

			char type = synset.type;
			String definition = synset.getDefinition();
			String domain = synset.getLexfile();
			int lexdomainId = BuiltIn.LEXFILE_NIDS.get(domain);
			return String.format("'%c',%d,'%s'", type, lexdomainId, Utils.escape(definition));
		};
		if (!Printers.withComment)
		{
			Printers.printInsert(ps, Names.SYNSETS.TABLE, columns, synsets, Synset::getSynsetId, synsetIdToNID, toString);
		}
		else
		{
			Function<Synset, String[]> toStrings = (synset) -> {

				var stringsWithComment = new String[2];
				stringsWithComment[0] = toString.apply(synset);
				stringsWithComment[1] = synset.synsetId;
				return stringsWithComment;
			};
			Printers.printInsertWithComment(ps, Names.SYNSETS.TABLE, columns, synsets, Synset::getSynsetId, synsetIdToNID, toStrings);
		}
		return synsetIdToNID;
	}

	/**
	 * Generate synset relations table
	 *
	 * @param ps               print stream
	 * @param synsets          synsets
	 * @param synsetIdToNIDMap id-to-nid map
	 */
	public static void generateSynsetRelations(final PrintStream ps, final Collection<Synset> synsets, final Map<String, Integer> synsetIdToNIDMap)
	{
		// synset stream
		Stream<Synset> synsetStream = synsets.stream() //
				.filter(synset -> {
					var relations = synset.getRelations();
					return relations != null && relations.size() > 0;
				}) //
				.sorted(Comparator.comparing(Synset::getSynsetId));

		// insert
		final String columns = String.join(",", Names.SEMRELATIONS.synset1id, Names.SEMRELATIONS.synset2id, Names.SEMRELATIONS.relationid);
		Function<Synset, List<String>> toString = (synset) -> {

			var strings = new ArrayList<String>();
			String synset1Id = synset.synsetId;
			int synset1NID = NIDMaps.lookup(synsetIdToNIDMap, synset1Id);
			var relations = synset.getRelations();
			for (String relation : relations.keySet())
			{
				if (!BuiltIn.OEWN_RELATION_TYPES.containsKey(relation))
				{
					throw new IllegalArgumentException(relation);
				}
				int relationId = BuiltIn.OEWN_RELATION_TYPES.get(relation);
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
			Printers.printInserts(ps, Names.SEMRELATIONS.TABLE, columns, synsetStream, toString, false);
		}
		else
		{
			Function<Synset, List<String[]>> toStrings = (synset) -> {

				var strings = toString.apply(synset);
				var stringsWithComment = new ArrayList<String[]>();
				String synset1Id = synset.synsetId;
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
			Printers.printInsertsWithComment(ps, Names.SEMRELATIONS.TABLE, columns, synsetStream, toStrings, false);
		}
	}

	/**
	 * Generate samples table
	 *
	 * @param ps               print stream
	 * @param synsets          synsets
	 * @param synsetIdToNIDMap id-to-nid map
	 */
	public static void generateSamples(final PrintStream ps, final Collection<Synset> synsets, final Map<String, Integer> synsetIdToNIDMap)
	{
		// stream of synsets
		Stream<Synset> synsetStream = synsets.stream() //
				.filter(synset -> {
					var examples = synset.examples;
					return examples != null && examples.length > 0;
				}) //
				.sorted(Comparator.comparing(Synset::getSynsetId));

		// insert
		final String columns = String.join(",", Names.SAMPLES.sampleid, Names.SAMPLES.synsetid, Names.SAMPLES.sample);
		Function<Synset, List<String>> toString = (synset) -> {

			var strings = new ArrayList<String>();
			String synsetId1 = synset.synsetId;
			int synsetNID1 = NIDMaps.lookup(synsetIdToNIDMap, synsetId1);
			var examples = synset.examples;
			for (String example : examples)
			{
				strings.add(String.format("%d,'%s'", synsetNID1, Utils.escape(example)));
			}
			return strings;
		};
		Printers.printInserts(ps, Names.SAMPLES.TABLE, columns, synsetStream, toString, true);
	}
}
