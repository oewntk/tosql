/*
 * Copyright (c) $originalComment.match("Copyright \(c\) (\d+)", 1, "-")2021. Bernard Bou.
 */

package org.oewntk.sql.out;

import java.util.ResourceBundle;

/**
 * Names from resources
 */
public class Names
{
	static final ResourceBundle bundle = ResourceBundle.getBundle("Names");

	static void set(ResourceBundle bundle)
	{
		WORDS.set(bundle);
		CASEDWORDS.set(bundle);
		PRONUNCIATIONS.set(bundle);
		MORPHS.set(bundle);
		SYNSETS.set(bundle);
		POSES.set(bundle);
		RELS.set(bundle);
		SAMPLES.set(bundle);
		DOMAINS.set(bundle);
		VFRAMES.set(bundle);
		VTEMPLATES.set(bundle);
		ADJPOSITIONS.set(bundle);
		LEXES.set(bundle);
		SENSES.set(bundle);
		SEMRELATIONS.set(bundle);
		LEXRELATIONS.set(bundle);
		LEXES_MORPHS.set(bundle);
		LEXES_PRONUNCIATIONS.set(bundle);
		SENSES_ADJPOSITIONS.set(bundle);
		SENSES_VFRAMES.set(bundle);
		SENSES_VTEMPLATES.set(bundle);
	}

	public static class WORDS
	{
		public static String FILE;
		public static String TABLE;
		public static String wordid;
		public static String word;

		public static void set(ResourceBundle bundle)
		{
			FILE = bundle.getString("words.file");
			TABLE = bundle.getString("words.table");
			wordid = bundle.getString("words.wordid");
			word = bundle.getString("words.word");
		}

		static
		{
			set(bundle);
		}
	}

	public static class CASEDWORDS
	{
		public static String FILE;
		public static String TABLE;
		public static String casedwordid;
		public static String casedword;
		public static String wordid;

		public static void set(ResourceBundle bundle)
		{
			FILE = bundle.getString("casedwords.file");
			TABLE = bundle.getString("casedwords.table");
			casedwordid = bundle.getString("casedwords.casedwordid");
			casedword = bundle.getString("casedwords.casedword");
			wordid = bundle.getString("casedwords.wordid");
		}

		static
		{
			set(bundle);
		}
	}

	public static class PRONUNCIATIONS
	{
		public static String FILE;
		public static String TABLE;
		public static String pronunciationid;
		public static String pronunciation;

		public static void set(ResourceBundle bundle)
		{
			FILE = bundle.getString("pronunciations.file");
			TABLE = bundle.getString("pronunciations.table");
			pronunciationid = bundle.getString("pronunciations.pronunciationid");
			pronunciation = bundle.getString("pronunciations.pronunciation");
		}

		static
		{
			set(bundle);
		}
	}

	public static class MORPHS
	{
		public static String FILE;
		public static String TABLE;
		public static String morphid;
		public static String morph;

		public static void set(ResourceBundle bundle)
		{
			FILE = bundle.getString("morphs.file");
			TABLE = bundle.getString("morphs.table");
			morphid = bundle.getString("morphs.morphid");
			morph = bundle.getString("morphs.morph");
		}

		static
		{
			set(bundle);
		}
	}

	public static class SYNSETS
	{
		public static String FILE;
		public static String TABLE;
		public static String synsetid;
		public static String posid;
		public static String domainid;
		public static String definition;

		public static void set(ResourceBundle bundle)
		{
			FILE = bundle.getString("synsets.file");
			TABLE = bundle.getString("synsets.table");
			synsetid = bundle.getString("synsets.synsetid");
			posid = bundle.getString("synsets.posid");
			domainid = bundle.getString("synsets.domainid");
			definition = bundle.getString("synsets.definition");
		}

		static
		{
			set(bundle);
		}
	}

	public static class POSES
	{
		public static String FILE;
		public static String TABLE;
		public static String posid;
		public static String pos;

		public static void set(ResourceBundle bundle)
		{
			FILE = bundle.getString("poses.file");
			TABLE = bundle.getString("poses.table");
			posid = bundle.getString("poses.posid");
			pos = bundle.getString("poses.pos");
		}

		static
		{
			set(bundle);
		}
	}

	public static class RELS
	{
		public static String FILE;
		public static String TABLE;
		public static String relationid;
		public static String relation;
		public static String recurses;

		public static void set(ResourceBundle bundle)
		{
			FILE = bundle.getString("relations.file");
			TABLE = bundle.getString("relations.table");
			relationid = bundle.getString("relations.relationid");
			relation = bundle.getString("relations.relation");
			recurses = bundle.getString("relations.recurses");
		}

