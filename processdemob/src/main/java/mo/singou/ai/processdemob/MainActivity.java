package mo.singou.ai.processdemob;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.zbq.library.api.service.IServiceManager;
import com.zbq.library.model.Email;
import com.zbq.library.model.LoginBean;
import com.zbq.library.api.client.IClientManager;
import com.zbq.library.process.OnProcessConnectListener;
import com.zbq.library.process.ProcessManager;
import com.zbq.library.utils.JsonUtils;


public class MainActivity extends AppCompatActivity implements OnProcessConnectListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private IServiceManager serviceManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ProcessManager.getInstance().setOnProcessConnectListener(this);
        ClientManagerAdapter.getInstance().setOnRemoteMessageListener(new ClientManagerAdapter.OnRemoteMessageListener() {
            @Override
            public void onMessage(String message) {
                Log.d(TAG,"demo b onCallback receive message -- >"+message);
            }
        });
        ProcessManager.getInstance().regist(IClientManager.class);
    }

    public void connect(View view) {
        ProcessManager.getInstance().connect(this,"mo.singou.ai.processdemoa","mo.singou.ai.processdemoa.service.ProcessService");
    }


    @Override
    public void onConnected(String clzName) {
        Log.d(TAG,"onConnected --->"+clzName);
        serviceManager = ProcessManager.getInstance().getInstance(IServiceManager.class);
    }

    @Override
    public void onDisconnected(String clzName) {
        Log.d(TAG,"onConnected --->"+clzName);
        serviceManager = null;
    }

    public void send(View view) {
        Email responseBase = new Email();
        responseBase.eventId = 100001;
        responseBase.data = "{\"key\":\"abc\",\"value\":\"123\"}";
        responseBase.serial = ""+System.currentTimeMillis();
        serviceManager.sendAiuiMessage(JsonUtils.toJson(responseBase));
    }

    public void setListener(View view) {
        serviceManager = ProcessManager.getInstance().getInstance(IServiceManager.class);
    }

    public void disconnect(View view) {
        ProcessManager.getInstance().disconnect(this);
    }

    public void getLoginInfo(View view) {
        long time = System.currentTimeMillis();
        Log.e("zbq","begin:"+time);
        LoginBean loginBean = serviceManager.getLoginInfo();
        Log.e("zbq","end:"+System.currentTimeMillis()+",wastTime:"+(System.currentTimeMillis()-time));
        Log.e("zbq","result:"+loginBean.toString());
    }
}
