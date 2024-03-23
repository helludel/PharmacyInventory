package com.pharmacynew.hanaelnael.DAO;

import com.pharmacynew.hanaelnael.Entity.Transactions;
import com.pharmacynew.hanaelnael.Service.SalesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionDAO extends CrudRepository<Transactions,Integer> {
    static final Logger logger = LoggerFactory.getLogger(SalesService.class);

    public static void doSomething() {
        logger.debug("This is a debug message.");
        logger.info("This is an info message.");
        logger.warn("This is a warning message.");
        logger.error("This is an error message.");}
}
