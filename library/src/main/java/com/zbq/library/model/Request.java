package com.zbq.library.model;


import android.os.Parcel;
import android.os.Parcelable;
import android.os.Process;

/**
 * create by maikel
 * 请求实体类，模拟http请求
 *
 */
public class Request implements Parcelable {
    /**請求類型******/
    private int type;
    /**类名**/
    private String className;
    /**需要调用的方法名**/
    private String methodName;
    /**所需參數***/
    private RequestParams[] requestParams;

    private int mPid;
    /**
     * 无参构造函数
      */
    public Request(){
        mPid = Process.myPid();
    }

    /**
     * 有参构造函数
     * @param type 请求类型 1：单例 2：方法
     * @param className 类名
     * @param methodName 方法名
     * @param params 方法参数
     */
    public Request(int type, String className, String methodName, RequestParams...params){
        this.type = type;
        this.className = className;
        this.methodName = methodName;
        this.requestParams = params;
        this.mPid = Process.myPid();
    }

    protected Request(Parcel in) {
        type = in.readInt();
        className = in.readString();
        methodName = in.readString();
        ClassLoader classLoader = Request.class.getClassLoader();
        Parcelable[] parcelables = in.readParcelableArray(classLoader);
        if (parcelables == null) {
            requestParams = null;
        } else {
            int length = parcelables.length;
            requestParams = new RequestParams[length];
            for (int i = 0; i < length; ++i) {
                requestParams[i] = (RequestParams) parcelables[i];
            }
        }
        mPid = in.readInt();
    }

    public static final Creator<Request> CREATOR = new Creator<Request>() {
        @Override
        public Request createFromParcel(Parcel in) {
            return new Request(in);
        }

        @Override
        public Request[] newArray(int size) {
            return new Request[size];
        }
    };

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

    public int getPid() {
        return mPid;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(type);
        parcel.writeString(className);
        parcel.writeString(methodName);
        parcel.writeParcelableArray(requestParams, i);
        parcel.writeInt(mPid);
    }
}
