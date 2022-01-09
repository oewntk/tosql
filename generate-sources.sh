#!/bin/bash

#
# Copyright (c) 2021. Bernard Bou.
#
# outdir

source define_colors.sh

outdir="$1"
shift
if [ "${outdir}" == "" ]; then
  outdir="sql"
fi

java -ea -cp generate-schema.jar org.oewntk.sql.out.SourcesGenerator "${outdir}"
