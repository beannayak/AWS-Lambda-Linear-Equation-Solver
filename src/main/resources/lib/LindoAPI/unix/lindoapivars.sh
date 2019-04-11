#!/bin/sh
#
# Modify LINDOAPI_HOME variable to point your installation path
#

if [ "$1" == "" ]; then
	LINDOAPI_HOME=$HOME/usr/lindoapi
else
	LINDOAPI_HOME=${1}
fi	
export LINDOAPI_HOME

#
# No changes required below this line
#

source "$LINDOAPI_HOME/include/lsversion.sh"

LD_LIBRARY_PATH=$LD_LIBRARY_PATH:$LINDOAPI_HOME/bin/linux64
export LD_LIBRARY_PATH

LINDOAPI_LICENSE_FILE="$LINDOAPI_HOME/license/lndapi$LS_MAJOR$LS_MINOR.lic"
export LINDOAPI_LICENSE_FILE