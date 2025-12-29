#!/bin/bash
cd "$(dirname "$0")/src"
javac -encoding UTF-8 -cp "../lib/*:." model/*.java util/*.java dao/*.java view/*.java Main.java 2>&1
if [ $? -eq 0 ]; then
    java -cp "../lib/*:." Main
else
    echo "Compile failed!"
fi
