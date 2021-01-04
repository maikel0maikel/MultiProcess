package com.zbq.library.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * create by maikel
 * 请求参数实体
 */
public class RequestParams implements Parcelable {
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

    protected RequestParams(Parcel in) {
        paramClzName = in.readString();
        paramValue = in.readString();
    }

    public static final Creator<RequestParams> CREATOR = new Creator<RequestParams>() {
        @Override
        public RequestParams createFromParcel(Parcel in) {
            return new RequestParams(in);
        }

        @Override
        public RequestParams[] newArray(int size) {
            return new RequestParams[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(paramClzName);
        parcel.writeString(paramValue);
    }
}
