/*
 * Copyright (c) 2024. Bernard Bou.
 */
package org.oewntk.sql.out

import org.oewntk.model.Key
import org.oewntk.model.Lex
import java.io.PrintStream

/**
 * Insert printers
 */
object Printers {

    const val WITH_COMMENT: Boolean = true

    // from maps

    /**
     * Print inserts for objects mapped by nids
     *
     * @param ps          print stream
     * @param table       table name
     * @param columns     column names
     * @param objectToNID objects-to-nid map
     * @param toRow       stringifier of objects
     * @param T           type of objects
     */
    fun <T> printInsert(
        ps: PrintStream,
        table: String,
        columns: String,
        objectToNID: Map<T, Int>,
        toRow: (T) -> String,
    ) {
        if (objectToNID.isEmpty()) {
            ps.print("-- NONE")
        } else {
            ps.printf("INSERT INTO %s (%s) VALUES", table, columns)
            objectToNID.keys
                .withIndex()
                .forEach { (index, key) ->
                    val s = toRow.invoke(key)
                    if (index > 0) {
                        ps.print(',')
                    }
                    val nid = NIDMaps.lookup(objectToNID, key)
                    ps.printf("%n(%d,%s)", nid, s)
                }
            ps.println(";")
        }
    }

    /**
     * Print inserts for collection
     *
     * @param ps            print stream
     * @param table         table name
     * @param columns       column names
     * @param objects       collection of objects
     * @param toId          id extractor
     * @param objectIdToNID id-to-nid map
     * @param toRow         stringifier of objects
     * @param T             type of objects
     */
    fun <T> printInsert(
        ps: PrintStream,
        table: String,
        columns: String,
        objects: Collection<T>,
        toId: (T) -> String,
        objectIdToNID: Map<String, Int>,
        toRow: (T) -> String,
    ) {
        if (objects.isEmpty()) {
            ps.print("-- NONE")
        } else {
            ps.printf("INSERT INTO %s (%s) VALUES", table, columns)
            objects
                .asSequence()
                .map { it to NIDMaps.lookup(objectIdToNID, toId.invoke(it)) }
                .toList()
                .sortedBy { it.second }
                .withIndex()
                .forEach { (index, pair) ->
                    if (index > 0) {
                        ps.print(',')
                    }
                    val s = toRow.invoke(pair.first)
                    ps.printf("%n(%d,%s)", pair.second, s)
                }
            ps.println(";")
        }
    }

    /**
     * Print inserts for collection with comments
     *
     * @param ps                   print stream
     * @param table                table name
     * @param columns              column names
     * @param objects              collection of objects
     * @param toId                 id extractor
     * @param objectIdToNID        id-to-nid map
     * @param toRowWithComment     double stringifier of objects, two strings are produced: [0] insert values , [1] comment
     * @param T                    type of objects
     */
    fun <T> printInsertWithComment(
        ps: PrintStream,
        table: String,
        columns: String,
        objects: Collection<T>,
        toId: (T) -> String,
        objectIdToNID: Map<String, Int>,
        toRowWithComment: (T) -> Pair<String, String>,
    ) {
        if (objects.isEmpty()) {
            ps.print("-- NONE")
        } else {
            ps.printf("INSERT INTO %s (%s) VALUES", table, columns)
            objects
                .asSequence()
                .map { it to NIDMaps.lookup(objectIdToNID, toId.invoke(it)) }
                .toList()
                .sortedBy { it.second }
                .withIndex()
                .forEach { (index, pair) ->
                    if (index > 0) {
                        ps.print(',')
                    }
                    val s = toRowWithComment.invoke(pair.first)
                    ps.printf("%n(%d,%s) /* %s */", pair.second, s.first, s.second)
                }
            ps.println(";")
        }
    }

    // lexes

