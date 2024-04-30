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
        arrayOf("adjs", "a", "adj.all", 0),
        arrayOf("adjs.pert", "a", "adj.pert", 1),
        arrayOf("advs", "r", "adv.all", 2),
        arrayOf("tops", "n", "noun.Tops", 3),
        arrayOf("act", "n", "noun.act", 4),
        arrayOf("animal", "n", "noun.animal", 5),
        arrayOf("artifact", "n", "noun.artifact", 6),
        arrayOf("attribute", "n", "noun.attribute", 7),
        arrayOf("body", "n", "noun.body", 8),
        arrayOf("cognition", "n", "noun.cognition", 9),
        arrayOf("communication", "n", "noun.communication", 10),
        arrayOf("event", "n", "noun.event", 11),
        arrayOf("feeling", "n", "noun.feeling", 12),
        arrayOf("food", "n", "noun.food", 13),
        arrayOf("group", "n", "noun.group", 14),
        arrayOf("location", "n", "noun.location", 15),
        arrayOf("motive", "n", "noun.motive", 16),
        arrayOf("object", "n", "noun.object", 17),
        arrayOf("person", "n", "noun.person", 18),
        arrayOf("phenomenon", "n", "noun.phenomenon", 19),
        arrayOf("plant", "n", "noun.plant", 20),
        arrayOf("possession", "n", "noun.possession", 21),
        arrayOf("process", "n", "noun.process", 22),
        arrayOf("quantity", "n", "noun.quantity", 23),
        arrayOf("relation", "n", "noun.relation", 24),
        arrayOf("shape", "n", "noun.shape", 25),
        arrayOf("state", "n", "noun.state", 26),
        arrayOf("substance", "n", "noun.substance", 27),
        arrayOf("time", "n", "noun.time", 28),
        arrayOf("body", "v", "verb.body", 29),
        arrayOf("change", "v", "verb.change", 30),
        arrayOf("cognition", "v", "verb.cognition", 31),
        arrayOf("communication", "v", "verb.communication", 32),
        arrayOf("competition", "v", "verb.competition", 33),
        arrayOf("consumption", "v", "verb.consumption", 34),
        arrayOf("contact", "v", "verb.contact", 35),
        arrayOf("creation", "v", "verb.creation", 36),
        arrayOf("emotion", "v", "verb.emotion", 37),
        arrayOf("motion", "v", "verb.motion", 38),
        arrayOf("perception", "v", "verb.perception", 39),
        arrayOf("possession", "v", "verb.possession", 40),
        arrayOf("social", "v", "verb.social", 41),
        arrayOf("stative", "v", "verb.stative", 42),
        arrayOf("weather", "v", "verb.weather", 43),
        arrayOf("adjs.ppl", "a", "adj.ppl", 44),
    )

    val LEXFILE_NIDS: Map<String, Int> = sequenceOf(*domainsArray)
        .map { it[2] as String to it[3] as Int }
        .toMap()

    private val DOMAIN_TO_NIDS = sequenceOf(*domainsArray)
        .map { arrayOf(escape(it[0] as String), escape(it[1] as String), escape(it[2] as String)) to it[3] as Int }
        .toMap()

    // link, recurses, linkid

    private val relationTypesArray = arrayOf(
        arrayOf("hypernym", "hypernym", 1, 1),
        arrayOf("hyponym", "hyponym", 1, 2),
        arrayOf("instance_hypernym", "instance hypernym", 1, 3),
        arrayOf("instance_hyponym", "instance hyponym", 1, 4),
        arrayOf("holo_part", "part holonym", 1, 11),
        arrayOf("mero_part", "part meronym", 1, 12),
        arrayOf("holo_member", "member holonym", 1, 13),
        arrayOf("mero_member", "member meronym", 1, 14),
        arrayOf("holo_substance", "substance holonym", 1, 15),
        arrayOf("mero_substance", "substance meronym", 1, 16),
        arrayOf("entails", "entails", 1, 21),
        arrayOf("is_entailed_by", "is entailed by", 1, 22),
        arrayOf("causes", "causes", 1, 23),
        arrayOf("is_caused_by", "is caused by", 1, 24),
        arrayOf("antonym", "antonym", 0, 30),
        arrayOf("similar", "similar", 0, 40),
        arrayOf("also", "also", 0, 50),
        arrayOf("attribute", "attribute", 0, 60),
        arrayOf("verb_group", "verb group", 0, 70),
        arrayOf("participle", "participle", 0, 71),
        arrayOf("pertainym", "pertainym", 0, 80),
        arrayOf("derivation", "derivation", 0, 81),
        arrayOf("domain_topic", "domain topic", 0, 91),
        arrayOf("has_domain_topic", "has domain topic", 0, 92),
        arrayOf("domain_region", "domain region", 0, 93),
        arrayOf("has_domain_region", "has domain region", 0, 94),
        arrayOf("exemplifies", "exemplifies", 0, 95),  // domain usage
        arrayOf("is_exemplified_by", "is exemplified by", 0, 96),  // domain member usage
        arrayOf("domain", "domain", 0, 97),
        arrayOf("member", "member", 0, 98),
        arrayOf("other", "other", 0, 99),

        arrayOf("state", "state", 0, 100),
        arrayOf("result", "result", 0, 101),
        arrayOf("event", "event", 0, 102),
        arrayOf("property", "property", 0, 110),
        arrayOf("location", "location", 0, 120),
        arrayOf("destination", "destination", 0, 121),
        arrayOf("agent", "agent", 0, 130),
        arrayOf("undergoer", "undergoer", 0, 131),
        arrayOf("uses", "uses", 0, 140),
        arrayOf("instrument", "instrument", 0, 141),
        arrayOf("by_means_of", "by means of", 0, 142),
        arrayOf("material", "material", 0, 150),
        arrayOf("vehicle", "vehicle", 0, 160),  //,
        arrayOf("body_part", "body part", 0, 170),
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
    fun generatePosTypes(ps: PrintStream) {
        printInsert(
            ps,
            Names.POSES.TABLE,
            listOf(Names.POSES.posid, Names.POSES.pos).joinToString(","),
            "%n('%s','%s')",
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
            listOf(Names.ADJPOSITIONS.positionid, Names.ADJPOSITIONS.position).joinToString(","),
            "%n('%s','%s')",
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
            listOf(Names.RELS.relationid, Names.RELS.relation, Names.RELS.recurses).joinToString(","),
            "%n(%d,'%s', %d)",
            RELATION_TO_NIDS
        )
    }

    /**
     * Generate domains table
     *
     * @param ps print stream
     */
    fun generateDomains(ps: PrintStream?) {
        printInsert3(
            ps!!,
            Names.DOMAINS.TABLE,
            listOf(Names.DOMAINS.domainid, Names.DOMAINS.domain, Names.DOMAINS.posid, Names.DOMAINS.domainname).joinToString(","),
            "%n(%d,'%s','%s','%s')",
            DOMAIN_TO_NIDS
        )
    }
}
