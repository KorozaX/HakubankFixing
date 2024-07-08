package com.climawan.comp6844001.pertemuan5.hakubank.common.guards.impl;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.climawan.comp6844001.pertemuan5.hakubank.R;
import com.climawan.comp6844001.pertemuan5.hakubank.activities.LoginActivity;
import com.climawan.comp6844001.pertemuan5.hakubank.common.guards.BaseGuard;
import com.climawan.comp6844001.pertemuan5.hakubank.services.HttpRequestService;

import java.util.HashMap;
import java.util.Map;

public class AuthGuardImpl extends BaseGuard {

    public AuthGuardImpl(Context context) {
        super(context);

        this.httpRequestService = HttpRequestService.getInstance(context);

        this.authUrl = "https://hakubank-api.climawan.com/auth/check";
    }

    @Override
    public boolean check() {
        SharedPreferences preferences = this.context.getSharedPreferences(this.context.getString(R.string.sp__storage_id), Context.MODE_PRIVATE);
        String accessToken = preferences.getString(context.getString(R.string.sp__access_token_id), "");
        if (accessToken.equals("")) return false;

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, this.authUrl, null, response -> {}, error -> {
            Toast.makeText(context, "Invalid authentication token, returning to login page...", Toast.LENGTH_SHORT).show();
            returnToLoginActivity();
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", "Bearer " + accessToken);

                return params;
            }
        };

        this.httpRequestService.addToRequestQueue(req);

        return true;
    }

    private void returnToLoginActivity() {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }


    private final HttpRequestService httpRequestService;
    private final String authUrl;
}
