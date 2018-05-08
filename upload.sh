#!/bin/bash
cd `dirname $0`
BASE_DIR=`pwd`
echo "Starting upload hasan.war"
scp $BASE_DIR/hasan-web/target/hasan.war root@121.196.193.96:/root/web/hasan/webapps

