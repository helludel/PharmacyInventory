package com.pharmacynew.hanaelnael.Entity;

import com.pharmacynew.hanaelnael.Service.SalesService;
import jakarta.persistence.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table
public class Transactions {
    private static final Logger logger = LoggerFactory.getLogger(SalesService.class);

    public Transactions(LocalDate tDate, double dailyExpense) {
    }

    public void doSomething() {
        logger.debug("This is a debug message.");
        logger.info("This is an info message.");
        logger.warn("This is a warning message.");
        logger.error("This is an error message.");}
    @Column(name="Id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int Id;
    @Column(name="t_date")
    public LocalDate tDate;
    @Column(name="daily_sales")
    public double dailySales;
    @Column(name="daily_expense")
    public double dailyExpense;
    @Column(name="daily_profit")
    public double dailyProfit;
    @OneToMany
    @JoinColumn(name ="sales_Id")
    public List<Sales > sales;

    public Transactions(){
    }

    public Transactions(LocalDate tDate,double dailySales, double dailyExpense,double dailyProfit) {
        this.tDate = tDate;
        this.dailySales = dailySales;
        this.dailyExpense = dailyExpense;
        this.dailyProfit = dailyProfit;
    }

    public LocalDate getTDate() {
        return tDate;
    }

    public void settDate(LocalDate tDate) {
        this.tDate = tDate;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }


    public double getDailySales() {
        return dailySales;
    }

    public void setDailySales(double dailySales) {
        this.dailySales = dailySales;
    }

    public double getDailyExpense() {
        return dailyExpense;
    }

    public void setDailyExpense(double dailyExpense) {
        this.dailyExpense = dailyExpense;
    }

    public double getDailyProfit() {
        return dailyProfit;
    }

    public void setDailyProfit(double dailyProfit) {
        this.dailyProfit = dailyProfit;
    }
    @Override
    public String toString() {
        return "Transactions{" +
                "Id=" + Id +
                "tDate=" + tDate +
                ", dailySales=" + dailySales +
                ", dailyExpense=" + dailyExpense +
                ", dailyProfit=" + dailyProfit +
                '}';
    }
}
