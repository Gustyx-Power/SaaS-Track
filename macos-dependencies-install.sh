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
brew install openjdk@17

# Add Java to PATH
echo 'export PATH="/opt/homebrew/opt/openjdk@17/bin:$PATH"' >> ~/.zshrc
export PATH="/opt/homebrew/opt/openjdk@17/bin:$PATH"

# Link Java
sudo ln -sfn /opt/homebrew/opt/openjdk@17/libexec/openjdk.jdk /Library/Java/JavaVirtualMachines/openjdk-17.jdk

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
