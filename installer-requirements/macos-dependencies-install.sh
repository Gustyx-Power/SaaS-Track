#!/bin/bash
# macOS Dependencies Installer for SaaS-Track
# Run with: ./macos-dependencies-install.sh

set -e

echo "=================================="
echo "SaaS-Track Dependencies Installer"
echo "macOS Edition"
echo "=================================="

# Check if Homebrew is installed
echo ""
echo "[0/3] Checking Homebrew package manager..."
if ! command -v brew &> /dev/null; then
    echo "Installing Homebrew..."
    /bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
fi
echo "Homebrew OK"

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
    brew install openjdk@17
    
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
brew install mysql

# Start MySQL service
brew services start mysql

# Wait for MySQL to start
sleep 5

echo ""
echo "[3/3] Setting up Database..."
echo "Creating database and user..."

mysql -u root -e "CREATE DATABASE IF NOT EXISTS db_saas_track;"
mysql -u root -e "CREATE USER IF NOT EXISTS 'saastrack'@'localhost' IDENTIFIED BY 'saastrack123';"
mysql -u root -e "GRANT ALL PRIVILEGES ON db_saas_track.* TO 'saastrack'@'localhost';"
mysql -u root -e "FLUSH PRIVILEGES;"

echo "Importing database schema..."
SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
mysql -u root db_saas_track < "$SCRIPT_DIR/sql/db_saas_track.sql"

echo ""
echo "=================================="
echo "Installation Complete!"
echo "=================================="
echo ""
echo "Java version:"
java -version
echo ""
echo "MySQL status:"
brew services list | grep mysql
echo ""
echo "To run the application:"
echo "  ./linux-run.sh"
echo ""
echo "Default login:"
echo "  Username: admin"
echo "  Password: admin123"
echo "=================================="
