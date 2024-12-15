/*
 * Copyright (c) 2024. Bernard Bou.
 */
package org.oewntk.sql.out

import org.oewntk.sql.out.Printers.printInsert
import org.oewntk.sql.out.Printers.printInsert2
import org.oewntk.sql.out.Printers.printInsert3
import org.oewntk.sql.out.Utils.escape
import java.io.PrintStream

/**
 * Builtins
 */
object BuiltIn {

    // lexdomain, lexdomainid

    private val domainsArray = arrayOf(
        arrayOf<Any>("adjs", "a", "adj.all", 0),
        arrayOf<Any>("adjs.pert", "a", "adj.pert", 1),
        arrayOf<Any>("advs", "r", "adv.all", 2),
        arrayOf<Any>("tops", "n", "noun.Tops", 3),
        arrayOf<Any>("act", "n", "noun.act", 4),
        arrayOf<Any>("animal", "n", "noun.animal", 5),
        arrayOf<Any>("artifact", "n", "noun.artifact", 6),
        arrayOf<Any>("attribute", "n", "noun.attribute", 7),
        arrayOf<Any>("body", "n", "noun.body", 8),
        arrayOf<Any>("cognition", "n", "noun.cognition", 9),
        arrayOf<Any>("communication", "n", "noun.communication", 10),
        arrayOf<Any>("event", "n", "noun.event", 11),
        arrayOf<Any>("feeling", "n", "noun.feeling", 12),
        arrayOf<Any>("food", "n", "noun.food", 13),
        arrayOf<Any>("group", "n", "noun.group", 14),
        arrayOf<Any>("location", "n", "noun.location", 15),
        arrayOf<Any>("motive", "n", "noun.motive", 16),
        arrayOf<Any>("object", "n", "noun.object", 17),
        arrayOf<Any>("person", "n", "noun.person", 18),
        arrayOf<Any>("phenomenon", "n", "noun.phenomenon", 19),
        arrayOf<Any>("plant", "n", "noun.plant", 20),
        arrayOf<Any>("possession", "n", "noun.possession", 21),
        arrayOf<Any>("process", "n", "noun.process", 22),
        arrayOf<Any>("quantity", "n", "noun.quantity", 23),
        arrayOf<Any>("relation", "n", "noun.relation", 24),
        arrayOf<Any>("shape", "n", "noun.shape", 25),
        arrayOf<Any>("state", "n", "noun.state", 26),
        arrayOf<Any>("substance", "n", "noun.substance", 27),
        arrayOf<Any>("time", "n", "noun.time", 28),
        arrayOf<Any>("body", "v", "verb.body", 29),
        arrayOf<Any>("change", "v", "verb.change", 30),
        arrayOf<Any>("cognition", "v", "verb.cognition", 31),
        arrayOf<Any>("communication", "v", "verb.communication", 32),
        arrayOf<Any>("competition", "v", "verb.competition", 33),
        arrayOf<Any>("consumption", "v", "verb.consumption", 34),
        arrayOf<Any>("contact", "v", "verb.contact", 35),
        arrayOf<Any>("creation", "v", "verb.creation", 36),
        arrayOf<Any>("emotion", "v", "verb.emotion", 37),
        arrayOf<Any>("motion", "v", "verb.motion", 38),
        arrayOf<Any>("perception", "v", "verb.perception", 39),
        arrayOf<Any>("possession", "v", "verb.possession", 40),
        arrayOf<Any>("social", "v", "verb.social", 41),
        arrayOf<Any>("stative", "v", "verb.stative", 42),
        arrayOf<Any>("weather", "v", "verb.weather", 43),
        arrayOf<Any>("adjs.ppl", "a", "adj.ppl", 44),
    )

    val LEXFILE_NIDS: Map<String, Int> = sequenceOf(*domainsArray)
        .map { it[2] as String to it[3] as Int }
        .toMap()

    private val DOMAIN_TO_NIDS = sequenceOf(*domainsArray)
        .map { arrayOf(escape(it[0] as String), escape(it[1] as String), escape(it[2] as String)) to it[3] as Int }
        .toMap()

    // link, recurses, linkid

