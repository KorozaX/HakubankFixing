package com.climawan.comp6844001.pertemuan5.hakubank.common.dto.impl;

import com.climawan.comp6844001.pertemuan5.hakubank.common.dto.BaseDto;

import org.json.JSONException;
import org.json.JSONObject;

public class AccountCreateDtoImpl extends BaseDto {

    public AccountCreateDtoImpl(String email, String accountNumber, String password) {
        this.email = email;
        this.accountNumber = accountNumber;
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

    private String email;
    private String accountNumber;
    private String password;
}
