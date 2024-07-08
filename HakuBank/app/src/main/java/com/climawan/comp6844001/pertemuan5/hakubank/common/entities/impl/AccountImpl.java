package com.climawan.comp6844001.pertemuan5.hakubank.common.entities.impl;

import com.climawan.comp6844001.pertemuan5.hakubank.common.entities.BaseModel;

public class AccountImpl extends BaseModel {

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    private String accountNumber;
    private int balance;
}
