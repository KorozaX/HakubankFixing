package com.climawan.comp6844001.pertemuan5.hakubank.services;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class HttpRequestService {

    private HttpRequestService(Context context) {
        HttpRequestService.context = context;
        this.requestQueue = getRequestQueue();
    }

    public static synchronized HttpRequestService getInstance(Context context) {
        if (instance == null) {
            instance = new HttpRequestService(context);
        }

        return instance;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        this.getRequestQueue().add(req);
    }

    private RequestQueue getRequestQueue() {
        if (this.requestQueue == null) {
            this.requestQueue = Volley.newRequestQueue(HttpRequestService.context);
        }

        return this.requestQueue;
    }

    private static HttpRequestService instance;
    private static Context context;
    private RequestQueue requestQueue;
}
