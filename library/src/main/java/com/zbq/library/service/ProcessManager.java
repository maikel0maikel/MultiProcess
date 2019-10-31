package com.zbq.library.service;


import android.os.RemoteException;

import com.zbq.library.bean.Request;
import com.zbq.library.bean.RequestParams;
import com.zbq.library.bean.Response;
import com.zbq.library.cash.CacheManager;
import com.zbq.library.constant.Constants;
import com.zbq.library.internal.IProcessService;
import com.zbq.library.utils.JsonUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ProcessManager extends IProcessService.Stub {

    private static IProcessService.Stub process;

    private ProcessManager() {
    }

    static IProcessService.Stub getProcess() {
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
    public Response send(Request requst) throws RemoteException {
        int pid = requst.getPid();
        switch (requst.getType()) {
            case Constants.TYPE_INSTANCE: {
                RequestParams[] params = requst.getRequestParams();
                Method method = CacheManager.getInstance().getMethod(requst.getClassName(), "getInstance");
                try {
                    Object iServiceManager = method.invoke(null, makeObjects(params));
                    CacheManager.getInstance().cacheObject(requst.getClassName(), iServiceManager);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
                break;
            case Constants.TYPE_METHOD: {
                Object serviceManager = CacheManager.getInstance().getObject(requst.getClassName());
                Method getLoginInfo = CacheManager.getInstance().getMethod(requst.getClassName(), requst.getMethodName());
                Object[] parames = makeObjects(requst.getRequestParams());
                try {
                    Object loginInfo = getLoginInfo.invoke(serviceManager, parames);
                    if (loginInfo == null){
                        return null;
                    }
                    Response response = new Response();
                    response.setResponse(loginInfo);
                    response.setClazz(loginInfo.getClass().getName());
                    //String response = JsonUtils.fromString(loginInfo);
                    return response;
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
                break;
//            case Constants.TYPE_OBJECT: {
//                Object serviceManager = CacheManager.getInstance().getObject(requestBean.getClassName());
//                Method getLoginInfo = CacheManager.getInstance().getMethod(requestBean.getClassName(), requestBean.getMethodName());
//                Object[] parames = makeObjects(requestBean.getRequestParams());
//                try {
//                    return getLoginInfo.invoke(serviceManager, parames);
//                } catch (IllegalAccessException e) {
//                    e.printStackTrace();
//                } catch (InvocationTargetException e) {
//                    e.printStackTrace();
//                }
//            }
//                break;
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
