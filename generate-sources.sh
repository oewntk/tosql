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
shift
if [ "${outdir}" == "" ]; then
  outdir="sql"
fi

jar=target/generator-uber.jar
java -ea -cp "${jar}" org.oewntk.sql.out.SourcesGenerator "${outdir}"
