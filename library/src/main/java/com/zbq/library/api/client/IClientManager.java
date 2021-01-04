package com.zbq.library.api.client;

import com.zbq.library.anotion.ClassId;

@ClassId("com.zbq.library.process.ClientManager")
public interface IClientManager {
    void onCallback(String json);
    void notify(int y);
}
