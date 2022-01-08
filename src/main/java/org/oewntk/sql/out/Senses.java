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
	private static final Function<Sense, String> makeId = sense -> sense.getSensekey() + ' ' + sense.getLex().getLemma().replace(' ', '_');

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

			Lex lex = sense.getLex();
			String casedWord = lex.getLemma();
			String word = lex.getLCLemma();
			String synsetId = sense.getSynsetId();
			String sensekey = sense.getSensekey();
			int senseNum = sense.getLexIndex() + 1;
			int lexid = sense.findLexid();
			TagCount tagCount = sense.getTagCount();
			int wordNID = NIDMaps.lookupLC(wordIdToNIDMap, word);
			int synsetNID = NIDMaps.lookup(synsetIdToNIDMap, synsetId);
			int lexNID = NIDMaps.lookup(lexKeyToNIDMap, Key.W_P_A.of_t(lex));
			String casedWordNID = NIDMaps.lookupNullable(casedWordIdToNIDMap, casedWord);
			String tagCnt = tagCount == null ? "NULL" : Integer.toString(tagCount.getCount());
			return String.format("'%s',%d,%d,%d,%d,%s,%s,%s", Utils.escape(sensekey), senseNum, synsetNID, lexNID, wordNID, casedWordNID, lexid, tagCnt);
		};
		if (!Printers.withComment)
		{
			Printers.printInsert(ps, Names.SENSES.TABLE, columns, senses, Senses.makeId, idToNID, toString);
		}
		else
		{
			final Function<Sense, String[]> toStringWithComment = sense -> {

				Lex lex = sense.getLex();
				String casedWord = lex.getLemma();
				String synsetId = sense.getSynsetId();
				String sensekey = sense.getSensekey();
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
				.sorted(Comparator.comparing(Sense::getSensekey));

		// insert map
		final String columns = String.join(",", Names.SENSES_SENSES.synset1id, Names.SENSES_SENSES.lu1id, Names.SENSES_SENSES.word1id, Names.SENSES_SENSES.synset2id, Names.SENSES_SENSES.lu2id, Names.SENSES_SENSES.word2id, Names.SENSES_SENSES.relationid);
		Function<Sense, List<String>> toString = (sense) -> {

			var strings = new ArrayList<String>();
			String synsetId1 = sense.getSynsetId();
			Lex lex1 = sense.getLex();
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
					String synsetId2 = sense2.getSynsetId();
					Lex lex2 = sense2.getLex();
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
			Printers.printInserts(ps, Names.SENSES_SENSES.TABLE, columns, senseStream, toString, false);
		}
		else
		{
			Function<Sense, List<String[]>> toStrings = (sense) -> {

				var strings = toString.apply(sense);
				var stringWithComments = new ArrayList<String[]>();
				String synsetId1 = sense.getSynsetId();
				Lex lex1 = sense.getLex();
				String casedword1 = lex1.getLemma();
				var relations = sense.getRelations();
				int i = 0;
				for (String relation : relations.keySet())
				{
					for (String senseId2 : relations.get(relation))
					{
						Sense sense2 = sensesById.get(senseId2);
						String synsetId2 = sense2.getSynsetId();
						Lex lex2 = sense2.getLex();
						String casedword2 = lex2.getLemma();
						stringWithComments.add(new String[]{ //
								strings.get(i), //
								String.format("%s '%s' -%s-> %s '%s'", synsetId1, casedword1, relation, synsetId2, casedword2), //
						});
						i++;
					}
				}
				return stringWithComments;
			};
			Printers.printInsertsWithComment(ps, Names.SENSES_SENSES.TABLE, columns, senseStream, toStrings, false);

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
					var adjPosition = s.getAdjPosition();
					return adjPosition != null;
				}) //
				.sorted(Comparator.comparing(Sense::getSensekey));

		// insert map
		final String columns = String.join(",", Names.SENSES_ADJPOSITIONS.synsetid, Names.SENSES_ADJPOSITIONS.luid, Names.SENSES_ADJPOSITIONS.wordid, Names.SENSES_ADJPOSITIONS.positionid);
		Function<Sense, String> toString = (sense) -> {

			String synsetId = sense.getSynsetId();
			Lex lex = sense.getLex();
			String word = lex.getLCLemma();
			int synsetNID = NIDMaps.lookup(synsetIdToNIDMap, synsetId);
			int luNID = NIDMaps.lookup(lexKeyToNIDMap, Key.W_P_A.of_t(lex));
			int wordNID = NIDMaps.lookupLC(wordIdToNIDMap, word);
			return String.format("%d,%d,%d,'%s'", synsetNID, luNID, wordNID, sense.getAdjPosition());
		};
		if (!Printers.withComment)
		{
			Printers.printInsert(ps, Names.SENSES_ADJPOSITIONS.TABLE, columns, senseStream, toString, false);
		}
		else
		{
			Function<Sense, String[]> toStrings = (sense) -> new String[]{ //
					toString.apply(sense), //
					sense.getSensekey()};
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
					var frames = s.getVerbFrames();
					return frames != null && frames.length > 0;
				}) //
				.sorted(Comparator.comparing(Sense::getSensekey));

		// insert map
		final String columns = String.join(",", Names.SENSES_VFRAMES.synsetid, Names.SENSES_VFRAMES.luid, Names.SENSES_VFRAMES.wordid, Names.SENSES_VFRAMES.frameid);
		Function<Sense, List<String>> toString = (sense) -> {

			var strings = new ArrayList<String>();
			String synsetId = sense.getSynsetId();
			String word = sense.getLCLemma();
			int synsetNID = NIDMaps.lookup(synsetIdToNIDMap, synsetId);
			int wordNID = NIDMaps.lookupLC(wordIdToNIDMap, word);
			Lex lex = sense.getLex();
			int luNID = NIDMaps.lookup(lexKeyToNIDMap, Key.W_P_A.of_t(lex));

			for (var frameId : sense.getVerbFrames())
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
				String sensekey = sense.getSensekey();
				for (int i = 0; i < sense.getVerbFrames().length; i++)
				{
					stringsWithComment.add(new String[]{ //
							strings.get(i), //
							sensekey});
					i++;
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
				.sorted(Comparator.comparing(Sense::getSensekey));

		// insert map
		final String columns = String.join(",", Names.SENSES_VTEMPLATES.synsetid, Names.SENSES_VTEMPLATES.luid, Names.SENSES_VTEMPLATES.wordid, Names.SENSES_VTEMPLATES.templateid);
		Function<Sense, List<String>> toString = (sense) -> {

			var strings = new ArrayList<String>();
			String synsetId = sense.getSynsetId();
			String word = sense.getLCLemma();
			int synsetNID = NIDMaps.lookup(synsetIdToNIDMap, synsetId);
			int wordNID = NIDMaps.lookupLC(wordIdToNIDMap, word);
			Lex lex = sense.getLex();
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
				String sensekey = sense.getSensekey();
				for (int i = 0; i < sense.getVerbTemplates().length; i++)
				{
					stringsWithComment.add(new String[]{ //
							strings.get(i), //
							sensekey});
					i++;
				}
				return stringsWithComment;
			};
			Printers.printInsertsWithComment(ps, Names.SENSES_VTEMPLATES.TABLE, columns, senseStream, toStrings, false);
		}
	}
}
