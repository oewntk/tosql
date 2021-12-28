/*
 * Copyright (c) $originalComment.match("Copyright \(c\) (\d+)", 1, "-")2021. Bernard Bou.
 */

package org.oewntk.sql.out;

import java.util.ResourceBundle;

public class Names
{
	private static final boolean COMPAT = false;

	private static final ResourceBundle bundle = ResourceBundle.getBundle(COMPAT ? "NamesCompat" : "Names");

	public static class WORDS
	{
		public static final String FILE = bundle.getString("words.file");
		public static final String TABLE = bundle.getString("words.table");
		public static final String wordid = bundle.getString("words.wordid");
		public static final String word = bundle.getString("words.word");
	}

	public static class CASEDWORDS
	{
		public static final String FILE = bundle.getString("casedwords.file");
		public static final String TABLE = bundle.getString("casedwords.table");
		public static final String casedwordid = bundle.getString("casedwords.casedwordid");
		public static final String casedword = bundle.getString("casedwords.casedword");
		public static final String wordid = bundle.getString("casedwords.wordid");
	}

	public static class PRONUNCIATIONS
	{
		public static final String FILE = bundle.getString("pronunciations.file");
		public static final String TABLE = bundle.getString("pronunciations.table");
		public static final String pronunciationid = bundle.getString("pronunciations.pronunciationid");
		public static final String pronunciation = bundle.getString("pronunciations.pronunciation");
	}

	public static class MORPHS
	{
		public static final String FILE = bundle.getString("morphs.file");
		public static final String TABLE = bundle.getString("morphs.table");
		public static final String morphid = bundle.getString("morphs.morphid");
		public static final String morph = bundle.getString("morphs.morph");
	}

	public static class SYNSETS
	{
		public static final String FILE = bundle.getString("synsets.file");
		public static final String TABLE = bundle.getString("synsets.table");
		public static final String synsetid = bundle.getString("synsets.synsetid");
		public static final String posid = bundle.getString("synsets.posid");
		public static final String domainid = bundle.getString("synsets.domainid");
		public static final String definition = bundle.getString("synsets.definition");
	}

	public static class POSES
	{
		public static final String FILE = bundle.getString("poses.file");
		public static final String TABLE = bundle.getString("poses.table");
		public static final String posid = bundle.getString("poses.posid");
		public static final String pos = bundle.getString("poses.pos");
	}

	public static class RELS
	{
		public static final String FILE = bundle.getString("relations.file");
		public static final String TABLE = bundle.getString("relations.table");
		public static final String relationid = bundle.getString("relations.relationid");
		public static final String relation = bundle.getString("relations.relation");
		public static final String recurses = bundle.getString("relations.recurses");
	}

	public static class SAMPLES
	{
		public static final String FILE = bundle.getString("samples.file");
		public static final String TABLE = bundle.getString("samples.table");
		public static final String sampleid = bundle.getString("samples.sampleid");
		public static final String sample = bundle.getString("samples.sample");
		public static final String synsetid = bundle.getString("samples.synsetid");
	}

	public static class DOMAINS
	{
		public static final String FILE = bundle.getString("domains.file");
		public static final String TABLE = bundle.getString("domains.table");
		public static final String domainid = bundle.getString("domains.domainid");
		public static final String domain = bundle.getString("domains.domain");
		public static final String domainname = bundle.getString("domains.domainname");
		public static final String posid = bundle.getString("domains.posid");
	}

	public static class VFRAMES
	{
		public static final String FILE = bundle.getString("vframes.file");
		public static final String TABLE = bundle.getString("vframes.table");
		public static final String frameid = bundle.getString("vframes.frameid");
		public static final String frame = bundle.getString("vframes.frame");
	}

	public static class VTEMPLATES
	{
		public static final String FILE = bundle.getString("vtemplates.file");
		public static final String TABLE = bundle.getString("vtemplates.table");
		public static final String templateid = bundle.getString("vtemplates.templateid");
		public static final String template = bundle.getString("vtemplates.template");
	}

	public static class ADJPOSITIONS
	{
		public static final String FILE = bundle.getString("adjpositions.file");
		public static final String TABLE = bundle.getString("adjpositions.table");
		public static final String positionid = bundle.getString("adjpositions.positionid");
		public static final String position = bundle.getString("adjpositions.position");
	}

	// maps

	public static class LEXES // word - casedword - pronunciations - morphs
	{
		public static final String FILE = bundle.getString("lexes.file");
		public static final String TABLE = bundle.getString("lexes.table");
		public static final String luid = bundle.getString("lexes.luid");
		public static final String posid = bundle.getString("lexes.posid");
		public static final String wordid = bundle.getString("lexes.wordid");
		public static final String casedwordid = bundle.getString("lexes.casedwordid");
	}

