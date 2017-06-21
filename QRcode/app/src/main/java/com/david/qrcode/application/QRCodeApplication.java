package com.david.qrcode.application;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;


public class QRCodeApplication extends Application {

    public static QRCodeApplication mApp;

    public static QRCodeApplication getApplication() {
        return mApp;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mApp = this;

        initYouMi();
    }

    private void initYouMi() {

    }
}
