package com.pharmacynew.hanaelnael.Service;

import com.pharmacynew.hanaelnael.DAO.PharmacyInventoryDAO;
import com.pharmacynew.hanaelnael.DTO.PharmacyInventoryDTO;
import com.pharmacynew.hanaelnael.Entity.PharmacyInventory;
import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.MockitoRule;
import org.slf4j.Logger;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
class PharmacyInventoryServiceTest {
    PharmacyInventoryDAO pharmacyInventoryDAOMock = mock(PharmacyInventoryDAO.class);
    Logger loggerMock = mock(Logger.class);
    QRCodeService qrCodeServiceMock = mock(QRCodeService.class);
    PharmacyInventoryService pharmacyInventoryService = new PharmacyInventoryService(qrCodeServiceMock,pharmacyInventoryDAOMock, loggerMock);

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    PharmacyInventoryDAO pharmacyInventoryDAO;

    @Test
    public void testCreateInventory_Success() {
        PharmacyInventoryDAO pharmacyInventoryDAOMock = mock(PharmacyInventoryDAO.class);
        QRCodeService qrCodeServiceMock = mock(QRCodeService.class);
        Logger loggerMock = mock(Logger.class);

        PharmacyInventoryService pharmacyInventoryServiceMock=new PharmacyInventoryService(qrCodeServiceMock,pharmacyInventoryDAOMock,loggerMock);

        String name = "Test Item";
        int quantity = 10;
        double unitCost = 5.0;
        LocalDate exDate = LocalDate.of(2023, 12, 31);

        // Mock behavior of DAO to return null for existing inventory

     //   ArgumentCaptor<PharmacyInventory> inventoryCaptor = ArgumentCaptor.forClass(PharmacyInventory.class);
     //   when(pharmacyInventoryDAOMock.save(inventoryCaptor.capture())).thenAnswer(invocation -> invocation.getArgument(0));

        // Mock behavior of QR code service to return a mock QR code image
        byte[] mockedQRCodeImage = "mocked_qr_code_image".getBytes();
        when(qrCodeServiceMock.generateQRCode(anyString())).thenReturn(mockedQRCodeImage);
        PharmacyInventory testInventory=new PharmacyInventory(name,unitCost,,quantity,);
        when(pharmacyInventoryDAOMock.save(any())).thenReturn()
        // Call the service method
        PharmacyInventory createdInventory = pharmacyInventoryServiceMock.createInventory(name, quantity, unitCost, exDate);

        // Verify the DAO method is called with the expected inventory object
       verify(pharmacyInventoryDAOMock).save(createdInventory);

        assertNotNull(createdInventory);
        assertEquals(name, createdInventory.getName());
        assertEquals(quantity, createdInventory.getRQuantity());
        assertEquals(unitCost, createdInventory.getUnitCost());
        // Add more assertions for other fields as needed
    }

    @Test
    void testUpdateQuantity() {
        // Create a mock PharmacyInventory object
        PharmacyInventory order = new PharmacyInventory();
        order.setRQuantity(10); // Initial quantity

        // Call the method to be tested
        PharmacyInventoryService pharmacyInventoryService = new PharmacyInventoryService();
        int salesQuantity = 3;
        PharmacyInventory updatedOrder = pharmacyInventoryService.updateQuantity(order, salesQuantity);

        // Verify the updated quantity
        assertNotNull(updatedOrder);
        assertEquals(7, updatedOrder.getRQuantity());
    }

    @Test
    void testUpdateInventoryBasedOnQRCode_ExistingInventory() {
        
        // Mock dependencies
        PharmacyInventoryDAO pharmacyInventoryDAOMock = mock(PharmacyInventoryDAO.class);
        QRCodeService qrCodeServiceMock=mock(QRCodeService.class);
        Logger loggerMock = mock(Logger.class);
        PharmacyInventoryService pharmacyInventoryService = new PharmacyInventoryService(qrCodeServiceMock,pharmacyInventoryDAOMock,loggerMock);

        // Create test data
        byte[] qrCodeImage = "mocked_qr_code_image".getBytes();
        PharmacyInventoryDTO inventoryDTO = new PharmacyInventoryDTO();
        inventoryDTO.setUnitCost(5.0);
        inventoryDTO.setRQuantity(10);
        inventoryDTO.setExDate(LocalDate.of(2023, 12, 31));
        inventoryDTO.setName("Test Item");

        PharmacyInventory existingInventory = new PharmacyInventory();
        when(pharmacyInventoryDAOMock.findByQrCodeImage(qrCodeImage)).thenReturn(existingInventory);

        // Call the method
        PharmacyInventory updatedInventory = pharmacyInventoryService.updateInventoryBasedOnQRCode(qrCodeImage, inventoryDTO);

        // Verify the interactions and changes
        verify(loggerMock).info("existing item is found" + existingInventory.toString());
        verify(pharmacyInventoryDAOMock).save(existingInventory);
        assertEquals(inventoryDTO.getRQuantity(), updatedInventory.getRQuantity());
        assertEquals(inventoryDTO.getExDate(), updatedInventory.getExDate());
        assertEquals(inventoryDTO.getName(), updatedInventory.getName());
        assertEquals(inventoryDTO.getUnitCost() * 1.25, updatedInventory.getUnitPrice());
        assertEquals(qrCodeImage, updatedInventory.getQrCodeImage());
    }

