package com.zbq.library.process;

import com.zbq.library.anotion.ClassId;

@ClassId("com.zbq.library.process.ClientManager")
public interface IClientManager {
    void onCallback(String json);
}
