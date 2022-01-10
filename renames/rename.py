#!/usr/bin/python

from __future__ import print_function
import sys
import re
import string

subs=(
("word","lemma"), # words.word
("casedword","cased"), # casedwords.casedword
("posid","pos"), # synsets.posid
("domainid","lexdomainid"), # synsets.domainid
("poses","postypes"), # poses.table
("posid","pos"), # poses.posid
("pos","posname"), # poses.pos
("relations","linktypes"), # relations.table
("relationid","linkid"), # relations.relationid
("relation","link"), # relations.relation
("domains","lexdomains"), # domains.table
("domainid","lexdomainid"), # domains.domainid
("domain","lexdomain"), # domains.domain
("domainname","lexdomainname"), # domains.domainname
("posid","pos"), # domains.posid
("vtemplates","vframesentences"), # vtemplates.table
("templateid","sentenceid"), # vtemplates.templateid
("template","sentence"), # vtemplates.template
("adjpositions","adjpositiontypes"), # adjpositions.table
("positionid","position"), # adjpositions.positionid
("position","positionname"), # adjpositions.position
("posid","pos"), # lexes.posid
("lexes_morphs","morphmaps"), # lexes_morphs.table
("posid","pos"), # lexes_morphs.posid
("lexes_pronunciations","pronunciationmaps"), # lexes_pronunciations.table
("posid","pos"), # lexes_pronunciations.posid
("synsets_synsets","semlinks"), # synsets_synsets.table
("relationid","linkid"), # synsets_synsets.relationid
("senses_senses","lexlinks"), # senses_senses.table
("relationid","linkid"), # senses_senses.relationid
("senses_adjpositions","adjpositions"), # senses_adjpositions.table
("positionid","position"), # senses_adjpositions.positionid
("senses_vframes","vframemaps"), # senses_vframes.table
("senses_vtemplates","vframesentencemaps"), # senses_vtemplates.table
("templateid","sentenceid") # senses_vtemplates.templateid
)

def outputdata(data):
	print("%s" % (data))

def replace(line):
	for r,s in subs:
		line=re.sub(r'\b' + s + r'\b',r,line)
	return line

lines=sys.__stdin__.readlines()
for i in range(len(lines)):
	line=lines[i].strip()
	line=replace(line)
	outputdata(line)
