#!/bin/bash

args="$PRODUCT_RUNNER_ARGS"
if [ "" == "$args" ]; then
  args="file:dist-runner.args"
  echo "WARNING: PRODUCT_RUNNER_ARGS was not defined, running with file:dist-runner.args"
fi

echo "running with args: $args"

java_prog=java
if [ "$JAVA_HOME" != "" ]; then
   echo "JAVA_HOME: $JAVA_HOME"
   java_prog=$JAVA_HOME/bin/java
fi

nohup $java_prog -jar pax-runner-1.4.0.jar --args=$args
