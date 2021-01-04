package com.zbq.library.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * create by maikel
 * 实体登录类
 */
public class LoginBean implements Parcelable {
    /**用户名***/
    private String userName;
    /**密码****/
    private String password;

    /**
     * 带参数构造方法
     * @param userName 用户名
     * @param password 密码
     */
    public LoginBean(String userName,String password){
        this.userName = userName;
        this.password = password;
    }

    /**
     * 无参构造函数
     */
    public LoginBean(){}

    protected LoginBean(Parcel in) {
        userName = in.readString();
        password = in.readString();
    }

    public static final Creator<LoginBean> CREATOR = new Creator<LoginBean>() {
        @Override
        public LoginBean createFromParcel(Parcel in) {
            return new LoginBean(in);
        }

        @Override
        public LoginBean[] newArray(int size) {
            return new LoginBean[size];
        }
    };

    /**
     * 获取用户名
     * @return 返回用户名
     */
    public String getUserName() {
        return userName;
    }

    /**
     * 设置用户名
     * @param userName 用户名
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * 获取密码
     * @return 返回字符串密码
     */
    public String getPassword() {
        return password;
    }

    /**
     * 设置密码
     * @param password 密码字符串
     */
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "LoginBean{" +
                "userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(userName);
        parcel.writeString(password);
    }
}
