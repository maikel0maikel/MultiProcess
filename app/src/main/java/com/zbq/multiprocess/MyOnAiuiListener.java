package com.zbq.multiprocess;

import android.util.Log;

import com.zbq.library.OnAiuiMessageListener;


public class MyOnAiuiListener implements OnAiuiMessageListener {
    @Override
    public void onMessage(String message) {
        Log.d("MyOnAiuiListener","json "+message);
    }
}
