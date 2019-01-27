package com.zbq.library.service;


import android.os.RemoteException;

import com.zbq.library.IProcess;
import com.zbq.library.bean.RequestBean;
import com.zbq.library.bean.RequestParams;
import com.zbq.library.cash.CacheManager;
import com.zbq.library.constant.Constants;
import com.zbq.library.utils.JsonUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ProcessManager extends IProcess.Stub {

    private static IProcess.Stub process;

    private ProcessManager() {
    }

    static IProcess.Stub getProcess() {
        if (process == null) {
            synchronized (ProcessManager.class) {
                if (process == null) {
                    process = new ProcessManager();
                }
            }
        }
        return process;
    }


    @Override
    public String sendMessage(String requst) throws RemoteException {
        RequestBean requestBean = JsonUtils.fromObject(requst, RequestBean.class);
        switch (requestBean.getType()) {
            case Constants.TYPE_INSTANCE:
                RequestParams [] params = requestBean.getRequestParams();
                Method method = CacheManager.getInstance().getMethod(requestBean.getClassName(),"getInstance");
                try {
                  Object iServiceManager =  method.invoke(null,makeObjects(params));
                  CacheManager.getInstance().cacheObject(requestBean.getClassName(),iServiceManager);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
                break;
            case Constants.TYPE_METHOD:

                Object serviceManager = CacheManager.getInstance().getObject(requestBean.getClassName());
                Method getLoginInfo = CacheManager.getInstance().getMethod(requestBean.getClassName(),requestBean.getMethodName());

                Object[] parames = makeObjects(requestBean.getRequestParams());
                try {
                    Object loginInfo = getLoginInfo.invoke(serviceManager,parames);
                    String response = JsonUtils.fromString(loginInfo);
                    return response;
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }

                break;
        }
        return null;
    }

    private Object[] makeObjects(RequestParams [] params){
        Object[] paramsObjects = null;
        if (params!=null&&params.length>0) {
            paramsObjects = new Object[params.length];
            int index = 0;
            for (RequestParams requestParams : params) {
                String clazName = requestParams.getParamClzName();
                Class<?> clz = CacheManager.getInstance().getClass(clazName);
                paramsObjects[index++] = JsonUtils.fromObject(requestParams.getParamValue(),clz);
            }
        }else {
            paramsObjects = new Object[0];
        }
        return paramsObjects;
    }
}
