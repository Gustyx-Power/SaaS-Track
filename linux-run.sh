#!/bin/bash
cd "$(dirname "$0")/src"

# JVM flags untuk BIRT compatibility dengan Java 17+
JAVA_OPTS="--add-opens java.base/java.lang=ALL-UNNAMED"
JAVA_OPTS="$JAVA_OPTS --add-opens java.base/java.util=ALL-UNNAMED"
JAVA_OPTS="$JAVA_OPTS --add-opens java.base/java.lang.reflect=ALL-UNNAMED"
JAVA_OPTS="$JAVA_OPTS --add-opens java.base/java.text=ALL-UNNAMED"
JAVA_OPTS="$JAVA_OPTS --add-opens java.desktop/java.awt.font=ALL-UNNAMED"

javac -encoding UTF-8 -cp "../lib/*:../lib/birt/*:." model/*.java util/*.java dao/*.java view/*.java Main.java 2>&1
if [ $? -eq 0 ]; then
    java $JAVA_OPTS -cp "../lib/*:../lib/birt/*:." Main
else
    echo "Compile failed!"
fi
