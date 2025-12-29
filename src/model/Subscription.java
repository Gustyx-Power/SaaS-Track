package model;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

/**
 * Model class untuk tabel subscriptions
 * Menyimpan informasi langganan SaaS
 */
public class Subscription {

    // Private fields (enkapsulasi)
    private int id;
    private String namaLayanan;
    private String vendor;
    private BigDecimal harga;
    private Date tglExpired;
    private String status; // "active", "expired", atau "cancelled"
    private int idDept;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    // Default constructor
    public Subscription() {
    }

    // Constructor dengan parameter utama
    public Subscription(String namaLayanan, String vendor, BigDecimal harga,
            Date tglExpired, String status, int idDept) {
        this.namaLayanan = namaLayanan;
        this.vendor = vendor;
        this.harga = harga;
        this.tglExpired = tglExpired;
        this.status = status;
        this.idDept = idDept;
    }

    // Full constructor
    public Subscription(int id, String namaLayanan, String vendor, BigDecimal harga,
            Date tglExpired, String status, int idDept,
            Timestamp createdAt, Timestamp updatedAt) {
        this.id = id;
        this.namaLayanan = namaLayanan;
        this.vendor = vendor;
        this.harga = harga;
        this.tglExpired = tglExpired;
        this.status = status;
        this.idDept = idDept;
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

    // Getter dan Setter untuk namaLayanan
    public String getNamaLayanan() {
        return namaLayanan;
    }

    public void setNamaLayanan(String namaLayanan) {
        this.namaLayanan = namaLayanan;
    }

    // Getter dan Setter untuk vendor
    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    // Getter dan Setter untuk harga
    public BigDecimal getHarga() {
        return harga;
    }

    public void setHarga(BigDecimal harga) {
        this.harga = harga;
    }

    // Getter dan Setter untuk tglExpired
    public Date getTglExpired() {
        return tglExpired;
    }

    public void setTglExpired(Date tglExpired) {
        this.tglExpired = tglExpired;
    }

    // Getter dan Setter untuk status
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // Getter dan Setter untuk idDept
    public int getIdDept() {
        return idDept;
    }

    public void setIdDept(int idDept) {
        this.idDept = idDept;
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

    // Method untuk mengecek apakah subscription aktif
    public boolean isActive() {
        return "active".equalsIgnoreCase(this.status);
    }

    // Method untuk mengecek apakah subscription expired
    public boolean isExpired() {
        return "expired".equalsIgnoreCase(this.status);
    }

    // Method untuk mengecek apakah subscription cancelled
    public boolean isCancelled() {
        return "cancelled".equalsIgnoreCase(this.status);
    }

    @Override
    public String toString() {
        return "Subscription{" +
                "id=" + id +
                ", namaLayanan='" + namaLayanan + '\'' +
                ", vendor='" + vendor + '\'' +
                ", harga=" + harga +
                ", tglExpired=" + tglExpired +
                ", status='" + status + '\'' +
                ", idDept=" + idDept +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
