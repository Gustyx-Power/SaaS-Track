package model;

import java.sql.Timestamp;

/**
 * Model class untuk tabel users
 * Menyimpan informasi pengguna sistem
 */
public class User {
    
    // Private fields (enkapsulasi)
    private int id;
    private String username;
    private String password;
    private String role; // "admin" atau "operator"
    private Timestamp createdAt;
    private Timestamp updatedAt;
    
    // Default constructor
    public User() {
    }
    
    // Constructor dengan parameter utama
    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }
    
    // Full constructor
    public User(int id, String username, String password, String role, 
                Timestamp createdAt, Timestamp updatedAt) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    // Getter dan Setter untuk id
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    // Getter dan Setter untuk username
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    // Getter dan Setter untuk password
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    // Getter dan Setter untuk role
    public String getRole() {
        return role;
    }
    
    public void setRole(String role) {
        this.role = role;
    }
    
    // Getter dan Setter untuk createdAt
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    
    // Getter dan Setter untuk updatedAt
    public Timestamp getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    // Method untuk mengecek apakah user adalah admin
    public boolean isAdmin() {
        return "admin".equalsIgnoreCase(this.role);
    }
    
    // Method untuk mengecek apakah user adalah operator
    public boolean isOperator() {
        return "operator".equalsIgnoreCase(this.role);
    }
    
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", role='" + role + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
