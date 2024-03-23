package com.pharmacynew.hanaelnael.Entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table
public class Sales {
    @Column(name="id")
    @jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int Id;
    @Column(name = "sale_date")
    public LocalDate saleDate;
    @PrePersist
    public void prePersist() {
        this.saleDate = LocalDate.now();}
    @Column(name="quantity")
    public int salesQuantity;
    @Column(name="total_price")
    public double totalPrice;

    @ManyToOne
    @JoinColumn(name = "pharmacy_inventory_id")
    public PharmacyInventory pharmacyInventory;

    public Sales(){

    }

    public Sales(int salesQuantity, double totalPrice, PharmacyInventory inventory,LocalDate saleDate) {
        this.salesQuantity = salesQuantity;
        this.totalPrice = totalPrice;
        this.pharmacyInventory=inventory;
        this.saleDate = saleDate;
    }


    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public LocalDate getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(LocalDate saleDate) {
        this.saleDate = saleDate;
    }

    public int getSalesQuantity() {
        return salesQuantity;
    }

    public void setSalesQuantity(int quantity) {
        this.salesQuantity = quantity;
    }

   // public double getUnitPrice() {
    //    return unitPrice;
   // }

   // public void setUnitPrice(double unitPrice) {
   //     this.unitPrice = unitPrice;
   // }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    @Override
    public String toString() {
        return "Sales{" +
                "Id=" + Id +
                ", salesQuantity=" + salesQuantity +
               // ", unitPrice=" + unitPrice +
                ", totalPrice=" + totalPrice +
                "saleDate=" + saleDate +
                '}';
    }

}
