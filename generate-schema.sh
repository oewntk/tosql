#!/bin/bash

#
# Copyright (c) 2021. Bernard Bou.
#
# -compat
# outdir
# indir
# files*: external sql template file names, if none they are taken from resources

R='\u001b[31m'
G='\u001b[32m'
B='\u001b[34m'
Y='\u001b[33m'
M='\u001b[35m'
C='\u001b[36m'
Z='\u001b[0m'

if [ "$1" == "-compat" ]; then
  compatswitch="-compat"
  shift
  echo -e "${Y}compat mode${Z}"
else
  compatswitch=
fi

outdir="$1"
shift
if [ "${outdir}" == "" ]; then
  outdir="sql"
fi

m=wn

if [ "$*" != "" ]; then
  indir="$1"
  shift
  for sql in $*; do
    base=$(basename ${sql})
    java -ea -cp generate-schema.jar org.oewntk.sql.out.SchemaGenerator ${compatswitch} ${m} "${outdir}" "${indir}" "${sql}"
  done
else
  echo -e "${C}$(readlink -f ${outdir})${Z}"
  for db in mysql sqlite; do
    for type in create index reference; do
      echo -e "${M}${db}/${type}${Z}"
      java -ea -cp generate-schema.jar org.oewntk.sql.out.SchemaGenerator ${compatswitch} ${m} "${outdir}/${db}/${type}" "${db}/${type}" $*
    done
  done
fi
