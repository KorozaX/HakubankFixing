package com.climawan.comp6844001.pertemuan5.hakubank.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.climawan.comp6844001.pertemuan5.hakubank.R;
import com.climawan.comp6844001.pertemuan5.hakubank.common.dto.impl.AuthLoginDtoImpl;
import com.climawan.comp6844001.pertemuan5.hakubank.services.HttpRequestService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        httpRequestService = HttpRequestService.getInstance(this);

        loginOnClickListener = new LoginOnClickListener();
        registerOnClickListener = new RegisterOnClickListener();

        btnLogin = findViewById(R.id.btn_login);
        btnRegister = findViewById(R.id.btn_register);
        etEmail = findViewById(R.id.et_email);
        etAccountNumber = findViewById(R.id.et_account_number);
        etPassword = findViewById(R.id.et_password);

        authUrl = "https://hakubank-api.climawan.com/auth/login";
    }

    @Override
    protected void onStart() {
        super.onStart();

        setListeners();
    }

    private void setListeners() {
        btnLogin.setOnClickListener(loginOnClickListener);
        btnRegister.setOnClickListener(registerOnClickListener);
    }

    private void checkLoginCredentials(AuthLoginDtoImpl authLoginDto) throws JSONException {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, authUrl,
            authLoginDto.transformToJsonObject(), response -> {
                try {
                    saveAccessToken(response);
                    startNextActivity();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }, error -> {
                try {
                    error.printStackTrace();
                    if (error.networkResponse == null) {
                        Toast.makeText(this, "Internal error detected, please contact developer for further details", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    byte[] responseData = error.networkResponse.data;
                    if (responseData == null) {
                        Toast.makeText(this, "Internal error detected, please contact developer for further details", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String errorResponse = new String(responseData, "utf-8");
                    JSONObject errorObject = new JSONObject(errorResponse);
                    String errorString = errorObject.getJSONArray("message").get(0).toString();
                    Toast.makeText(this, errorString, Toast.LENGTH_SHORT).show();
                } catch (UnsupportedEncodingException | JSONException e) {
                    Toast.makeText(this, "Internal error detected, please contact developer for further details", Toast.LENGTH_SHORT).show();
                }
            }
        );

        httpRequestService.addToRequestQueue(request);
    }

    private void saveAccessToken(JSONObject accessTokenResponse) throws JSONException {
        SharedPreferences preferences = getSharedPreferences(getString(R.string.sp__storage_id), MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(getString(R.string.sp__access_token_id), accessTokenResponse.getString("access_token"));
        editor.apply();
    }

    private void startNextActivity() {
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private class LoginOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            try {
                checkLoginCredentials(
                        new AuthLoginDtoImpl(etEmail.getText().toString(),
                                etAccountNumber.getText().toString(),
                                etPassword.getText().toString())
                );
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class RegisterOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            startActivity(
                    new Intent(LoginActivity.this.getApplicationContext(), RegisterActivity.class)
            );
        }
    }

    private HttpRequestService httpRequestService;

    private LoginOnClickListener loginOnClickListener;
    private RegisterOnClickListener registerOnClickListener;

    private Button btnLogin;
    private Button btnRegister;
    private EditText etEmail;
    private EditText etAccountNumber;
    private EditText etPassword;

    private String authUrl;
}