    /**
     * Print inserts from lexes
     *
     * @param ps          print stream
     * @param table       table name
     * @param columns     column names
     * @param lexes       lexes
     * @param lexKeyToNID lex_key-to-nid map
     * @param toRow       stringifier of lexes
     */
    fun printInsert(
        ps: PrintStream,
        table: String,
        columns: String,
        lexes: Collection<Lex>,
        lexKeyToNID: Map<Key, Int>,
        toRow: (Lex) -> String,
    ) {
        if (lexes.isEmpty()) {
            ps.print("-- NONE")
        } else {
            ps.printf("INSERT INTO %s (%s) VALUES", table, columns)
            lexes
                .asSequence()
                .map { it to NIDMaps.lookup(lexKeyToNID, Key.KeyLCP.of_t(it)) }
                .toList()
                .sortedBy { it.second }
                .withIndex()
                .forEach { (index, pair) ->
                    if (index > 0) {
                        ps.print(',')
                    }
                    val lex = pair.first
                    val v = pair.second
                    val s = toRow.invoke(lex)
                    ps.printf("%n(%d,%s)", v, s)
                }
            ps.println(";")
        }
    }

    /**
     * Print inserts from lexes with comments
     *
     * @param ps                   print stream
     * @param table                table name
     * @param columns              column names
     * @param lexes                lexes
     * @param lexKeyToNID          lex_key-to-nid map
     * @param toRowWithComment     double stringifier of lexes, two strings are produced: [0] insert values , [1] comment
     */
    fun printInsertWithComment(
        ps: PrintStream,
        table: String,
        columns: String,
        lexes: Collection<Lex>,
        lexKeyToNID: Map<Key, Int>,
        toRowWithComment: (Lex) -> Pair<String, String>,
    ) {
        if (lexes.isEmpty()) {
            ps.print("-- NONE")
        } else {
            ps.printf("INSERT INTO %s (%s) VALUES", table, columns)
            lexes
                .asSequence()
                .map { it to NIDMaps.lookup(lexKeyToNID, Key.KeyLCP.of_t(it)) }
                .toList()
                .sortedBy { it.second }
                .withIndex()
                .forEach { (index, pair) ->
                    if (index > 0) {
                        ps.print(',')
                    }
                    val lex = pair.first
                    val v = pair.second
                    val s = toRowWithComment.invoke(lex)
                    ps.printf("%n(%d,%s) /* %s */", v, s.first, s.second)
                }
            ps.println(";")
        }
    }

    // from sequences

    /**
     * Print inserts from sequence
     *
     * @param ps         print stream
     * @param table      table name
     * @param columns    column names
     * @param seq        sequence of objects
     * @param toRow      stringifier of objects
     * @param withNumber whether to number objects
     * @param T          type of objects in sequence
     */
    fun <T> printInsert(
        ps: PrintStream,
        table: String,
        columns: String,
        seq: Sequence<T>,
        toRow: (T) -> String,
        withNumber: Boolean,
    ) {
        seq
            .withIndex()
            .forEach { (index, thing) ->
                if (index == 0) {
                    ps.printf("INSERT INTO %s (%s) VALUES", table, columns)
                } else {
                    ps.print(',')
                }
                val s = toRow.invoke(thing)
                if (withNumber) {
                    ps.printf("%n(%d,%s)", index + 1, s)
                } else {
                    ps.printf("%n(%s)", s)
                }
            }
        ps.println(";")
    }

    /**
     * Print inserts from sequence with comments
     *
     * @param ps                  print stream
     * @param table               table name
     * @param columns             column names
     * @param seq                 sequence of objects
     * @param toRowWithComment    double stringifier of objects, two strings are produced: [0] insert values , [1] comment
     * @param withNumber          whether to number objects
     * @param T                   type of objects in sequence
     */
    fun <T> printInsertWithComment(
        ps: PrintStream,
        table: String,
        columns: String,
        seq: Sequence<T>,
        toRowWithComment: (T) -> Pair<String,String>,
        withNumber: Boolean,
    ) {
        seq
            .withIndex()
            .forEach { (index, thing) ->
                if (index == 0) {
                    ps.printf("INSERT INTO %s (%s) VALUES", table, columns)
                } else {
                    ps.print(',')
                }
                val s = toRowWithComment.invoke(thing)
                if (withNumber) {
                    ps.printf("%n(%d,%s) /* %s */", index + 1, s.first, s.second)
                } else {
                    ps.printf("%n(%s) /* %s */", s.first, s.second)
                }
            }
        ps.println(";")
    }

