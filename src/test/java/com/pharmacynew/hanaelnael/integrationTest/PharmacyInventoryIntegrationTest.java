package com.pharmacynew.hanaelnael.integrationTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class PharmacyInventoryIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testShowCreateInventoryForm() throws Exception {
        mockMvc.perform(get("/inventory/showCreateInventoryForm"))
                .andExpect(status().isOk())
                .andExpect(view().name("showCreateInventoryForm"))
                .andExpect(model().attributeExists("inventoryDTO"));
    }
}





































































































































































