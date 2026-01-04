#!/bin/bash
# macOS Run Script for SaaS-Track
cd "$(dirname "$0")/src"

# JVM flags for BIRT compatibility with Java 17+
JAVA_OPTS="--add-opens java.base/java.lang=ALL-UNNAMED"
JAVA_OPTS="$JAVA_OPTS --add-opens java.base/java.util=ALL-UNNAMED"
JAVA_OPTS="$JAVA_OPTS --add-opens java.base/java.lang.reflect=ALL-UNNAMED"
JAVA_OPTS="$JAVA_OPTS --add-opens java.base/java.text=ALL-UNNAMED"
JAVA_OPTS="$JAVA_OPTS --add-opens java.desktop/java.awt.font=ALL-UNNAMED"

# Ensure Homebrew Java is in PATH
export PATH="/opt/homebrew/opt/openjdk@17/bin:$PATH"

javac -encoding UTF-8 -cp "../lib/*:../lib/birt/*:." model/*.java util/*.java dao/*.java view/*.java Main.java 2>&1
if [ $? -eq 0 ]; then
    java $JAVA_OPTS -cp "../lib/*:../lib/birt/*:." Main
else
    echo "Compile failed!"
fi