    @Test
    void testUpdateInventoryBasedOnQRCode_NonExistingInventory() {
        // Mock dependencies
        PharmacyInventoryDAO pharmacyInventoryDAOMock = mock(PharmacyInventoryDAO.class);
        Logger loggerMock = mock(Logger.class);
        QRCodeService qrCodeServiceMock=mock(QRCodeService.class);
        PharmacyInventoryService pharmacyInventoryService = new PharmacyInventoryService(qrCodeServiceMock,pharmacyInventoryDAOMock,loggerMock);

        // Create test data
        byte[] qrCodeImage = "mocked_qr_code_image".getBytes();
        PharmacyInventoryDTO inventoryDTO = new PharmacyInventoryDTO();

        when(pharmacyInventoryDAOMock.findByQrCodeImage(qrCodeImage)).thenReturn(null);

        // Call the method and verify the exception
        assertThrows(RuntimeException.class, () -> pharmacyInventoryService.updateInventoryBasedOnQRCode(qrCodeImage, inventoryDTO));

        // Verify no interactions with DAO or logger
        verify(loggerMock).info(any());
        verify(pharmacyInventoryDAOMock).findByQrCodeImage(qrCodeImage);
        verify(pharmacyInventoryDAOMock).save(any());
    }

    @Test
    void testGetInventoriesWithQuantityBelowThreshold() {
        int threshold = 10;
        List<PharmacyInventory> expectedInventories = Arrays.asList(new PharmacyInventory(), new PharmacyInventory());

        when(pharmacyInventoryDAOMock.findInventoriesWithQuantityBelowThreshold(threshold)).thenReturn(expectedInventories);

        List<PharmacyInventory> result = pharmacyInventoryService.getInventoriesWithQuantityBelowThreshold(threshold);

        assertEquals(expectedInventories, result);
        verify(loggerMock).info("entered quantity is " + threshold);
    }

    @Test
    void testFindInventoryByExpiryDateThreshold() {
        LocalDate thresholdDate = LocalDate.of(2024, 3, 15);
        List<PharmacyInventory> expectedInventories = Arrays.asList(new PharmacyInventory(), new PharmacyInventory());

        when(pharmacyInventoryDAOMock.findInventoryByExpiryDateThreshold(thresholdDate)).thenReturn(expectedInventories);

        List<PharmacyInventory> result = pharmacyInventoryService.findInventoryByExpiryDateThreshold(thresholdDate);

        assertEquals(expectedInventories, result);
    }

    @Test
    void testFindByInventoryId() {
        int id = 123;
        PharmacyInventory expectedInventory = new PharmacyInventory();

        when(pharmacyInventoryDAOMock.findById(id)).thenReturn(java.util.Optional.of(expectedInventory));

        PharmacyInventory result = pharmacyInventoryService.findByInventoryId(id);

        assertEquals(expectedInventory, result);
    }

    @Test
    void testGetAllInventoryItems() {
        List<PharmacyInventory> expectedInventories = Arrays.asList(new PharmacyInventory(), new PharmacyInventory());

        when(pharmacyInventoryDAOMock.findAll()).thenReturn(expectedInventories);

        List<PharmacyInventory> result = pharmacyInventoryService.getAllInventoryItems();

        assertEquals(expectedInventories, result);
    }

    @Test
    void testQrCode() {
        int id = 123;
        byte[] expectedQrCode = "mocked_qr_code_image".getBytes();
        PharmacyInventory inventoryMock = mock(PharmacyInventory.class);

        when(pharmacyInventoryDAOMock.findById(id)).thenReturn(java.util.Optional.of(inventoryMock));
        when(inventoryMock.getQrCodeImage()).thenReturn(expectedQrCode);

        byte[] result = pharmacyInventoryService.qrCode(id);

        assertArrayEquals(expectedQrCode, result);
    }
}