#!/bin/bash
# Fedora/RHEL Dependencies Installer for SaaS-Track
# Run with: sudo ./fedora-rhel-dependencies-install.sh

set -e

echo "=================================="
echo "SaaS-Track Dependencies Installer"
echo "Fedora/RHEL Edition"
echo "=================================="

# Check if running as root
if [ "$EUID" -ne 0 ]; then
    echo "Please run as root: sudo $0"
    exit 1
fi

echo ""
echo "[1/3] Installing Java JDK 17..."
dnf install -y java-17-openjdk java-17-openjdk-devel

# Set Java 17 as default
alternatives --set java java-17-openjdk.x86_64

echo ""
echo "[2/3] Installing MySQL Server..."
dnf install -y mysql-server

# Start MySQL
systemctl start mysqld
systemctl enable mysqld

echo ""
echo "[3/3] Setting up Database..."
echo "Creating database and user..."

mysql -e "CREATE DATABASE IF NOT EXISTS db_saas_track;"
mysql -e "CREATE USER IF NOT EXISTS 'saastrack'@'localhost' IDENTIFIED BY 'saastrack123';"
mysql -e "GRANT ALL PRIVILEGES ON db_saas_track.* TO 'saastrack'@'localhost';"
mysql -e "FLUSH PRIVILEGES;"

echo "Importing database schema..."
SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
mysql db_saas_track < "$SCRIPT_DIR/sql/db_saas_track.sql"

echo ""
echo "=================================="
echo "Installation Complete!"
echo "=================================="
echo ""
echo "Java version:"
java -version
echo ""
echo "MySQL status:"
systemctl status mysqld --no-pager | head -5
echo ""
echo "To run the application:"
echo "  ./linux-run.sh"
echo ""
echo "Default login:"
echo "  Username: admin"
echo "  Password: admin123"
echo "=================================="
