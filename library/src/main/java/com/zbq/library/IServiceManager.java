package com.zbq.library;

import com.zbq.library.anotion.ClassId;
@ClassId("com.zbq.library.ServiceManager")
public interface IServiceManager {

    void sendAiuiMessage(String json);

    void setOnAiuiMessageListener(OnAiuiMessageListener listener,int pid);

    OnAiuiMessageListener getListener(int pid);
}
