/*
 * Copyright (c) $originalComment.match("Copyright \(c\) (\d+)", 1, "-")2021. Bernard Bou.
 */

package org.oewntk.sql.out;

import java.io.*;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Variable substitution
 */
public class Variables
{
	/**
	 * Variable-value map
	 */
	public static final Map<String, String> toValue = new HashMap<>();

	/**
	 * Set values in map
	 */
	public static void set()
	{
		toValue.put("words.file", Names.WORDS.FILE);
		toValue.put("words.table", Names.WORDS.TABLE);
		toValue.put("words.wordid", Names.WORDS.wordid);
		toValue.put("words.word", Names.WORDS.word);

		toValue.put("casedwords.file", Names.CASEDWORDS.FILE);
		toValue.put("casedwords.table", Names.CASEDWORDS.TABLE);
		toValue.put("casedwords.casedwordid", Names.CASEDWORDS.casedwordid);
		toValue.put("casedwords.casedword", Names.CASEDWORDS.casedword);
		toValue.put("casedwords.wordid", Names.CASEDWORDS.wordid);

		toValue.put("pronunciations.file", Names.PRONUNCIATIONS.FILE);
		toValue.put("pronunciations.table", Names.PRONUNCIATIONS.TABLE);
		toValue.put("pronunciations.pronunciationid", Names.PRONUNCIATIONS.pronunciationid);
		toValue.put("pronunciations.pronunciation", Names.PRONUNCIATIONS.pronunciation);

		toValue.put("morphs.file", Names.MORPHS.FILE);
		toValue.put("morphs.table", Names.MORPHS.TABLE);
		toValue.put("morphs.morphid", Names.MORPHS.morphid);
		toValue.put("morphs.morph", Names.MORPHS.morph);

		toValue.put("synsets.file", Names.SYNSETS.FILE);
		toValue.put("synsets.table", Names.SYNSETS.TABLE);
		toValue.put("synsets.synsetid", Names.SYNSETS.synsetid);
		toValue.put("synsets.posid", Names.SYNSETS.posid);
		toValue.put("synsets.domainid", Names.SYNSETS.domainid);
		toValue.put("synsets.definition", Names.SYNSETS.definition);

		toValue.put("poses.file", Names.POSES.FILE);
		toValue.put("poses.table", Names.POSES.TABLE);
		toValue.put("poses.posid", Names.POSES.posid);
		toValue.put("poses.pos", Names.POSES.pos);

		toValue.put("relations.file", Names.RELS.FILE);
		toValue.put("relations.table", Names.RELS.TABLE);
		toValue.put("relations.relationid", Names.RELS.relationid);
		toValue.put("relations.relation", Names.RELS.relation);
		toValue.put("relations.recurses", Names.RELS.recurses);

		toValue.put("samples.file", Names.SAMPLES.FILE);
		toValue.put("samples.table", Names.SAMPLES.TABLE);
		toValue.put("samples.sampleid", Names.SAMPLES.sampleid);
		toValue.put("samples.sample", Names.SAMPLES.sample);
		toValue.put("samples.synsetid", Names.SAMPLES.synsetid);

		toValue.put("domains.file", Names.DOMAINS.FILE);
		toValue.put("domains.table", Names.DOMAINS.TABLE);
		toValue.put("domains.domainid", Names.DOMAINS.domainid);
		toValue.put("domains.domain", Names.DOMAINS.domain);
		toValue.put("domains.domainname", Names.DOMAINS.domainname);
		toValue.put("domains.posid", Names.DOMAINS.posid);

		toValue.put("vframes.file", Names.VFRAMES.FILE);
		toValue.put("vframes.table", Names.VFRAMES.TABLE);
		toValue.put("vframes.frameid", Names.VFRAMES.frameid);
		toValue.put("vframes.frame", Names.VFRAMES.frame);

		toValue.put("vtemplates.file", Names.VTEMPLATES.FILE);
		toValue.put("vtemplates.table", Names.VTEMPLATES.TABLE);
		toValue.put("vtemplates.templateid", Names.VTEMPLATES.templateid);
		toValue.put("vtemplates.template", Names.VTEMPLATES.template);

		toValue.put("adjpositions.file", Names.ADJPOSITIONS.FILE);
		toValue.put("adjpositions.table", Names.ADJPOSITIONS.TABLE);
		toValue.put("adjpositions.positionid", Names.ADJPOSITIONS.positionid);
		toValue.put("adjpositions.position", Names.ADJPOSITIONS.position);

		toValue.put("lexes.file", Names.LEXES.FILE);
		toValue.put("lexes.table", Names.LEXES.TABLE);
		toValue.put("lexes.luid", Names.LEXES.luid);
		toValue.put("lexes.posid", Names.LEXES.posid);
		toValue.put("lexes.wordid", Names.LEXES.wordid);
		toValue.put("lexes.casedwordid", Names.LEXES.casedwordid);

		toValue.put("lexes_morphs.file", Names.LEXES_MORPHS.FILE);
		toValue.put("lexes_morphs.table", Names.LEXES_MORPHS.TABLE);
		toValue.put("lexes_morphs.luid", Names.LEXES_MORPHS.luid);
		toValue.put("lexes_morphs.wordid", Names.LEXES_MORPHS.wordid);
		toValue.put("lexes_morphs.posid", Names.LEXES_MORPHS.posid);
		toValue.put("lexes_morphs.morphid", Names.LEXES_MORPHS.morphid);

		toValue.put("lexes_pronunciations.file", Names.LEXES_PRONUNCIATIONS.FILE);
		toValue.put("lexes_pronunciations.table", Names.LEXES_PRONUNCIATIONS.TABLE);
		toValue.put("lexes_pronunciations.luid", Names.LEXES_PRONUNCIATIONS.luid);
		toValue.put("lexes_pronunciations.wordid", Names.LEXES_PRONUNCIATIONS.wordid);
		toValue.put("lexes_pronunciations.posid", Names.LEXES_PRONUNCIATIONS.posid);
		toValue.put("lexes_pronunciations.pronunciationid", Names.LEXES_PRONUNCIATIONS.pronunciationid);
		toValue.put("lexes_pronunciations.variety", Names.LEXES_PRONUNCIATIONS.variety);

		toValue.put("senses.file", Names.SENSES.FILE);
		toValue.put("senses.table", Names.SENSES.TABLE);
		toValue.put("senses.senseid", Names.SENSES.senseid);
		toValue.put("senses.sensekey", Names.SENSES.sensekey);
		toValue.put("senses.luid", Names.SENSES.luid);
		toValue.put("senses.wordid", Names.SENSES.wordid);
		toValue.put("senses.casedwordid", Names.SENSES.casedwordid);
		toValue.put("senses.synsetid", Names.SENSES.synsetid);
		toValue.put("senses.sensenum", Names.SENSES.sensenum);
		toValue.put("senses.lexid", Names.SENSES.lexid);
		toValue.put("senses.tagcount", Names.SENSES.tagcount);

		toValue.put("senses_senses.file", Names.SENSES_SENSES.FILE);
		toValue.put("senses_senses.table", Names.SENSES_SENSES.TABLE);
		toValue.put("senses_senses.synset1id", Names.SENSES_SENSES.synset1id);
		toValue.put("senses_senses.lu1id", Names.SENSES_SENSES.lu1id);
		toValue.put("senses_senses.word1id", Names.SENSES_SENSES.word1id);
		toValue.put("senses_senses.synset2id", Names.SENSES_SENSES.synset2id);
		toValue.put("senses_senses.lu2id", Names.SENSES_SENSES.lu2id);
		toValue.put("senses_senses.word2id", Names.SENSES_SENSES.word2id);
		toValue.put("senses_senses.relationid", Names.SENSES_SENSES.relationid);

		toValue.put("synsets_synsets.file", Names.SYNSETS_SYNSETS.FILE);
		toValue.put("synsets_synsets.table", Names.SYNSETS_SYNSETS.TABLE);
		toValue.put("synsets_synsets.synset1id", Names.SYNSETS_SYNSETS.synset1id);
		toValue.put("synsets_synsets.synset2id", Names.SYNSETS_SYNSETS.synset2id);
		toValue.put("synsets_synsets.relationid", Names.SYNSETS_SYNSETS.relationid);

		toValue.put("senses_adjpositions.file", Names.SENSES_ADJPOSITIONS.FILE);
		toValue.put("senses_adjpositions.table", Names.SENSES_ADJPOSITIONS.TABLE);
		toValue.put("senses_adjpositions.synsetid", Names.SENSES_ADJPOSITIONS.synsetid);
		toValue.put("senses_adjpositions.luid", Names.SENSES_ADJPOSITIONS.luid);
		toValue.put("senses_adjpositions.wordid", Names.SENSES_ADJPOSITIONS.wordid);
		toValue.put("senses_adjpositions.positionid", Names.SENSES_ADJPOSITIONS.positionid);

		toValue.put("senses_vframes.file", Names.SENSES_VFRAMES.FILE);
		toValue.put("senses_vframes.table", Names.SENSES_VFRAMES.TABLE);
		toValue.put("senses_vframes.synsetid", Names.SENSES_VFRAMES.synsetid);
		toValue.put("senses_vframes.luid", Names.SENSES_VFRAMES.luid);
		toValue.put("senses_vframes.wordid", Names.SENSES_VFRAMES.wordid);
		toValue.put("senses_vframes.frameid", Names.SENSES_VFRAMES.frameid);

		toValue.put("senses_vtemplates.file", Names.SENSES_VTEMPLATES.FILE);
		toValue.put("senses_vtemplates.table", Names.SENSES_VTEMPLATES.TABLE);
		toValue.put("senses_vtemplates.luid", Names.SENSES_VTEMPLATES.luid);
		toValue.put("senses_vtemplates.wordid", Names.SENSES_VTEMPLATES.wordid);
		toValue.put("senses_vtemplates.synsetid", Names.SENSES_VTEMPLATES.synsetid);
		toValue.put("senses_vtemplates.templateid", Names.SENSES_VTEMPLATES.templateid);
	}

