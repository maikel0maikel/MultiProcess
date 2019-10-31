package com.zbq.multiprocess;

import android.app.Activity;
import android.os.Bundle;
import android.os.Process;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.zbq.library.IServiceManager;
import com.zbq.library.OnAiuiMessageListener;
import com.zbq.library.bean.LoginBean;
import com.zbq.library.process.ProcessManager;

public class SecondActivity extends Activity {

    IServiceManager serviceManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        ProcessManager.getInstance().connect(this);
        //serviceManager = ProcessManager.getInstance().getInstance(IServiceManager.class);
//        serviceManager.setOnAiuiListener(this);
    }

    public void setInfo(View view) {
        serviceManager = ProcessManager.getInstance().getInstance(IServiceManager.class);
        //serviceManager.setOnAiuiMessageListener(new MyOnAiuiListener(), Process.myPid());
    }


    public void getInfo(View view) {
        serviceManager = ProcessManager.getInstance().getInstance(IServiceManager.class);
//        OnAiuiMessageListener listener = serviceManager.getListener(Process.myPid());
//        Log.d("SecondActivity",""+listener);
//        if (listener!=null)listener.onMessage("me call me");
    }

}
