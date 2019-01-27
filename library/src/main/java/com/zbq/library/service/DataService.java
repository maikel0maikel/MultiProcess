package com.zbq.library.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

public class DataService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return ProcessManager.getProcess();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}