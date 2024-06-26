package com.pharmacynew.hanaelnael.Service;

import com.pharmacynew.hanaelnael.DAO.PharmacyInventoryDAO;
import com.pharmacynew.hanaelnael.DAO.SalesDAO;
import com.pharmacynew.hanaelnael.DTO.SalesCreateDTO;
import com.pharmacynew.hanaelnael.Entity.PharmacyInventory;
import com.pharmacynew.hanaelnael.Entity.Sales;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class SalesService {
    PharmacyInventory pharmacyInventory;
    @Autowired
    PharmacyInventoryDAO pharmacyInventoryDAO;
    @Autowired
    SalesDAO salesDAO;
    private static final Logger logger = LoggerFactory.getLogger(SalesService.class);


    public PharmacyInventory updateQuantity(PharmacyInventory Order, int salesQuantity) {
        int rQuantity = Order.getRQuantity( );
        int netQuantity = rQuantity - salesQuantity;
        Order.setRQuantity(netQuantity);
        logger.info("old quantity "+rQuantity+"new quantity "+netQuantity);
        return Order;
    }

    public Sales createSales(SalesCreateDTO dto,byte[] qrCodeImage) {
        PharmacyInventory inventory=pharmacyInventoryDAO.findByQrCodeImage(qrCodeImage);
        logger.info("item found for sale "+inventory.toString());
        if (inventory.getRQuantity( ) >= dto.getSalesQuantity()) {
            double unitPrice = inventory.getUnitPrice();
            double totalPrice = dto.getSalesQuantity() * unitPrice;
            updateQuantity (inventory, dto.getSalesQuantity());
            Sales newSale = new Sales(dto.getSalesQuantity(), totalPrice, inventory,dto.getSaleDate());
            newSale.setSaleDate(LocalDate.now());
           Sales savedSales= salesDAO.save(newSale);
            logger.info("the new sales is "+newSale.toString());
            logger.info("requested sale is "+dto.toString());
            return savedSales;
        }
         else {
             logger.warn("Oops not enough quantity to process the transaction!!! ");
            throw new RuntimeException("Oops not enough quantity to process the transaction!!! ");
        }
    }


    public double totalSales( byte[] qrCodeImage) {
       try{ PharmacyInventory inventory=pharmacyInventoryDAO.findByQrCodeImage(qrCodeImage);
        List<Sales> listSales = salesDAO.findByInventoryId(inventory.getId());
        double unitPrice = inventory.getUnitPrice( );
        ;
        int totalQuantity = 0;

        for (Sales s : listSales) {
            totalQuantity = totalQuantity + s.getSalesQuantity( );
        }
        double totalSale = totalQuantity * unitPrice;
        return totalSale;

    }catch (Exception e){ logger.error("error occurred while getting total sale "+e.getMessage(),e);
       throw e;
       }
    }
    public List<Sales>getDailySales(LocalDate saleDate){
        return salesDAO.findBySaleDate(saleDate);
    }
}