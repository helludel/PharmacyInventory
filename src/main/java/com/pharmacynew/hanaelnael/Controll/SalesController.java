package com.pharmacynew.hanaelnael.Controll;

import com.pharmacynew.hanaelnael.DTO.SalesCreateDTO;
import com.pharmacynew.hanaelnael.DTO.SalesDTO;
import com.pharmacynew.hanaelnael.Entity.Sales;
import com.pharmacynew.hanaelnael.Service.PharmacyInventoryService;
import com.pharmacynew.hanaelnael.Service.SalesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/Pharma")
public class SalesController {
    private static final Logger logger = LoggerFactory.getLogger(SalesService.class);

    @Autowired
    SalesService salesService;
    @Autowired
    PharmacyInventoryService pharmacyInventoryService;

    @GetMapping("/showCreateSalesForm")
    public String showCreateSaleForm(Model model) {
        model.addAttribute("salesDTO", new SalesDTO( ));
        return "createSalesForm";
    }
    @PostMapping("/createSales")
    public String createSales(Model model, @ModelAttribute SalesCreateDTO salesDTO, @RequestParam ("qrCodeImage" ) MultipartFile qrCodeImageFile) {
        try {
            byte[] qrCodeImage = qrCodeImageFile.getBytes( );
            Sales newSales = salesService.createSales(salesDTO, qrCodeImage);
            logger.info("sale is created for item" + salesDTO.getInventoryId( ), salesDTO);
            return "success";
        } catch (Exception e) {

            e.printStackTrace( );

            model.addAttribute("errorMessage", "The item doesn't exist");

            return "errorView";
        }
    }
    @GetMapping("/totalSaleQr")
    public String shoeTotalSaleForm(){
        return "totalSale";
    }

    @PostMapping("/totalSales")
    public String totalSales( Model model,@RequestParam("qrCodeImage")
    MultipartFile qrCodeFileImage) throws IOException {

        try
        {byte[] qrCodeImage= qrCodeFileImage.getBytes( );
      double totalSale= salesService.totalSales(qrCodeImage);
            model.addAttribute("totalSale", totalSale);

            return "totalSaleResult";
        }catch (Exception e) {

            e.printStackTrace( );
            model.addAttribute("error", "An error occurred while processing the request.");
            return "errorView";
        }
    }
    @GetMapping("/dailySaleForm")
    public String showDailySaleForm(){
        return "saleForm";
    }
    @GetMapping("/dailySale")
    public String getDailySales(Model model,@RequestParam("saleDate") LocalDate saleDate) {
        List<Sales> salesRecords = salesService.getDailySales(saleDate);
        logger.info("the date entered is "+saleDate);
        model.addAttribute("salesRecords", salesRecords);
        return "salesRecords";
    }


}