    private val relationTypesArray = arrayOf(
        arrayOf<Any>("hypernym", "hypernym", 1, 1),
        arrayOf<Any>("hyponym", "hyponym", 1, 2),
        arrayOf<Any>("instance_hypernym", "instance hypernym", 1, 3),
        arrayOf<Any>("instance_hyponym", "instance hyponym", 1, 4),
        arrayOf<Any>("holo_part", "part holonym", 1, 11),
        arrayOf<Any>("mero_part", "part meronym", 1, 12),
        arrayOf<Any>("holo_member", "member holonym", 1, 13),
        arrayOf<Any>("mero_member", "member meronym", 1, 14),
        arrayOf<Any>("holo_substance", "substance holonym", 1, 15),
        arrayOf<Any>("mero_substance", "substance meronym", 1, 16),
        arrayOf<Any>("entails", "entails", 1, 21),
        arrayOf<Any>("is_entailed_by", "is entailed by", 1, 22),
        arrayOf<Any>("causes", "causes", 1, 23),
        arrayOf<Any>("is_caused_by", "is caused by", 1, 24),
        arrayOf<Any>("antonym", "antonym", 0, 30),
        arrayOf<Any>("similar", "similar", 0, 40),
        arrayOf<Any>("also", "also", 0, 50),
        arrayOf<Any>("attribute", "attribute", 0, 60),
        arrayOf<Any>("verb_group", "verb group", 0, 70),
        arrayOf<Any>("participle", "participle", 0, 71),
        arrayOf<Any>("pertainym", "pertainym", 0, 80),
        arrayOf<Any>("derivation", "derivation", 0, 81),
        arrayOf<Any>("domain_topic", "domain topic", 0, 91),
        arrayOf<Any>("has_domain_topic", "has domain topic", 0, 92),
        arrayOf<Any>("domain_region", "domain region", 0, 93),
        arrayOf<Any>("has_domain_region", "has domain region", 0, 94),
        arrayOf<Any>("exemplifies", "exemplifies", 0, 95),  // domain usage
        arrayOf<Any>("is_exemplified_by", "is exemplified by", 0, 96),  // domain member usage
        arrayOf<Any>("domain", "domain", 0, 97),
        arrayOf<Any>("member", "member", 0, 98),
        arrayOf<Any>("other", "other", 0, 99),

        arrayOf<Any>("state", "state", 0, 100),
        arrayOf<Any>("result", "result", 0, 101),
        arrayOf<Any>("event", "event", 0, 102),
        arrayOf<Any>("property", "property", 0, 110),
        arrayOf<Any>("location", "location", 0, 120),
        arrayOf<Any>("destination", "destination", 0, 121),
        arrayOf<Any>("agent", "agent", 0, 130),
        arrayOf<Any>("undergoer", "undergoer", 0, 131),
        arrayOf<Any>("uses", "uses", 0, 140),
        arrayOf<Any>("instrument", "instrument", 0, 141),
        arrayOf<Any>("by_means_of", "by means of", 0, 142),
        arrayOf<Any>("material", "material", 0, 150),
        arrayOf<Any>("vehicle", "vehicle", 0, 160),  //,
        arrayOf<Any>("body_part", "body part", 0, 170),

        arrayOf<Any>("collocation", "collocation", 0, 200),
    )

    private val RELATION_TO_NIDS = sequenceOf(*relationTypesArray)
        .map { arrayOf(escape(it[1] as String), it[2]) to it[3] as Int }
        .toMap()

    val OEWN_RELATION_TYPES: Map<String, Int> = sequenceOf(*relationTypesArray)
        .map { it[0] as String to it[3] as Int }
        .toMap()

    // positionname, position

    private val adjPositionTypesArray = arrayOf(
        arrayOf("predicate", "p"),
        arrayOf("attributive", "a"),
        arrayOf("immediately postnominal", "ip"),
    )

    private val ADJPOSITION_TYPES = sequenceOf(*adjPositionTypesArray)
        .map { it[0] to it[1] }
        .toMap()

    // posname, pos
    private val posArray = arrayOf(
        arrayOf("noun", "n"),
        arrayOf("verb", "v"),
        arrayOf("adjective", "a"),
        arrayOf("adverb", "r"),
        arrayOf("adjective satellite", "s"),
    )

    private val POS_TYPES = sequenceOf(*posArray)
        .map { it[0] to it[1] }
        .toMap()

    /**
     * Generate pos types table
     *
     * @param ps print stream
     */
    fun generatePoses(ps: PrintStream) {
        printInsert(
            ps,
            Names.POSES.TABLE,
            listOf(
                Names.POSES.posid,
                Names.POSES.pos
            ).joinToString(","),
            "('%s','%s')",
            POS_TYPES
        )
    }

    /**
     * Generate adjective position types table
     *
     * @param ps print stream
     */
    fun generateAdjectivePositionTypes(ps: PrintStream) {
        printInsert(
            ps,
            Names.ADJPOSITIONS.TABLE,
            listOf(
                Names.ADJPOSITIONS.positionid,
                Names.ADJPOSITIONS.position
            ).joinToString(","),
            "('%s','%s')",
            ADJPOSITION_TYPES
        )
    }

    /**
     * Generate relation types table
     *
     * @param ps print stream
     */
    fun generateRelationTypes(ps: PrintStream) {
        printInsert2(
            ps,
            Names.RELS.TABLE,
            listOf(
                Names.RELS.relationid,
                Names.RELS.relation,
                Names.RELS.recurses
            ).joinToString(","),
            "(%d,'%s', %d)",
            RELATION_TO_NIDS
        )
    }

    /**
     * Generate domains table
     *
     * @param ps print stream
     */
    fun generateDomains(ps: PrintStream) {
        printInsert3(
            ps,
            Names.DOMAINS.TABLE,
            listOf(
                Names.DOMAINS.domainid,
                Names.DOMAINS.domain,
                Names.DOMAINS.posid,
                Names.DOMAINS.domainname
            ).joinToString(","),
            "(%d,'%s','%s','%s')",
            DOMAIN_TO_NIDS
        )
    }
}
