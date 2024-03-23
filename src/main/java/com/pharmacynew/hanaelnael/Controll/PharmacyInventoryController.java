package com.pharmacynew.hanaelnael.Controll;

import com.pharmacynew.hanaelnael.DTO.PharmacyInventoryDTO;
import com.pharmacynew.hanaelnael.Entity.PharmacyInventory;
import com.pharmacynew.hanaelnael.Service.PharmacyInventoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/Pharma")
public class PharmacyInventoryController {
    private static final Logger logger = LoggerFactory.getLogger(PharmacyInventoryController.class);
    @Autowired
    PharmacyInventoryService pharmacyInventoryService;

    @GetMapping("/showCreateInventoryForm")
    public String showCreateInventoryForm(Model model) {
        model.addAttribute("inventoryDTO", new PharmacyInventoryDTO( ));
        return "showCreateInventoryForm";
    }

    @PostMapping("/createInventory")
    public String createInventory(@ModelAttribute PharmacyInventoryDTO inventoryDTO) {
        // Logic to create the inventory
        pharmacyInventoryService.createInventory(

                inventoryDTO.getName( ),
                inventoryDTO.getRQuantity( ),
                inventoryDTO.getUnitCost( ),
                inventoryDTO.getExDate( )
        );
        return "success";
    }
    @GetMapping("/lowStockForm")
        public String showLowStockForm(){
        return"lowStockForm";
    }
    @GetMapping("/listLowStock")
    public String getInventoriesWithQuantityBelowThreshold(Model model,@RequestParam("threshold") int threshold) {
        logger.info("low stock inventory is needed below"+threshold );
        List<PharmacyInventory> lowStockInventories =
                pharmacyInventoryService.getInventoriesWithQuantityBelowThreshold(threshold);
        model.addAttribute("lowStockInventories", lowStockInventories);
        logger.info("low stock inventory list is needed below a quantity of " + threshold);
        return "lowStockInventoryList";
    }
    @GetMapping("/nearExpiryForm")
    public String showNearExpiryForm(){
        return"nearExpiryForm";
    }


    @GetMapping("/nearExpiryInventoryList")
    public String findInventoryByExpiryDateThreshold(Model model,@RequestParam("thresholdDate") LocalDate thresholdDate) {
        List<PharmacyInventory> nearExpiryResult =
                pharmacyInventoryService.findInventoryByExpiryDateThreshold(thresholdDate);
        logger.info("near expiry items before date of " + thresholdDate);
        model.addAttribute("nearExpiryResult", nearExpiryResult);
        return "nearExpiryResult";
    }

    @GetMapping("/showUpdateInventoryForm/{Id}")
    public String showUpdateInventoryForm(Model model, @PathVariable("Id") int Id) {
        try {
            PharmacyInventory existingInventory = pharmacyInventoryService.findByInventoryId(Id);

            if (existingInventory != null) {
                model.addAttribute("inventory", existingInventory);
                logger.info("Existing Inventory: {}", existingInventory);
                return "showUpdateInventoryForm"; // Assuming the name of the form view
            } else {
                return "error";
            }
        } catch (Exception e) {
            // Log the exception
            logger.error("An error occurred while fetching the existing inventory:", e);

            // Show an error view indicating that an unexpected error occurred
            return "error";
        }
    }

    @PostMapping("/updateInventory/{Id}")
    public String updateInventory(@PathVariable int Id, @ModelAttribute PharmacyInventoryDTO dto, Model model) {
        model.getAttribute("dynamicValue"
        );
        pharmacyInventoryService.updateInventory(dto, Id);
        return "redirect:/showUpdateInventoryForm";
    }

    @GetMapping("/listInventory")
    public String listOfInventory(Model model) {
        List<PharmacyInventory> inventoryList = pharmacyInventoryService.getAllInventoryItems( );
        model.addAttribute("inventoryList", inventoryList);
        return "listInventory";
    }

    @GetMapping(value = "/{id}/qrCode", produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] getInventoryQRCode(@PathVariable int id) {
        PharmacyInventory inventory = pharmacyInventoryService.findByInventoryId(id);
        byte[] qrCodeImage = pharmacyInventoryService.qrCode(id);

        return qrCodeImage;
    }

    @GetMapping("/showUpdateInventoryForm")
    public String showUpdateInventoryForm(Model model) {
        model.addAttribute("inventoryDTO", new PharmacyInventoryDTO( ));
        return "showUpdateInventoryForm";
    }

    @PostMapping("/updateInventoryFromQRCode")
    public String updateInventoryFromQRCode(Model model ,@RequestParam("qrCodeImage") MultipartFile qrCodeImageFile,
                                            @ModelAttribute PharmacyInventoryDTO inventoryDTO
    ) {
        try {
        byte[] qrCodeImage = qrCodeImageFile.getBytes();

            PharmacyInventory updatedInventory = pharmacyInventoryService.updateInventoryBasedOnQRCode(qrCodeImage, inventoryDTO);
           logger.info("item update information is received for "+inventoryDTO.toString());

            return "showUpdateInventoryForm";
        } catch (Exception e) {

            e.printStackTrace();

            model.addAttribute("errorMessage", "The item doesn't exist");

            return "errorView";
        }
        }
    }






