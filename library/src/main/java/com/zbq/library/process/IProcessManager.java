package com.zbq.library.process;

import android.content.Context;

/**
 * create by maikel
 * 多进程管理接口
 */
public interface IProcessManager {
    /**
     * 把一个类进行注册
     * @param serviceClz
     */
    void regist(Class<?> serviceClz);

    /**
     * 绑定服务进行连接
     * @param context 上下文
     */
    void connect(Context context);

    /**
     * 绑定服务进行连接
     * @param context 上下文
     * @param packageName 包名
     */
    void connect(Context context,String packageName);

    /**
     * 获取单例
     * @param clz 服务类类型
     * @param params 参数数组
     * @param <T> 实体
     * @return
     */
    <T> T getInstance(Class<T> clz,Object ... params);
}
