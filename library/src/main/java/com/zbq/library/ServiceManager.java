package com.zbq.library;


public abstract class ServiceManager implements IServiceManager{


    public static IServiceManager getInstance(){
        return ServiceManagerAdapter.getInstance();
    }
    @Override
    public void sendAiuiMessage(String json) {
        onClientSendMessage(json);
    }


    abstract void onClientSendMessage(String json);

}
