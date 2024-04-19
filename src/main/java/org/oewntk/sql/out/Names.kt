/*
 * Copyright (c) $originalComment.match("Copyright \(c\) (\d+)", 1, "-")2021. Bernard Bou.
 */
package org.oewntk.sql.out

import java.util.*

/**
 * Names from resources
 */
object Names {
	val bundle: ResourceBundle = ResourceBundle.getBundle("wn/Names")

	fun set(bundle: ResourceBundle) {
		WORDS.set(bundle)
		CASEDWORDS.set(bundle)
		PRONUNCIATIONS.set(bundle)
		MORPHS.set(bundle)
		SYNSETS.set(bundle)
		POSES.set(bundle)
		RELS.set(bundle)
		SAMPLES.set(bundle)
		DOMAINS.set(bundle)
		VFRAMES.set(bundle)
		VTEMPLATES.set(bundle)
		ADJPOSITIONS.set(bundle)
		LEXES.set(bundle)
		SENSES.set(bundle)
		SEMRELATIONS.set(bundle)
		LEXRELATIONS.set(bundle)
		LEXES_MORPHS.set(bundle)
		LEXES_PRONUNCIATIONS.set(bundle)
		SENSES_ADJPOSITIONS.set(bundle)
		SENSES_VFRAMES.set(bundle)
		SENSES_VTEMPLATES.set(bundle)
	}

	object WORDS {
		lateinit var FILE: String
		lateinit var TABLE: String
		lateinit var wordid: String
		lateinit var word: String

		fun set(bundle: ResourceBundle) {
			FILE = bundle.getString("words.file")
			TABLE = bundle.getString("words.table")
			wordid = bundle.getString("words.wordid")
			word = bundle.getString("words.word")
		}

		init {
			set(bundle)
		}
	}

	object CASEDWORDS {
		lateinit var FILE: String
		lateinit var TABLE: String
		lateinit var casedwordid: String
		lateinit var casedword: String
		lateinit var wordid: String

		fun set(bundle: ResourceBundle) {
			FILE = bundle.getString("casedwords.file")
			TABLE = bundle.getString("casedwords.table")
			casedwordid = bundle.getString("casedwords.casedwordid")
			casedword = bundle.getString("casedwords.casedword")
			wordid = bundle.getString("casedwords.wordid")
		}

		init {
			set(bundle)
		}
	}

	object PRONUNCIATIONS {
		lateinit var FILE: String
		lateinit var TABLE: String
		lateinit var pronunciationid: String
		lateinit var pronunciation: String

		fun set(bundle: ResourceBundle) {
			FILE = bundle.getString("pronunciations.file")
			TABLE = bundle.getString("pronunciations.table")
			pronunciationid = bundle.getString("pronunciations.pronunciationid")
			pronunciation = bundle.getString("pronunciations.pronunciation")
		}

		init {
			set(bundle)
		}
	}

	object MORPHS {
		lateinit var FILE: String
		lateinit var TABLE: String
		lateinit var morphid: String
		lateinit var morph: String

		fun set(bundle: ResourceBundle) {
			FILE = bundle.getString("morphs.file")
			TABLE = bundle.getString("morphs.table")
			morphid = bundle.getString("morphs.morphid")
			morph = bundle.getString("morphs.morph")
		}

		init {
			set(bundle)
		}
	}

	object SYNSETS {
		lateinit var FILE: String
		lateinit var TABLE: String
		lateinit var synsetid: String
		lateinit var posid: String
		lateinit var domainid: String
		lateinit var definition: String

		fun set(bundle: ResourceBundle) {
			FILE = bundle.getString("synsets.file")
			TABLE = bundle.getString("synsets.table")
			synsetid = bundle.getString("synsets.synsetid")
			posid = bundle.getString("synsets.posid")
			domainid = bundle.getString("synsets.domainid")
			definition = bundle.getString("synsets.definition")
		}

		init {
			set(bundle)
		}
	}

	object POSES {
		lateinit var FILE: String
		lateinit var TABLE: String
		lateinit var posid: String
		lateinit var pos: String

		fun set(bundle: ResourceBundle) {
			FILE = bundle.getString("poses.file")
			TABLE = bundle.getString("poses.table")
			posid = bundle.getString("poses.posid")
			pos = bundle.getString("poses.pos")
		}

		init {
			set(bundle)
		}
	}

	object RELS {
		lateinit var FILE: String
		lateinit var TABLE: String
		lateinit var relationid: String
		lateinit var relation: String
		lateinit var recurses: String

		fun set(bundle: ResourceBundle) {
			FILE = bundle.getString("relations.file")
			TABLE = bundle.getString("relations.table")
			relationid = bundle.getString("relations.relationid")
			relation = bundle.getString("relations.relation")
			recurses = bundle.getString("relations.recurses")
		}

