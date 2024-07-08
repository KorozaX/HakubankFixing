package com.climawan.comp6844001.pertemuan5.hakubank.common.dto;

import org.json.JSONException;
import org.json.JSONObject;

public interface RequestBodyTransformable {
    JSONObject transformToJsonObject() throws JSONException;
}
