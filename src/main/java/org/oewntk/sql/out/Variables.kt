/*
 * Copyright (c) 2024. Bernard Bou.
 */
package org.oewntk.sql.out

import java.io.*
import java.nio.charset.Charset
import java.util.*
import java.util.regex.MatchResult
import java.util.regex.Pattern

/**
 * Variable substitution
 *
 * @param bundle resource bundle
 */
class Variables(bundle: ResourceBundle) {

    /**
     * Variable-value map
     */
    private val toValue: MutableMap<String, String> = HashMap()

    /**
     * Set values in map from resource bundle
     */
    init {
        for (k in bundle.keySet()) {
            toValue[k] = bundle.getString(k)
        }
    }

    /**
     * Add key-value pair
     *
     * @param key   key
     * @param value value
     * @return old value if present, null otherwise
     */
    fun put(key: String, value: String): String? {
        return toValue.put(key, value)
    }

    /**
     * Substitute values to variables in file
     *
     * @param file         input file
     * @param ps           print stream
     * @param useBackticks surround with back-ticks
     * @param compress     whether to compress spaces to single space
     * @throws IOException io exception
     */
    @Throws(IOException::class)
    fun varSubstitutionInFile(file: File, ps: PrintStream, useBackticks: Boolean, compress: Boolean) {

        // iterate on lines
        try {
            FileInputStream(file).use { inStream ->
                varSubstitutionInIS(inStream, ps, useBackticks, compress)
            }
        } catch (iae: IllegalArgumentException) {
            System.err.printf("At %s%n%s%n", file, iae.message)
            throw iae
        }
    }

    /**
     * Substitute values to variables in input stream
     *
     * @param inStream     input stream
     * @param ps           print stream
     * @param useBackticks surround with back-ticks
     * @param compress     whether to compress spaces to single space
     * @throws IOException io exception
     */
    @Throws(IOException::class)
    fun varSubstitutionInIS(inStream: InputStream, ps: PrintStream, useBackticks: Boolean, compress: Boolean) {

        // iterate on lines
        BufferedReader(InputStreamReader(inStream, Charset.defaultCharset())).use { reader ->
            var lineNum = 0
            while (true) {
                var line: String = reader.readLine() ?: break
                lineNum++
                try {
                    //initVars(line);
                    line = varSubstitution(line, useBackticks)
                } catch (iae: IllegalArgumentException) {
                    System.err.printf("At line %d content: [%s]%n", lineNum, line)
                    throw iae
                }
                if (compress) {
                    line = line.replace("\\s+".toRegex(), " ")
                }
                ps.println(line)
            }
        }
    }

    /**
     * Substitute values to variables in string
     *
     * @param input        input string
     * @param useBackticks surround with back-ticks
     * @return string with values substituted fir variable name
     */
    fun varSubstitution(input: String, useBackticks: Boolean): String {
        return varSubstitution(varSubstitution(input, AT_PATTERN, false), DOLLAR_PATTERN, useBackticks)
    }

    /**
     * Substitute values to variables in string
     *
     * @param input        input string
     * @param p            pattern for variable
     * @param useBackticks whether to surround substitution result with back ticks
     * @return string with values substituted for variable name
     */
    private fun varSubstitution(input: String, p: Pattern, useBackticks: Boolean): String {

        val m = p.matcher(input)
        if (m.find()) {
            val output = m.replaceAll { r: MatchResult ->
                val varName = r.group(1)
                require(toValue.containsKey(varName)) { varName }
                val `val` = toValue[varName]
                if (useBackticks) "`$`val``" else `val`
            }
            require(
                !output.contains(
                    p.pattern().substring(0, 1)
                )
            ) { p.pattern()[0].toString() + ",{,} used in '" + input + "'" }
            return output
        }
        return input
    }

    companion object {

        private val DOLLAR_PATTERN: Pattern = Pattern.compile("\\$\\{([a-zA-Z0-9_.]+)}")

        private val AT_PATTERN: Pattern = Pattern.compile("@\\{([a-zA-Z0-9_.]+)}")

        /**
         * Scan input and produces list on stderr with same value
         *
         * @param input input
         */
        fun dumpVars(input: String) {
            val p = Pattern.compile("\\$\\{([a-zA-Z0-9_.]+)}")
            val m = p.matcher(input)
            if (m.find()) {
                val varName = m.group(1)
                System.err.printf("%s=%s%n", varName, varName)
            }
        }
    }
}