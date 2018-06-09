#!/bin/bash

BASEDIR=`dirname $0`/..
BASEDIR=`(cd "$BASEDIR"; pwd)`

FEATURE="$BASEDIR/conf"

echo "ps axfww | grep $FEATURE | grep -v 'grep'"
ps auxfww | grep $FEATURE | grep -v 'grep'

COUNT=`ps axfww | grep $FEATURE | grep -v 'grep' | wc -l`
if [ $COUNT -ge 1 ]; then
	ps axfww | grep $FEATURE | grep -v 'grep' | awk '{print $1}' | xargs kill -9

	sleep 1

	COUNT=`ps axfww | grep $FEATURE | grep -v 'grep' | wc -l`
	if [ $COUNT -ge 1 ]; then
		ps axfww | grep $FEATURE | grep -v 'grep' | awk '{print $1}' | xargs kill -9
	fi
fi
echo "Killed"