package com.zbq.library;

import android.os.RemoteException;

import com.zbq.library.bean.Request;
import com.zbq.library.bean.Response;
import com.zbq.library.constant.Constants;
import com.zbq.library.internal.IProcessCallback;
import com.zbq.library.utils.ProcessUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.concurrent.ConcurrentHashMap;


public class ServiceManagerAdapter extends ServiceManager{
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

    /**
     * 采用动态代理
     * 获取T单例实体，得到的实例是远程服务的实体
     *
     * @param clz    服务类类型
     * @param params 参数数组
     * @param <T>
     * @return
     */
    public <T> T getInstance(Class<T> clz,int pid, Object... params) {
        IProcessCallback callback = mCallbacks.get(pid);
        if (callback == null) return null;
        sendRequest(Constants.TYPE_INSTANCE, clz, null, params,callback);
        T t = (T) Proxy.newProxyInstance(clz.getClassLoader(), new Class[]{clz}, new ProxyInstanceHandler(clz,callback));
        return t;
    }

    /**
     * 向服务端发送请求
     *
     * @param type   请求类型
     * @param clz    请求类类型
     * @param method 方法
     * @param params 参数列表
     * @param <T>
     * @return
     */
    private <T> Response sendRequest(int type, Class<T> clz, Method method, Object[] params,IProcessCallback callback) {
        Request request = ProcessUtils.buildRequest(type,clz,method,params);
        if (callback != null) {
            try {
                return callback.callback(request);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private class ProxyInstanceHandler implements InvocationHandler {
        private Class<?> clz;
        IProcessCallback callback;
        public ProxyInstanceHandler(Class<?> clz,IProcessCallback callback) {
            this.clz = clz;
            this.callback = callback;
        }

        @Override
        public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
            if (callback != null) {
                Response respose = sendRequest(Constants.TYPE_METHOD, clz, method, objects,callback);
                Class info = method.getReturnType();
//                Object responseBean = JsonUtils.fromObject(respose.getResponse(), info);
                return respose == null ? null : respose.getResponse();
            }
            return null;
        }
    }
}
