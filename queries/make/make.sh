#!/bin/bash

thisdir="`dirname $(readlink -m $0)`/"
thisdir="$(readlink -m ${thisdir})"

source define_colors.sh

pushd ${thisdir} > /dev/null
echo -e "${Y}queries${Z}"
./make-queries.sh
echo -e "${Y}queries (one)${Z}"
./make-queries.sh -one
echo -e "${Y}pdf${Z}"
./make-queries-pdf.sh
echo -e "${Y}stats${Z}"
./make-stats.sh
popd > /dev/null

