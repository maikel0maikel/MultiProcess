package com.zbq.library;

import com.zbq.library.anotion.ClassId;
import com.zbq.library.bean.LoginBean;
@ClassId("com.zbq.library.ServiceManager")
public interface IServiceManager {

    LoginBean getLoginInfo();

    void setLoginInfo(LoginBean loginInfo);
}
