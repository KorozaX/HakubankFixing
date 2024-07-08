package com.climawan.comp6844001.pertemuan5.hakubank.common.guards;

import android.content.Context;

public abstract class BaseGuard implements GuardTriggerable {
    public BaseGuard(Context context) {
        this.context = context;
    }

    protected Context context;
}
