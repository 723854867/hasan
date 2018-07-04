#!/bin/bash
cd `dirname $0`
BASE_DIR=`pwd`
echo "Starting upload hasan.war"
DIR="hasan_test"
if [ "$1" = "online" ] ; then
	DIR="hasan_online"
fi
scp $BASE_DIR/hasan-web/target/hasan.war root@121.196.193.96:/root/web/$DIR/webapps

