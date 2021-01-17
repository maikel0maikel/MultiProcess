package com.zbq.library.cash;

import com.zbq.library.model.RequestParams;

import java.lang.reflect.Method;

/**
 * create by maikel
 * 缓存方法、对象、类
 * 因为反射比较耗时，所以先缓存下来
 */
public interface ICacheManager {
    /**
     * 缓存类下的所有方法
     * @param clz 类
     */
    void cacheMethod(Class<?> clz);

    /**
     * 缓存对象实例
     * @param name 键值
     * @param object 对象实例
     */
    void cacheObject(String name,Object object);

    /**
     * 缓存类
     * @param name 键值
     * @param clz 类
     */
    void cacheClass(String name,Class<?> clz);

    /**
     * 根据键值得到方法
     * @param clasName 类名
     * @param methodName 方法名
     * @return 方法
     */
    Method getMethod(String clasName, String methodName, RequestParams[] requestParams);

    /**
     * 根据键值获取对象实例
     * @param name 键值
     * @return 返回对象实例
     */
    Object getObject(String name);

    /**
     * 根据键值获取类
     * @param name 键值
     * @return 返回类
     */
    Class<?> getClass(String name);

    class INSTANCE{
       public static ICacheManager getInstance(){
            return CacheManagerImpl.getCacheManager();
        }
    }

}
