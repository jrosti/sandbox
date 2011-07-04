#!/bin/sh

# Usage: clusterWebapp.sh <your-ear-war-file>
# e.g  : clusterWebapp.sh myapp.ear
# e.g  : clusterWebapp.sh mywebapp.war

java -cp ../lib/hazelcast-1.9.3.1.jar:../lib/hazelcast-wm-1.9.3.1.jar com.hazelcast.web.Installer -version1.9.3.1 $*