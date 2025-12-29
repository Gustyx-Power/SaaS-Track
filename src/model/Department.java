package model;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Model class untuk tabel departments
 * Menyimpan informasi departemen
 */
public class Department {

    // Private fields (enkapsulasi)
    private int id;
    private String namaDept;
    private BigDecimal budgetLimit;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    // Default constructor
    public Department() {
    }

    // Constructor dengan parameter utama
    public Department(String namaDept, BigDecimal budgetLimit) {
        this.namaDept = namaDept;
        this.budgetLimit = budgetLimit;
    }

    // Full constructor
    public Department(int id, String namaDept, BigDecimal budgetLimit,
            Timestamp createdAt, Timestamp updatedAt) {
        this.id = id;
        this.namaDept = namaDept;
        this.budgetLimit = budgetLimit;
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

    // Getter dan Setter untuk namaDept
    public String getNamaDept() {
        return namaDept;
    }

    public void setNamaDept(String namaDept) {
        this.namaDept = namaDept;
    }

    // Getter dan Setter untuk budgetLimit
    public BigDecimal getBudgetLimit() {
        return budgetLimit;
    }

    public void setBudgetLimit(BigDecimal budgetLimit) {
        this.budgetLimit = budgetLimit;
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

    @Override
    public String toString() {
        return "Department{" +
                "id=" + id +
                ", namaDept='" + namaDept + '\'' +
                ", budgetLimit=" + budgetLimit +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
