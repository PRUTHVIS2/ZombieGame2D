@echo off
REM Zombie Defense Game - Build & Run Script for Windows

setlocal enabledelayedexpansion
set PROJECT_DIR=%~dp0
set SRC_DIR=%PROJECT_DIR%src
set BIN_DIR=%PROJECT_DIR%bin

echo ======================================
echo   Zombie Defense Game - Build Script
echo ======================================
echo.

REM Create bin directory if it doesn't exist
if not exist "%BIN_DIR%" (
    echo [*] Creating bin directory...
    mkdir "%BIN_DIR%"
)

REM Compile
echo [*] Compiling source files...
cd /d "%PROJECT_DIR%"
javac -d "%BIN_DIR%" "%SRC_DIR%\*.java"

if %ERRORLEVEL% EQU 0 (
    echo [✓] Compilation successful!
    echo.
    echo [*] Starting game...
    echo ======================================
    echo.
    
    REM Run
    java -cp "%BIN_DIR%" App
) else (
    echo [✗] Compilation failed!
    exit /b 1
)

endlocal
