package com.zbq.library.process;

public  abstract class ClientManager implements IClientManager{
    public static IClientManager getInstance(){
        return ClientManagerAdapter.getInstance();
    }
    @Override
    public void onCallback(String json) {
        onRemoteMessage(json);
    }

    protected abstract void onRemoteMessage(String message);

}
