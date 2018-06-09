#! /bin/sh 

SCRIPT_PATH=`dirname $0`
cd $SCRIPT_PATH

SERVICE_ROOT_PATH=${PWD%/*}
SERVICE_CLASSPATH=$SERVICE_ROOT_PATH:$SERVICE_ROOT_PATH/config

for f in $SERVICE_ROOT_PATH/lib/*.jar; do
  SERVICE_CLASSPATH=$SERVICE_CLASSPATH:$f;
done

export SERVICE_CLASSPATH

JAVA_OPTS='-Xms512m -Xmx512m'
nohup java -server $JAVA_OPTS $JAVA_AGENT -classpath $SERVICE_CLASSPATH lark.server.LarkServer >/dev/null 2>&1 &