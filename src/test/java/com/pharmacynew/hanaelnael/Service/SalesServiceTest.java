package com.pharmacynew.hanaelnael.Service;

import com.pharmacynew.hanaelnael.DAO.PharmacyInventoryDAO;
import com.pharmacynew.hanaelnael.DAO.SalesDAO;
import com.pharmacynew.hanaelnael.DTO.SalesCreateDTO;
import com.pharmacynew.hanaelnael.Entity.PharmacyInventory;
import com.pharmacynew.hanaelnael.Entity.Sales;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class SalesServiceTest {

    @Mock
    private PharmacyInventoryDAO pharmacyInventoryDAOMock;

    @Mock
    private SalesDAO salesDAOMock;

    @Mock
    private Logger loggerMock;

    @InjectMocks
    private SalesService salesService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateSales_EnoughQuantity() {
        // Create test data
        SalesCreateDTO dto = new SalesCreateDTO();
        dto.setSalesQuantity(5);
        dto.setSaleDate(LocalDate.now());
        byte[] qrCodeImage = "mocked_qr_code_image".getBytes();
        PharmacyInventory inventory = new PharmacyInventory();
        inventory.setId(1);
        inventory.setRQuantity(10);
        inventory.setUnitPrice(2.5);

        // Mock behavior
        when(pharmacyInventoryDAOMock.findByQrCodeImage(qrCodeImage)).thenReturn(inventory);
        ArgumentCaptor<Sales>savedSale=ArgumentCaptor.forClass(Sales.class);
        salesService.createSales(dto, qrCodeImage);
        verify(salesDAOMock).save(savedSale.capture());

        Sales createdSale=savedSale.getValue();
        // Verify that the sale is created with the correct quantity and total price
        assertEquals(dto.getSalesQuantity(), createdSale.getSalesQuantity());
        assertEquals(12.5, createdSale.getTotalPrice());

        // Verify that the quantity is updated correctly
        assertEquals(5, inventory.getRQuantity());

        // Verify interactions with DAO and logger

        // Ensure no additional interactions with DAO and logger
        verifyNoMoreInteractions(salesDAOMock, loggerMock);
    }
    @Test
    void testCreateSales_NotEnoughQuantity() {
        // Create test data
        SalesCreateDTO dto = new SalesCreateDTO();
        dto.setSalesQuantity(15);
        dto.setSaleDate(LocalDate.now());
        byte[] qrCodeImage = "mocked_qr_code_image".getBytes();
        PharmacyInventory inventory = new PharmacyInventory();
        inventory.setId(1);
        inventory.setRQuantity(10);
        inventory.setUnitPrice(2.5);

        // Mock behavior
        when(pharmacyInventoryDAOMock.findByQrCodeImage(qrCodeImage)).thenReturn(inventory);

        // Call the method and verify the exception
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                salesService.createSales(dto, qrCodeImage)
        );

        // Verify that the exception message is as expected
        assertEquals("Oops not enough quantity to process the transaction!!! ", exception.getMessage());

    }
    @Test
    void testTotalSales() {
        // Create test data
        byte[] qrCodeImage = "mocked_qr_code_image".getBytes();
        PharmacyInventory inventory = new PharmacyInventory();
        inventory.setId(1);
        inventory.setUnitPrice(2.5);
        List<Sales> salesList = new ArrayList<>();
        salesList.add(new Sales(5, 12.5, inventory, LocalDate.now()));

        // Mock behavior
        when(pharmacyInventoryDAOMock.findByQrCodeImage(qrCodeImage)).thenReturn(inventory);
        when(salesDAOMock.findByInventoryId(inventory.getId())).thenReturn(salesList);

        // Call the method
        double totalSale = salesService.totalSales(qrCodeImage);

        // Verify the total sale amount
        assertEquals(12.5, totalSale);

    }
    @Test
    void testGetDailySales() {
        // Create test data
        LocalDate saleDate = LocalDate.now();
        List<Sales> salesList = new ArrayList<>();
        salesList.add(new Sales(5, 12.5, new PharmacyInventory(), saleDate));

        // Mock behavior
        when(salesDAOMock.findBySaleDate(saleDate)).thenReturn(salesList);

        // Call the method
        List<Sales> dailySales = salesService.getDailySales(saleDate);

        // Verify the daily sales
        assertEquals(salesList, dailySales);

        // Verify interactions with DAO and logger
        verifyNoInteractions(loggerMock);
    }
}