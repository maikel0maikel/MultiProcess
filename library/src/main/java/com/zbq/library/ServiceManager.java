package com.zbq.library;

import com.zbq.library.bean.LoginBean;

public class ServiceManager implements IServiceManager{

    private LoginBean loginBean;

    private static IServiceManager instance  ;


    public static IServiceManager getInstance(){
        if (instance == null){
            synchronized (ServiceManager.class){
                if (instance == null){
                    instance = new ServiceManager();
                }
            }
        }
        return instance;
    }

    @Override
    public LoginBean getLoginInfo() {

        return loginBean;
    }

    @Override
    public void setLoginInfo(LoginBean loginInfo) {
        loginBean = loginInfo;
    }

}
