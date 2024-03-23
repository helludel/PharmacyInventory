package com.pharmacynew.hanaelnael.DAO;

import com.pharmacynew.hanaelnael.Entity.PharmacyInventory;
import com.pharmacynew.hanaelnael.Service.SalesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Repository
public interface PharmacyInventoryDAO extends CrudRepository<PharmacyInventory,Integer>{
    static final Logger logger = LoggerFactory.getLogger(SalesService.class);

    public static void doSomething() {
        logger.debug("This is a debug message.");
        logger.info("This is an info message.");
        logger.warn("This is a warning message.");
        logger.error("This is an error message.");}

  static List<PharmacyInventory> findALL(){
    List<PharmacyInventory> allInventory=new ArrayList<>();
    return allInventory;}

    PharmacyInventory findByQrCodeImage(byte[] qrCodeImage);
      //  findByQrCode(qrCodeImage);


    // return findByQrCode(qrCodeImage);

    // static PharmacyInventory findByInventoryId(int inventoryId) {
  //  PharmacyInventory inventoryById=new PharmacyInventory();
     //   return inventoryById;}

        @Query("SELECT i FROM PharmacyInventory i WHERE i.rQuantity < :threshold")
        List<PharmacyInventory> findInventoriesWithQuantityBelowThreshold(@Param("threshold") int threshold);
    @Query("SELECT p FROM PharmacyInventory p WHERE p.exDate <= :thresholdDate")
    List<PharmacyInventory> findInventoryByExpiryDateThreshold(@Param("thresholdDate") LocalDate thresholdDate);

    PharmacyInventory findByName(String name);

}




