/*
 * Copyright (c) 2021-2024. Bernard Bou.
 */
package org.oewntk.sql

import org.junit.Assert
import org.junit.BeforeClass
import org.junit.Test
import org.oewntk.model.Tracing
import org.oewntk.sql.out.Variables
import java.util.*

class TestSqlVariables {

    private val wellFormedInputs = arrayOf(
        "\${senses.file} \${senses_vtemplates.templateid} a \${senses_vframes.file} b \${lexes_pronunciations.wordid} c @{senses.file} d @{senses.file}",
    )

    private val illFormedInputs = arrayOf(
        // "${senses.file} a $ { }", //
        "\${senses.file} b \${var4} ",  //
        "\${senses.file} c \${senses._file_} ",  //
    )

    @Test
    fun wellFormedVarSubstitution() {
        for (input in wellFormedInputs) {
            Assert.assertNotNull(input)
            try {
                val output = variables!!.varSubstitution(input, false)
                ps.println(output)
            } catch (e: IllegalArgumentException) {
                Assert.fail("Not expected to fail $input")
            }
        }
    }

    @Test
    fun illFormedVarSubstitution() {
        for (input in illFormedInputs) {
            Assert.assertNotNull(input)
            try {
                val output = variables!!.varSubstitution(input, false)
                Assert.fail("Not expected to succeed $input yields $output")
            } catch (e: IllegalArgumentException) {
                // Tracing.psErr.println(e)
            }
        }
    }

    companion object {

        private val ps = if (!System.getProperties().containsKey("SILENT")) Tracing.psInfo else Tracing.psNull

        var variables: Variables? = null

        @JvmStatic
        @BeforeClass
        fun init() {
            val bundle = ResourceBundle.getBundle("wn/Names")
            variables = Variables(bundle)
        }
    }
}
