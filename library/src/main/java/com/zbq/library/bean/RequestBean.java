package com.zbq.library.bean;


/**
 * create by maikel
 * 请求实体类，模拟http请求
 *
 */
public class RequestBean {
    /**請求類型******/
    private int type;
    /**类名**/
    private String className;
    /**需要调用的方法名**/
    private String methodName;
    /**所需參數***/
    private RequestParams[] requestParams;

    /**
     * 无参构造函数
      */
    public RequestBean(){

    }

    /**
     * 有参构造函数
     * @param type 请求类型 1：单例 2：方法
     * @param className 类名
     * @param methodName 方法名
     * @param params 方法参数
     */
    public RequestBean(int type, String className,String methodName,RequestParams...params){
        this.type = type;
        this.className = className;
        this.methodName = methodName;
        this.requestParams = params;
    }

    /**
     * 获取请求类型
     * @return 返回请求类型int值 1：单例 2：方法
     */
    public int getType() {
        return type;
    }

    /**
     * 设置请求类型
     * @param type 请求类型 1：单例 2：方法
     */
    public void setType(int type) {
        this.type = type;
    }

    /**
     * 获取类名
     * @return 返回字符串类名
     */
    public String getClassName() {
        return className;
    }

    /**
     * 设置类名
     * @param className 类名如java.lang.Object
     */
    public void setClassName(String className) {
        this.className = className;
    }

    /**
     * 获取方法名
     * @return 返回方法名字符串
     */
    public String getMethodName() {
        return methodName;
    }

    /**
     * 设置方法名
     * @param methodName 方法名
     */
    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    /**
     * 请求参数
     * @return 参数数组 {@link RequestParams}
     */
    public RequestParams[] getRequestParams() {
        return requestParams;
    }

    /**
     * 设置请求参数
     * @param requestParams 参数数组 {@link RequestParams}
     */
    public void setRequestParams(RequestParams[] requestParams) {
        this.requestParams = requestParams;
    }
}
