package mo.singou.ai.processdemoa.service;


import android.os.RemoteException;
import android.util.Log;

import com.zbq.library.model.Request;
import com.zbq.library.model.Response;
import com.zbq.library.internal.IProcessCallback;
import com.zbq.library.internal.IProcessService;
import com.zbq.library.utils.ProcessUtils;

import static android.content.ContentValues.TAG;

public class ProcessServiceManager extends IProcessService.Stub {

    private static Stub process;

    private ProcessServiceManager() {
    }

    static Stub getProcess() {
        if (process == null) {
            synchronized (ProcessServiceManager.class) {
                if (process == null) {
                    process = new ProcessServiceManager();
                }
            }
        }
        return process;
    }

    @Override
    public void register(IProcessCallback callback, int pid) throws RemoteException {
        ServiceManagerAdapter.getInstance().registerCallback(pid,callback);
        callback.asBinder().linkToDeath(new ClientDeath(pid),0);
    }

    @Override
    public Response send(Request request) throws RemoteException {
      return ProcessUtils.getResponse(request);
    }
    class ClientDeath implements DeathRecipient {
        private int mClientPid;
        ClientDeath(int pid){
            mClientPid = pid;
        }
        @Override
        public void binderDied() {
            Log.e(TAG,"binderDied client is death---->mClientPid = "+mClientPid);
            ServiceManagerAdapter.getInstance().unRegisterCallback(mClientPid);
        }
    }
}
