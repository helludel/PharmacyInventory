package com.pharmacynew.hanaelnael.Controll;

import com.pharmacynew.hanaelnael.DTO.SalesCreateDTO;
import com.pharmacynew.hanaelnael.DTO.SalesDTO;
import com.pharmacynew.hanaelnael.Entity.Sales;
import com.pharmacynew.hanaelnael.Service.PharmacyInventoryService;
import com.pharmacynew.hanaelnael.Service.SalesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class SalesControllerTest {

    @Mock
    private SalesService salesService;

    @Mock
    private PharmacyInventoryService pharmacyInventoryService;

    @InjectMocks
    private SalesController salesController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(salesController).build();
    }
    @Test
    public void testShowCreateSaleForm() throws Exception {
        Model model = Mockito.mock(Model.class);
        String viewName = salesController.showCreateSaleForm(model);

        assertEquals("createSalesForm", viewName); // Ensure this matches the new view name

        ArgumentCaptor<SalesDTO> captor = ArgumentCaptor.forClass(SalesDTO.class);
        verify(model).addAttribute(Mockito.eq("salesDTO"), captor.capture());

        SalesDTO capturedSalesDTO = captor.getValue();
        SalesDTO expectedSalesDTO = new SalesDTO(0, 0.0, 0.0, null); // Adjust with actual constructor arguments

        assertEquals(expectedSalesDTO.getSalesQuantity(), capturedSalesDTO.getSalesQuantity());
        double delta = 0.0001;
        assertEquals(expectedSalesDTO.getUnitPrice(), capturedSalesDTO.getUnitPrice(), delta);
        assertEquals(expectedSalesDTO.getTotalPrice(), capturedSalesDTO.getTotalPrice(), delta);

        assertEquals(expectedSalesDTO.getInventory(), capturedSalesDTO.getInventory());

        // MockMvc test
        mockMvc.perform(get("/Pharma/showCreateSalesForm"))
                .andExpect(status().isOk())
                .andExpect(view().name("createSalesForm")); // Ensure this matches the new view name
    }
    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(salesController).build();
    }

    @Test
    public void testCreateSalesSuccess() throws Exception {
        // Mock the MultipartFile
        MockMultipartFile mockFile = new MockMultipartFile("qrCodeImage", "qrCodeImage.png", "image/png", "sampleImage".getBytes());

        // Mock the SalesService behavior
        SalesCreateDTO salesCreateDTO = new SalesCreateDTO();
        Sales newSales = new Sales();
        when(salesService.createSales(any(SalesCreateDTO.class), any(byte[].class))).thenReturn(newSales);

        // Perform the POST request
        mockMvc.perform(multipart("/Pharma/createSales")
                        .file(mockFile)
                        .param("salesQuantity", "10") // Example parameter, adjust as necessary
                        .param("unitPrice", "100.0")  // Example parameter, adjust as necessary
                        .param("inventoryId", "1"))   // Example parameter, adjust as necessary
                .andExpect(status().isOk())
                .andExpect(view().name("success"));

        // Verify interactions
        verify(salesService, times(1)).createSales(any(SalesCreateDTO.class), any(byte[].class));
    }

    @Test
    public void testCreateSalesError() throws Exception {
        // Mock the MultipartFile
        MockMultipartFile mockFile = new MockMultipartFile("qrCodeImage", "qrCodeImage.png", "image/png", "sampleImage".getBytes());

        // Mock the SalesService to throw an exception
        when(salesService.createSales(any(SalesCreateDTO.class), any(byte[].class))).thenThrow(new RuntimeException("Item doesn't exist"));

        // Perform the POST request
        mockMvc.perform(multipart("/Pharma/createSales")
                        .file(mockFile)
                        .param("salesQuantity", "10") // Example parameter, adjust as necessary
                        .param("unitPrice", "100.0")  // Example parameter, adjust as necessary
                        .param("inventoryId", "1"))   // Example parameter, adjust as necessary
                .andExpect(status().isOk())
                .andExpect(view().name("errorView"))
                .andExpect(model().attributeExists("errorMessage"))
                .andExpect(model().attribute("errorMessage", "The item doesn't exist"));

        // Verify interactions
        verify(salesService, times(1)).createSales(any(SalesCreateDTO.class), any(byte[].class));
    }
    @Test
    public void testShoeTotalSaleForm() throws Exception {
        mockMvc.perform(get("/Pharma/totalSaleQr"))
                .andExpect(status().isOk())
                .andExpect(view().name("totalSale"));
    }

    @Test
    public void testTotalSalesSuccess() throws Exception {
        // Prepare mock file
        byte[] fileContent = "dummy content".getBytes();
        MockMultipartFile mockFile = new MockMultipartFile("qrCodeImage", "qrCodeImage.png", "image/png", fileContent);

        // Mock the service behavior
        double totalSale = 100.0;
        when(salesService.totalSales(any(byte[].class))).thenReturn(totalSale);

        // Perform the POST request
        mockMvc.perform(multipart("/Pharma/totalSales").file(mockFile))
                .andExpect(status().isOk())
                .andExpect(view().name("totalSaleResult"))
                .andExpect(model().attributeExists("totalSale"))
                .andExpect(model().attribute("totalSale", totalSale));
    }

    @Test
    public void testTotalSalesError() throws Exception {
        // Prepare mock file
        byte[] fileContent = "dummy content".getBytes();
        MockMultipartFile mockFile = new MockMultipartFile("qrCodeImage", "qrCodeImage.png", "image/png", fileContent);

        // Mock the service to throw an exception
        when(salesService.totalSales(any(byte[].class))).thenThrow(new RuntimeException("Service error"));

        // Perform the POST request
        mockMvc.perform(multipart("/Pharma/totalSales").file(mockFile))
                .andExpect(status().isOk())
                .andExpect(view().name("errorView"))
                .andExpect(model().attributeExists("error"))
                .andExpect(model().attribute("error", "An error occurred while processing the request."));
    }
    @Test
    public void testShowDailySaleForm() throws Exception {
        mockMvc.perform(get("/Pharma/dailySaleForm"))
                .andExpect(status().isOk())
                .andExpect(view().name("saleForm"));
    }
    @Test
    public void testGetDailySales() throws Exception {
        // Mock data
        LocalDate saleDate = LocalDate.now(); // Or any other date you want to test with
        List<Sales> salesRecords = Collections.singletonList(new Sales()); // Mock sales records

        // Mock service behavior
        when(salesService.getDailySales(saleDate)).thenReturn(salesRecords);

        // Perform the GET request with parameters
        mockMvc.perform(get("/Pharma/dailySale")
                        .param("saleDate", saleDate.toString())) // Convert LocalDate to string
                .andExpect(status().isOk())
                .andExpect(view().name("salesRecords"))
                .andExpect(model().attributeExists("salesRecords"))
                .andExpect(model().attribute("salesRecords", salesRecords));
    }
}