	static
	{
		set();
	}

	/**
	 * Set values from resource bundle
	 *
	 * @param bundle resource bundle
	 */
	public static void set(final ResourceBundle bundle)
	{
		Names.set(bundle);
		set();
	}

	/**
	 * Substitute values to variables in file
	 *
	 * @param file     input file
	 * @param ps       print stream
	 * @param compress whether to compress spaces to single space
	 * @throws IOException io exception
	 */
	public static void varSubstitutionInFile(final File file, final PrintStream ps, final boolean compress) throws IOException
	{
		// iterate on lines
		try (InputStream is = new FileInputStream(file))
		{
			varSubstitutionInIS(is, ps, compress);
		}
		catch (IllegalArgumentException iae)
		{
			Tracing.psErr.printf("%s %s", file, iae.getMessage());
			throw iae;
		}
	}

	/**
	 * Substitute values to variables in input stream
	 *
	 * @param is       input stream
	 * @param ps       print stream
	 * @param compress whether to compress spaces to single space
	 * @throws IOException io exception
	 */
	public static void varSubstitutionInIS(final InputStream is, final PrintStream ps, final boolean compress) throws IOException
	{
		// iterate on lines
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, Charset.defaultCharset())))
		{
			int lineNum = 0;
			String line;
			while ((line = reader.readLine()) != null)
			{
				lineNum++;
				try
				{
					line = varSubstitution(line);
				}
				catch (IllegalArgumentException iae)
				{
					Tracing.psErr.printf("%d '%s'", lineNum, line);
					throw iae;
				}
				if (compress)
				{
					line = line.replaceAll("\\s+", " ");
				}
				ps.println(line);
			}
		}
	}

	/**
	 * Substitute values to variables in string
	 *
	 * @param input input string
	 * @return string with values substituted fir variable name
	 */
	public static String varSubstitution(String input)
	{
		Pattern p = Pattern.compile("\\$\\{([a-zA-Z0-9_.]+)}");
		Matcher m = p.matcher(input);
		if (m.find())
		{
			var output = m.replaceAll(r -> {
				String varName = r.group(1);
				if (!toValue.containsKey(varName))
				{
					throw new IllegalArgumentException(varName);
				}
				return /* "@" + */ toValue.get(varName) /* + "@" */;
			});
			if (output.contains("$") || output.contains("{") || output.contains("}"))
			{
				throw new IllegalArgumentException("$,{,} used in '" + input + "'");
			}
			return output;
		}
		return input;
	}
}
