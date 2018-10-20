# Retel_Automation


Pre - Requisites :-
JDK 1.7 +
Nodejs 8.12
Appium 1.7.1
Android SDK 
Apache Maven 3.2.5

Add Environment variables:-
JAVA_HOME
ANDROID_HOME

How to execute

1. Make mobile device specific changes in config.txt (src/main/resources)
2. Change NODEJSPATH, APPIUMJSPATH in config.txt for local machines
3. Add device udid under "#Device Farm"


1. Import Project in IDE(Eclipse / Intellij)
2. Add Maven run configurations
3. Add "exec:java -DTESTBEDNAME=Nokia" to goals, replace Nokia with devicename from "#Device Farm"
4. Run test

Or

1. Go to terminal / cmd
2. Navigate to project directory
3. execute command - mvn exec:java -DTESTBEDNAME=Nokia replace Nokia with devicename from "#Device Farm"
