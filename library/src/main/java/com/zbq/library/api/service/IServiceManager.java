package com.zbq.library.api.service;


import com.zbq.library.anotion.ClassId;
import com.zbq.library.model.LoginBean;

@ClassId("mo.singou.ai.processdemoa.service.ServiceManager")
public interface IServiceManager {
   void sendAiuiMessage(String json);

   LoginBean getLoginInfo();

   int u();
}
