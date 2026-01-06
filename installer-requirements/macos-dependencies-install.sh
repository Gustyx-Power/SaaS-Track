#!/bin/bash
# macOS Dependencies Installer for SaaS-Track
# Run with: ./macos-dependencies-install.sh

set -e

# ============================================
# System Information & Prerequisite Checks
# ============================================

# Function to display system information
show_system_info() {
    echo ""
    echo "╔══════════════════════════════════════════════════════════════╗"
    echo "║              SYSTEM INFORMATION                              ║"
    echo "╠══════════════════════════════════════════════════════════════╣"
    
    # macOS Version
    MACOS_VER=$(sw_vers -productVersion)
    MACOS_NAME=$(sw_vers -productName)
    echo "║ 💻 OS:        $MACOS_NAME $MACOS_VER"
    
    # CPU/Processor
    CPU_MODEL=$(sysctl -n machdep.cpu.brand_string 2>/dev/null || echo "Unknown")
    CPU_CORES=$(sysctl -n hw.ncpu 2>/dev/null || echo "?")
    echo "║ 🧠 CPU:       $CPU_MODEL"
    echo "║ 🔢 Cores:     $CPU_CORES cores"
    
    # Architecture
    ARCH=$(uname -m)
    echo "║ 📐 Arch:      $ARCH"
    
    # RAM
    RAM_BYTES=$(sysctl -n hw.memsize 2>/dev/null || echo "0")
    RAM_GB=$((RAM_BYTES / 1024 / 1024 / 1024))
    echo "║ 🧮 RAM:       ${RAM_GB} GB"
    
    # Disk Space
    DISK_FREE=$(df -h / | awk 'NR==2 {print $4}')
    DISK_TOTAL=$(df -h / | awk 'NR==2 {print $2}')
    echo "║ 💾 Disk:      $DISK_FREE free of $DISK_TOTAL"
    
    echo "╠══════════════════════════════════════════════════════════════╣"
    
    # Estimate installation time based on system specs
    estimate_time
    
    echo "╚══════════════════════════════════════════════════════════════╝"
    echo ""
}

# Function to estimate installation time
estimate_time() {
    local cpu_cores=$(sysctl -n hw.ncpu 2>/dev/null || echo "4")
    local ram_gb=$(($(sysctl -n hw.memsize 2>/dev/null || echo "8589934592") / 1024 / 1024 / 1024))
    local macos_major=$(sw_vers -productVersion | cut -d. -f1)
    
    # Base time in minutes
    local base_time=5
    
    # Check if prebuilt binaries available (macOS 13+)
    if [ "$macos_major" -ge 13 ]; then
        echo "║ ⏱️  Est. Time: ~5-10 minutes (prebuilt binaries)"
        echo "║ ℹ️  Note:      Fast install - binaries available"
    else
        # Older macOS needs compilation
        # More cores = faster, more RAM = faster
        if [ "$cpu_cores" -ge 8 ] && [ "$ram_gb" -ge 16 ]; then
            echo "║ ⏱️  Est. Time: ~15-25 minutes (compilation needed)"
        elif [ "$cpu_cores" -ge 4 ] && [ "$ram_gb" -ge 8 ]; then
            echo "║ ⏱️  Est. Time: ~25-40 minutes (compilation needed)"
        else
            echo "║ ⏱️  Est. Time: ~40-60 minutes (compilation needed)"
        fi
        echo "║ ⚠️  Note:      macOS $macos_major requires source compilation"
    fi
}

# Function to check and install Xcode Command Line Tools
check_xcode_cli() {
    echo ""
    echo "[PREREQ] Checking Xcode Command Line Tools..."
    
    if xcode-select -p &>/dev/null; then
        echo "✓ Xcode CLI already installed: $(xcode-select -p)"
    else
        echo "⚠️  Xcode Command Line Tools not found!"
        echo "   This is REQUIRED for compilation and will speed up installation."
        echo ""
        echo "   Installing Xcode CLI (a popup may appear)..."
        
        # Trigger installation
        xcode-select --install 2>/dev/null || true
        
        echo ""
        echo "   ⏳ Please complete the Xcode CLI installation in the popup."
        echo "   Press ENTER after installation completes..."
        read -r
        
        # Verify installation
        if xcode-select -p &>/dev/null; then
            echo "✓ Xcode CLI installed successfully!"
        else
            echo "⚠️  Xcode CLI installation may not have completed."
            echo "   Continuing anyway, but compilation may fail."
        fi
    fi
}

