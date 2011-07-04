@ECHO OFF

@REM Usage: clusterWebapp.bat <your-ear-war-file>
@REM e.g  : clusterWebapp.bat myapp.ear
@REM e.g  : clusterWebapp.bat mywebapp.war

java -cp ../lib/hazelcast-1.9.3.1.jar:../lib/hazelcast-wm-1.9.3.1.jar com.hazelcast.web.Installer -version1.9.3.1 %*