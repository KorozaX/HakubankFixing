package com.climawan.comp6844001.pertemuan5.hakubank.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.climawan.comp6844001.pertemuan5.hakubank.R;
import com.climawan.comp6844001.pertemuan5.hakubank.common.dto.impl.AccountUpdateDtoImpl;
import com.climawan.comp6844001.pertemuan5.hakubank.common.guards.impl.AuthGuardImpl;
import com.climawan.comp6844001.pertemuan5.hakubank.services.HttpRequestService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class ChangePasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        authGuard = new AuthGuardImpl(this);

        httpRequestService = HttpRequestService.getInstance(this);

        changePasswordStatusDialogBuilder = new AlertDialog.Builder(this);

        changeOnClickListener = new ChangeOnClickListener();
        etPassword = findViewById(R.id.et_password);
        btnChange = findViewById(R.id.btn_change);

        getCurrentUserUrl = "https://hakubank-api.climawan.com/auth/check";
        passwordChangeUrl = "https://hakubank-api.climawan.com/accounts/update";
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
        btnChange.setOnClickListener(changeOnClickListener);
    }

    private void startNextActivity() {
        Intent intent = new Intent(ChangePasswordActivity.this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void returnToLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void getCurrentUserUrl(String accessToken, AccountUpdateDtoImpl accountUpdateDto) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, this.getCurrentUserUrl, null, response -> {
            try {
                Log.d("testing", response.toString());
                int id = response.getInt("account_id");
                changePassword(accessToken, id, accountUpdateDto);
            } catch (JSONException e) {
                Toast.makeText(ChangePasswordActivity.this, "Invalid authentication token, return to login page...", Toast.LENGTH_SHORT).show();
                returnToLoginActivity();
            }
        }, error -> {
            Toast.makeText(ChangePasswordActivity.this, "Internal error, please contact developer for further details", Toast.LENGTH_SHORT).show();
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", "Bearer " + accessToken);

                return params;
            }
        };

        httpRequestService.addToRequestQueue(request);
    }

    private void changePassword(String accessToken, int id, AccountUpdateDtoImpl accountUpdateDto) throws JSONException {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PATCH, passwordChangeUrl + "/" + id,
                accountUpdateDto.transformToJsonObject(), response -> {
            ChangePasswordActivity.this.changePasswordStatusDialogBuilder.setMessage("Success change password!")
                    .setPositiveButton(getString(R.string.home_activity__check_balance_positive_resp_text), (dialog, which) -> {
                        startNextActivity();
                    });

            ChangePasswordActivity.this.changePasswordStatusDialogBuilder.create().show();
        }, error -> {
            try {
                if (error.networkResponse == null) {
                    Toast.makeText(this, "Internal error detected, please contact developer for further details", Toast.LENGTH_SHORT).show();
                    return;
                }

                byte[] responseData = error.networkResponse.data;
                if (responseData == null) {
                    Toast.makeText(ChangePasswordActivity.this, "Internal error, please contact developer for further details", Toast.LENGTH_SHORT).show();
                    return;
                }

                String errorResponse = new String(responseData, "utf-8");
                JSONObject errorObject = new JSONObject(errorResponse);
                String errorString = errorObject.getJSONArray("message").get(0).toString();
                Toast.makeText(ChangePasswordActivity.this, errorString, Toast.LENGTH_SHORT).show();
            } catch (UnsupportedEncodingException | JSONException e) {
                Toast.makeText(ChangePasswordActivity.this, "Internal error, please contact developer for further details", Toast.LENGTH_SHORT).show();
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

    private class ChangeOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            SharedPreferences preferences = getSharedPreferences(getString(R.string.sp__storage_id), MODE_PRIVATE);
            String accessToken = preferences.getString(getString(R.string.sp__access_token_id), "");
            AccountUpdateDtoImpl accountUpdateDto = new AccountUpdateDtoImpl(
                    etPassword.getText().toString()
            );

            getCurrentUserUrl(accessToken, accountUpdateDto);
        }
    }

    private AuthGuardImpl authGuard;

    private HttpRequestService httpRequestService;

    private ChangeOnClickListener changeOnClickListener;

    private AlertDialog.Builder changePasswordStatusDialogBuilder;

    private EditText etPassword;
    private Button btnChange;

    private String getCurrentUserUrl;
    private String passwordChangeUrl;
}