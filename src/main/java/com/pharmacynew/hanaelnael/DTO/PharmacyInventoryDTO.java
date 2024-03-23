package com.pharmacynew.hanaelnael.DTO;

import jakarta.persistence.Column;

import java.time.LocalDate;

public class PharmacyInventoryDTO {

    public String name;
    public int rQuantity;
    public double unitCost;
    public LocalDate exDate;

    public PharmacyInventoryDTO() {

    }



    public PharmacyInventoryDTO(String name, int rQuantity, double unitCost, String expiryDate,LocalDate exDate) {
        this.name = name;
        this.rQuantity = rQuantity;
        this.unitCost = unitCost;
        this.exDate = exDate;
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

    public LocalDate getExDate() {
        return exDate;
    }

    public void setExDate(LocalDate exDate) {
        this.exDate = exDate;
    }

    @Override
    public String toString() {
        return "PharmacyInventoryDTO{" +
                "name='" + name + '\'' +
                ", rQuantity=" + rQuantity +
                ", unitCost=" + unitCost +
                ",exDate='" + exDate +
                '}';
    }

}
