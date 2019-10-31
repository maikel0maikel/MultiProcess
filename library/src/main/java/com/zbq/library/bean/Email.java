package com.zbq.library.bean;


import android.os.Process;

public class Email {


    /**
     * 601-科大讯飞语音识别结果
     * 602-tts播报内容
     * 603-aiui开发板的唤醒事件
     * 604-发送图片给客户端
     * <p>
     * <p>
     * 101-tts结束播报
     * 102-导航状态
     */
    public int eventId;
    public String data;
    public String serial;
    public int mPid;
    public Email() {
        mPid = Process.myPid();
    }
    public Email(int eventId) {
        this.eventId = eventId;
        mPid = Process.myPid();
    }
    public Email(int eventId, String data) {
        this.eventId = eventId;
        this.data = data;
        mPid = Process.myPid();
    }
    public Email(int eventId, String data, String serial) {
        this.eventId = eventId;
        this.data = data;
        this.serial = serial;
        mPid = Process.myPid();
    }

    @Override
    public String toString() {
        return "Email{" +
                "eventId=" + eventId +
                ", data='" + data + '\'' +
                ", serial='" + serial + '\'' +
                '}';
    }
}

