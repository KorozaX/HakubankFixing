package com.climawan.comp6844001.pertemuan5.hakubank.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.climawan.comp6844001.pertemuan5.hakubank.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (isDeviceRooted()) {
            // Handle rooted device
            Toast.makeText(this, "This device is rooted. The application may not work properly.", Toast.LENGTH_LONG).show();
            finish(); // Close the app if rooted device detected
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (!isDeviceRooted()) {
            startActivity(
                    new Intent(this, LoginActivity.class)
            );
        }
    }

    private boolean isDeviceRooted() {
        // Check for test-keys
        String buildTags = android.os.Build.TAGS;
        if (buildTags != null && buildTags.contains("test-keys")) {
            return true;
        }

        // Check for Superuser APK
        try {
            File file = new File("/system/app/Superuser.apk");
            if (file.exists()) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Check for SU binary
        return canExecuteCommand("/system/xbin/which su") || canExecuteCommand("/system/bin/which su") || canExecuteCommand("which su");
    }

    private boolean canExecuteCommand(String command) {
        boolean executedSuccessfully;
        try {
            Process process = Runtime.getRuntime().exec(command);
            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
            if (in.readLine() != null) {
                executedSuccessfully = true;
            } else {
                executedSuccessfully = false;
            }
            process.destroy();
        } catch (Exception e) {
            executedSuccessfully = false;
        }
        return executedSuccessfully;
    }
}