		init {
			set(bundle)
		}
	}

	object SAMPLES {
		lateinit var FILE: String
		lateinit var TABLE: String
		lateinit var sampleid: String
		lateinit var sample: String
		lateinit var synsetid: String

		fun set(bundle: ResourceBundle) {
			FILE = bundle.getString("samples.file")
			TABLE = bundle.getString("samples.table")
			sampleid = bundle.getString("samples.sampleid")
			sample = bundle.getString("samples.sample")
			synsetid = bundle.getString("samples.synsetid")
		}

		init {
			set(bundle)
		}
	}

	object DOMAINS {
		private lateinit var FILE: String
		lateinit var TABLE: String
		lateinit var domainid: String
		lateinit var domain: String
		lateinit var domainname: String
		lateinit var posid: String

		fun set(bundle: ResourceBundle) {
			FILE = bundle.getString("domains.file")
			TABLE = bundle.getString("domains.table")
			domainid = bundle.getString("domains.domainid")
			domain = bundle.getString("domains.domain")
			domainname = bundle.getString("domains.domainname")
			posid = bundle.getString("domains.posid")
		}

		init {
			set(bundle)
		}
	}

	object VFRAMES {
		lateinit var FILE: String
		lateinit var TABLE: String
		lateinit var frameid: String
		lateinit var frame: String

		fun set(bundle: ResourceBundle) {
			FILE = bundle.getString("vframes.file")
			TABLE = bundle.getString("vframes.table")
			frameid = bundle.getString("vframes.frameid")
			frame = bundle.getString("vframes.frame")
		}

		init {
			set(bundle)
		}
	}

	object VTEMPLATES {
		lateinit var FILE: String
		lateinit var TABLE: String
		lateinit var templateid: String
		lateinit var template: String

		fun set(bundle: ResourceBundle) {
			FILE = bundle.getString("vtemplates.file")
			TABLE = bundle.getString("vtemplates.table")
			templateid = bundle.getString("vtemplates.templateid")
			template = bundle.getString("vtemplates.template")
		}

		init {
			set(bundle)
		}
	}

	object ADJPOSITIONS {
		lateinit var FILE: String
		lateinit var TABLE: String
		lateinit var positionid: String
		lateinit var position: String

		fun set(bundle: ResourceBundle) {
			FILE = bundle.getString("adjpositions.file")
			TABLE = bundle.getString("adjpositions.table")
			positionid = bundle.getString("adjpositions.positionid")
			position = bundle.getString("adjpositions.position")
		}

		init {
			set(bundle)
		}
	}

	// word - casedword - pronunciations - morphs
	object LEXES {
		lateinit var FILE: String
		lateinit var TABLE: String
		lateinit var luid: String
		lateinit var posid: String
		lateinit var wordid: String
		lateinit var casedwordid: String

		fun set(bundle: ResourceBundle) {
			FILE = bundle.getString("lexes.file")
			TABLE = bundle.getString("lexes.table")
			luid = bundle.getString("lexes.luid")
			posid = bundle.getString("lexes.posid")
			wordid = bundle.getString("lexes.wordid")
			casedwordid = bundle.getString("lexes.casedwordid")
		}

		init {
			set(bundle)
		}
	}

	object SENSES // word - casedword - synset
	{
		lateinit var FILE: String
		lateinit var TABLE: String
		lateinit var senseid: String
		lateinit var sensekey: String
		lateinit var luid: String
		lateinit var wordid: String
		lateinit var casedwordid: String
		lateinit var synsetid: String
		lateinit var sensenum: String
		lateinit var lexid: String
		lateinit var tagcount: String

		fun set(bundle: ResourceBundle) {
			FILE = bundle.getString("senses.file")
			TABLE = bundle.getString("senses.table")
			senseid = bundle.getString("senses.senseid")
			sensekey = bundle.getString("senses.sensekey")
			luid = bundle.getString("senses.luid")
			wordid = bundle.getString("senses.wordid")
			casedwordid = bundle.getString("senses.casedwordid")
			synsetid = bundle.getString("senses.synsetid")
			sensenum = bundle.getString("senses.sensenum")
			lexid = bundle.getString("senses.lexid")
			tagcount = bundle.getString("senses.tagcount")
		}

		init {
			set(bundle)
		}
	}

	object SEMRELATIONS {
		lateinit var FILE: String
		lateinit var TABLE: String
		lateinit var synset1id: String
		lateinit var synset2id: String
		lateinit var relationid: String

		fun set(bundle: ResourceBundle) {
			FILE = bundle.getString("semrelations.file")
			TABLE = bundle.getString("semrelations.table")
			synset1id = bundle.getString("semrelations.synset1id")
			synset2id = bundle.getString("semrelations.synset2id")
			relationid = bundle.getString("semrelations.relationid")
		}

