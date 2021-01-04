package mo.singou.ai.processdemoa;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.zbq.library.api.service.IServiceManager;
import com.zbq.library.model.Email;
import com.zbq.library.model.LoginBean;
import com.zbq.library.api.client.IClientManager;
import com.zbq.library.process.ProcessManager;
import com.zbq.library.utils.JsonUtils;

import mo.singou.ai.processdemoa.service.ServiceManagerAdapter;

public class MainActivity extends AppCompatActivity implements ServiceManagerAdapter.OnClientMessageListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //startService(new Intent(this, ProcessService.class));
        setContentView(R.layout.activity_main);
        ServiceManagerAdapter.getInstance().setClientMessageListener(this);
        ServiceManagerAdapter.getInstance().setLoginBean(new LoginBean("uyxxd","223333333"));
    }

    public void registerService(View view) {
        ProcessManager.getInstance().regist(IServiceManager.class);
    }

    @Override
    public void onMessage(String json) {
        Log.d(TAG,"onMessage ---- >"+json);

        Email responseBase = JsonUtils.parse(json,Email.class);
        if (responseBase!=null){
            Email result = new Email();
            result.serial = String.valueOf(System.currentTimeMillis());
            result.data = "{}";
            result.eventId = 333333333;
            IClientManager clientManager = ProcessManager.getInstance().getInstance(IClientManager.class);
            clientManager.onCallback(JsonUtils.toJson(result));
            clientManager.notify(19);
        }
    }
}
