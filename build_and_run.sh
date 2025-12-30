#!/bin/bash

# Zombie Defense Game - Build & Run Script

PROJECT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
SRC_DIR="$PROJECT_DIR/src"
BIN_DIR="$PROJECT_DIR/bin"

echo "======================================"
echo "  Zombie Defense Game - Build Script"
echo "======================================"
echo ""

# Create bin directory if it doesn't exist
if [ ! -d "$BIN_DIR" ]; then
    echo "[*] Creating bin directory..."
    mkdir -p "$BIN_DIR"
fi

# Compile
echo "[*] Compiling source files..."
cd "$PROJECT_DIR"
javac -d "$BIN_DIR" "$SRC_DIR"/*.java

if [ $? -eq 0 ]; then
    echo "[✓] Compilation successful!"
    echo ""
    echo "[*] Starting game..."
    echo "======================================"
    echo ""
    
    # Run
    java -cp "$BIN_DIR" App
else
    echo "[✗] Compilation failed!"
    exit 1
fi
