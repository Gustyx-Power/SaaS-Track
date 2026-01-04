@echo off
REM Run SaaS-Track Application on Windows
REM Make sure Java is installed and in PATH

cd /d "%~dp0\src"

REM JVM flags untuk BIRT compatibility dengan Java 17+
set JAVA_OPTS=--add-opens java.base/java.lang=ALL-UNNAMED
set JAVA_OPTS=%JAVA_OPTS% --add-opens java.base/java.util=ALL-UNNAMED
set JAVA_OPTS=%JAVA_OPTS% --add-opens java.base/java.lang.reflect=ALL-UNNAMED
set JAVA_OPTS=%JAVA_OPTS% --add-opens java.base/java.text=ALL-UNNAMED
set JAVA_OPTS=%JAVA_OPTS% --add-opens java.desktop/java.awt.font=ALL-UNNAMED

javac -encoding UTF-8 -cp "../lib/*;../lib/birt/*;." model/*.java util/*.java dao/*.java view/*.java Main.java
if %errorlevel%==0 (
    java %JAVA_OPTS% -cp "../lib/*;../lib/birt/*;." Main
) else (
    echo Compile failed!
    pause
)
