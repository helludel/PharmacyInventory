package com.pharmacynew.hanaelnael.Entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PharmacyInventoryTest {
    PharmacyInventory pharmacyInventory;

    @BeforeEach
    public void setUp() {
        pharmacyInventory = new PharmacyInventory();
        pharmacyInventory.setId(1);
        pharmacyInventory.setName("ExampleName");
        pharmacyInventory.setRQuantity(100);
        pharmacyInventory.setUnitCost(10.5);
        pharmacyInventory.setUnitPrice(15.0);
        pharmacyInventory.setExDate(LocalDate.parse("2024-03-04"));
        pharmacyInventory.setQrCodeImage(new byte[]{1, 2, 3});
    }

    @Test
    public void testToString() {
        String expectedToString = "PharmacyInventory{" +
                "Id=1, " +
                "name='ExampleName', " +
                "rQuantity=100, " +
                "unitCost=10.5, " +
                "unitPrice=15.0, " +
                "exDate=2024-03-04, " +
                "qrCodeImage=[1, 2, 3]" +
                "}";

        String actualToString = pharmacyInventory.toString();

        assertEquals(expectedToString, actualToString,
                "toString should return the expected string");
    }
}

