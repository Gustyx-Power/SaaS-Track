package dao;

import model.Subscription;
import util.DatabaseConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// Apache POI imports
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Data Access Object untuk tabel subscriptions
 * Menyediakan operasi CRUD dan export ke Excel
 */
public class SubscriptionDAO {

    /**
     * Mengambil semua data subscription dengan JOIN ke tabel departments
     * 
     * @return List of Subscription dengan nama departemen
     */
    public List<Object[]> getAllSubscriptions() {
        List<Object[]> subscriptions = new ArrayList<>();

        String sql = "SELECT s.id, s.nama_layanan, s.vendor, s.harga, s.tgl_expired, " +
                "s.status, d.nama_dept, s.id_dept " +
                "FROM subscriptions s " +
                "INNER JOIN departments d ON s.id_dept = d.id " +
                "ORDER BY s.id";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Object[] row = new Object[8];
                row[0] = rs.getInt("id");
                row[1] = rs.getString("nama_layanan");
                row[2] = rs.getString("vendor");
                row[3] = rs.getBigDecimal("harga");
                row[4] = rs.getDate("tgl_expired");
                row[5] = rs.getString("status");
                row[6] = rs.getString("nama_dept");
                row[7] = rs.getInt("id_dept");
                subscriptions.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "Error mengambil data: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }

        return subscriptions;
    }

    /**
     * Mencari subscription berdasarkan nama layanan menggunakan LIKE
     * 
     * @param name Kata kunci pencarian
     * @return List of matching subscriptions
     */
    public List<Object[]> searchByName(String name) {
        List<Object[]> subscriptions = new ArrayList<>();

        String sql = "SELECT s.id, s.nama_layanan, s.vendor, s.harga, s.tgl_expired, " +
                "s.status, d.nama_dept, s.id_dept " +
                "FROM subscriptions s " +
                "INNER JOIN departments d ON s.id_dept = d.id " +
                "WHERE s.nama_layanan LIKE ? " +
                "ORDER BY s.id";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + name + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Object[] row = new Object[8];
                    row[0] = rs.getInt("id");
                    row[1] = rs.getString("nama_layanan");
                    row[2] = rs.getString("vendor");
                    row[3] = rs.getBigDecimal("harga");
                    row[4] = rs.getDate("tgl_expired");
                    row[5] = rs.getString("status");
                    row[6] = rs.getString("nama_dept");
                    row[7] = rs.getInt("id_dept");
                    subscriptions.add(row);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "Error pencarian: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }

        return subscriptions;
    }

    /**
     * Insert subscription baru
     */
    public boolean insert(Subscription sub) {
        String sql = "INSERT INTO subscriptions (nama_layanan, vendor, harga, tgl_expired, status, id_dept) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, sub.getNamaLayanan());
            stmt.setString(2, sub.getVendor());
            stmt.setBigDecimal(3, sub.getHarga());
            stmt.setDate(4, sub.getTglExpired());
            stmt.setString(5, sub.getStatus());
            stmt.setInt(6, sub.getIdDept());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Update subscription
     */
    public boolean update(Subscription sub) {
        String sql = "UPDATE subscriptions SET nama_layanan=?, vendor=?, harga=?, " +
                "tgl_expired=?, status=?, id_dept=? WHERE id=?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, sub.getNamaLayanan());
            stmt.setString(2, sub.getVendor());
            stmt.setBigDecimal(3, sub.getHarga());
            stmt.setDate(4, sub.getTglExpired());
            stmt.setString(5, sub.getStatus());
            stmt.setInt(6, sub.getIdDept());
            stmt.setInt(7, sub.getId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Delete subscription
     */
    public boolean delete(int id) {
        String sql = "DELETE FROM subscriptions WHERE id=?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Export data JTable ke file Excel (.xlsx) menggunakan Apache POI
     * 
     * @param table    JTable yang akan diekspor
     * @param filePath Path file tujuan
     * @return true jika berhasil
     */
    public boolean exportToExcel(JTable table, String filePath) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Data Langganan");

            DefaultTableModel model = (DefaultTableModel) table.getModel();

            // Create header style
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setColor(IndexedColors.WHITE.getIndex());
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setBorderBottom(BorderStyle.THIN);
            headerStyle.setBorderTop(BorderStyle.THIN);
            headerStyle.setBorderLeft(BorderStyle.THIN);
            headerStyle.setBorderRight(BorderStyle.THIN);

            // Create data style
            CellStyle dataStyle = workbook.createCellStyle();
            dataStyle.setBorderBottom(BorderStyle.THIN);
            dataStyle.setBorderTop(BorderStyle.THIN);
            dataStyle.setBorderLeft(BorderStyle.THIN);
            dataStyle.setBorderRight(BorderStyle.THIN);

            // Create currency style
            CellStyle currencyStyle = workbook.createCellStyle();
            currencyStyle.cloneStyleFrom(dataStyle);
            DataFormat format = workbook.createDataFormat();
            currencyStyle.setDataFormat(format.getFormat("#,##0"));

            // Write header row
            Row headerRow = sheet.createRow(0);
            for (int col = 0; col < model.getColumnCount(); col++) {
                Cell cell = headerRow.createCell(col);
                cell.setCellValue(model.getColumnName(col));
                cell.setCellStyle(headerStyle);
            }

            // Write data rows
            for (int row = 0; row < model.getRowCount(); row++) {
                Row dataRow = sheet.createRow(row + 1);
                for (int col = 0; col < model.getColumnCount(); col++) {
                    Cell cell = dataRow.createCell(col);
                    Object value = model.getValueAt(row, col);

                    if (value != null) {
                        if (value instanceof Number) {
                            cell.setCellValue(((Number) value).doubleValue());
                            cell.setCellStyle(currencyStyle);
                        } else {
                            cell.setCellValue(value.toString());
                            cell.setCellStyle(dataStyle);
                        }
                    } else {
                        cell.setCellValue("");
                        cell.setCellStyle(dataStyle);
                    }
                }
            }

            // Auto-size columns
            for (int col = 0; col < model.getColumnCount(); col++) {
                sheet.autoSizeColumn(col);
            }

            // Write to file
            try (FileOutputStream fos = new FileOutputStream(new File(filePath))) {
                workbook.write(fos);
            }

            JOptionPane.showMessageDialog(null,
                    "Data berhasil diekspor ke:\n" + filePath,
                    "Export Berhasil", JOptionPane.INFORMATION_MESSAGE);

            return true;

        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "Error export: " + e.getMessage(),
                    "Export Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    /**
     * Export dengan dialog file chooser
     */
    public boolean exportToExcelWithDialog(JTable table) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Export ke Excel");
        fileChooser.setSelectedFile(new File("data_langganan.xlsx"));

        int result = fileChooser.showSaveDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            String filePath = fileChooser.getSelectedFile().getAbsolutePath();
            if (!filePath.endsWith(".xlsx")) {
                filePath += ".xlsx";
            }
            return exportToExcel(table, filePath);
        }
        return false;
    }
}
