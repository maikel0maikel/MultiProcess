package com.zbq.library.process;

public class ClientManagerAdapter extends ClientManager{

    private static ClientManagerAdapter clientManager;
    private OnRemoteMessageListener mListener;
    private ClientManagerAdapter(){}
    public static ClientManagerAdapter getInstance(){
        if (clientManager == null ){
            synchronized (ClientManagerAdapter.class){
                if (clientManager == null){
                    clientManager = new ClientManagerAdapter();
                }
            }
        }
        return clientManager;
    }

    @Override
    protected void onRemoteMessage(String message) {
        if (mListener!=null)mListener.onMessage(message);
    }

    public void setOnRemoteMessageListener(OnRemoteMessageListener listener){
        mListener = listener;
    }

    public interface OnRemoteMessageListener{
        void onMessage(String message);
    }
}
