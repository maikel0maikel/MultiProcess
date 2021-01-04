package com.zbq.library.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.zbq.library.utils.JsonUtils;

/**
 * create by maikel
 * 响应实体
 */
public class Response implements Parcelable {
    /**响应的对象实例***/
    private String clazz;
    private Object response;

    public Response(){}

    protected Response(Parcel in) {
        clazz = in.readString();
        try {
            response = JsonUtils.parse(in.readString(), Class.forName(clazz));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        if (response == null){
            response = new Object();
        }
    }

    public static final Creator<Response> CREATOR = new Creator<Response>() {
        @Override
        public Response createFromParcel(Parcel in) {
            return new Response(in);
        }

        @Override
        public Response[] newArray(int size) {
            return new Response[size];
        }
    };

    public Object getResponse() {
        return response;
    }

    public void setResponse(Object response) {
        this.response = response;
    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(clazz);
        parcel.writeString(JsonUtils.toJson(response));
    }
}
