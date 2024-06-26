package com.pharmacynew.hanaelnael.Service;

import com.pharmacynew.hanaelnael.DAO.PharmacyInventoryDAO;
import com.pharmacynew.hanaelnael.DTO.PharmacyInventoryDTO;
import com.pharmacynew.hanaelnael.Entity.PharmacyInventory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PharmacyInventoryServiceTest {
    @Mock
    private PharmacyInventoryDAO pharmacyInventoryDAO;
    @Mock
    private Logger logger;
    @Mock
    private QRCodeService qrCodeService;


    @InjectMocks
    private PharmacyInventoryService pharmacyInventoryService;

    private PharmacyInventory existingInventory;
    private PharmacyInventory newInventory;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateInventory_NewInventory_Success() {
        // Setup test data
        String name = "NewMed";
        int quantity = 200;
        double unitCost = 30.0;
        LocalDate exDate = LocalDate.now().plusDays(60);
        byte[] qrCodeImage = new byte[]{1, 2, 3}; // Example QR code image data

        // Create new inventory object with QR code image
        PharmacyInventory newInventory = new PharmacyInventory();
        newInventory.setQrCodeImage(qrCodeImage);

        // Mocking the DAO and service methods
        when(pharmacyInventoryDAO.findByName(name)).thenReturn(null);
        when(pharmacyInventoryDAO.save(any(PharmacyInventory.class))).thenAnswer(invocation -> {
            PharmacyInventory inventory = invocation.getArgument(0);
            return inventory;
        });

        // Mock the QR code service to return the pre-defined QR code image
        when(qrCodeService.generateQRCode(anyString())).thenReturn(qrCodeImage);

        // Call the method under test
        PharmacyInventory result = pharmacyInventoryService.createInventory(name, quantity, unitCost, exDate);

        // Verify the result
        assertNotNull(result);
        assertEquals(name, result.getName());
        assertEquals(quantity, result.getRQuantity());
        assertEquals(unitCost, result.getUnitCost(), 0.001);
        assertEquals(37.5, result.getUnitPrice(), 0.001); // Assuming unit price is calculated and should be 37.5
        assertEquals(exDate, result.getExDate());
        assertArrayEquals(qrCodeImage, result.getQrCodeImage());

        // Capture the saved PharmacyInventory object
        ArgumentCaptor<PharmacyInventory> inventoryCaptor = ArgumentCaptor.forClass(PharmacyInventory.class);
        verify(pharmacyInventoryDAO).save(inventoryCaptor.capture());
        PharmacyInventory savedInventory = inventoryCaptor.getValue();

        // Verify that the saved inventory has the correct details
        assertNotNull(savedInventory);
        assertEquals(name, savedInventory.getName());
        assertEquals(quantity, savedInventory.getRQuantity());
        assertEquals(unitCost, savedInventory.getUnitCost(), 0.001);
        assertEquals(37.5, savedInventory.getUnitPrice(), 0.001);
        assertEquals(exDate, savedInventory.getExDate());
        assertArrayEquals(qrCodeImage, savedInventory.getQrCodeImage());

        // Verify the interactions with the mocks
        verify(pharmacyInventoryDAO).findByName(name);
        verify(pharmacyInventoryDAO).save(any(PharmacyInventory.class));
        verify(qrCodeService, times(1)).generateQRCode(anyString()); // Allow the QR code to be generated only once
    }

    @Test
    void testCreateInventory_ExistingInventory_ExceptionThrown() {
        // Define the input parameters for the inventory item
        String name = "TestMed";
        int quantity = 100;
        double unitCost = 50.0;
        LocalDate exDate = LocalDate.now().plusDays(30);

        // Mock the behavior of the DAO to return an existing inventory item when searched by name
        PharmacyInventory existingInventory = new PharmacyInventory(); // Mock existing inventory
        when(pharmacyInventoryDAO.findByName(name)).thenReturn(existingInventory);

        // Assert that the createInventory method throws a RuntimeException
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            pharmacyInventoryService.createInventory(name, quantity, unitCost, exDate);
        });

        // Check that the exception message is as expected
        assertEquals("item exists update?", exception.getMessage());

        // Verify that the findByName method was called
        verify(pharmacyInventoryDAO).findByName(name);

        // Verify that the save method was never called
        verify(pharmacyInventoryDAO, never()).save(any(PharmacyInventory.class));

        // Verify that the generateQRCode method was never called
        verify(qrCodeService, never()).generateQRCode(any(String.class));
    }
    @Test
    void testUpdateQuantity() {
        // Create a mock PharmacyInventory object
        PharmacyInventory order = new PharmacyInventory();
        order.setRQuantity(10); // Initial quantity

        // Call the method to be tested
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
        QRCodeService qrCodeServiceMock = mock(QRCodeService.class);

        // Create an instance of the service class
        PharmacyInventoryService pharmacyInventoryService = new PharmacyInventoryService(pharmacyInventoryDAOMock, qrCodeServiceMock);

        byte[] qrCodeImage = "mocked_qr_code_image".getBytes();
        PharmacyInventoryDTO inventoryDTO = new PharmacyInventoryDTO();
        inventoryDTO.setUnitCost(5.0);
        inventoryDTO.setRQuantity(10);
        inventoryDTO.setExDate(LocalDate.of(2023, 12, 31));
        inventoryDTO.setName("Test Item");

        PharmacyInventory existingInventory = new PharmacyInventory();
        existingInventory.setQrCodeImage(qrCodeImage); // Ensure the existing inventory has a QR code image
        when(pharmacyInventoryDAOMock.findByQrCodeImage(qrCodeImage)).thenReturn(existingInventory);

        PharmacyInventory updatedInventory =
                pharmacyInventoryService.updateInventoryBasedOnQRCode(qrCodeImage, inventoryDTO);

        // Capture the argument passed to the save method
        ArgumentCaptor<PharmacyInventory> inventoryCaptor = ArgumentCaptor.forClass(PharmacyInventory.class);
        verify(pharmacyInventoryDAOMock).save(inventoryCaptor.capture());

        PharmacyInventory capturedInventory = inventoryCaptor.getValue();

        assertEquals(inventoryDTO.getRQuantity(), capturedInventory.getRQuantity());
        assertEquals(inventoryDTO.getExDate(), capturedInventory.getExDate());
        assertEquals(inventoryDTO.getName(), capturedInventory.getName());
        // Ensure the unit price is calculated correctly
        assertEquals(inventoryDTO.getUnitCost() * 1.25, capturedInventory.getUnitPrice(), 0.001);
        assertArrayEquals(qrCodeImage, capturedInventory.getQrCodeImage());
    }

    @Test
    void testUpdateInventoryBasedOnQRCode_NonExistingInventory() {
        // Mock dependencies
        PharmacyInventoryDAO pharmacyInventoryDAOMock = mock(PharmacyInventoryDAO.class);
        Logger loggerMock = mock(Logger.class);
        QRCodeService qrCodeServiceMock = mock(QRCodeService.class);

        // Create instance of service with mocks
        PharmacyInventoryService pharmacyInventoryService = new PharmacyInventoryService(qrCodeServiceMock, pharmacyInventoryDAOMock, loggerMock);

        // Create test data
        byte[] qrCodeImage = "mocked_qr_code_image".getBytes();
        PharmacyInventoryDTO inventoryDTO = new PharmacyInventoryDTO();

        // Mock the behavior of the DAO to return null when searching by QR code image
        when(pharmacyInventoryDAOMock.findByQrCodeImage(qrCodeImage)).thenReturn(null);

        // Call the method and verify the exception
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                pharmacyInventoryService.updateInventoryBasedOnQRCode(qrCodeImage, inventoryDTO)
        );

        // Check that the exception message is as expected
        assertEquals("Inventory not found for the provided QR code", exception.getMessage());

        // Verify interactions with the DAO and logger
        verify(pharmacyInventoryDAOMock).findByQrCodeImage(qrCodeImage);
        verify(pharmacyInventoryDAOMock, never()).save(any(PharmacyInventory.class));

    }

    @Test
    void testGetInventoriesWithQuantityBelowThreshold() {
        int threshold = 10;
        List<PharmacyInventory> expectedInventories = Arrays.asList(new PharmacyInventory(), new PharmacyInventory());

        when(pharmacyInventoryDAO.findInventoriesWithQuantityBelowThreshold(threshold)).thenReturn(expectedInventories);

        List<PharmacyInventory> result = pharmacyInventoryService.getInventoriesWithQuantityBelowThreshold(threshold);

        assertEquals(expectedInventories, result);
    }

    @Test
    void testFindInventoryByExpiryDateThreshold() {
        LocalDate thresholdDate = LocalDate.of(2024, 3, 15);
        List<PharmacyInventory> expectedInventories = Arrays.asList(new PharmacyInventory(), new PharmacyInventory());

        when(pharmacyInventoryDAO.findInventoryByExpiryDateThreshold(thresholdDate)).thenReturn(expectedInventories);

        List<PharmacyInventory> result = pharmacyInventoryService.findInventoryByExpiryDateThreshold(thresholdDate);

        assertEquals(expectedInventories, result);
    }

    @Test
    void testFindByInventoryId() {
        int id = 123;
        PharmacyInventory expectedInventory = new PharmacyInventory();

        when(pharmacyInventoryDAO.findById(id)).thenReturn(java.util.Optional.of(expectedInventory));

        PharmacyInventory result = pharmacyInventoryService.findByInventoryId(id);

        assertEquals(expectedInventory, result);
    }

    @Test
    void testGetAllInventoryItems() {
        List<PharmacyInventory> expectedInventories = Arrays.asList(new PharmacyInventory(), new PharmacyInventory());

        when(pharmacyInventoryDAO.findAll()).thenReturn(expectedInventories);

        List<PharmacyInventory> result = pharmacyInventoryService.getAllInventoryItems();

        assertEquals(expectedInventories, result);
    }

    @Test
    void testQrCode() {
        int id = 123;
        byte[] expectedQrCode = "mocked_qr_code_image".getBytes();
        PharmacyInventory inventoryMock = mock(PharmacyInventory.class);

        // Mock dependencies
        PharmacyInventoryDAO pharmacyInventoryDAOMock = mock(PharmacyInventoryDAO.class);
        QRCodeService qrCodeServiceMock = mock(QRCodeService.class);
        Logger loggerMock = mock(Logger.class);

        // Create instance of service with mocks
        PharmacyInventoryService pharmacyInventoryService = new PharmacyInventoryService(qrCodeServiceMock, pharmacyInventoryDAOMock, loggerMock);

        // Set up mocks
        when(pharmacyInventoryDAOMock.findById(id)).thenReturn(java.util.Optional.of(inventoryMock));
        when(inventoryMock.getQrCodeImage()).thenReturn(expectedQrCode);

        // Call the method
        byte[] result = pharmacyInventoryService.qrCode(id);

        // Verify result
        assertArrayEquals(expectedQrCode, result);

        // Capture arguments
        ArgumentCaptor<Integer> idCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(pharmacyInventoryDAOMock).findById(idCaptor.capture());

        // Verify captured arguments
        assertEquals(Integer.valueOf(id), idCaptor.getValue());

    }
}