package dao;

import model.Department;
import util.DatabaseConnection;

import javax.swing.*;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object untuk tabel departments
 */
public class DepartmentDAO {

    public List<Object[]> getAllDepartments() {
        List<Object[]> departments = new ArrayList<>();

        String sql = "SELECT id, nama_dept, budget_limit, created_at FROM departments ORDER BY id";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Object[] row = new Object[4];
                row[0] = rs.getInt("id");
                row[1] = rs.getString("nama_dept");
                row[2] = rs.getBigDecimal("budget_limit");
                row[3] = rs.getTimestamp("created_at");
                departments.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Database Error",
                    JOptionPane.ERROR_MESSAGE);
        }

        return departments;
    }

    public boolean insert(Department dept) {
        String sql = "INSERT INTO departments (nama_dept, budget_limit) VALUES (?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, dept.getNamaDept());
            stmt.setBigDecimal(2, dept.getBudgetLimit());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(Department dept) {
        String sql = "UPDATE departments SET nama_dept=?, budget_limit=? WHERE id=?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, dept.getNamaDept());
            stmt.setBigDecimal(2, dept.getBudgetLimit());
            stmt.setInt(3, dept.getId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM departments WHERE id=?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
