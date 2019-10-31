package com.zbq.library.utils;

import com.zbq.library.anotion.ClassId;
import com.zbq.library.bean.Request;
import com.zbq.library.bean.RequestParams;
import com.zbq.library.bean.Response;
import com.zbq.library.cash.CacheManager;
import com.zbq.library.constant.Constants;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class ProcessUtils {


    /**
     * 获取响应类
     * @param request 请求
     * @return
     */
    public static Response getResponse(Request request){
        switch (request.getType()) {
            case Constants.TYPE_INSTANCE:
                RequestParams[] params = request.getRequestParams();
                Method method = CacheManager.getInstance().getMethod(request.getClassName(),"getInstance");
                try {
                    Object iServiceManager =  method.invoke(null,makeObjects(params));
                    CacheManager.getInstance().cacheObject(request.getClassName(),iServiceManager);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
                break;
            case Constants.TYPE_METHOD:
                Object serviceManager = CacheManager.getInstance().getObject(request.getClassName());
                Method methodInvok = CacheManager.getInstance().getMethod(request.getClassName(),request.getMethodName());
                Object[] parames = makeObjects(request.getRequestParams());
                try {
                    Object loginInfo = methodInvok.invoke(serviceManager, parames);
                    if (loginInfo == null){
                        return null;
                    }
                    Response response = new Response();
                    response.setResponse(loginInfo);
                    response.setClazz(loginInfo.getClass().getName());
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

    /**
     * 构建请求
     * @param type 请求类型，instance或方法
     * @param clz 需要请求类的类或接口
     * @param method 类或接口中声明的方法
     * @param params 请求参数
     * @param <T>
     * @return
     */
    public static <T> Request buildRequest(int type, Class<T> clz, Method method, Object[] params){
        Request request = new Request();
        if (params != null && params.length > 0) {//建造参数列表
            RequestParams[] requestParams = new RequestParams[params.length];
            int index = 0;
            int i = 0;
            Class<?>[] classes = method.getParameterTypes();
            for (Object object : params) {
                String paramClzName;
                boolean isAbs = Modifier.isAbstract(classes[i].getModifiers());
                if (isAbs && !classes[i].getName().equals("int")) {
                    paramClzName = classes[i].getName();
                } else {
                    paramClzName = object.getClass().getName();
                }
                i++;
                String paramValue = JsonUtils.toJson(object);
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
        String methodName = method == null ? "" : method.getName();
        request.setMethodName(methodName);
        request.setType(type);
        return request;
    }


    private static Object[] makeObjects(RequestParams [] params){
        Object[] paramsObjects = null;
        if (params!=null&&params.length>0) {
            paramsObjects = new Object[params.length];
            int index = 0;
            for (RequestParams requestParams : params) {
                String clazName = requestParams.getParamClzName();
                Class<?> clz = CacheManager.getInstance().getClass(clazName);
                paramsObjects[index++] = JsonUtils.parse(requestParams.getParamValue(),clz);
            }
        }else {
            paramsObjects = new Object[0];
        }
        return paramsObjects;
    }
}