echo "=================================="
echo "SaaS-Track Dependencies Installer"
echo "macOS Edition"
echo "=================================="

# Show system info and time estimate
show_system_info

# Check Xcode CLI first (critical for compilation)
check_xcode_cli

# ============================================
# Helper Functions for Homebrew Lock Handling
# ============================================

# Function to cleanup Homebrew locks and stale processes
cleanup_brew_locks() {
    echo "→ Checking for Homebrew lock issues..."
    
    # Check for running brew processes
    BREW_PIDS=$(pgrep -f "brew" 2>/dev/null || true)
    
    if [ -n "$BREW_PIDS" ]; then
        echo "⚠️  Found running Homebrew processes: $BREW_PIDS"
        echo "   Waiting 10 seconds for them to complete..."
        sleep 10
        
        # Check again
        BREW_PIDS=$(pgrep -f "brew" 2>/dev/null || true)
        if [ -n "$BREW_PIDS" ]; then
            echo "⚠️  Homebrew processes still running."
            read -p "   Terminate them and continue? (y/n): " -n 1 -r
            echo
            if [[ $REPLY =~ ^[Yy]$ ]]; then
                pkill -f "brew" 2>/dev/null || true
                sleep 2
            else
                echo "Please wait for Homebrew processes to finish and run this script again."
                exit 1
            fi
        fi
    fi
    
    # Remove stale .incomplete lock files
    BREW_CACHE="${HOME}/Library/Caches/Homebrew/downloads"
    if [ -d "$BREW_CACHE" ]; then
        INCOMPLETE_FILES=$(find "$BREW_CACHE" -name "*.incomplete" 2>/dev/null || true)
        if [ -n "$INCOMPLETE_FILES" ]; then
            echo "→ Removing stale lock files..."
            rm -f "$BREW_CACHE"/*.incomplete 2>/dev/null || true
        fi
    fi
    
    # Run brew cleanup to ensure clean state
    echo "→ Cleaning Homebrew cache..."
    brew cleanup 2>/dev/null || true
    
    echo "✓ Homebrew is ready"
}

# Function to safely install a brew package with retry logic
safe_brew_install() {
    local package="$1"
    local max_retries=3
    local retry_count=0
    
    while [ $retry_count -lt $max_retries ]; do
        echo "→ Installing $package (attempt $((retry_count + 1))/$max_retries)..."
        
        if brew install "$package" 2>&1; then
            echo "✓ $package installed successfully"
            return 0
        else
            retry_count=$((retry_count + 1))
            if [ $retry_count -lt $max_retries ]; then
                echo "⚠️  Installation failed, cleaning up and retrying..."
                cleanup_brew_locks
                sleep 3
            fi
        fi
    done
    
    echo "✗ Failed to install $package after $max_retries attempts"
    return 1
}

# ============================================
# Main Installation Script
# ============================================

# Check if Homebrew is installed
echo ""
echo "[0/3] Checking Homebrew package manager..."
if ! command -v brew &> /dev/null; then
    echo "Installing Homebrew..."
    /bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
fi
echo "Homebrew OK"

# Pre-installation cleanup
cleanup_brew_locks

echo ""
echo "[1/3] Installing Java JDK 17..."

# Detect macOS version
MACOS_VERSION=$(sw_vers -productVersion)
MACOS_MAJOR=$(echo "$MACOS_VERSION" | cut -d. -f1)

# Detect CPU architecture
ARCH=$(uname -m)

echo "Detected macOS version: $MACOS_VERSION (Major: $MACOS_MAJOR)"
echo "Detected architecture: $ARCH"

# Function to install Java via Adoptium/Temurin (direct download)
install_java_adoptium() {
    echo "Using Adoptium Temurin direct download method..."
    
    # Determine download URL based on architecture
    if [ "$ARCH" = "arm64" ]; then
        JDK_URL="https://github.com/adoptium/temurin17-binaries/releases/download/jdk-17.0.13%2B11/OpenJDK17U-jdk_aarch64_mac_hotspot_17.0.13_11.pkg"
        JDK_PKG="OpenJDK17U-jdk_aarch64_mac_hotspot_17.0.13_11.pkg"
    else
        JDK_URL="https://github.com/adoptium/temurin17-binaries/releases/download/jdk-17.0.13%2B11/OpenJDK17U-jdk_x64_mac_hotspot_17.0.13_11.pkg"
        JDK_PKG="OpenJDK17U-jdk_x64_mac_hotspot_17.0.13_11.pkg"
    fi
    
    # Download JDK
    echo "Downloading JDK 17 from Adoptium..."
    curl -L -o "/tmp/$JDK_PKG" "$JDK_URL"
    
    # Install JDK
    echo "Installing JDK 17 (requires sudo)..."
    sudo installer -pkg "/tmp/$JDK_PKG" -target /
    
    # Cleanup
    rm -f "/tmp/$JDK_PKG"
    
    # Set JAVA_HOME for Adoptium installation
    JAVA_HOME_PATH="/Library/Java/JavaVirtualMachines/temurin-17.jdk/Contents/Home"
    if [ ! -d "$JAVA_HOME_PATH" ]; then
        # Fallback to find the installed JDK
        JAVA_HOME_PATH=$(/usr/libexec/java_home -v 17 2>/dev/null || echo "")
    fi
    
    if [ -n "$JAVA_HOME_PATH" ]; then
        echo "export JAVA_HOME=\"$JAVA_HOME_PATH\"" >> ~/.zshrc
        echo "export PATH=\"\$JAVA_HOME/bin:\$PATH\"" >> ~/.zshrc
        export JAVA_HOME="$JAVA_HOME_PATH"
        export PATH="$JAVA_HOME/bin:$PATH"
    fi
}

# Function to install Java via Homebrew
install_java_homebrew() {
    echo "Using Homebrew installation method..."
    safe_brew_install openjdk@17
    
    # Determine Homebrew prefix (different for Intel vs Apple Silicon)
    BREW_PREFIX=$(brew --prefix)
    
    # Add Java to PATH
    echo "export PATH=\"$BREW_PREFIX/opt/openjdk@17/bin:\$PATH\"" >> ~/.zshrc
    export PATH="$BREW_PREFIX/opt/openjdk@17/bin:$PATH"
    
    # Link Java
    sudo ln -sfn "$BREW_PREFIX/opt/openjdk@17/libexec/openjdk.jdk" /Library/Java/JavaVirtualMachines/openjdk-17.jdk
}

# Check if Java 17 is already installed
if java -version 2>&1 | grep -q "version \"17"; then
    echo "Java 17 is already installed!"
else
    # For macOS 12 (Monterey) or older, use direct download
    # Homebrew doesn't provide pre-built binaries and requires Xcode
    if [ "$MACOS_MAJOR" -le 12 ]; then
        echo "macOS $MACOS_VERSION detected (older version)"
        echo "Homebrew may require Xcode for compilation on this version."
        echo "Using alternative installation method..."
        install_java_adoptium
    else
        # For macOS 13+ (Ventura and newer), use Homebrew
        install_java_homebrew
    fi
fi

echo "Java installation complete!"

echo ""
echo "[2/3] Installing MySQL Server..."

# Function to install MySQL via direct DMG (for older macOS - no compilation needed!)
install_mysql_dmg() {
    echo "Using MySQL Community Server DMG installer (no compilation required)..."
    echo "This is MUCH faster than Homebrew on older macOS!"
    echo ""
    
    # MySQL 8.0 LTS version - compatible with macOS 10.15+
    local MYSQL_VERSION="8.0.42"
    local MYSQL_DMG=""
    local MYSQL_PKG_NAME=""
    
    if [ "$ARCH" = "arm64" ]; then
        MYSQL_DMG="mysql-${MYSQL_VERSION}-macos15-arm64.dmg"
        MYSQL_PKG_NAME="mysql-${MYSQL_VERSION}-macos15-arm64.pkg"
    else
        MYSQL_DMG="mysql-${MYSQL_VERSION}-macos15-x86_64.dmg"
        MYSQL_PKG_NAME="mysql-${MYSQL_VERSION}-macos15-x86_64.pkg"
    fi
    
    local MYSQL_URL="https://dev.mysql.com/get/Downloads/MySQL-8.0/${MYSQL_DMG}"
    local DOWNLOAD_PATH="/tmp/${MYSQL_DMG}"
    
    echo "→ Downloading MySQL ${MYSQL_VERSION}..."
    echo "  URL: $MYSQL_URL"
    
    if ! curl -L --progress-bar -o "$DOWNLOAD_PATH" "$MYSQL_URL"; then
        echo "✗ Download failed. Trying alternate mirror..."
        # Fallback to older compatible version if needed
        MYSQL_VERSION="8.0.36"
        if [ "$ARCH" = "arm64" ]; then
            MYSQL_DMG="mysql-${MYSQL_VERSION}-macos14-arm64.dmg"
            MYSQL_PKG_NAME="mysql-${MYSQL_VERSION}-macos14-arm64.pkg"
        else
            MYSQL_DMG="mysql-${MYSQL_VERSION}-macos14-x86_64.dmg"
            MYSQL_PKG_NAME="mysql-${MYSQL_VERSION}-macos14-x86_64.pkg"
        fi
        MYSQL_URL="https://dev.mysql.com/get/Downloads/MySQL-8.0/${MYSQL_DMG}"
        DOWNLOAD_PATH="/tmp/${MYSQL_DMG}"
        
        curl -L --progress-bar -o "$DOWNLOAD_PATH" "$MYSQL_URL" || {
            echo "✗ Failed to download MySQL. Please download manually from https://dev.mysql.com/downloads/mysql/"
            exit 1
        }
    fi
    
    echo "→ Mounting DMG..."
    local MOUNT_POINT=$(hdiutil attach "$DOWNLOAD_PATH" -nobrowse | grep "/Volumes" | awk '{print $3}')
    
    if [ -z "$MOUNT_POINT" ]; then
        echo "✗ Failed to mount DMG"
        exit 1
    fi
    
    echo "→ Installing MySQL (requires sudo password)..."
    # Find the .pkg file in the mounted volume
    local PKG_FILE=$(find "$MOUNT_POINT" -name "*.pkg" -type f | head -1)
    
    if [ -z "$PKG_FILE" ]; then
        echo "✗ Could not find installer package in DMG"
        hdiutil detach "$MOUNT_POINT" 2>/dev/null
        exit 1
    fi
    
    sudo installer -pkg "$PKG_FILE" -target /
    
    echo "→ Unmounting DMG..."
    hdiutil detach "$MOUNT_POINT" 2>/dev/null || true
    
    echo "→ Cleaning up..."
    rm -f "$DOWNLOAD_PATH"
    
    # Add MySQL to PATH
    echo "→ Configuring MySQL PATH..."
    MYSQL_BIN="/usr/local/mysql/bin"
    if [ -d "$MYSQL_BIN" ]; then
        if ! grep -q "mysql/bin" ~/.zshrc 2>/dev/null; then
            echo "export PATH=\"$MYSQL_BIN:\$PATH\"" >> ~/.zshrc
        fi
        export PATH="$MYSQL_BIN:$PATH"
    fi
    
    # Initialize MySQL database (required for fresh DMG install!)
    echo "→ Initializing MySQL database..."
    if [ ! -d "/usr/local/mysql/data" ] || [ -z "$(ls -A /usr/local/mysql/data 2>/dev/null)" ]; then
        sudo mkdir -p /usr/local/mysql/data
        sudo chown -R _mysql:_mysql /usr/local/mysql/data
        sudo /usr/local/mysql/bin/mysqld --initialize-insecure --user=_mysql --datadir=/usr/local/mysql/data
        echo "✓ MySQL database initialized (root has no password)"
    else
        echo "✓ MySQL data directory already exists"
    fi
    
    echo "✓ MySQL installed successfully via DMG!"
}

# Function to start MySQL service (works for both DMG and Homebrew installs)
start_mysql_service() {
    echo "→ Starting MySQL service..."
    
    # Check if it's a DMG installation
    if [ -f "/usr/local/mysql/support-files/mysql.server" ]; then
        sudo /usr/local/mysql/support-files/mysql.server start || true
    elif command -v brew &>/dev/null && brew list mysql &>/dev/null; then
        brew services start mysql || true
    else
        echo "⚠️  Could not auto-start MySQL. You may need to start it manually."
    fi
    
    # Wait for MySQL to start
    echo "→ Waiting for MySQL to initialize..."
    sleep 8
}

# Check if MySQL is already installed
if command -v mysql &>/dev/null || [ -f "/usr/local/mysql/bin/mysql" ]; then
    echo "MySQL is already installed!"
    # Make sure PATH includes MySQL
    if [ -d "/usr/local/mysql/bin" ]; then
        export PATH="/usr/local/mysql/bin:$PATH"
    fi
else
    # For macOS 12 (Monterey) or older, use DMG installer (FAST!)
    # Homebrew requires compiling LLVM, cmake, etc. which takes HOURS
    if [ "$MACOS_MAJOR" -le 12 ]; then
        echo "macOS $MACOS_VERSION detected (older version)"
        echo "⚡ Using direct DMG installer to avoid hours of compilation!"
        install_mysql_dmg
    else
        # For macOS 13+ (Ventura and newer), Homebrew has prebuilt binaries
        safe_brew_install mysql
    fi
fi

# Start MySQL
start_mysql_service

echo ""
echo "[3/3] Setting up Database..."
echo "Creating database and user..."

# Handle both DMG and Homebrew MySQL paths
if [ -d "/usr/local/mysql/bin" ]; then
    MYSQL_CMD="/usr/local/mysql/bin/mysql"
else
    MYSQL_CMD="mysql"
fi

# For fresh MySQL DMG install, root has no password
# For Homebrew, root also has no password by default
$MYSQL_CMD -u root -e "CREATE DATABASE IF NOT EXISTS db_saas_track;" 2>/dev/null || \
    $MYSQL_CMD -u root -p -e "CREATE DATABASE IF NOT EXISTS db_saas_track;"

$MYSQL_CMD -u root -e "CREATE USER IF NOT EXISTS 'saastrack'@'localhost' IDENTIFIED BY 'saastrack123';" 2>/dev/null || true
$MYSQL_CMD -u root -e "GRANT ALL PRIVILEGES ON db_saas_track.* TO 'saastrack'@'localhost';" 2>/dev/null || true
$MYSQL_CMD -u root -e "FLUSH PRIVILEGES;" 2>/dev/null || true

echo "Importing database schema..."
SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
PROJECT_DIR="$(dirname "$SCRIPT_DIR")"
SQL_FILE="$PROJECT_DIR/sql/db_saas_track.sql"

if [ -f "$SQL_FILE" ]; then
    $MYSQL_CMD -u root db_saas_track < "$SQL_FILE" 2>/dev/null || \
        $MYSQL_CMD -u root -p db_saas_track < "$SQL_FILE"
    echo "✓ Database schema imported"
else
    echo "⚠️  SQL file not found at: $SQL_FILE"
    echo "   You may need to import the schema manually."
fi

echo ""
echo "=================================="
echo "Installation Complete!"
echo "=================================="
echo ""
echo "Java version:"
java -version
echo ""
echo "MySQL location:"
if [ -d "/usr/local/mysql" ]; then
    echo "  /usr/local/mysql (DMG installation)"
    /usr/local/mysql/bin/mysql --version
else
    echo "  $(which mysql) (Homebrew installation)"
    mysql --version
fi
echo ""
echo "To run the application:"
echo "  ./macos-run.sh"
echo ""
echo "Default login:"
echo "  Username: admin"
echo "  Password: admin123"
echo "=================================="

