package com.zbq.library.cash;

import com.zbq.library.model.RequestParams;

import java.lang.reflect.Method;

/**
 * create by maikel
 * 缓存管理类
 */
public class CacheManager {

    private ICacheManager cacheManager;

    private static CacheManager manager;

    private CacheManager(){
        cacheManager = ICacheManager.INSTANCE.getInstance();
    }

    /**
     * 单例
     * @return 返回CacheManager对象
     */
    public static CacheManager getInstance(){
        if (manager == null){
            synchronized (CacheManager.class){
                if (manager == null){
                    manager = new CacheManager();
                }
            }
        }
        return manager;
    }

    /**
     * 缓存类下的所有方法
     * @param clz 类
     */
    public void cacheMethod(Class<?> clz){
        cacheManager.cacheMethod(clz);
    }

    /**
     * 缓存对象实例
     * @param name 键值
     * @param object 对象实例
     */
    public void cacheObject(String name,Object object){
        cacheManager.cacheObject(name,object);
    }

    /**
     * 缓存类
     * @param name 键值
     * @param clz 类
     */
    public   void cacheClass(String name,Class<?> clz){
        cacheManager.cacheClass(name,clz);
    }

    /**
     * 根据键值得到方法
     * @param clasName 类名
     * @param methodName 方法名
     * @return 方法
     */
    public Method getMethod(String clasName, String methodName, RequestParams[] requestParams){
        return cacheManager.getMethod(clasName,methodName,requestParams);
    }

    /**
     * 根据键值获取对象实例
     * @param name 键值
     * @return 返回对象实例
     */
    public Object getObject(String name){
        return cacheManager.getObject(name);
    }

    /**
     * 根据键值获取类
     * @param name 键值
     * @return 返回类
     */
    public  Class<?> getClass(String name){
        return cacheManager.getClass(name);
    }
}
