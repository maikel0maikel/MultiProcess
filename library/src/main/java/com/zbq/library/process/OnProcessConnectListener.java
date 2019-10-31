package com.zbq.library.process;


import com.zbq.library.service.DataService;

public interface OnProcessConnectListener {

    void onConnected(Class<? extends DataService> service);


    void onDisconnected(Class<? extends DataService> service);
}
