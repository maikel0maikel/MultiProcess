package com.zbq.library.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class DataService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return ProcessServiceManager.getProcess();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
