/*
 * Copyright (c) $originalComment.match("Copyright \(c\) (\d+)", 1, "-")2021. Bernard Bou.
 */

package org.oewntk.sql.out;

import org.oewntk.model.Key;
import org.oewntk.model.Lex;
import org.oewntk.model.Sense;
import org.oewntk.model.TagCount;

import java.io.PrintStream;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Process senses
 */
public class Senses
{
	private Senses()
	{
	}

	/**
	 * Make id function, this adds the case-sensitive lemma to make it unique
	 */
	private static final Function<Sense, String> makeId = sense -> sense.getSenseKey() + ' ' + sense.lex.lemma.replace(' ', '_');

	/**
	 * Make sense id-to-nid map
	 *
	 * @param senses senses
	 * @return id-to-nid map
	 */
	public static Map<String, Integer> makeSenseNIDs(final Collection<Sense> senses)
	{
		// stream of sensekey␣lemma
		Stream<String> idStream = senses.stream() //
				.map(Senses.makeId) //
				.distinct() //
				.sorted();

		// make sensekey␣lemma-to-nid map
		return Utils.makeNIDMap(idStream);
	}

	/**
	 * Generate senses table
	 *
	 * @param ps                  print stream
	 * @param senses              senses
	 * @param synsetIdToNIDMap    id-to-nid map for synsets
	 * @param lexKeyToNIDMap      key-to-nid map for lexes
	 * @param wordIdToNIDMap      id-to-nid map for words
	 * @param casedWordIdToNIDMap id-to-nid map for cased words
	 * @return senses id-to-nid map
	 */
	@SuppressWarnings("UnusedReturnValue")
	public static Map<String, Integer> generateSenses(final PrintStream ps, final Collection<Sense> senses, final Map<String, Integer> synsetIdToNIDMap, final Map<Key, Integer> lexKeyToNIDMap, final Map<String, Integer> wordIdToNIDMap, final Map<String, Integer> casedWordIdToNIDMap)
	{
		// make sensekey␣lemma-to-nid map
		Map<String, Integer> idToNID = makeSenseNIDs(senses);

		// insert map
		final String columns = String.join(",", Names.SENSES.senseid, Names.SENSES.sensekey, Names.SENSES.sensenum, Names.SENSES.synsetid, Names.SENSES.luid, Names.SENSES.wordid, Names.SENSES.casedwordid, Names.SENSES.lexid, Names.SENSES.tagcount);
		final Function<Sense, String> toString = sense -> {

			Lex lex = sense.lex;
			String casedWord = lex.lemma;
			String word = lex.getLCLemma();
			String synsetId = sense.synsetId;
			String sensekey = sense.getSenseKey();
			int senseNum = sense.getLexIndex() + 1;
			int lexid = sense.findLexid();
			TagCount tagCount = sense.getTagCount();
			int wordNID = NIDMaps.lookupLC(wordIdToNIDMap, word);
			int synsetNID = NIDMaps.lookup(synsetIdToNIDMap, synsetId);
			int lexNID = NIDMaps.lookup(lexKeyToNIDMap, Key.W_P_A.of_t(lex));
			String casedWordNID = NIDMaps.lookupNullable(casedWordIdToNIDMap, casedWord);
			String tagCnt = tagCount == null ? "NULL" : Integer.toString(tagCount.count);
			return String.format("'%s',%d,%d,%d,%d,%s,%s,%s", Utils.escape(sensekey), senseNum, synsetNID, lexNID, wordNID, casedWordNID, lexid, tagCnt);
		};
		if (!Printers.withComment)
		{
			Printers.printInsert(ps, Names.SENSES.TABLE, columns, senses, Senses.makeId, idToNID, toString);
		}
		else
		{
			final Function<Sense, String[]> toStringWithComment = sense -> {

				Lex lex = sense.lex;
				String casedWord = lex.lemma;
				String synsetId = sense.synsetId;
				String sensekey = sense.getSenseKey();
				return new String[]{ //
						toString.apply(sense), //
						String.format("%s %s '%s'", sensekey, synsetId, casedWord),};
			};
			Printers.printInsertWithComment(ps, Names.SENSES.TABLE, columns, senses, Senses.makeId, idToNID, toStringWithComment);
		}
		return idToNID;
	}