	public static class SENSES // word - casedword - synset
	{
		public static final String FILE = bundle.getString("senses.file");
		public static final String TABLE = bundle.getString("senses.table");
		public static final String senseid = bundle.getString("senses.senseid");
		public static final String sensekey = bundle.getString("senses.sensekey");
		public static final String luid = bundle.getString("senses.luid");
		public static final String wordid = bundle.getString("senses.wordid");
		public static final String casedwordid = bundle.getString("senses.casedwordid");
		public static final String synsetid = bundle.getString("senses.synsetid");
		public static final String sensenum = bundle.getString("senses.sensenum");
		public static final String lexid = bundle.getString("senses.lexid");
		public static final String tagcount = bundle.getString("senses.tagcount");
	}

	public static class SYNSETS_SYNSETS
	{
		public static final String FILE = bundle.getString("synsets_synsets.file");
		public static final String TABLE = bundle.getString("synsets_synsets.table");
		public static final String synset1id = bundle.getString("synsets_synsets.synset1id");
		public static final String synset2id = bundle.getString("synsets_synsets.synset2id");
		public static final String relationid = bundle.getString("synsets_synsets.relationid");
	}

	public static class SENSES_SENSES
	{
		public static final String FILE = bundle.getString("senses_senses.file");
		public static final String TABLE = bundle.getString("senses_senses.table");
		public static final String synset1id = bundle.getString("senses_senses.synset1id");
		public static final String lu1id = bundle.getString("synsets_synsets.lu1id");
		public static final String word1id = bundle.getString("synsets_synsets.word1id");
		public static final String synset2id = bundle.getString("senses_senses.synset2id");
		public static final String lu2id = bundle.getString("synsets_synsets.lu2id");
		public static final String word2id = bundle.getString("synsets_synsets.word2id");
		public static final String relationid = bundle.getString("senses_senses.relationid");
	}

	public static class LEXES_MORPHS
	{
		public static final String FILE = bundle.getString("lexes_morphs.file");
		public static final String TABLE = bundle.getString("lexes_morphs.table");
		public static final String luid = bundle.getString("lexes_morphs.luid");
		public static final String wordid = bundle.getString("lexes_morphs.wordid");
		public static final String posid = bundle.getString("lexes_morphs.posid");
		public static final String morphid = bundle.getString("lexes_morphs.morphid");
	}

	public static class LEXES_PRONUNCIATIONS
	{
		public static final String FILE = bundle.getString("lexes_pronunciations.file");
		public static final String TABLE = bundle.getString("lexes_pronunciations.file");
		public static final String luid = bundle.getString("lexes_pronunciations.luid");
		public static final String wordid = bundle.getString("lexes_pronunciations.wordid");
		public static final String posid = bundle.getString("lexes_pronunciations.posid");
		public static final String pronunciationid = bundle.getString("lexes_pronunciations.pronunciationid");
		public static final String variety = bundle.getString("lexes_pronunciations.variety");
	}

	public static class SENSES_ADJPOSITIONS
	{
		public static final String FILE = bundle.getString("senses_adjpositions.file");
		public static final String TABLE = bundle.getString("senses_adjpositions.file");
		public static final String synsetid = bundle.getString("senses_adjpositions.synsetid");
		public static final String luid = bundle.getString("senses_adjpositions.luid");
		public static final String wordid = bundle.getString("senses_adjpositions.wordid");
		public static final String positionid = bundle.getString("senses_adjpositions.positionid");
	}

	public static class SENSES_VFRAMES
	{
		public static final String FILE = bundle.getString("senses_vframes.file");
		public static final String TABLE = bundle.getString("senses_vframes.table");
		public static final String synsetid = bundle.getString("senses_vframes.synsetid");
		public static final String luid = bundle.getString("senses_vframes.luid");
		public static final String wordid = bundle.getString("senses_vframes.wordid");
		public static final String frameid = bundle.getString("senses_vframes.frameid");
	}

	public static class SENSES_VTEMPLATES
	{
		public static final String FILE = bundle.getString("senses_vtemplates.file");
		public static final String TABLE = bundle.getString("senses_vtemplates.table");
		public static final String luid = bundle.getString("senses_vtemplates.luid");
		public static final String wordid = bundle.getString("senses_vtemplates.wordid");
		public static final String synsetid = bundle.getString("senses_vtemplates.synsetid");
		public static final String templateid = bundle.getString("senses_vtemplates.templateid");
	}
}
