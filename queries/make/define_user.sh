#!/bin/bash
# 22/11/2021

# I N C L U D E

source define_colors.sh

function getcredentialslegacy()
{
  # read user
	read -p "Enter database user: " dbuser
	if [ -z "${dbuser}" ]; then
		echo "Define ${dbtype} user"
		exit 1
	fi

  # read password unless et in en variable
	if [ -z "$MYSQLPASSWORD" ]; then
		read -s -p "Enter ${dbuser}'s password (type '?' if you want to be asked each time, because it's unsafe): " dbpasswd
	else
	  dbpasswd="$MYSQLPASSWORD"
	fi

  # output as commandline switches
  echo -n "-u ${dbuser} "
	if [ ! -z "${dbpasswd}" ]; then
		if [ "${dbpasswd}" == "?" ]; then
			echo "--password"
		else
			echo "--password=${dbpasswd}"
		fi
	fi
}

function getcredentials()
{
  >&2 echo "This requires mysql_config_editor."
  profiles=`mysql_config_editor print --all | grep '\[.*\]'`
  if [ ! -z "${profiles}" ]; then
    >&2 echo "Existing profiles recorded by mysql_config_editor:"
    >&2 echo "${profiles}"
  fi

  # read profile
  read -p "Enter database user profile: " dbprofile
  if [ -z "${dbprofile}" ]; then
    echo "Define ${dbtype} user profile"
    exit 1
  fi

  if ! echo "${profiles}" | grep -q "\[${dbprofile}\]"; then

    # read user
    read -p "Enter database user: " dbuser
    if [ -z "${dbuser}" ]; then
      echo "Define ${dbtype} user"
      exit 1
    fi

    # editor
    >&2 echo "Passing data to mysql_config_editor (password will be obfuscated ~/.mylogin.cnf)"
    mysql_config_editor set --login-path=${dbprofile} --host=localhost --user=${dbuser} --password

  fi

	# output as commandline switches
	echo "--login-path=${dbprofile}"
}

# R U N

#credentials
#export lcreds=`getcredentialslegacy`
#echo "credentials (old style) ${lcreds}"
export creds=`getcredentials`
#echo "credentials ${creds}"

# use:
# mysql ${creds} "${db}" < "${sqlfile}"