	/**
	 * Generate sense relations
	 *
	 * @param ps               print stream
	 * @param senses           senses
	 * @param sensesById       senses by id
	 * @param synsetIdToNIDMap id-to-nid map for synsets
	 * @param lexKeyToNIDMap   key-to-nid map for lexes
	 * @param wordIdToNIDMap   id-to-nid map for words
	 */
	public static void generateSenseRelations(final PrintStream ps, final Collection<Sense> senses, final Map<String, Sense> sensesById, final Map<String, Integer> synsetIdToNIDMap, final Map<Key, Integer> lexKeyToNIDMap, final Map<String, Integer> wordIdToNIDMap)
	{
		// stream of senses
		Stream<Sense> senseStream = senses.stream() //
				.filter(s -> {
					var relations = s.getRelations();
					return relations != null && relations.size() > 0;
				}) //
				.sorted(Comparator.comparing(Sense::getSenseKey));

		// insert map
		final String columns = String.join(",", Names.LEXRELATIONS.synset1id, Names.LEXRELATIONS.lu1id, Names.LEXRELATIONS.word1id, Names.LEXRELATIONS.synset2id, Names.LEXRELATIONS.lu2id, Names.LEXRELATIONS.word2id, Names.LEXRELATIONS.relationid);
		Function<Sense, List<String>> toString = (sense) -> {

			var strings = new ArrayList<String>();
			String synsetId1 = sense.synsetId;
			Lex lex1 = sense.lex;
			String word1 = lex1.getLCLemma();
			int lu1NID = NIDMaps.lookup(lexKeyToNIDMap, Key.W_P_A.of_t(lex1));
			int wordNID1 = NIDMaps.lookupLC(wordIdToNIDMap, word1);
			int synsetNID1 = NIDMaps.lookup(synsetIdToNIDMap, synsetId1);
			var relations = sense.getRelations();
			for (String relation : relations.keySet())
			{
				if (!BuiltIn.OEWN_RELATION_TYPES.containsKey(relation))
				{
					throw new IllegalArgumentException(relation);
				}
				int relationId = BuiltIn.OEWN_RELATION_TYPES.get(relation);
				for (String senseId2 : relations.get(relation))
				{
					Sense sense2 = sensesById.get(senseId2);
					String synsetId2 = sense2.synsetId;
					Lex lex2 = sense2.lex;
					String word2 = lex2.getLCLemma();

					int lu2NID = NIDMaps.lookup(lexKeyToNIDMap, Key.W_P_A.of_t(lex2));
					int wordNID2 = NIDMaps.lookupLC(wordIdToNIDMap, word2);
					int synsetNID2 = NIDMaps.lookup(synsetIdToNIDMap, synsetId2);
					strings.add(String.format("%d,%d,%d,%d,%d,%d,%d", synsetNID1, lu1NID, wordNID1, synsetNID2, lu2NID, wordNID2, relationId));
				}
			}
			return strings;
		};
		if (!Printers.withComment)
		{
			Printers.printInserts(ps, Names.LEXRELATIONS.TABLE, columns, senseStream, toString, false);
		}
		else
		{
			Function<Sense, List<String[]>> toStrings = (sense) -> {

				var strings = toString.apply(sense);
				var stringWithComments = new ArrayList<String[]>();
				String synsetId1 = sense.synsetId;
				Lex lex1 = sense.lex;
				String casedword1 = lex1.lemma;
				var relations = sense.getRelations();
				int i = 0;
				for (String relation : relations.keySet())
				{
					for (String senseId2 : relations.get(relation))
					{
						Sense sense2 = sensesById.get(senseId2);
						String synsetId2 = sense2.synsetId;
						Lex lex2 = sense2.lex;
						String casedword2 = lex2.lemma;
						stringWithComments.add(new String[]{ //
								strings.get(i), //
								String.format("%s '%s' -%s-> %s '%s'", synsetId1, casedword1, relation, synsetId2, casedword2), //
						});
						i++;
					}
				}
				return stringWithComments;
			};
			Printers.printInsertsWithComment(ps, Names.LEXRELATIONS.TABLE, columns, senseStream, toStrings, false);

		}
	}

