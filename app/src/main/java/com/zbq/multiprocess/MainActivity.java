package com.zbq.multiprocess;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.zbq.library.IServiceManager;
import com.zbq.library.ServiceManager;
import com.zbq.library.bean.LoginBean;
import com.zbq.library.process.ProcessManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ProcessManager.getInstance().regist(IServiceManager.class);
    }

    public void goSecond(View view) {
        startActivity(new Intent(this,SecondActivity.class));
    }

}
