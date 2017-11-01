package com.zhangyang.charpter2.book;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import com.zhangyang.charpter2.utils.Constants;

/**
 * Created by zhang on 2017/11/1.
 */

public class BookService2 extends Service {



    private static class MessengerHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case Constants.MSG_FROM_CLIENT:
                    Log.i("Service2",msg.getData().getString("msg"));
                    Messenger replyTo = msg.replyTo;
                    Message replyMessage = Message.obtain(null,Constants.MSG_FROM_SERVICE);
                    Bundle bundle = new Bundle();
                    bundle.putString("reply","msg received!");
                    replyMessage.setData(bundle);
                    try {
                        replyTo.send(msg);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }

    private Messenger msg = new Messenger(new MessengerHandler());

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return msg.getBinder();
    }
}
