package com.pharmacynew.hanaelnael.Entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Arrays;

@Entity
@Table(name="Pharmacy_Inventory")
public class PharmacyInventory {
    @Column(name = "Id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int Id;
    @Column(name = "Name")

    public String name;
    @Column(name = "received_quantity")
    public int rQuantity;
    @Column(name = "Unit_cost")
    public double unitCost;
    @Column(name = "Unit_price")
    public double unitPrice;

    @Column(name="Ex_date")
    public LocalDate exDate;
    @Column(name="qr_code" ,columnDefinition = "BLOB")
    @Lob
    private byte[] qrCodeImage; // Store the barcode image as byte array

    // Getters and setters for the fields

    public byte[] getQrCodeImage() {
        return qrCodeImage;
    }

    public void setQrCodeImage(byte[] qrCodeImage) {
        this.qrCodeImage = qrCodeImage;
    }

    public PharmacyInventory() {

    }

    public PharmacyInventory(byte[] qrCodeImage) {
        this.qrCodeImage = qrCodeImage;
    }

    public PharmacyInventory(String name, int rQuantity, double unitCost, double unitPrice, LocalDate exDate) {
        this.name = name;
        this.rQuantity = rQuantity;
        this.unitCost = unitCost;
        this.unitPrice = unitPrice;
        this.exDate = exDate;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRQuantity() {
        return rQuantity;
    }

    public void setRQuantity(int rQuantity) {
        this.rQuantity = rQuantity;
    }

    public double getUnitCost() {
        return unitCost;
    }

    public void setUnitCost(double unitCost) {
        this.unitCost = unitCost;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public LocalDate getExDate() {
        return exDate;
    }

    public void setExDate(LocalDate exDate) {
        this.exDate = exDate;
    }

    @Override
    public String toString() {
        return "PharmacyInventory{" +
                "Id=" + Id +
                ", name='" + name + '\'' +
                ", rQuantity=" + rQuantity +
                ", unitCost=" + unitCost +
                ", unitPrice=" + unitPrice +
                ", exDate=" + exDate +
                ", qrCodeImage=" + Arrays.toString(qrCodeImage) +
                '}';
    }

}


