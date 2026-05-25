#!/bin/bash

#
# Copyright (c) 2021-2024. Bernard Bou.
#

set -e

export R='\u001b[31m'
export G='\u001b[32m'
export B='\u001b[34m'
export Y='\u001b[33m'
export M='\u001b[35m'
export C='\u001b[36m'
export Z='\u001b[0m'

outdir="$1"
[ "$#" -eq 0 ] || shift
if [ "${outdir}" == "" ]; then
  outdir="sql"
fi

jar=generator-uber.jar
if [ ! -e "${jar}" ]; then
  if [ ! -e "target/${jar}" ]; then
    echo "Non existing uber jar" >&2
    exit 1
    fi
  ln -s "target/${jar}"
  fi
if [ ! -e "${jar}" ]; then
  echo "Non existing uber jar" >&2
  exit 2
  fi
java -ea -cp "${jar}" org.oewntk.sql.out.SourcesGenerator "${outdir}"
