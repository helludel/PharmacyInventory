package com.pharmacynew.hanaelnael.DTO;

import com.pharmacynew.hanaelnael.Entity.PharmacyInventory;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

public class SalesDTO {
    public int salesQuantity;

    public double unitPrice;

    public double totalPrice;

    public PharmacyInventory inventory;
    public SalesDTO(){

    }

    public SalesDTO(int salesQuantity, double unitPrice, double totalPrice, PharmacyInventory inventory) {
        this.salesQuantity = salesQuantity;
        this.unitPrice = unitPrice;
        this.totalPrice = totalPrice;
        this.inventory = inventory;
    }

    public int getSalesQuantity() {
        return salesQuantity;
    }

    public void setSalesQuantity(int salesQuantity) {
        this.salesQuantity = salesQuantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public PharmacyInventory getInventory() {
        return inventory;
    }

    public void setInventory(PharmacyInventory inventory) {
        this.inventory = inventory;
    }

    @Override
    public String toString() {
        return "SalesDTO{" +
                "salesQuantity=" + salesQuantity +
                ", unitPrice=" + unitPrice +
                ", totalPrice=" + totalPrice +
                ", inventory=" + inventory +
                '}';
    }
}
