/*
 * Copyright (c) 2024. Bernard Bou.
 */
package org.oewntk.sql.out

import org.oewntk.model.PartOfSpeech
import org.oewntk.model.SynsetType
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
        // @formatter:off
        arrayOf<Any>("adjs",           PartOfSpeech.A, "${PartOfSpeech.A.fullName}.all",             0),
        arrayOf<Any>("adjs.pert",      PartOfSpeech.A, "${PartOfSpeech.A.fullName}.pert",            1),
        arrayOf<Any>("advs",           PartOfSpeech.R, "${PartOfSpeech.R.fullName}.all",             2),
        arrayOf<Any>("tops",           PartOfSpeech.N, "${PartOfSpeech.N.fullName}.Tops",            3),
        arrayOf<Any>("act",            PartOfSpeech.N, "${PartOfSpeech.N.fullName}.act",             4),
        arrayOf<Any>("animal",         PartOfSpeech.N, "${PartOfSpeech.N.fullName}.animal",          5),
        arrayOf<Any>("artifact",       PartOfSpeech.N, "${PartOfSpeech.N.fullName}.artifact",        6),
        arrayOf<Any>("attribute",      PartOfSpeech.N, "${PartOfSpeech.N.fullName}.attribute",       7),
        arrayOf<Any>("body",           PartOfSpeech.N, "${PartOfSpeech.N.fullName}.body",            8),
        arrayOf<Any>("cognition",      PartOfSpeech.N, "${PartOfSpeech.N.fullName}.cognition",       9),
        arrayOf<Any>("communication",  PartOfSpeech.N, "${PartOfSpeech.N.fullName}.communication",   10),
        arrayOf<Any>("event",          PartOfSpeech.N, "${PartOfSpeech.N.fullName}.event",           11),
        arrayOf<Any>("feeling",        PartOfSpeech.N, "${PartOfSpeech.N.fullName}.feeling",         12),
        arrayOf<Any>("food",           PartOfSpeech.N, "${PartOfSpeech.N.fullName}.food",            13),
        arrayOf<Any>("group",          PartOfSpeech.N, "${PartOfSpeech.N.fullName}.group",           14),
        arrayOf<Any>("location",       PartOfSpeech.N, "${PartOfSpeech.N.fullName}.location",        15),
        arrayOf<Any>("motive",         PartOfSpeech.N, "${PartOfSpeech.N.fullName}.motive",          16),
        arrayOf<Any>("object",         PartOfSpeech.N, "${PartOfSpeech.N.fullName}.object",          17),
        arrayOf<Any>("person",         PartOfSpeech.N, "${PartOfSpeech.N.fullName}.person",          18),
        arrayOf<Any>("phenomenon",     PartOfSpeech.N, "${PartOfSpeech.N.fullName}.phenomenon",      19),
        arrayOf<Any>("plant",          PartOfSpeech.N, "${PartOfSpeech.N.fullName}.plant",           20),
        arrayOf<Any>("possession",     PartOfSpeech.N, "${PartOfSpeech.N.fullName}.possession",      21),
        arrayOf<Any>("process",        PartOfSpeech.N, "${PartOfSpeech.N.fullName}.process",         22),
        arrayOf<Any>("quantity",       PartOfSpeech.N, "${PartOfSpeech.N.fullName}.quantity",        23),
        arrayOf<Any>("relation",       PartOfSpeech.N, "${PartOfSpeech.N.fullName}.relation",        24),
        arrayOf<Any>("shape",          PartOfSpeech.N, "${PartOfSpeech.N.fullName}.shape",           25),
        arrayOf<Any>("state",          PartOfSpeech.N, "${PartOfSpeech.N.fullName}.state",           26),
        arrayOf<Any>("substance",      PartOfSpeech.N, "${PartOfSpeech.N.fullName}.substance",       27),
        arrayOf<Any>("time",           PartOfSpeech.N, "${PartOfSpeech.N.fullName}.time",            28),
        arrayOf<Any>("body",           PartOfSpeech.V, "${PartOfSpeech.V.fullName}.body",            29),
        arrayOf<Any>("change",         PartOfSpeech.V, "${PartOfSpeech.V.fullName}.change",          30),
        arrayOf<Any>("cognition",      PartOfSpeech.V, "${PartOfSpeech.V.fullName}.cognition",       31),
        arrayOf<Any>("communication",  PartOfSpeech.V, "${PartOfSpeech.V.fullName}.communication",   32),
        arrayOf<Any>("competition",    PartOfSpeech.V, "${PartOfSpeech.V.fullName}.competition",     33),
        arrayOf<Any>("consumption",    PartOfSpeech.V, "${PartOfSpeech.V.fullName}.consumption",     34),
        arrayOf<Any>("contact",        PartOfSpeech.V, "${PartOfSpeech.V.fullName}.contact",         35),
        arrayOf<Any>("creation",       PartOfSpeech.V, "${PartOfSpeech.V.fullName}.creation",        36),
        arrayOf<Any>("emotion",        PartOfSpeech.V, "${PartOfSpeech.V.fullName}.emotion",         37),
        arrayOf<Any>("motion",         PartOfSpeech.V, "${PartOfSpeech.V.fullName}.motion",          38),
        arrayOf<Any>("perception",     PartOfSpeech.V, "${PartOfSpeech.V.fullName}.perception",      39),
        arrayOf<Any>("possession",     PartOfSpeech.V, "${PartOfSpeech.V.fullName}.possession",      40),
        arrayOf<Any>("social",         PartOfSpeech.V, "${PartOfSpeech.V.fullName}.social",          41),
        arrayOf<Any>("stative",        PartOfSpeech.V, "${PartOfSpeech.V.fullName}.stative",         42),
        arrayOf<Any>("weather",        PartOfSpeech.V, "${PartOfSpeech.V.fullName}.weather",         43),
        arrayOf<Any>("adjs.ppl",       PartOfSpeech.A, "${PartOfSpeech.A.fullName}.ppl",             44),
        // @formatter:on
        )

    val LEXFILE_NIDS: Map<String, Int> = sequenceOf(*domainsArray).associate { it[2] as String to it[3] as Int }

    private val DOMAIN_TO_NIDS = sequenceOf(*domainsArray).associate { arrayOf(escape(it[0] as String), escape(it[1] as String), escape(it[2] as String)) to it[3] as Int }

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

    private val RELATION_TO_NIDS = sequenceOf(*relationTypesArray).associate { arrayOf(escape(it[1] as String), it[2]) to it[3] as Int }

    val OEWN_RELATION_TYPES: Map<String, Int> = sequenceOf(*relationTypesArray).associate { it[0] as String to it[3] as Int }

    // positionname, position

    private val adjPositionTypesArray = arrayOf(
        arrayOf("predicate", "p"),
        arrayOf("attributive", "a"),
        arrayOf("immediately postnominal", "ip"),
    )

    private val ADJPOSITION_TYPES = sequenceOf(*adjPositionTypesArray).associate { it[0] to it[1] }

    // posname, pos
    private val posArray = arrayOf(
        arrayOf("noun",                SynsetType.N.value.toString()),
        arrayOf("verb",                SynsetType.V.value.toString()),
        arrayOf("adjective",           SynsetType.A.value.toString()),
        arrayOf("adverb",              SynsetType.R.value.toString()),
        arrayOf("adjective satellite", SynsetType.S.value.toString()),
    )

    private val POS_TYPES = sequenceOf(*posArray).associate { it[0] to it[1] }

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
