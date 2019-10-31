package com.zbq.library;


import android.util.Log;

import java.util.concurrent.ConcurrentHashMap;

public class ServiceManager implements IServiceManager{
    private static final String TAG = ServiceManager.class.getSimpleName();
    private ConcurrentHashMap<Integer,OnAiuiMessageListener> mAiuiListeners = new ConcurrentHashMap<>();
    private static IServiceManager instance  ;
    public static IServiceManager getInstance(){
        if (instance == null){
            synchronized (ServiceManager.class){
                if (instance == null){
                    instance = new ServiceManager();
                }
            }
        }
        return instance;
    }
    @Override
    public void sendAiuiMessage(String json) {
        Log.d(TAG,"sendAiuiMessage----"+json);
    }

    @Override
    public void setOnAiuiMessageListener(OnAiuiMessageListener listener,int pid) {
        mAiuiListeners.put(pid,listener);
    }

    @Override
    public OnAiuiMessageListener getListener(int pid) {
        return mAiuiListeners.get(pid);
    }
}
