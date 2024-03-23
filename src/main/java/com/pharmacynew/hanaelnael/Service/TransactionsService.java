package com.pharmacynew.hanaelnael.Service;

import com.pharmacynew.hanaelnael.DAO.SalesDAO;
import com.pharmacynew.hanaelnael.DTO.TransactionDTO;
import com.pharmacynew.hanaelnael.Entity.Sales;
import com.pharmacynew.hanaelnael.Entity.Transactions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransactionsService {
    SalesDAO salesDAO;
    private static final Logger logger = LoggerFactory.getLogger(SalesService.class);

    public void doSomething() {
        logger.debug("This is a debug message.");
        logger.info("This is an info message.");
        logger.warn("This is a warning message.");
        logger.error("This is an error message.");}



}



