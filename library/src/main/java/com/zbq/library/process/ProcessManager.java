package com.zbq.library.process;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Process;
import android.os.RemoteException;
import android.text.TextUtils;

import com.zbq.library.anotion.ClassId;
import com.zbq.library.bean.Request;
import com.zbq.library.bean.Response;
import com.zbq.library.cash.CacheManager;
import com.zbq.library.constant.Constants;
import com.zbq.library.internal.IProcessCallback;
import com.zbq.library.internal.IProcessService;
import com.zbq.library.service.ProcessService;
import com.zbq.library.utils.ProcessUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.concurrent.ConcurrentHashMap;


/**
 * create by maikel
 * 多进程管理实现类
 */
public class ProcessManager implements IProcessManager {
    private static final String TAG = ProcessManager.class.getSimpleName();
    private static IProcessManager processManager;

    private CacheManager cacheManager = CacheManager.getInstance();

    private final ConcurrentHashMap<Integer, IProcessService> mProcessServices = new ConcurrentHashMap<>();

    private final ConcurrentHashMap<Integer, ProcessServiceConnection> mProcessServiceConnections = new ConcurrentHashMap<>();

    private final ConcurrentHashMap<Integer, Boolean> mBindings = new ConcurrentHashMap<>();

    private final ConcurrentHashMap<Integer, Boolean> mBounds = new ConcurrentHashMap<>();

    private OnProcessConnectListener mListener;

    private IProcessCallback processCallback = new IProcessCallback.Stub() {
        @Override
        public Response callback(Request request) throws RemoteException {
            return ProcessUtils.getResponse(request);
        }

    };

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
     *
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
     *
     * @param context 上下文
     */
    @Override
    public void connect(Context context) {
        bindService(context, null, ProcessService.class);
    }

    /**
     * 连接
     *
     * @param context     上下文
     * @param packageName 包名
     */
    @Override
    public void connect(Context context, String packageName) {
        bindService(context, packageName, ProcessService.class);
    }

    /**
     * 绑定服务
     *
     * @param context     上下文
     * @param packageName 包名
     * @param service     服务类
     * @return 绑定成功true，否则false
     */
    private boolean bindService(Context context, String packageName, Class<? extends ProcessService> service) {

        ProcessServiceConnection connection;
        synchronized (this) {
            if (getBound(service)) {
                return false;
            }
            Boolean binding = mBindings.get(service);
            if (binding != null && binding) {
                return false;
            }
            mBindings.put(Process.myPid(), true);
            connection = new ProcessServiceConnection(service);
            mProcessServiceConnections.put(Process.myPid(), connection);
        }
        Intent intent;
        if (TextUtils.isEmpty(packageName)) {
            intent = new Intent(context, service);
        } else {
            intent = new Intent();
            intent.setClassName(packageName, service.getName());
        }
        return context.bindService(intent, connection, Context.BIND_AUTO_CREATE);

    }

    private boolean getBound(Class<? extends ProcessService> service) {
        Boolean bound = mBounds.get(service);
        return bound != null && bound;
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
    @Override
    public <T> T getInstance(Class<T> clz, Object... params) {
        sendRequest(Constants.TYPE_INSTANCE, clz, null, params);
        T t = (T) Proxy.newProxyInstance(clz.getClassLoader(), new Class[]{clz}, new ProxyInstanceHandler(clz));
        return t;
    }

    @Override
    public void disconnect(Context context) {
        synchronized (this) {
            Boolean bound = mBounds.get(Process.myPid());
            if (bound != null && bound) {
                ProcessServiceConnection connection = mProcessServiceConnections.get(Process.myPid());
                if (connection != null) {
                    context.unbindService(connection);
                }
                mBounds.put(Process.myPid(), false);
            }
        }
    }


    @Override
    public boolean isConnected() {
        Boolean isBound = mBounds.get(Process.myPid());
        return isBound == null ? false : isBound;
    }

    @Override
    public void setOnProcessConnectListener(OnProcessConnectListener listener) {
        mListener = listener;
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
    private <T> Response sendRequest(int type, Class<T> clz, Method method, Object[] params) {
        Request request = ProcessUtils.buildRequest(type,clz,method,params);
        IProcessService processService = mProcessServices.get(Process.myPid());
        if (processService != null) {
            try {
                return processService.send(request);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    private class ProcessServiceConnection implements ServiceConnection {

        private Class<? extends ProcessService> mClass;

        ProcessServiceConnection(Class<? extends ProcessService> service) {
            mClass = service;
        }

        public void onServiceConnected(ComponentName className, IBinder service) {
            synchronized (ProcessManager.this) {
                mBounds.put(Process.myPid(), true);
                mBindings.put(Process.myPid(), false);
                IProcessService processService = IProcessService.Stub.asInterface(service);
                mProcessServices.put(Process.myPid(), processService);
                try {
                    processService.register(processCallback, Process.myPid());
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
            if (mListener != null) {
                mListener.onConnected(mClass);
            }
        }

        public void onServiceDisconnected(ComponentName className) {
            synchronized (ProcessManager.this) {
                mProcessServices.remove(mClass);
                mBounds.put(Process.myPid(), false);
                mBindings.put(Process.myPid(), false);
            }
            if (mListener != null) {
                mListener.onDisconnected(mClass);
            }
        }
    }

    private class ProxyInstanceHandler implements InvocationHandler {
        private Class<?> clz;

        public ProxyInstanceHandler(Class<?> clz) {
            this.clz = clz;
        }

        @Override
        public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
            IProcessService processService = mProcessServices.get(Process.myPid());
            if (processService != null) {
                Response respose = sendRequest(Constants.TYPE_METHOD, clz, method, objects);
                Class info = method.getReturnType();
//                Object responseBean = JsonUtils.fromObject(respose.getResponse(), info);
                return respose == null ? null : respose.getResponse();
            }
            return null;
        }
    }
}
