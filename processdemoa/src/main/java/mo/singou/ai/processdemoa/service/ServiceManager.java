package mo.singou.ai.processdemoa.service;

import com.zbq.library.api.service.IServiceManager;
import com.zbq.library.model.LoginBean;

public abstract class ServiceManager implements IServiceManager {
    LoginBean loginBean ;
    ServiceManager(){
    }
    public static IServiceManager getInstance(){
        return ServiceManagerAdapter.getInstance();
    }
    public void setLoginBean(LoginBean loginBean){
        this.loginBean = loginBean;
    }
    @Override
    public void sendAiuiMessage(String json) {
        onClientSendMessage(json);
    }

    @Override
    public LoginBean getLoginInfo() {
        return loginBean;
    }

    @Override
    public int u() {
        return 0;
    }

    abstract void onClientSendMessage(String json);

}
