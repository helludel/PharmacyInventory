package com.pharmacynew.hanaelnael.Service;

import com.pharmacynew.hanaelnael.DAO.PharmacyInventoryDAO;
import com.pharmacynew.hanaelnael.DTO.PharmacyInventoryDTO;
import com.pharmacynew.hanaelnael.Entity.PharmacyInventory;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class PharmacyInventoryService {

    private static  Logger logger = LoggerFactory.getLogger(PharmacyInventoryService.class);
    QRCodeService qrCodeService;
    PharmacyInventoryDAO pharmacyInventoryDAO;

    @Autowired
    public PharmacyInventoryService(PharmacyInventoryDAO pharmacyInventoryDAO,QRCodeService qrCodeService) {
        this.pharmacyInventoryDAO = pharmacyInventoryDAO;
        this.qrCodeService = qrCodeService;
    }


    public PharmacyInventoryService(QRCodeService qrCodeService, PharmacyInventoryDAO pharmacyInventoryDAO, Logger loggerMock) {
        this.qrCodeService=qrCodeService;
        this.pharmacyInventoryDAO=pharmacyInventoryDAO;
        this.logger=loggerMock;
    }

   // public PharmacyInventoryService() {

  //  }

    @Transactional
    public PharmacyInventory createInventory(String name,
                                             int quantity, double unitCost, LocalDate exDate) {
        PharmacyInventory existingInventory = pharmacyInventoryDAO.findByName(name);
        if (existingInventory == null) {
            double unitPrice = unitCost * 1.25;
            PharmacyInventory inventory = new PharmacyInventory(name,
                    quantity, unitCost, unitPrice, exDate);

          //  pharmacyInventoryDAO.save(inventory);//

            String qrCodeContent = String.valueOf(inventory.getId( ));

            byte[] qrCodeImage = qrCodeService.generateQRCode(qrCodeContent);
            inventory.setQrCodeImage(qrCodeImage);
          logger.info("entered info is " + " name " + name + " quantity " + quantity + " unit cost  " + unitCost + " expiry date " + exDate);
            logger.info("received inventory is" + inventory.toString( ));
            logger.info("qr content created  "+qrCodeContent);
            logger.info("qr Image is also created "+ qrCodeImage);
            return pharmacyInventoryDAO.save(inventory);
        }else throw new RuntimeException("item exists update?");


    }
    public PharmacyInventory updateQuantity(PharmacyInventory Order, int salesQuantity) {
        int rQuantity = Order.getRQuantity( );
        int netQuantity = rQuantity - salesQuantity;
        Order.setRQuantity(netQuantity);
        return Order;
    }
    public PharmacyInventory updateInventoryBasedOnQRCode(byte[] qrCodeImage,PharmacyInventoryDTO inventoryDTO) {
        PharmacyInventory inventory = pharmacyInventoryDAO.findByQrCodeImage(qrCodeImage);
        logger.info("existing item is found: {}", (inventory == null ? "null" : inventory.toString()));

        if(inventory!=null){
            Double unitPrice = (1.25 * (inventoryDTO.getUnitCost( )));
            inventory.setRQuantity(inventoryDTO.getRQuantity( ));
            inventory.setExDate(inventoryDTO.getExDate( ));
            inventory.setName(inventoryDTO.getName( ));
            inventory.setQrCodeImage(qrCodeImage);
            inventory.setUnitPrice(unitPrice);

            pharmacyInventoryDAO.save(inventory);
            return inventory;
        }else if  (inventory==null)throw new RuntimeException("Inventory not found for the provided QR code");
        else throw new RuntimeException("error occured" );
    }
    @Transactional
    public PharmacyInventory updateInventory(PharmacyInventoryDTO dto, int Id) {
        PharmacyInventory existingInventory = pharmacyInventoryDAO.findById(Id)
                .orElseThrow(() -> new EntityNotFoundException("Inventory with ID " + Id + " not found"));
        existingInventory.setName(dto.getName( ));
        existingInventory.setUnitCost(dto.getUnitCost( ));
        existingInventory.setExDate(dto.getExDate( ));
        existingInventory.setRQuantity(dto.getRQuantity( ));

        return pharmacyInventoryDAO.save(existingInventory);
    }

    public List<PharmacyInventory> getInventoriesWithQuantityBelowThreshold(int threshold) {
        try {
            logger.info("entered quantity is " + threshold);
            return pharmacyInventoryDAO.findInventoriesWithQuantityBelowThreshold(threshold);
        } catch (Exception e) {
            logger.error("error occurred while getting low stock " + e.getMessage( ), e);
            throw e;
        }
    }

    public List<PharmacyInventory> findInventoryByExpiryDateThreshold(LocalDate thresholdDate) {
        return pharmacyInventoryDAO.findInventoryByExpiryDateThreshold(thresholdDate);

    }
    public PharmacyInventory findByInventoryId(int Id) {
        return pharmacyInventoryDAO.findById(Id).get( );
    }
    public List<PharmacyInventory> getAllInventoryItems() {
        List<PharmacyInventory> allListInventory = (List<PharmacyInventory>) pharmacyInventoryDAO.findAll( );
        return allListInventory;
    }
    public byte[] qrCode(int id) {
        PharmacyInventory inventory = pharmacyInventoryDAO.findById(id).get( );
        byte[] qrCode = inventory.getQrCodeImage( );
        return qrCode;
    }
}