package com.pharmacynew.hanaelnael.DTO;

import com.pharmacynew.hanaelnael.Entity.PharmacyInventory;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.time.LocalDate;

public class SalesCreateDTO {
    public int salesQuantity;
    public int inventoryId;
    public LocalDate saleDate;
    public SalesCreateDTO getInventoryId;

    public SalesCreateDTO(){

    }

    public SalesCreateDTO(int salesQuantity) {
        this.salesQuantity = salesQuantity;
    }

    public int getSalesQuantity() {
        return salesQuantity;
    }

    public LocalDate getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(LocalDate saleDate) {
        this.saleDate = saleDate;
    }

    public void setSalesQuantity(int salesQuantity) {
        this.salesQuantity = salesQuantity;
    }

    public int getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(int inventoryId) {
        this.inventoryId = inventoryId;
    }

    @Override
    public String toString() {
        return "SalesCreateDTO{" +
                "salesQuantity=" + salesQuantity +
                ", inventoryId=" + inventoryId +
                '}';
    }
}
