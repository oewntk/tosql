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

# M A I N

source define_colors.sh

# make pdfs from html

for f in ${htmldir}/*.html; do
	#echo ${f}
	b=`basename ${f%.*}`
	echo -e "${M}${b}.pdf${Z}"
	wkhtmltopdf --page-size A4 --orientation Landscape ${f} ${pdfdir}/${b}.pdf
done

pushd ${pdfdir} > /dev/null

# rotate pdfs from html

echo -e "${M}rotate pdfs${Z}"
rm *rotated.pdf

wnfiles=`ls sql-wn*.pdf`
allpdfs="${wnfiles}"

allpdfs2=
for f in ${allpdfs}; do 
	echo "rotate ${f}"
	b=`basename ${f%.*}`
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

popd  > /dev/null

