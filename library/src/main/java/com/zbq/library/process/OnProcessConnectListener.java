package com.zbq.library.process;


import com.zbq.library.service.ProcessService;

public interface OnProcessConnectListener {

    void onConnected(Class<? extends ProcessService> service);


    void onDisconnected(Class<? extends ProcessService> service);
}
