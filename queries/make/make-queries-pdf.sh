#!/bin/bash

# D I R S

thisdir=$(dirname $(readlink -m $0))
parentdir=$(readlink -m "${thisdir}/..")

# in
htmldir=${parentdir}/html

# out
pdfdir=${parentdir}/pdf
mkdir -p ${pdfdir}

docdir=${parentdir}/doc
mkdir -p ${docdir}

# C O L O R S

export R='\u001b[31m'
export G='\u001b[32m'
export B='\u001b[34m'
export Y='\u001b[33m'
export M='\u001b[35m'
export C='\u001b[36m'
export Z='\u001b[0m'

# M A I N

# make pdfs from html

for f in ${htmldir}/*.html; do
  #echo ${f}
  b=$(basename ${f%.*})
  echo -e "${M}${b}.pdf${Z}"
  wkhtmltopdf --enable-local-file-access --page-size A4 --orientation Landscape ${f} ${pdfdir}/${b}.pdf
done

pushd ${pdfdir} >/dev/null

# rotate pdfs from html

echo -e "${M}rotate pdfs${Z}"
rm *rotated.pdf

wnfiles=$(ls sql-wn*.pdf)
allpdfs="${wnfiles}"

allpdfs2=
for f in ${allpdfs}; do
  echo "rotate ${f}"
  b=$(basename ${f%.*})
  #pdf90 --suffix 'rotated' --batch ${f} > /dev/null 2> /dev/null
  pdftk ${f} rotate 1-endwest output ${b}-rotated.pdf # > /dev/null 2> /dev/null
  allpdfs2="${allpdfs2} ${b}-rotated.pdf"
done

# bind

echo -e "${M}assemble oewn-queries.pdf${Z}"
echo "pdfs2=${allpdfs2}"

p1=${thisdir}/oewn-frontpage-queries.pdf
param="--pdftitle 'OEWN Queries' --pdfauthor 'Bernard Bou' --paper A4 --twoside --no-landscape --rotateoversize 'false'"
pdfjam ${params} --outfile ${docdir}/oewn-queries.pdf ${p1} ${allpdfs2}

rm ${allpdfs2}

popd >/dev/null