		static
		{
			set(bundle);
		}
	}

	public static class SAMPLES
	{
		public static String FILE;
		public static String TABLE;
		public static String sampleid;
		public static String sample;
		public static String synsetid;

		public static void set(ResourceBundle bundle)
		{
			FILE = bundle.getString("samples.file");
			TABLE = bundle.getString("samples.table");
			sampleid = bundle.getString("samples.sampleid");
			sample = bundle.getString("samples.sample");
			synsetid = bundle.getString("samples.synsetid");
		}

		static
		{
			set(bundle);
		}
	}

	public static class DOMAINS
	{
		public static String FILE;
		public static String TABLE;
		public static String domainid;
		public static String domain;
		public static String domainname;
		public static String posid;

		public static void set(ResourceBundle bundle)
		{
			FILE = bundle.getString("domains.file");
			TABLE = bundle.getString("domains.table");
			domainid = bundle.getString("domains.domainid");
			domain = bundle.getString("domains.domain");
			domainname = bundle.getString("domains.domainname");
			posid = bundle.getString("domains.posid");
		}

		static
		{
			set(bundle);
		}
	}

	public static class VFRAMES
	{
		public static String FILE;
		public static String TABLE;
		public static String frameid;
		public static String frame;

		public static void set(ResourceBundle bundle)
		{
			FILE = bundle.getString("vframes.file");
			TABLE = bundle.getString("vframes.table");
			frameid = bundle.getString("vframes.frameid");
			frame = bundle.getString("vframes.frame");
		}

		static
		{
			set(bundle);
		}
	}

	public static class VTEMPLATES
	{
		public static String FILE;
		public static String TABLE;
		public static String templateid;
		public static String template;

		public static void set(ResourceBundle bundle)
		{
			FILE = bundle.getString("vtemplates.file");
			TABLE = bundle.getString("vtemplates.table");
			templateid = bundle.getString("vtemplates.templateid");
			template = bundle.getString("vtemplates.template");
		}

		static
		{
			set(bundle);
		}
	}

	public static class ADJPOSITIONS
	{
		public static String FILE;
		public static String TABLE;
		public static String positionid;
		public static String position;

		public static void set(ResourceBundle bundle)
		{
			FILE = bundle.getString("adjpositions.file");
			TABLE = bundle.getString("adjpositions.table");
			positionid = bundle.getString("adjpositions.positionid");
			position = bundle.getString("adjpositions.position");
		}

		static
		{
			set(bundle);
		}
	}

	// maps

	public static class LEXES // word - casedword - pronunciations - morphs
	{
		public static String FILE;
		public static String TABLE;
		public static String luid;
		public static String posid;
		public static String wordid;
		public static String casedwordid;

		public static void set(ResourceBundle bundle)
		{
			FILE = bundle.getString("lexes.file");
			TABLE = bundle.getString("lexes.table");
			luid = bundle.getString("lexes.luid");
			posid = bundle.getString("lexes.posid");
			wordid = bundle.getString("lexes.wordid");
			casedwordid = bundle.getString("lexes.casedwordid");
		}

		static
		{
			set(bundle);
		}
	}

	public static class SENSES // word - casedword - synset
	{
		public static String FILE;
		public static String TABLE;
		public static String senseid;
		public static String sensekey;
		public static String luid;
		public static String wordid;
		public static String casedwordid;
		public static String synsetid;
		public static String sensenum;
		public static String lexid;
		public static String tagcount;

		public static void set(ResourceBundle bundle)
		{
			FILE = bundle.getString("senses.file");
			TABLE = bundle.getString("senses.table");
			senseid = bundle.getString("senses.senseid");
			sensekey = bundle.getString("senses.sensekey");
			luid = bundle.getString("senses.luid");
			wordid = bundle.getString("senses.wordid");
			casedwordid = bundle.getString("senses.casedwordid");
			synsetid = bundle.getString("senses.synsetid");
			sensenum = bundle.getString("senses.sensenum");
			lexid = bundle.getString("senses.lexid");
			tagcount = bundle.getString("senses.tagcount");
		}

		static
		{
			set(bundle);
		}
	}

	public static class SEMRELATIONS
	{
		public static String FILE;
		public static String TABLE;
		public static String synset1id;
		public static String synset2id;
		public static String relationid;

		public static void set(ResourceBundle bundle)
		{
			FILE = bundle.getString("semrelations.file");
			TABLE = bundle.getString("semrelations.table");
			synset1id = bundle.getString("semrelations.synset1id");
			synset2id = bundle.getString("semrelations.synset2id");
			relationid = bundle.getString("semrelations.relationid");
		}

