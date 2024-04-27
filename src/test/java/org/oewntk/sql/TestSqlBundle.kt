/*
 * Copyright (c) 2021. Bernard Bou.
 */
package org.oewntk.sql

import org.junit.Assert
import org.junit.BeforeClass
import org.junit.Test
import org.oewntk.model.Tracing
import org.oewntk.sql.out.Names
import org.oewntk.sql.out.Variables
import java.util.*

class TestSqlBundle {

	@Test
	fun testBundleValues() {
		for (key in TreeSet(bundle!!.keySet())) {
			val value = bundle!!.getString(key)
			val valueCompat = bundleCompat!!.getString(key)
			if (value != valueCompat) {
				ps.printf("[%s] %s <> %s%n", key, value, valueCompat)
			}
		}
	}

	@Test
	fun testClasses() {
		Assert.assertEquals("words", Names.WORDS.FILE)
		Assert.assertEquals("senses", Names.SENSES.FILE)
		Assert.assertEquals("synsets", Names.SYNSETS.FILE)
	}

	@Test
	fun testCompat() {
		val v = Variables(bundle!!)
		val vc = Variables(bundleCompat!!)

		Assert.assertEquals("vframesentences", vc.varSubstitution("\${vtemplates.table}", false))
		Assert.assertEquals("vtemplates", v.varSubstitution("\${vtemplates.table}", false))
		Assert.assertEquals("`vframesentences`", vc.varSubstitution("\${vtemplates.table}", true))
		Assert.assertEquals("`vtemplates`", v.varSubstitution("\${vtemplates.table}", true))
	}

	companion object {
		private val ps = if (!System.getProperties().containsKey("SILENT")) Tracing.psInfo else Tracing.psNull

		private var bundle: ResourceBundle? = null

		private var bundleCompat: ResourceBundle? = null

		@JvmStatic
		@BeforeClass
		fun init() {
			bundle = ResourceBundle.getBundle("wn/Names")
			bundleCompat = ResourceBundle.getBundle("wn/NamesCompat")
		}
	}
}