	/**
	 * Generate senses to adj position
	 *
	 * @param ps               print stream
	 * @param senses           senses
	 * @param synsetIdToNIDMap id-to-nid map for synsets
	 * @param lexKeyToNIDMap   key-to-nid map for lexes
	 * @param wordIdToNIDMap   id-to-nid map for words
	 */
	public static void generateSensesAdjPositions(final PrintStream ps, final Collection<Sense> senses, final Map<String, Integer> synsetIdToNIDMap, final Map<Key, Integer> lexKeyToNIDMap, final Map<String, Integer> wordIdToNIDMap)
	{
		// stream of senses
		Stream<Sense> senseStream = senses.stream() //
				.filter(s -> {
					var adjPosition = s.adjPosition;
					return adjPosition != null;
				}) //
				.sorted(Comparator.comparing(Sense::getSenseKey));

		// insert map
		final String columns = String.join(",", Names.SENSES_ADJPOSITIONS.synsetid, Names.SENSES_ADJPOSITIONS.luid, Names.SENSES_ADJPOSITIONS.wordid, Names.SENSES_ADJPOSITIONS.positionid);
		Function<Sense, String> toString = (sense) -> {

			String synsetId = sense.synsetId;
			Lex lex = sense.lex;
			String word = lex.getLCLemma();
			int synsetNID = NIDMaps.lookup(synsetIdToNIDMap, synsetId);
			int luNID = NIDMaps.lookup(lexKeyToNIDMap, Key.W_P_A.of_t(lex));
			int wordNID = NIDMaps.lookupLC(wordIdToNIDMap, word);
			return String.format("%d,%d,%d,'%s'", synsetNID, luNID, wordNID, sense.adjPosition);
		};
		if (!Printers.withComment)
		{
			Printers.printInsert(ps, Names.SENSES_ADJPOSITIONS.TABLE, columns, senseStream, toString, false);
		}
		else
		{
			Function<Sense, String[]> toStrings = (sense) -> new String[]{ //
					toString.apply(sense), //
					sense.getSenseKey()};
			Printers.printInsertWithComment(ps, Names.SENSES_ADJPOSITIONS.TABLE, columns, senseStream, toStrings, false);
		}
	}