    /**
     * Print inserts from sequence
     *
     * @param ps         print sequence
     * @param table      table name
     * @param columns    column names
     * @param seq        sequence of objects
     * @param toRows     stringifier for multiple values
     * @param withNumber whether to number objects
     * @param T          type of objects in sequence
     */
    fun <T> printInserts(
        ps: PrintStream,
        table: String,
        columns: String,
        seq: Sequence<T>,
        toRows: (T) -> List<String>,
        withNumber: Boolean,
    ) {
        seq
            .flatMap(toRows)
            .withIndex()
            .forEach {
                if (it.index == 0) {
                    ps.printf("INSERT INTO %s (%s) VALUES", table, columns)
                } else {
                    ps.print(',')
                }
                if (withNumber) {
                    ps.printf("%n(%d,%s)", it.index + 1, it.value)
                } else {
                    ps.printf("%n(%s)", it.value)
                }
            }
        ps.println(";")
    }

    /**
     * Print inserts from sequence with comments
     *
     * @param ps                    print stream
     * @param table                 table name
     * @param columns               column names
     * @param seq                   sequence of objects
     * @param toRowsWithComments double stringifier of objects, two strings are produced: [0] insert values , [1] comment
     * @param withNumber            whether to number objects
     * @param T                     type of objects in sequence
     */
    fun <T> printInsertsWithComment(
        ps: PrintStream,
        table: String,
        columns: String,
        seq: Sequence<T>,
        toRowsWithComments: (T) -> Sequence<Pair<String, String>>,
        withNumber: Boolean,
    ) {
        seq
            .flatMap(toRowsWithComments)
            .withIndex()
            .forEach {
                if (it.index == 0) {
                    ps.printf("INSERT INTO %s (%s) VALUES", table, columns)
                } else {
                    ps.print(',')
                }
                if (withNumber) {
                    ps.printf("%n(%d,%s) /* %s */", it.index + 1, it.value.first, it.value.second)
                } else {
                    ps.printf("%n(%s) /* %s */", it.value.first, it.value.second)
                }
            }
        ps.println(";")
    }

    // from map

    /**
     * Print inserts, single value
     *
     * @param ps      print stream
     * @param table   table name
     * @param columns column names
     * @param format  value format
     * @param mapper  objects by id
     * @param T       type of objects
     */
    fun <T : Comparable<T>> printInsert(
        ps: PrintStream,
        table: String,
        columns: String,
        format: String,
        mapper: Map<String, T>,
    ) {
        ps.printf("INSERT INTO %s (%s) VALUES", table, columns)
        mapper.entries
            .asSequence()
            .sortedBy { it.value }
            .withIndex()
            .forEach { (index, pair) ->
                if (index > 0) {
                    ps.print(',')
                }
                val k = pair.key
                val v = pair.value
                ps.printf(format, v, Utils.escape(k))
            }
        ps.println(";")
    }

    /**
     * Print inserts, 2 values
     *
     * @param ps      print stream
     * @param table   table name
     * @param columns column names
     * @param format  value format
     * @param mapper  object arrays by id
     * @param T       type of objects
     */
    fun <T : Comparable<T>> printInsert2(
        ps: PrintStream,
        table: String,
        columns: String,
        format: String,
        mapper: Map<Array<Any>, T>,
    ) {
        ps.printf("INSERT INTO %s (%s) VALUES", table, columns)
        mapper.entries
            .toList()
            .sortedBy { it.value }
            .withIndex()
            .forEach { (index, pair) ->
                if (index > 0) {
                    ps.print(',')
                }
                val k = pair.key
                val v = pair.value
                ps.printf(format, v, k[0], k[1])
            }
        ps.println(";")
    }

    /**
     * Print inserts, 3 values
     *
     * @param ps      print stream
     * @param table   table name
     * @param columns column names
     * @param format  value format
     * @param mapper  object arrays by id
     * @param T       type of objects
     */
    fun <T : Comparable<T>> printInsert3(
        ps: PrintStream,
        table: String,
        columns: String,
        format: String,
        mapper: Map<Array<String>, T>,
    ) {
        ps.printf("INSERT INTO %s (%s) VALUES", table, columns)
        mapper.entries
            .toList()
            .sortedBy { it.value }
            .withIndex()
            .forEach { (index, pair) ->
                if (index > 0) {
                    ps.print(',')
                }
                val k = pair.key
                val v = pair.value
                ps.printf(format, v, k[0], k[1], k[2])
            }
        ps.println(";")
    }
}
