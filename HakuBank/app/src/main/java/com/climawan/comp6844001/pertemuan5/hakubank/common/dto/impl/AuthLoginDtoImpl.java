package com.climawan.comp6844001.pertemuan5.hakubank.common.dto.impl;

import com.climawan.comp6844001.pertemuan5.hakubank.common.dto.BaseDto;

import org.json.JSONException;
import org.json.JSONObject;

public class AuthLoginDtoImpl extends BaseDto {
    private String email;
    private String accountNumber;
    private String password;

    public AuthLoginDtoImpl(String email, String accountNumber, String password) {
        this.email = email;
        this.accountNumber = accountNumber;
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public JSONObject transformToJsonObject() throws JSONException {
        JSONObject jsonBody = new JSONObject();
        jsonBody.put("email", this.email);
        jsonBody.put("accountNumber", this.accountNumber);
        jsonBody.put("password", this.password);

        return jsonBody;
    }
}
