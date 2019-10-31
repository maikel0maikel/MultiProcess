package com.zbq.library.service;


import android.os.RemoteException;

import com.zbq.library.ServiceManagerAdapter;
import com.zbq.library.bean.Request;
import com.zbq.library.bean.Response;
import com.zbq.library.internal.IProcessCallback;
import com.zbq.library.internal.IProcessService;
import com.zbq.library.utils.ProcessUtils;


public class ProcessServiceManager extends IProcessService.Stub {

    private static IProcessService.Stub process;

    private ProcessServiceManager() {
    }

    static IProcessService.Stub getProcess() {
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
    }

    @Override
    public Response send(Request request) throws RemoteException {
      return ProcessUtils.getResponse(request);
    }

}
