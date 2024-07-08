package com.climawan.comp6844001.pertemuan5.hakubank.common.dto.impl;

import com.climawan.comp6844001.pertemuan5.hakubank.common.dto.BaseDto;

import org.json.JSONException;
import org.json.JSONObject;

public class AccountTransferDtoImpl extends BaseDto {

    public AccountTransferDtoImpl(String email, String accountNumber, int balance) {
        this.email = email;
        this.accountNumber = accountNumber;
        this.balance = balance;
    }

    @Override
    public JSONObject transformToJsonObject() throws JSONException {
        JSONObject jsonBody = new JSONObject();
        jsonBody.put("email", this.email);
        jsonBody.put("accountNumber", this.accountNumber);
        jsonBody.put("balance", this.balance);

        return jsonBody;
    }

    private String email;
    private String accountNumber;
    private int balance;
}
