package com.climawan.comp6844001.pertemuan5.hakubank.services;

import java.security.cert.CertificateException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.CertificatePinner;
import okhttp3.OkHttpClient;

public class NetworkClient {

  private static final String BASE_URL = "https://api.example.com/v1"; // Dummy Data
private static final String PINNED_PUBLIC_KEY = "sha256/BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB="; // Dummy Data

    private static OkHttpClient client;

    public static OkHttpClient getClient() {
        if (client == null) {
            CertificatePinner certificatePinner = new CertificatePinner.Builder()
                    .add("your.api.endpoint", PINNED_PUBLIC_KEY)
                    .build();

            client = new OkHttpClient.Builder()
                    .certificatePinner(certificatePinner)
                    .build();
        }
        return client;
    }

    // Method to get the base URL
    public static String getBaseUrl() {
        return BASE_URL;
    }
}
