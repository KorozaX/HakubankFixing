package com.climawan.comp6844001.pertemuan5.hakubank.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.climawan.comp6844001.pertemuan5.hakubank.R;
import com.climawan.comp6844001.pertemuan5.hakubank.common.dto.impl.AccountCreateDtoImpl;
import com.climawan.comp6844001.pertemuan5.hakubank.services.HttpRequestService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        httpRequestService = HttpRequestService.getInstance(this);

        registerOnClickListener = new RegisterOnClickListener();

        etEmail = findViewById(R.id.et_email);
        etAccountNumber = findViewById(R.id.et_account_number);
        etPassword = findViewById(R.id.et_password);
        btnRegister = findViewById(R.id.btn_register);

        authUrl = "https://hakubank-api.climawan.com/accounts/create";
    }

    @Override
    protected void onStart() {
        super.onStart();

        setListeners();
    }

    private void setListeners() {
        btnRegister.setOnClickListener(registerOnClickListener);
    }

    private void registerAccountCredentials(AccountCreateDtoImpl accountCreateDto) throws JSONException {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, authUrl,
            accountCreateDto.transformToJsonObject(), response -> {
                startNextActivity();
            }, error -> {
                try {
                    if (error.networkResponse == null) {
                        Toast.makeText(this, "Internal error detected, please contact developer for further details", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    byte[] responseData = error.networkResponse.data;
                    if (responseData == null) {
                        Toast.makeText(RegisterActivity.this, "Internal error, please contact developer for further details", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String errorResponse = new String(responseData, "utf-8");
                    JSONObject errorObject = new JSONObject(errorResponse);
                    String errorString = errorObject.getJSONArray("message").get(0).toString();
                    Toast.makeText(RegisterActivity.this, errorString, Toast.LENGTH_SHORT).show();
                } catch (UnsupportedEncodingException | JSONException e) {
                    Toast.makeText(RegisterActivity.this, "Internal error, please contact developer for further details", Toast.LENGTH_SHORT).show();
                }
            }
        );

        httpRequestService.addToRequestQueue(request);
    }

    private void startNextActivity() {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private class RegisterOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            AccountCreateDtoImpl accountCreateDto = new AccountCreateDtoImpl(
                    RegisterActivity.this.etEmail.getText().toString(),
                    RegisterActivity.this.etAccountNumber.getText().toString(),
                    RegisterActivity.this.etPassword.getText().toString()
            );

            try {
                registerAccountCredentials(accountCreateDto);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private HttpRequestService httpRequestService;

    private RegisterOnClickListener registerOnClickListener;

    private EditText etEmail;
    private EditText etAccountNumber;
    private EditText etPassword;
    private Button btnRegister;


    private String authUrl;
    private String email;
    private String accountNumber;
    private String password;
}