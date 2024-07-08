package com.climawan.comp6844001.pertemuan5.hakubank.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.climawan.comp6844001.pertemuan5.hakubank.R;
import com.climawan.comp6844001.pertemuan5.hakubank.common.dto.impl.AccountTransferDtoImpl;
import com.climawan.comp6844001.pertemuan5.hakubank.common.guards.impl.AuthGuardImpl;
import com.climawan.comp6844001.pertemuan5.hakubank.services.HttpRequestService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class TransferActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);

        authGuard = new AuthGuardImpl(this);

        httpRequestService = HttpRequestService.getInstance(this);

        transferOnClickListener = new TransferOnClickListener();

        transferStatusDialogBuilder = new AlertDialog.Builder(this);

        etEmail = findViewById(R.id.et_email);
        etAccountNumber = findViewById(R.id.et_account_number);
        etTransferAmount = findViewById(R.id.et_transfer_amount);
        btnTransfer = findViewById(R.id.btn_transfer);

        transferBalanceUrl = "https://hakubank-api.climawan.com/accounts/transfer";
    }

    @Override
    protected void onStart() {
        super.onStart();

        executeGuard();

        setListeners();
    }

    private void executeGuard() {
        this.authGuard.check();
    }

    private void setListeners() {
        btnTransfer.setOnClickListener(transferOnClickListener);
    }

    private void startNextActivity() {
        Intent intent = new Intent(TransferActivity.this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void transferBalance(String accessToken, AccountTransferDtoImpl accountTransferDto) throws JSONException {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, transferBalanceUrl,
                accountTransferDto.transformToJsonObject(), response -> {
            TransferActivity.this.transferStatusDialogBuilder.setMessage("Success transfer!")
                    .setPositiveButton(getString(R.string.home_activity__check_balance_positive_resp_text), (dialog, which) -> {
                        startNextActivity();
                    });

            TransferActivity.this.transferStatusDialogBuilder.create().show();
        }, error -> {
            try {
                if (error.networkResponse == null) {
                    Toast.makeText(this, "Internal error detected, please contact developer for further details", Toast.LENGTH_SHORT).show();
                    return;
                }

                byte[] responseData = error.networkResponse.data;
                if (responseData == null) {
                    Toast.makeText(TransferActivity.this, "Internal error, please contact developer for further details", Toast.LENGTH_SHORT).show();
                    return;
                }

                String errorResponse = new String(responseData, "utf-8");
                JSONObject errorObject = new JSONObject(errorResponse);
                String errorString = errorObject.getJSONArray("message").get(0).toString();
                Toast.makeText(TransferActivity.this, errorString, Toast.LENGTH_SHORT).show();
            } catch (UnsupportedEncodingException | JSONException e) {
                throw new RuntimeException(e);
            }
        }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", "Bearer " + accessToken);

                return params;
            }
        };

        httpRequestService.addToRequestQueue(request);
    }

    private class TransferOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            SharedPreferences preferences = TransferActivity.this.getSharedPreferences(getString(R.string.sp__storage_id), Context.MODE_PRIVATE);
            String accessToken = preferences.getString(getString(R.string.sp__access_token_id), "");

            AccountTransferDtoImpl accountTransferDto = new AccountTransferDtoImpl(
                    etEmail.getText().toString(),
                    etAccountNumber.getText().toString(),
                    Integer.parseInt(etTransferAmount.getText().toString())
            );

            try {
                transferBalance(accessToken, accountTransferDto);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private AuthGuardImpl authGuard;

    private HttpRequestService httpRequestService;

    private TransferOnClickListener transferOnClickListener;

    private AlertDialog.Builder transferStatusDialogBuilder;

    private EditText etEmail;
    private EditText etAccountNumber;
    private EditText etTransferAmount;
    private Button btnTransfer;

    private String transferBalanceUrl;
}