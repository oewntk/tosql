/*
 * Copyright (c) $originalComment.match("Copyright \(c\) (\d+)", 1, "-")2021. Bernard Bou.
 */

package org.oewntk.sql.out;

import org.oewntk.model.VerbFrame;

import java.io.PrintStream;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

public class BuiltIn
{
	// lexdomain, lexdomainid
	private static final Object[][] domainsArray = new Object[][]{ //
			{"adjs", "a", "adj.all", 0}, //
			{"adjs.pert", "a", "adj.pert", 1}, //
			{"advs", "r", "adv.all", 2}, //
			{"tops", "n", "noun.Tops", 3}, //
			{"act", "n", "noun.act", 4}, //
			{"animal", "n", "noun.animal", 5}, //
			{"artifact", "n", "noun.artifact", 6}, //
			{"attribute", "n", "noun.attribute", 7}, //
			{"body", "n", "noun.body", 8}, //
			{"cognition", "n", "noun.cognition", 9}, //
			{"communication", "n", "noun.communication", 10}, //
			{"event", "n", "noun.event", 11}, //
			{"feeling", "n", "noun.feeling", 12}, //
			{"food", "n", "noun.food", 13}, //
			{"group", "n", "noun.group", 14}, //
			{"location", "n", "noun.location", 15}, //
			{"motive", "n", "noun.motive", 16}, //
			{"object", "n", "noun.object", 17}, //
			{"person", "n", "noun.person", 18}, //
			{"phenomenon", "n", "noun.phenomenon", 19}, //
			{"plant", "n", "noun.plant", 20}, //
			{"possession", "n", "noun.possession", 21}, //
			{"process", "n", "noun.process", 22}, //
			{"quantity", "n", "noun.quantity", 23}, //
			{"relation", "n", "noun.relation", 24}, //
			{"shape", "n", "noun.shape", 25}, //
			{"state", "n", "noun.state", 26}, //
			{"substance", "n", "noun.substance", 27}, //
			{"time", "n", "noun.time", 28}, //
			{"body", "v", "verb.body", 29}, //
			{"change", "v", "verb.change", 30}, //
			{"cognition", "v", "verb.cognition", 31}, //
			{"communication", "v", "verb.communication", 32}, //
			{"competition", "v", "verb.competition", 33}, //
			{"consumption", "v", "verb.consumption", 34}, //
			{"contact", "v", "verb.contact", 35}, //
			{"creation", "v", "verb.creation", 36}, //
			{"emotion", "v", "verb.emotion", 37}, // //
			{"motion", "v", "verb.motion", 38}, //
			{"perception", "v", "verb.perception", 39}, //
			{"possession", "v", "verb.possession", 40}, //
			{"social", "v", "verb.social", 41}, //
			{"stative", "v", "verb.stative", 42}, //
			{"weather", "v", "verb.weather", 43}, //
			{"adjs.ppl", "a", "adj.ppl", 44}, //
	};

	public static final Map<String, Integer> DOMAINNIDS = Stream.of(domainsArray).collect(toMap(data -> (String) data[2], data -> (Integer) data[3]));

	public static final Map<Object[], Integer> DOMAIN2NIDS = Stream.of(domainsArray).collect(toMap(data -> new String[]{Utils.escape((String) data[0]), Utils.escape((String) data[1]), Utils.escape((String) data[2])}, data -> (Integer) data[3]));

	// link, recurses, linkid
	private static final Object[][] relationTypesArray = new Object[][]{ //
			{"hypernym", "hypernym", 1, 1}, //
			{"hyponym", "hyponym", 1, 2}, //
			{"instance_hypernym", "instance hypernym", 1, 3}, //
			{"instance_hyponym", "instance hyponym", 1, 4}, //
			{"holo_part", "part holonym", 1, 11}, //
			{"mero_part", "part meronym", 1, 12}, //
			{"holo_member", "member holonym", 1, 13}, //
			{"mero_member", "member meronym", 1, 14}, //
			{"holo_substance", "substance holonym", 1, 15}, //
			{"mero_substance", "substance meronym", 1, 16}, //
			{"entails", "entails", 1, 21}, //
			{"is_entailed_by", "is entailed by", 1, 22}, //
			{"causes", "causes", 1, 23}, //
			{"is_caused_by", "is caused by", 1, 24}, //
			{"antonym", "antonym", 0, 30}, //
			{"similar", "similar", 0, 40}, //
			{"also", "also", 0, 50}, //
			{"attribute", "attribute", 0, 60}, //
			{"verb_group", "verb group", 0, 70}, //
			{"participle", "participle", 0, 71}, //
			{"pertainym", "pertainym", 0, 80}, //
			{"derivation", "derivation", 0, 81}, //
			{"exemplifies", "exemplifies", 0, 82}, //
			{"is_exemplified_by", "is exemplified by", 0, 83}, //
			{"domain_topic", "domain category", 0, 91}, //
			{"has_domain_topic", "domain member category", 0, 92}, //
			{"domain_region", "domain region", 0, 93}, //
			{"has_domain_region", "domain member region", 0, 94}, //
			{"domain_usage", "domain usage", 0, 95}, //
			{"has_domain_usage", "domain member usage", 0, 96}, //
			{"domain", "domain", 0, 97}, //
			{"member", "member", 0, 98}, //
			{"other", "other", 0, 99}, //
	};

