package com.zbq.library.bean;

/**
 * create by maikel
 * 请求参数实体
 */
public class RequestParams {
    /**参数类类型如java.lang.String***/
    private String paramClzName;
    /**参数值****/
    private String paramValue;

    /**
     * 无参构造函数
     */
    public RequestParams(){

    }

    /**
     * 带参数构造函数
     * @param paramClzName 参数类类型如java.lang.String
     * @param paramValue 参数值
     */
    public RequestParams(String paramClzName,String paramValue){
        this.paramClzName = paramClzName;
        this.paramValue = paramValue;
    }

    public String getParamClzName() {
        return paramClzName;
    }

    public void setParamClzName(String paramClzName) {
        this.paramClzName = paramClzName;
    }

    public String getParamValue() {
        return paramValue;
    }

    public void setParamValue(String paramValue) {
        this.paramValue = paramValue;
    }
}
