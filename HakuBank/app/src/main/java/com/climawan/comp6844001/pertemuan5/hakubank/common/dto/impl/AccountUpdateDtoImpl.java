package com.climawan.comp6844001.pertemuan5.hakubank.common.dto.impl;

import com.climawan.comp6844001.pertemuan5.hakubank.common.dto.BaseDto;

import org.json.JSONException;
import org.json.JSONObject;

public class AccountUpdateDtoImpl extends BaseDto {

    public AccountUpdateDtoImpl(String password) {
        this.password = password;
    }

    @Override
    public JSONObject transformToJsonObject() throws JSONException {
        JSONObject jsonBody = new JSONObject();
        jsonBody.put("password", this.password);

        return jsonBody;
    }

    private String password;
}
