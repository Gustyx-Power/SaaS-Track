@echo off
REM Run SaaS-Track Application on Windows
REM Make sure Java is installed and in PATH

cd /d "%~dp0"
java -cp "dist/SaaS-Track.jar;lib/*" Main
pause
