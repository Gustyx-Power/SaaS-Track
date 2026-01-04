# Windows Dependencies Installer for SaaS-Track
# Run with Administrator privileges: Right-click -> Run as Administrator
# PowerShell: .\windows-dependencies-install.ps1

Write-Host "==================================" -ForegroundColor Cyan
Write-Host "SaaS-Track Dependencies Installer" -ForegroundColor Cyan
Write-Host "Windows Edition (PowerShell)" -ForegroundColor Cyan
Write-Host "==================================" -ForegroundColor Cyan

# Check if running as Administrator
$isAdmin = ([Security.Principal.WindowsPrincipal] [Security.Principal.WindowsIdentity]::GetCurrent()).IsInRole([Security.Principal.WindowsBuiltInRole]::Administrator)
if (-not $isAdmin) {
    Write-Host "Please run this script as Administrator!" -ForegroundColor Red
    Write-Host "Right-click PowerShell -> Run as Administrator" -ForegroundColor Yellow
    pause
    exit 1
}

# Auto-detect and change to project root directory
# Works for both git clone (SaaS-Track) and zip download (SaaS-Track-main)
$ScriptDir = Split-Path -Parent $MyInvocation.MyCommand.Path
$ProjectRoot = Split-Path -Parent $ScriptDir
Set-Location $ProjectRoot
Write-Host "Working directory: $ProjectRoot" -ForegroundColor Cyan

# Check if Chocolatey is installed
Write-Host ""
Write-Host "[0/3] Checking Chocolatey package manager..." -ForegroundColor Yellow
if (-not (Get-Command choco -ErrorAction SilentlyContinue)) {
    Write-Host "Installing Chocolatey..." -ForegroundColor Green
    Set-ExecutionPolicy Bypass -Scope Process -Force
    [System.Net.ServicePointManager]::SecurityProtocol = [System.Net.ServicePointManager]::SecurityProtocol -bor 3072
    Invoke-Expression ((New-Object System.Net.WebClient).DownloadString('https://community.chocolatey.org/install.ps1'))
    
    # Refresh environment
    $env:Path = [System.Environment]::GetEnvironmentVariable("Path", "Machine") + ";" + [System.Environment]::GetEnvironmentVariable("Path", "User")
}
Write-Host "Chocolatey OK" -ForegroundColor Green

Write-Host ""
Write-Host "[1/3] Installing Java JDK 17..." -ForegroundColor Yellow
choco install openjdk17 -y

# Refresh environment
$env:Path = [System.Environment]::GetEnvironmentVariable("Path", "Machine") + ";" + [System.Environment]::GetEnvironmentVariable("Path", "User")
$env:JAVA_HOME = "C:\Program Files\OpenJDK\jdk-17"

Write-Host ""
Write-Host "[2/3] Installing MySQL Server..." -ForegroundColor Yellow
choco install mysql -y

# Refresh environment
$env:Path = [System.Environment]::GetEnvironmentVariable("Path", "Machine") + ";" + [System.Environment]::GetEnvironmentVariable("Path", "User")

# Start MySQL service
Write-Host "Starting MySQL service..." -ForegroundColor Green
Start-Service MySQL -ErrorAction SilentlyContinue

Write-Host ""
Write-Host "[3/3] Setting up Database..." -ForegroundColor Yellow
Write-Host "Creating database and user..." -ForegroundColor Green

$ScriptDir = Split-Path -Parent $MyInvocation.MyCommand.Path
# Get project root (one level up from installer-requirements folder)
# Works for both git clone (SaaS-Track) and zip download (SaaS-Track-main)
$ProjectRoot = Split-Path -Parent $ScriptDir
$SqlFile = Join-Path $ProjectRoot "sql\db_saas_track.sql"

# Verify SQL file exists
if (-not (Test-Path $SqlFile)) {
    Write-Host "ERROR: SQL file not found at: $SqlFile" -ForegroundColor Red
    Write-Host "Please make sure the sql folder exists in the project root." -ForegroundColor Yellow
    pause
    exit 1
}

Write-Host "Project root: $ProjectRoot" -ForegroundColor Cyan
Write-Host "SQL file: $SqlFile" -ForegroundColor Cyan

# Wait for MySQL to be ready
Start-Sleep -Seconds 5

# Run MySQL commands
mysql -u root -e "CREATE DATABASE IF NOT EXISTS db_saas_track;"
mysql -u root -e "CREATE USER IF NOT EXISTS 'saastrack'@'localhost' IDENTIFIED BY 'saastrack123';"
mysql -u root -e "GRANT ALL PRIVILEGES ON db_saas_track.* TO 'saastrack'@'localhost';"
mysql -u root -e "FLUSH PRIVILEGES;"

Write-Host "Importing database schema..." -ForegroundColor Green
Get-Content $SqlFile | mysql -u root db_saas_track

Write-Host ""
Write-Host "==================================" -ForegroundColor Green
Write-Host "Installation Complete!" -ForegroundColor Green
Write-Host "==================================" -ForegroundColor Green
Write-Host ""
Write-Host "Java version:" -ForegroundColor Cyan
java -version
Write-Host ""
Write-Host "To run the application:" -ForegroundColor Yellow
Write-Host "  .\windows-run.bat" -ForegroundColor White
Write-Host ""
Write-Host "Default login:" -ForegroundColor Yellow
Write-Host "  Username: admin" -ForegroundColor White
Write-Host "  Password: admin123" -ForegroundColor White
Write-Host "==================================" -ForegroundColor Green
pause
