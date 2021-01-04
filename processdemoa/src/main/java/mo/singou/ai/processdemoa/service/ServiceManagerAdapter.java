package mo.singou.ai.processdemoa.service;

import com.zbq.library.internal.IProcessCallback;

import java.util.concurrent.ConcurrentHashMap;

public class ServiceManagerAdapter extends ServiceManager {
    private OnClientMessageListener mClientMessageListener;
    private static ServiceManagerAdapter instance;
    private ServiceManagerAdapter(){}
    private ConcurrentHashMap<Integer, IProcessCallback> mCallbacks = new ConcurrentHashMap<>();
    public static ServiceManagerAdapter getInstance(){
        if (instance == null){
            synchronized (ServiceManager.class){
                if (instance == null){
                    instance = new ServiceManagerAdapter();
                }
            }
        }
        return instance;
    }
    @Override
    protected void onClientSendMessage(String json) {
        if (mClientMessageListener!=null)mClientMessageListener.onMessage(json);
    }




    public interface OnClientMessageListener{
        void onMessage(String json);
    }

    public void setClientMessageListener(OnClientMessageListener listener){
        mClientMessageListener = listener;
    }

    public void registerCallback(int pid,IProcessCallback callback){
        mCallbacks.put(pid,callback);
    }

    public void unRegisterCallback(int pid) {
        mCallbacks.remove(pid);
    }
}
