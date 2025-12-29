-- ============================================
-- Database: db_saas_track
-- Description: Database untuk manajemen langganan SaaS per departemen
-- ============================================

-- Buat database jika belum ada
CREATE DATABASE IF NOT EXISTS db_saas_track
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

USE db_saas_track;

-- ============================================
-- Tabel 1: users
-- Menyimpan data pengguna sistem
-- ============================================
CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role ENUM('admin', 'operator') NOT NULL DEFAULT 'operator',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- ============================================
-- Tabel 2: departments
-- Menyimpan data departemen
-- ============================================
CREATE TABLE IF NOT EXISTS departments (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nama_dept VARCHAR(100) NOT NULL,
    budget_limit DECIMAL(15, 2) NOT NULL DEFAULT 0.00,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- ============================================
-- Tabel 3: subscriptions
-- Menyimpan data langganan SaaS
-- ============================================
CREATE TABLE IF NOT EXISTS subscriptions (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nama_layanan VARCHAR(100) NOT NULL,
    vendor VARCHAR(100) NOT NULL,
    harga DECIMAL(15, 2) NOT NULL,
    tgl_expired DATE NOT NULL,
    status ENUM('active', 'expired', 'cancelled') NOT NULL DEFAULT 'active',
    id_dept INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (id_dept) REFERENCES departments(id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

-- ============================================
-- Sample Data (Optional)
-- ============================================

-- Insert sample admin user (password: 'admin123' - harus di-hash di aplikasi)
INSERT INTO users (username, password, role) VALUES 
('admin', 'admin123', 'admin'),
('operator1', 'operator123', 'operator');

-- Insert sample departments
INSERT INTO departments (nama_dept, budget_limit) VALUES 
('IT Department', 50000000.00),
('Marketing', 30000000.00),
('Human Resources', 20000000.00);

-- Insert sample subscriptions
INSERT INTO subscriptions (nama_layanan, vendor, harga, tgl_expired, status, id_dept) VALUES 
('Microsoft 365 Business', 'Microsoft', 1500000.00, '2025-12-31', 'active', 1),
('Adobe Creative Cloud', 'Adobe', 2000000.00, '2025-06-30', 'active', 1),
('Slack Business+', 'Slack', 500000.00, '2025-03-15', 'active', 2),
('Zoom Pro', 'Zoom', 300000.00, '2024-12-01', 'expired', 3);
