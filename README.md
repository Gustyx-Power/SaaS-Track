# SaaS-Track

**Platform Manajemen Langganan SaaS Terintegrasi & Modern**

Aplikasi desktop berbasis Java Swing untuk mengelola langganan SaaS (Software as a Service) per departemen dalam organisasi.

## 📋 Fitur

- Dashboard dengan statistik langganan
- CRUD Langganan (Subscriptions)
- Manajemen Departemen
- Laporan & Analitik
- Otentikasi User (Admin/Operator)
- UI Modern dengan Material Design 3

## 🛠️ Prasyarat

- **Java JDK 17+** 
- **MySQL 8.0+**
- **Git** (opsional, untuk clone repository)

## 🗄️ Setup Database

### 1. Login ke MySQL
```bash
mysql -u root -p
```

### 2. Buat User Database
```sql
CREATE USER 'saastrack'@'localhost' IDENTIFIED BY 'saastrack123';
GRANT ALL PRIVILEGES ON db_saas_track.* TO 'saastrack'@'localhost';
FLUSH PRIVILEGES;
```

### 3. Import Database
```bash
mysql -u saastrack -p < sql/db_saas_track.sql
```

Atau melalui MySQL CLI:
```sql
SOURCE /path/to/SaaS-Track/sql/db_saas_track.sql;
```

### 4. Verifikasi
```sql
USE db_saas_track;
SHOW TABLES;
```
Output yang diharapkan:
```
+-------------------------+
| Tables_in_db_saas_track |
+-------------------------+
| departments             |
| subscriptions           |
| users                   |
+-------------------------+
```

## 🚀 Menjalankan Aplikasi

### Menggunakan Script (Recommended)
```bash
./run.sh
```

### Manual
```bash
cd src
javac -encoding UTF-8 -cp "../lib/*:." model/*.java util/*.java dao/*.java view/*.java Main.java
java -cp "../lib/*:." Main
```

## 🔐 Akun Default

| Username   | Password      | Role     |
|------------|---------------|----------|
| admin      | admin123      | Admin    |
| operator1  | operator123   | Operator |

## 📁 Struktur Proyek

```
SaaS-Track/
├── src/
│   ├── model/          # Entity classes
│   ├── view/           # UI components (Swing)
│   ├── dao/            # Data Access Objects
│   ├── util/           # Utilities (Theme, DB Connection)
│   └── Main.java       # Entry point
├── lib/                # JAR dependencies
├── sql/
│   └── db_saas_track.sql  # Database schema & sample data
└── run.sh              # Run script
```

## 📚 Dependencies

- FlatLaf 3.4 (Modern Look and Feel)
- MySQL Connector/J 8.2.0
- Apache POI 5.2.5 (Excel export)

## 📄 License

© 2025 Kelompok 8
