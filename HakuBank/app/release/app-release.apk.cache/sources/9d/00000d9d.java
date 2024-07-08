package com.climawan.comp6844001.pertemuan5.hakubank.services;

import android.content.Context;
import com.scottyab.rootbeer.RootBeer;

/* loaded from: classes.dex */
public class RootCheckService {
    private RootBeer instance;

    public RootCheckService(Context context) {
        this.instance = new RootBeer(context);
    }

    public boolean checkRoot() {
        return this.instance.isRooted();
    }
}