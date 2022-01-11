#!/bin/bash

db=oewn
sections="wn-basic wn-dict wn-cased wn-relations wn-misc wn-morph wn-pronunciation wn-legacy wn-vframe wn-cardinality"

# P A R A M S

oneoutput=
if [ "-one" == "$1" ]; then
	oneoutput=true
	shift
fi
createviews=true
if [ "-noviews" == "$1" ]; then
	donotcreateviews=
	shift
fi

source define_colors.sh

# C R E D E N T I A L S

source define_user.sh
echo "credentials ${creds}"

# D I R S

thisdir=$(dirname $(readlink -m $0))
parentdir=$(readlink -m "${thisdir}/..")

# in
sqldir=${parentdir}

# out
xmldir=${parentdir}/xml
mkdir -p ${xmldir}
htmldir=${parentdir}/html
mkdir -p ${htmldir}

# V I E W S

if [ ! -z "${createviews}" ]; then
	echo -e "${M}create views${Z}"
	mysql ${creds} ${db} < ${sqldir}/mysql-views.sql
	echo "created"
fi

# D O C S

if [ ! -z "${oneoutput}" ]; then
	echo -e "${C}merged${Z}"
	for s in ${sections}; do
		f="${sqldir}/mysql-${s}-query.sql"
		if [ ! -e ${f} ]; then
			echo -e "${R}does not exist${Z} ${f}"
			continue
		fi
		cat $f
	done \
	| mysql ${creds} -X ${db} \
	| tee ${xmldir}/sql-all.xml \
	| ./filter-xml2html.sh - > ${htmldir}/sql-all.html
else
	for s in ${sections}; do
		echo -e "${C}section=${s}${Z}"
		f="${sqldir}/mysql-${s}-query.sql"
		echo -e "sql=${M}${f}${Z}"
		if [ ! -e ${f} ]; then
			echo -e "${R}does not exist${Z} ${f}"
			continue
		fi
		cat $f \
		| mysql ${creds} -X ${db} \
		| tee ${xmldir}/sql-${s}.xml \
		| ./filter-xml2html.sh - > ${htmldir}/sql-${s}.html
	done \
fi

rm -Rf ${xmldir}

