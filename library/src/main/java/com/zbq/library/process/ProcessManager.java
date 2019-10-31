package com.zbq.library.process;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;

import com.zbq.library.anotion.ClassId;
import com.zbq.library.bean.Request;
import com.zbq.library.bean.RequestParams;
import com.zbq.library.bean.Response;
import com.zbq.library.cash.CacheManager;
import com.zbq.library.constant.Constants;
import com.zbq.library.internal.IProcessService;
import com.zbq.library.service.DataService;
import com.zbq.library.utils.JsonUtils;
import com.zbq.library.utils.StringUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * create by maikel
 * 多进程管理实现类
 */
public class ProcessManager implements IProcessManager {

    private static IProcessManager processManager;

    private CacheManager cacheManager = CacheManager.getInstance();
    /**
     * 远程代理接口
     */
    private IProcessService processService;

    public static IProcessManager getInstance() {
        if (processManager == null) {
            synchronized (ProcessManager.class) {
                if (processManager == null) {
                    processManager = new ProcessManager();
                }
            }
        }
        return processManager;
    }

    /**
     * 注册主要是把该类缓存与缓存该类下的所有方法
     * @param serviceClz 接口类
     */
    @Override
    public void regist(Class<?> serviceClz) {
        ClassId classId = serviceClz.getAnnotation(ClassId.class);
        if (classId != null) {
            try {
                Class<?> clz = Class.forName(classId.value());
                cacheManager.cacheMethod(clz);
                cacheManager.cacheClass(clz.getName(), clz);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            cacheManager.cacheMethod(serviceClz);
            cacheManager.cacheClass(serviceClz.getName(), serviceClz);
        }


    }

    /**
     * 连接
     * @param context 上下文
     */
    @Override
    public void connect(Context context) {
        bindService(context, null, DataService.class);
    }

    /**
     * 连接
     * @param context 上下文
     * @param packageName 包名
     */
    @Override
    public void connect(Context context, String packageName) {
        bindService(context, packageName, DataService.class);
    }

    /**
     * 绑定服务
     * @param context 上下文
     * @param packageName 包名
     * @param clz 服务类
     * @return 绑定成功true，否则false
     */
    private boolean bindService(Context context, String packageName, Class<? extends DataService> clz) {
        if (StringUtils.isEmpty(packageName)) {
            Intent intent = new Intent(context, clz);
            return context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        } else {
            Intent intent = new Intent();
            intent.setPackage(packageName);
            intent.setAction(clz.getName());
            return context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        }
    }

    /**
     * 采用动态代理
     * 获取T单例实体，得到的实例是远程服务的实体
     * @param clz 服务类类型
     * @param params 参数数组
     * @param <T>
     * @return
     */
    @Override
    public <T> T getInstance(Class<T> clz, Object... params) {
        sendRequest(Constants.TYPE_INSTANCE, clz, null, params);
        T t = (T) Proxy.newProxyInstance(clz.getClassLoader(), new Class[]{clz}, new ProxyInstanceHandler(clz));
        return t;
    }

    /**
     * 向服务端发送请求
     * @param type 请求类型
     * @param clz 请求类类型
     * @param method 方法
     * @param params 参数列表
     * @param <T>
     * @return
     */
    private <T> Response sendRequest(int type, Class<T> clz, Method method, Object[] params) {
        Request request = new Request();
        if (params != null && params.length > 0) {//建造参数列表
            RequestParams[] requestParams = new RequestParams[params.length];
            int index = 0;
            for (Object object : params) {
                String paramClzName = object.getClass().getName();
                String paramValue = JsonUtils.fromString(object);
                RequestParams requestParam = new RequestParams(paramClzName, paramValue);
                requestParams[index++] = requestParam;
            }
            request.setRequestParams(requestParams);
        }
        ClassId classId = clz.getAnnotation(ClassId.class);
        if (classId != null) {
            request.setClassName(classId.value());
        } else {
            request.setClassName(clz.getName());
        }
        String methodName = method==null?"":method.getName();
        request.setMethodName(methodName);
        request.setType(type);
        if (processService != null) {
            try {
                return processService.send(request);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            processService = IProcessService.Stub.asInterface(iBinder);

        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            processService = null;
        }
    };

    private class ProxyInstanceHandler implements InvocationHandler {
        private Class<?> clz;
        public ProxyInstanceHandler(Class<?> clz){
            this.clz = clz;
        }
        @Override
        public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
            if (processService != null) {
                Response respose = sendRequest(Constants.TYPE_METHOD, clz, method, objects);
                Class info = method.getReturnType();
//                Object responseBean = JsonUtils.fromObject(respose.getResponse(), info);
                return respose==null?null:respose.getResponse();
            }
            return null;
        }
    }
}
