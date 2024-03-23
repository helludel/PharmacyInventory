package com.pharmacynew.hanaelnael.DAO;

import com.pharmacynew.hanaelnael.Entity.Sales;
import com.pharmacynew.hanaelnael.Service.SalesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SalesDAO extends CrudRepository<Sales,Integer> {
    static final Logger logger = LoggerFactory.getLogger(SalesService.class);

    public static void doSomething() {
        logger.debug("This is a debug message.");
        logger.info("This is an info message.");
        logger.warn("This is a warning message.");
        logger.error("This is an error message.");}
    @Query("SELECT s FROM Sales s WHERE s.pharmacyInventory.Id = :Id")
    List<Sales> findByInventoryId(@Param("Id") int Id);
    @Query("SELECT s FROM Sales s WHERE s.saleDate = :date")
    List<Sales> findBySaleDate(@Param("date") LocalDate date);



}



