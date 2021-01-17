package com.zbq.library.cash;

import com.zbq.library.model.RequestParams;
import com.zbq.library.utils.StringUtils;

import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * create by maikel
 * 缓存实现类
 */
public class CacheManagerImpl implements ICacheManager {
    /**
     * 缓存方法表：一个类多个方法
     ***/
    private HashMap<Class<?>, HashMap<String, Method>> methodMap;
    /**
     * 缓存实例对象表
     ****/
    private HashMap<String, Object> mInstanceMap;
    /**
     * 缓存类型表
     ***/
    private HashMap<String, Class<?>> mClassTypeMap;
    private static ICacheManager cacheManager;

    private CacheManagerImpl() {
        methodMap = new HashMap<>();
        mInstanceMap = new HashMap<>();
        mClassTypeMap = new HashMap<>();
    }

    /**
     * 单例模式
     *
     * @return 返回 CacheManagerImpl
     */
    static ICacheManager getCacheManager() {
        if (cacheManager == null) {
            synchronized (CacheManagerImpl.class) {
                if (cacheManager == null) {
                    cacheManager = new CacheManagerImpl();
                }
            }
        }
        return cacheManager;
    }

    /**
     * 缓存类下的所有方法
     *
     * @param clz 类
     */
    public void cacheMethod(Class<?> clz) {
        if (clz == null) return;
        if (methodMap.get(clz) == null) {
            Method[] methods = clz.getMethods();
            HashMap<String, Method> methodHashMap = methodMap.get(clz);
            if (methodHashMap == null) methodHashMap = new HashMap<>();
            StringBuilder builder = new StringBuilder();
            for (Method method : methods) {
                //methodMap.putIfAbsent(clz,new HashMap<String, Method>());
                //HashMap<String,Method> methodHashMap = methodMap.get(clz);
                builder.setLength(0);
                builder.append(method.getName()).append("(");
                Class<?>[] parameterTypes = method.getParameterTypes();
                if (parameterTypes!=null&&parameterTypes.length>0){
                    builder.append(parameterTypes[0].getName());
                    for (int i=1;i<parameterTypes.length;i++){
                        builder.append(",").append(parameterTypes[i].getName());
                    }
                }
                builder.append(")");
                methodHashMap.put(builder.toString(), method);
                builder.setLength(0);
            }
            methodMap.put(clz, methodHashMap);
        }

    }

    /**
     * 缓存对象实例
     *
     * @param name   键值
     * @param object 对象实例
     */
    public void cacheObject(String name, Object object) {

        mInstanceMap.put(name, object);
    }

    /**
     * 缓存类
     *
     * @param name 键值
     * @param clz  类
     */
    public void cacheClass(String name, Class<?> clz) {
        mClassTypeMap.put(name, clz);
    }

    /**
     * 根据键值得到方法
     *
     * @param clasName   类
     * @param methodName 方法名
     * @return 方法
     */
    public Method getMethod(String clasName, String methodName, RequestParams[] parameters) {
        if (methodName != null) {
            //methodMap.putIfAbsent(getClass(clasName),new HashMap<String, Method>());
            HashMap<String, Method> methodHashMap = methodMap.get(getClass(clasName));
            if (methodHashMap == null) {
                Class<?> clazz = getClass(clasName);
                cacheMethod(clazz);
                return getMethod(clasName, methodName,parameters);
            }
            StringBuilder sb = new StringBuilder(methodName);
            sb.append("(");
            if (parameters!=null&&parameters.length>0){
                sb.append(parameters[0].getParamClzName());
                for (int i=1;i<parameters.length;i++){
                    sb.append(",").append(parameters[i].getParamClzName());
                }

            }
            sb.append(")");
            Method method = methodHashMap.get(sb.toString());
            if (method != null) return method;
        }
        return null;
    }

    /**
     * 根据键值获取对象实例
     *
     * @param name 键值
     * @return 返回对象实例
     */
    public Object getObject(String name) {
        if (StringUtils.isEmpty(name)) return null;
        return mInstanceMap.get(name);
    }

    /**
     * 根据键值获取类
     *
     * @param name 键值
     * @return 返回类
     */
    public Class<?> getClass(String name) {
        if (StringUtils.isEmpty(name)) return null;
        Class<?> clz = mClassTypeMap.get(name);
        if (clz == null) {
            try {
                clz = Class.forName(name);
                //如果为空缓存下来
                cacheClass(name, clz);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return clz;
    }


}