		init {
			set(bundle)
		}
	}

	object LEXRELATIONS {
		lateinit var FILE: String
		lateinit var TABLE: String
		lateinit var synset1id: String
		lateinit var lu1id: String
		lateinit var word1id: String
		lateinit var synset2id: String
		lateinit var lu2id: String
		lateinit var word2id: String
		lateinit var relationid: String

		fun set(bundle: ResourceBundle) {
			FILE = bundle.getString("lexrelations.file")
			TABLE = bundle.getString("lexrelations.table")
			synset1id = bundle.getString("lexrelations.synset1id")
			lu1id = bundle.getString("semrelations.lu1id")
			word1id = bundle.getString("semrelations.word1id")
			synset2id = bundle.getString("lexrelations.synset2id")
			lu2id = bundle.getString("semrelations.lu2id")
			word2id = bundle.getString("semrelations.word2id")
			relationid = bundle.getString("lexrelations.relationid")
		}

		init {
			set(bundle)
		}
	}

	object LEXES_MORPHS {
		lateinit var FILE: String
		lateinit var TABLE: String
		lateinit var luid: String
		lateinit var wordid: String
		lateinit var posid: String
		lateinit var morphid: String

		fun set(bundle: ResourceBundle) {
			FILE = bundle.getString("lexes_morphs.file")
			TABLE = bundle.getString("lexes_morphs.table")
			luid = bundle.getString("lexes_morphs.luid")
			wordid = bundle.getString("lexes_morphs.wordid")
			posid = bundle.getString("lexes_morphs.posid")
			morphid = bundle.getString("lexes_morphs.morphid")
		}

		init {
			set(bundle)
		}
	}

	object LEXES_PRONUNCIATIONS {
		lateinit var FILE: String
		lateinit var TABLE: String
		lateinit var luid: String
		lateinit var wordid: String
		lateinit var posid: String
		lateinit var pronunciationid: String
		lateinit var variety: String

		fun set(bundle: ResourceBundle) {
			FILE = bundle.getString("lexes_pronunciations.file")
			TABLE = bundle.getString("lexes_pronunciations.file")
			luid = bundle.getString("lexes_pronunciations.luid")
			wordid = bundle.getString("lexes_pronunciations.wordid")
			posid = bundle.getString("lexes_pronunciations.posid")
			pronunciationid = bundle.getString("lexes_pronunciations.pronunciationid")
			variety = bundle.getString("lexes_pronunciations.variety")
		}

		init {
			set(bundle)
		}
	}

	object SENSES_ADJPOSITIONS {
		lateinit var FILE: String
		lateinit var TABLE: String
		lateinit var synsetid: String
		lateinit var luid: String
		lateinit var wordid: String
		lateinit var positionid: String

		fun set(bundle: ResourceBundle) {
			FILE = bundle.getString("senses_adjpositions.file")
			TABLE = bundle.getString("senses_adjpositions.file")
			synsetid = bundle.getString("senses_adjpositions.synsetid")
			luid = bundle.getString("senses_adjpositions.luid")
			wordid = bundle.getString("senses_adjpositions.wordid")
			positionid = bundle.getString("senses_adjpositions.positionid")
		}

		init {
			set(bundle)
		}
	}

	object SENSES_VFRAMES {
		lateinit var FILE: String
		lateinit var TABLE: String
		lateinit var synsetid: String
		lateinit var luid: String
		lateinit var wordid: String
		lateinit var frameid: String

		fun set(bundle: ResourceBundle) {
			FILE = bundle.getString("senses_vframes.file")
			TABLE = bundle.getString("senses_vframes.table")
			synsetid = bundle.getString("senses_vframes.synsetid")
			luid = bundle.getString("senses_vframes.luid")
			wordid = bundle.getString("senses_vframes.wordid")
			frameid = bundle.getString("senses_vframes.frameid")
		}

		init {
			set(bundle)
		}
	}

	object SENSES_VTEMPLATES {
		lateinit var FILE: String
		lateinit var TABLE: String
		lateinit var luid: String
		lateinit var wordid: String
		lateinit var synsetid: String
		lateinit var templateid: String

		fun set(bundle: ResourceBundle) {
			FILE = bundle.getString("senses_vtemplates.file")
			TABLE = bundle.getString("senses_vtemplates.table")
			luid = bundle.getString("senses_vtemplates.luid")
			wordid = bundle.getString("senses_vtemplates.wordid")
			synsetid = bundle.getString("senses_vtemplates.synsetid")
			templateid = bundle.getString("senses_vtemplates.templateid")
		}

		init {
			set(bundle)
		}
	}
}
