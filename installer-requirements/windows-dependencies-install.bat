@echo off
chcp 65001 >nul
title SaaS-Track Dependencies Installer

REM Check if already running as Administrator
net session >nul 2>&1
if %errorlevel% neq 0 (
    echo Requesting Administrator privileges...
    powershell -Command "Start-Process cmd -ArgumentList '/c \"\"%~f0\"\"' -Verb RunAs"
    exit /b
)

echo ==================================
echo  SaaS-Track Dependencies Installer
echo  Windows Edition
echo ==================================
echo.

REM Get the directory where this batch file is located
set "SCRIPT_DIR=%~dp0"
set "SCRIPT_DIR=%SCRIPT_DIR:~0,-1%"

REM Get project root (one level up from installer-requirements)
for %%I in ("%SCRIPT_DIR%") do set "PROJECT_ROOT=%%~dpI"
set "PROJECT_ROOT=%PROJECT_ROOT:~0,-1%"

REM Check if sql folder exists
if not exist "%PROJECT_ROOT%\sql\db_saas_track.sql" (
    echo [ERROR] SQL file not found!
    echo Expected: %PROJECT_ROOT%\sql\db_saas_track.sql
    echo.
    echo Please make sure you extracted/cloned the project correctly.
    pause
    exit /b 1
)

echo [OK] Project detected: %PROJECT_ROOT%
echo [OK] SQL file found: %PROJECT_ROOT%\sql\db_saas_track.sql
echo.
echo Starting installation...
echo.

REM Run the PowerShell script with Bypass execution policy
powershell -ExecutionPolicy Bypass -File "%SCRIPT_DIR%\init.ps1"

echo.
echo Installation process completed.
pause