	/**
	 * Generate senses to verb frames
	 *
	 * @param ps               print stream
	 * @param senses           senses
	 * @param synsetIdToNIDMap id-to-nid map for synsets
	 * @param lexKeyToNIDMap   key-to-nid map for lexes
	 * @param wordIdToNIDMap   id-to-nid map for words
	 */
	public static void generateSensesVerbFrames(final PrintStream ps, final Collection<Sense> senses, final Map<String, Integer> synsetIdToNIDMap, final Map<Key, Integer> lexKeyToNIDMap, final Map<String, Integer> wordIdToNIDMap)
	{
		// stream of senses
		Stream<Sense> senseStream = senses.stream() //
				.filter(s -> {
					var frames = s.verbFrames;
					return frames != null && frames.length > 0;
				}) //
				.sorted(Comparator.comparing(Sense::getSenseKey));

		// insert map
		final String columns = String.join(",", Names.SENSES_VFRAMES.synsetid, Names.SENSES_VFRAMES.luid, Names.SENSES_VFRAMES.wordid, Names.SENSES_VFRAMES.frameid);
		Function<Sense, List<String>> toString = (sense) -> {

			var strings = new ArrayList<String>();
			String synsetId = sense.synsetId;
			String word = sense.getLCLemma();
			int synsetNID = NIDMaps.lookup(synsetIdToNIDMap, synsetId);
			int wordNID = NIDMaps.lookupLC(wordIdToNIDMap, word);
			Lex lex = sense.lex;
			int luNID = NIDMaps.lookup(lexKeyToNIDMap, Key.W_P_A.of_t(lex));

			for (var frameId : sense.verbFrames)
			{
				int frameNID = VerbFrames.VERB_FRAME_ID_TO_NIDS.get(frameId);
				strings.add(String.format("%d,%d,%d,%d", synsetNID, luNID, wordNID, frameNID));
			}
			return strings;
		};
		if (!Printers.withComment)
		{
			Printers.printInserts(ps, Names.SENSES_VFRAMES.TABLE, columns, senseStream, toString, false);
		}
		else
		{
			final Function<Sense, List<String[]>> toStrings = sense -> {

				var strings = toString.apply(sense);
				var stringsWithComment = new ArrayList<String[]>();
				String sensekey = sense.getSenseKey();
				for (int i = 0; i < sense.verbFrames.length; i++)
				{
					stringsWithComment.add(new String[]{ //
							strings.get(i), //
							sensekey});
				}
				return stringsWithComment;
			};
			Printers.printInsertsWithComment(ps, Names.SENSES_VFRAMES.TABLE, columns, senseStream, toStrings, false);
		}
	}

	/**
	 * Generate senses to verb templates
	 *
	 * @param ps               print stream
	 * @param sensesById       senses by id
	 * @param synsetIdToNIDMap id-to-nid map for synsets
	 * @param lexKeyToNIDMap   key-to-nid map for lexes
	 * @param wordIdToNIDMap   id-to-nid map for words
	 */
	public static void generateSensesVerbTemplates(final PrintStream ps, final Map<String, Sense> sensesById, final Map<String, Integer> synsetIdToNIDMap, final Map<Key, Integer> lexKeyToNIDMap, final Map<String, Integer> wordIdToNIDMap)
	{
		// stream of senses
		Stream<Sense> senseStream = sensesById.values() //
				.stream() //
				.filter(s -> {
					var templates = s.getVerbTemplates();
					return templates != null && templates.length > 0;
				}) //
				.sorted(Comparator.comparing(Sense::getSenseKey));

		// insert map
		final String columns = String.join(",", Names.SENSES_VTEMPLATES.synsetid, Names.SENSES_VTEMPLATES.luid, Names.SENSES_VTEMPLATES.wordid, Names.SENSES_VTEMPLATES.templateid);
		Function<Sense, List<String>> toString = (sense) -> {

			var strings = new ArrayList<String>();
			String synsetId = sense.synsetId;
			String word = sense.getLCLemma();
			int synsetNID = NIDMaps.lookup(synsetIdToNIDMap, synsetId);
			int wordNID = NIDMaps.lookupLC(wordIdToNIDMap, word);
			Lex lex = sense.lex;
			int luNID = NIDMaps.lookup(lexKeyToNIDMap, Key.W_P_A.of_t(lex));

			for (var templateId : sense.getVerbTemplates())
			{
				strings.add(String.format("%d,%d,%d,%d", synsetNID, luNID, wordNID, templateId));
			}
			return strings;
		};
		if (!Printers.withComment)
		{
			Printers.printInserts(ps, Names.SENSES_VTEMPLATES.TABLE, columns, senseStream, toString, false);
		}
		else
		{
			final Function<Sense, List<String[]>> toStrings = sense -> {

				var strings = toString.apply(sense);
				var stringsWithComment = new ArrayList<String[]>();
				String sensekey = sense.getSenseKey();
				for (int i = 0; i < sense.getVerbTemplates().length; i++)
				{
					stringsWithComment.add(new String[]{ //
							strings.get(i), //
							sensekey});
				}
				return stringsWithComment;
			};
			Printers.printInsertsWithComment(ps, Names.SENSES_VTEMPLATES.TABLE, columns, senseStream, toStrings, false);
		}
	}
}
