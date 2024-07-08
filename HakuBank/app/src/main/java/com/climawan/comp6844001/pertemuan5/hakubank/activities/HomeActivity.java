package com.climawan.comp6844001.pertemuan5.hakubank.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.climawan.comp6844001.pertemuan5.hakubank.R;
import com.climawan.comp6844001.pertemuan5.hakubank.common.guards.impl.AuthGuardImpl;
import com.climawan.comp6844001.pertemuan5.hakubank.services.HttpRequestService;
import com.google.android.material.card.MaterialCardView;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        authGuard = new AuthGuardImpl(this);

        httpRequestService = HttpRequestService.getInstance(this);

        accountBalanceOnClickListener = new AccountBalanceOnClickListener();
        transferOnClickListener = new TransferOnClickListener();
        changePasscodeOnClickListener = new ChangePasscodeOnClickListener();
        logoutOnClickListener = new LogoutOnClickListener();

        accountBalanceDialogBuilder = new AlertDialog.Builder(HomeActivity.this);

        mcvAccountBalance = findViewById(R.id.mcv_account_balance);
        mcvTransfer = findViewById(R.id.mcv_transfer);
        mcvChangePassword = findViewById(R.id.mcv_change_password);
        mcvLogout = findViewById(R.id.mcv_logout);

        accountDetailsUrl = "https://hakubank-api.climawan.com/accounts/logged-in";
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
        mcvAccountBalance.setOnClickListener(accountBalanceOnClickListener);
        mcvTransfer.setOnClickListener(transferOnClickListener);
        mcvChangePassword.setOnClickListener(changePasscodeOnClickListener);
        mcvLogout.setOnClickListener(logoutOnClickListener);
    }

    private void getAccountDetails(String accessToken) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, accountDetailsUrl,
            null, response -> {
                try {
                    String dialogMessage = getString(R.string.home_activity__check_balance_info_email_text) + " " + response.getString("email") + "\n" +
                            getString(R.string.home_activity__check_balance_info_account_number_text) + " " + response.getString("account_number") + "\n" +
                            getString(R.string.home_activity__check_balance_info_balance_text) + "" + response.getString("balance");

                    HomeActivity.this.accountBalanceDialogBuilder.setMessage(dialogMessage)
                                .setPositiveButton(getString(R.string.home_activity__check_balance_positive_resp_text), (dialog, which) -> {});

                    HomeActivity.this.accountBalanceDialogBuilder.create().show();
                } catch (JSONException e) {
                    Toast.makeText(HomeActivity.this, "Internal error, please contact developer for further details", Toast.LENGTH_SHORT).show();
                }
            }, error -> {
                Toast.makeText(HomeActivity.this, "User data not found, invalid authentication token, returning to login page...", Toast.LENGTH_SHORT).show();

                returnToLoginActivity();
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

    private void returnToLoginActivity() {
        Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private class AccountBalanceOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            SharedPreferences preferences = HomeActivity.this.getSharedPreferences(getString(R.string.sp__storage_id), MODE_PRIVATE);
            String accessToken = preferences.getString(getString(R.string.sp__access_token_id), "");

            getAccountDetails(accessToken);
        }
    }

    private class TransferOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            startActivity(
                    new Intent(HomeActivity.this, TransferActivity.class)
            );
        }
    }

    private class ChangePasscodeOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            startActivity(
                    new Intent(HomeActivity.this, ChangePasswordActivity.class)
            );
        }
    }

    private class LogoutOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            SharedPreferences preferences = getSharedPreferences(getString(R.string.sp__storage_id), MODE_PRIVATE);

            SharedPreferences.Editor editor = preferences.edit();
            editor.remove(getString(R.string.sp__access_token_id));
            editor.apply();

            Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

    private AuthGuardImpl authGuard;

    private HttpRequestService httpRequestService;

    private AccountBalanceOnClickListener accountBalanceOnClickListener;
    private TransferOnClickListener transferOnClickListener;
    private ChangePasscodeOnClickListener changePasscodeOnClickListener;
    private LogoutOnClickListener logoutOnClickListener;

    private AlertDialog.Builder accountBalanceDialogBuilder;

    private MaterialCardView mcvAccountBalance;
    private MaterialCardView mcvTransfer;
    private MaterialCardView mcvChangePassword;
    private MaterialCardView mcvLogout;

    private String accountDetailsUrl;
}