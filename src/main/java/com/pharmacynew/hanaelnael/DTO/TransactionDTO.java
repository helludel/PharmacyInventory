package com.pharmacynew.hanaelnael.DTO;

import com.pharmacynew.hanaelnael.Entity.Sales;
import com.pharmacynew.hanaelnael.Entity.Transactions;

import java.time.LocalDate;

public class TransactionDTO {
    public LocalDate tDate;
    public double dailyExpense;
    public Sales sales;

        public TransactionDTO (){

}

    public TransactionDTO(LocalDate tDate) {
        this.tDate = tDate;
    }

    public LocalDate getTDate() {
        return tDate;
    }



    public void settDate(LocalDate tDate) {
        this.tDate = tDate;
    }

    public double getDailyExpense() {
            return dailyExpense;
        }

        public void setDailyExpense(double dailyExpense) {
            this.dailyExpense = dailyExpense;
        }

        public Sales getSales() {
            return sales;
        }

        public void setSales(Sales sales) {
            this.sales = sales;
        }

        public TransactionDTO(double dailyExpense, Sales sales) {
            this.dailyExpense = dailyExpense;
            this.sales = sales;
        }

        @Override
        public String toString() {
            return "TransactionDTO{" +
                    "dailyExpense=" + dailyExpense +
                    ", sales=" + sales +
                            "tDate=" + tDate +
                    '}';
        }
    }
