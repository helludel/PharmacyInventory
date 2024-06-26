package com.pharmacynew.hanaelnael.Controll;

import com.pharmacynew.hanaelnael.DTO.PharmacyInventoryDTO;
import com.pharmacynew.hanaelnael.Entity.PharmacyInventory;
import com.pharmacynew.hanaelnael.Service.PharmacyInventoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class PharmacyInventoryControllerTest {

    @Mock
    private PharmacyInventoryService pharmacyInventoryService;

    @InjectMocks
    private PharmacyInventoryController pharmacyInventoryController;

    @Mock
    private MultipartFile qrCodeImageFile;

    @Mock
    private Model model;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testShowCreateInventoryForm() {
        String viewName = pharmacyInventoryController.showCreateInventoryForm(model);
        assertEquals("showCreateInventoryForm", viewName);
        verify(model).addAttribute(eq("inventoryDTO"), any(PharmacyInventoryDTO.class));
    }
    @Test
    public void testCreateInventory() {
        PharmacyInventoryDTO inventoryDTO = new PharmacyInventoryDTO( );
        inventoryDTO.setName("Test Medicine");
        inventoryDTO.setRQuantity(100);
        inventoryDTO.setUnitCost(10.0);
        inventoryDTO.setExDate(LocalDate.parse("2023-12-31"));

        String result = pharmacyInventoryController.createInventory(inventoryDTO);

        verify(pharmacyInventoryService, times(1)).createInventory(
                inventoryDTO.getName( ),
                inventoryDTO.getRQuantity( ),
                inventoryDTO.getUnitCost( ),
                inventoryDTO.getExDate( )
        );

        assertEquals("success", result);
    }

    @Test
    public void testShowLowStockForm() {
        String viewName = pharmacyInventoryController.showLowStockForm( );
        assertEquals("lowStockForm", viewName);
    }

    @Test
    public void testGetInventoriesWithQuantityBelowThreshold() {
        int threshold = 10;
        List<PharmacyInventory> lowStockInventories = new ArrayList<>( );
        // add inventory items to the lowStockInventories list

        when(pharmacyInventoryService.getInventoriesWithQuantityBelowThreshold(threshold))
                .thenReturn(lowStockInventories);

        String viewName = pharmacyInventoryController.getInventoriesWithQuantityBelowThreshold(model, threshold);
        assertEquals("lowStockInventoryList", viewName);
        verify(model).addAttribute("lowStockInventories", lowStockInventories);
    }

    @Test
    public void testShowNearExpiryForm() {
        String viewName = pharmacyInventoryController.showNearExpiryForm( );
        assertEquals("nearExpiryForm", viewName);
    }

    @Test
    void testFindInventoryByExpiryDateThreshold() {
        // Create a test LocalDate thresholdDate
        LocalDate thresholdDate = LocalDate.now().plusDays(7); // Assuming threshold date is 7 days from now

        // Create a list of PharmacyInventory objects for testing
        List<PharmacyInventory> mockInventoryList = new ArrayList<>();
        // Add some mock inventory items to the list
        // Add logic here to populate mockInventoryList with sample data for testing

        // Mock the behavior of pharmacyInventoryService.findInventoryByExpiryDateThreshold method
        when(pharmacyInventoryService.findInventoryByExpiryDateThreshold(any(LocalDate.class)))
                .thenReturn(mockInventoryList);

        // Call the controller method
        String viewName = pharmacyInventoryController.findInventoryByExpiryDateThreshold(model, thresholdDate);

        // Verify that the service method was called with the correct argument
        verify(pharmacyInventoryService).findInventoryByExpiryDateThreshold(eq(thresholdDate));

        // Verify that the model attribute was set with the correct name and value
        verify(model).addAttribute(eq("nearExpiryResult"), eq(mockInventoryList));

        // Assert that the view name returned by the controller method matches the expected view name
        assertEquals("nearExpiryResult", viewName);
    }

    @Test
    void testShowUpdateInventoryForm() {
        // Define test data
        int inventoryId = 1; // Replace with a valid inventory ID for testing
        PharmacyInventory mockInventory = new PharmacyInventory(); // Replace with a mock PharmacyInventory object

        // Mock the behavior of pharmacyInventoryService.findByInventoryId method
        when(pharmacyInventoryService.findByInventoryId(inventoryId)).thenReturn(mockInventory);

        // Call the controller method
        String viewName = pharmacyInventoryController.showUpdateInventoryForm(model, inventoryId);

        // Verify that the service method was called with the correct argument
        verify(pharmacyInventoryService).findByInventoryId(inventoryId);

        // If the existing inventory is not null, verify that the model attribute was set with the correct name and value
        if (mockInventory != null) {
            verify(model).addAttribute(eq("inventory"), eq(mockInventory));
            // Assert that the view name returned by the controller method matches the expected view name
            assertEquals("showUpdateInventoryForm", viewName);
        } else {
            // Assert that the view name returned by the controller method matches the expected view name
            assertEquals("error", viewName);
        }
    }

    @Test
    void testShowUpdateInventoryFormWithNullInventory() {
        // Define test data
        int inventoryId = 1; // Replace with a valid inventory ID for testing

        // Mock the behavior of pharmacyInventoryService.findByInventoryId method to return null
        when(pharmacyInventoryService.findByInventoryId(inventoryId)).thenReturn(null);

        // Call the controller method
        String viewName = pharmacyInventoryController.showUpdateInventoryForm(model, inventoryId);

        // Verify that the service method was called with the correct argument
        verify(pharmacyInventoryService).findByInventoryId(inventoryId);

        // Assert that the view name returned by the controller method matches the expected view name
        assertEquals("error", viewName);
    }

    @Test
    void testShowUpdateInventoryFormWithException() {
        // Define test data
        int inventoryId = 1; // Replace with a valid inventory ID for testing

        // Mock the behavior of pharmacyInventoryService.findByInventoryId method to throw an exception
        when(pharmacyInventoryService.findByInventoryId(inventoryId)).thenThrow(new RuntimeException("Database error"));

        // Call the controller method
        String viewName = pharmacyInventoryController.showUpdateInventoryForm(model, inventoryId);

        // Verify that the service method was called with the correct argument
        verify(pharmacyInventoryService).findByInventoryId(inventoryId);

        // Assert that the view name returned by the controller method matches the expected view name
        assertEquals("error", viewName);
    }
    @Test
    void testUpdateInventory() {
        // Define test data
        int inventoryId = 1; // Replace with a valid inventory ID for testing
        PharmacyInventoryDTO inventoryDTO = new PharmacyInventoryDTO();
        inventoryDTO.setName("Test Medicine");
        inventoryDTO.setRQuantity(100);
        inventoryDTO.setUnitCost(10.0);
        inventoryDTO.setExDate(LocalDate.parse("2023-12-31"));

        // Mock the behavior of model.getAttribute method
        when(model.getAttribute("dynamicValue")).thenReturn(null);

        // Call the controller method
        String viewName = pharmacyInventoryController.updateInventory(inventoryId, inventoryDTO, model);

        // Verify that the service method was called with the correct arguments
        verify(pharmacyInventoryService).updateInventory(inventoryDTO, inventoryId);

        // Assert that the view name returned by the controller method matches the expected view name
        assertEquals("redirect:/showUpdateInventoryForm", viewName);
    }
    @Test
    void testListOfInventory() {
        // Define test data
        List<PharmacyInventory> inventoryList = new ArrayList<>();
        inventoryList.add(new PharmacyInventory());
        inventoryList.add(new PharmacyInventory());

        // Mock the behavior of the service method
        when(pharmacyInventoryService.getAllInventoryItems()).thenReturn(inventoryList);

        // Call the controller method
        String viewName = pharmacyInventoryController.listOfInventory(model);

        // Verify that the service method was called
        verify(pharmacyInventoryService).getAllInventoryItems();

        // Verify that the model attribute was set
        verify(model).addAttribute("inventoryList", inventoryList);

        // Assert that the view name returned by the controller method matches the expected view name
        assertEquals("listInventory", viewName);
    }
    @Test
    void testGetInventoryQRCode() {
        // Define test data
        int inventoryId = 1; // Replace with a valid inventory ID for testing
        PharmacyInventory inventory = new PharmacyInventory();
        byte[] qrCodeImage = new byte[]{1, 2, 3}; // Example QR code image byte array

        // Mock the behavior of the service methods
        when(pharmacyInventoryService.findByInventoryId(inventoryId)).thenReturn(inventory);
        when(pharmacyInventoryService.qrCode(inventoryId)).thenReturn(qrCodeImage);

        // Call the controller method
        byte[] result = pharmacyInventoryController.getInventoryQRCode(inventoryId);

        // Verify that the service methods were called with the correct arguments
        verify(pharmacyInventoryService).findByInventoryId(inventoryId);
        verify(pharmacyInventoryService).qrCode(inventoryId);

        // Assert that the result matches the expected QR code image byte array
        assertArrayEquals(qrCodeImage, result);
    }
    @Test
    void testUpdateInventoryFromQRCodeSuccess() throws Exception {
        // Define test data
        PharmacyInventoryDTO inventoryDTO = new PharmacyInventoryDTO();
        byte[] qrCodeImage = new byte[]{1, 2, 3}; // Example QR code image byte array

        // Mock the behavior of the MultipartFile
        when(qrCodeImageFile.getBytes()).thenReturn(qrCodeImage);

        // Mock the behavior of the service method
        PharmacyInventory updatedInventory = new PharmacyInventory();
        when(pharmacyInventoryService.updateInventoryBasedOnQRCode(qrCodeImage, inventoryDTO)).thenReturn(updatedInventory);

        // Call the controller method
        String viewName = pharmacyInventoryController.updateInventoryFromQRCode(model, qrCodeImageFile, inventoryDTO);

        // Verify that the service method was called with the correct arguments
        verify(pharmacyInventoryService).updateInventoryBasedOnQRCode(qrCodeImage, inventoryDTO);

        // Assert that the view name is correct
        assertEquals("showUpdateInventoryForm", viewName);
    }

    @Test
    void testUpdateInventoryFromQRCodeError() throws Exception {
        // Define test data
        PharmacyInventoryDTO inventoryDTO = new PharmacyInventoryDTO();
        byte[] qrCodeImage = new byte[]{1, 2, 3}; // Example QR code image byte array

        // Mock the behavior of the MultipartFile
        when(qrCodeImageFile.getBytes()).thenReturn(qrCodeImage);

        // Mock the behavior of the service method to throw an exception
        when(pharmacyInventoryService.updateInventoryBasedOnQRCode(qrCodeImage, inventoryDTO)).thenThrow(new RuntimeException("The item doesn't exist"));

        // Call the controller method
        String viewName = pharmacyInventoryController.updateInventoryFromQRCode(model, qrCodeImageFile, inventoryDTO);

        // Verify that the service method was called with the correct arguments
        verify(pharmacyInventoryService).updateInventoryBasedOnQRCode(qrCodeImage, inventoryDTO);

        // Verify that the error message was added to the model
        verify(model).addAttribute("errorMessage", "The item doesn't exist");

        // Assert that the view name is correct
        assertEquals("errorView", viewName);
    }

}