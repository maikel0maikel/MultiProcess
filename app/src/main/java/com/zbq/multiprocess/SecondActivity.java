package com.zbq.multiprocess;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.zbq.library.IServiceManager;
import com.zbq.library.bean.LoginBean;
import com.zbq.library.process.ProcessManager;

public class SecondActivity extends Activity {

    IServiceManager serviceManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        ProcessManager.getInstance().connect(this);



    }

    public void setInfo(View view) {
        serviceManager.setLoginInfo(new LoginBean("zbq","698562455554444"));
    }


    public void getInfo(View view) {
        serviceManager = ProcessManager.getInstance().getInstance(IServiceManager.class);
        LoginBean bean = serviceManager.getLoginInfo();
        Toast.makeText(this, "pwd=" + bean.getPassword() + ",name=" + bean.getUserName(), Toast.LENGTH_SHORT).show();
    }
}