	public static final Map<String, Integer> RELATIONTYPES = Stream.of(relationTypesArray).collect(toMap(data -> (String) data[1], data -> (Integer) data[3]));

	public static final Map<Object[], Integer> RELATION2NIDS = Stream.of(relationTypesArray).collect(toMap(data -> new Object[]{Utils.escape((String) data[1]), data[2]}, data -> (Integer) data[3]));

	public static final Map<String, Integer> OEWN_RELATIONTYPES = Stream.of(relationTypesArray).collect(toMap(data -> (String) data[0], data -> (Integer) data[3]));

	// positionname, position
	private static final Object[][] adjPositionTypesArray = new Object[][]{ //
			{"predicate", "p"}, //
			{"attributive", "a"}, //
			{"immediately postnominal", "ip"}, //
	};

	public static final Map<String, String> ADJPOSITIONTYPES = Stream.of(adjPositionTypesArray).collect(toMap(data -> (String) data[0], data -> (String) data[1]));

	// posname, pos
	private static final Object[][] posArray = new Object[][]{ //
			{"noun", "n"}, //
			{"verb", "v"}, //
			{"adjective", "a"}, //
			{"adverb", "r"}, //
			{"adjective satellite", "s"}, //
	};

	public static final Map<String, String> POSTYPES = Stream.of(posArray).collect(toMap(data -> (String) data[0], data -> (String) data[1]));

	public static final Map<String, Integer> VERBFRAMES = Stream.of(VerbFrame.VALUES).collect(toMap(data -> (String) data[1], data -> (Integer) data[2]));

	public static final Map<String, Integer> VERBFRAMEID2NIDS = Stream.of(VerbFrame.VALUES).collect(toMap(data -> (String) data[0], data -> (Integer) data[2]));

	public static void generatePosTypes(final PrintStream ps)
	{
		Printers.printInsert(ps, Names.POSES.TABLE, String.join(",", Names.POSES.posid, Names.POSES.pos), "%n('%s','%s')", BuiltIn.POSTYPES);
	}

	public static void generateAdjectivePositionTypes(final PrintStream ps)
	{
		Printers.printInsert(ps, Names.ADJPOSITIONS.TABLE, String.join(",", Names.ADJPOSITIONS.positionid, Names.ADJPOSITIONS.position), "%n('%s','%s')", BuiltIn.ADJPOSITIONTYPES);
	}

	public static void generateRelationTypes(final PrintStream ps)
	{
		Printers.printInsert2(ps, Names.RELS.TABLE, String.join(",", Names.RELS.relationid, Names.RELS.relation, Names.RELS.recurses), "%n(%d,'%s', %d)", BuiltIn.RELATION2NIDS);
	}

	public static void generateDomains(final PrintStream ps)
	{
		Printers.printInsert3(ps, Names.DOMAINS.TABLE, String.join(",", Names.DOMAINS.domainid, Names.DOMAINS.domain, Names.DOMAINS.posid, Names.DOMAINS.domainname), "%n(%d,'%s','%s','%s')", BuiltIn.DOMAIN2NIDS);
	}

	public static void generateVerbFrames(final PrintStream ps)
	{
		Printers.printInsert(ps, Names.VFRAMES.TABLE, String.join(",", Names.VFRAMES.frameid, Names.VFRAMES.frame), "%n(%d,'%s')", BuiltIn.VERBFRAMES);
	}
}