		static
		{
			set(bundle);
		}
	}

	public static class LEXRELATIONS
	{
		public static String FILE;
		public static String TABLE;
		public static String synset1id;
		public static String lu1id;
		public static String word1id;
		public static String synset2id;
		public static String lu2id;
		public static String word2id;
		public static String relationid;

		public static void set(ResourceBundle bundle)
		{
			FILE = bundle.getString("lexrelations.file");
			TABLE = bundle.getString("lexrelations.table");
			synset1id = bundle.getString("lexrelations.synset1id");
			lu1id = bundle.getString("semrelations.lu1id");
			word1id = bundle.getString("semrelations.word1id");
			synset2id = bundle.getString("lexrelations.synset2id");
			lu2id = bundle.getString("semrelations.lu2id");
			word2id = bundle.getString("semrelations.word2id");
			relationid = bundle.getString("lexrelations.relationid");
		}

		static
		{
			set(bundle);
		}
	}

	public static class LEXES_MORPHS
	{
		public static String FILE;
		public static String TABLE;
		public static String luid;
		public static String wordid;
		public static String posid;
		public static String morphid;

		public static void set(ResourceBundle bundle)
		{
			FILE = bundle.getString("lexes_morphs.file");
			TABLE = bundle.getString("lexes_morphs.table");
			luid = bundle.getString("lexes_morphs.luid");
			wordid = bundle.getString("lexes_morphs.wordid");
			posid = bundle.getString("lexes_morphs.posid");
			morphid = bundle.getString("lexes_morphs.morphid");
		}

		static
		{
			set(bundle);
		}
	}

	public static class LEXES_PRONUNCIATIONS
	{
		public static String FILE;
		public static String TABLE;
		public static String luid;
		public static String wordid;
		public static String posid;
		public static String pronunciationid;
		public static String variety;

		public static void set(ResourceBundle bundle)
		{
			FILE = bundle.getString("lexes_pronunciations.file");
			TABLE = bundle.getString("lexes_pronunciations.file");
			luid = bundle.getString("lexes_pronunciations.luid");
			wordid = bundle.getString("lexes_pronunciations.wordid");
			posid = bundle.getString("lexes_pronunciations.posid");
			pronunciationid = bundle.getString("lexes_pronunciations.pronunciationid");
			variety = bundle.getString("lexes_pronunciations.variety");
		}

		static
		{
			set(bundle);
		}
	}

	public static class SENSES_ADJPOSITIONS
	{
		public static String FILE;
		public static String TABLE;
		public static String synsetid;
		public static String luid;
		public static String wordid;
		public static String positionid;

		public static void set(ResourceBundle bundle)
		{
			FILE = bundle.getString("senses_adjpositions.file");
			TABLE = bundle.getString("senses_adjpositions.file");
			synsetid = bundle.getString("senses_adjpositions.synsetid");
			luid = bundle.getString("senses_adjpositions.luid");
			wordid = bundle.getString("senses_adjpositions.wordid");
			positionid = bundle.getString("senses_adjpositions.positionid");
		}

		static
		{
			set(bundle);
		}
	}

	public static class SENSES_VFRAMES
	{
		public static String FILE;
		public static String TABLE;
		public static String synsetid;
		public static String luid;
		public static String wordid;
		public static String frameid;

		public static void set(ResourceBundle bundle)
		{
			FILE = bundle.getString("senses_vframes.file");
			TABLE = bundle.getString("senses_vframes.table");
			synsetid = bundle.getString("senses_vframes.synsetid");
			luid = bundle.getString("senses_vframes.luid");
			wordid = bundle.getString("senses_vframes.wordid");
			frameid = bundle.getString("senses_vframes.frameid");
		}

		static
		{
			set(bundle);
		}
	}

	public static class SENSES_VTEMPLATES
	{
		public static String FILE;
		public static String TABLE;
		public static String luid;
		public static String wordid;
		public static String synsetid;
		public static String templateid;

		public static void set(ResourceBundle bundle)
		{
			FILE = bundle.getString("senses_vtemplates.file");
			TABLE = bundle.getString("senses_vtemplates.table");
			luid = bundle.getString("senses_vtemplates.luid");
			wordid = bundle.getString("senses_vtemplates.wordid");
			synsetid = bundle.getString("senses_vtemplates.synsetid");
			templateid = bundle.getString("senses_vtemplates.templateid");
		}

		static
		{
			set(bundle);
		}
	}
}
