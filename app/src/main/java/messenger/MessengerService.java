package messenger;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import utils.MyConstants;

public class MessengerService extends Service {

    private static final String TAG = "MessengerService";
    /**
     * 通过MessengerService内部的MessengerHandler来创建一个Messenger
     */
    private final Messenger mMessenger = new Messenger(new MessengerHandler());

    public MessengerService() {
    }

    /**
     * 当Service被绑定时,返回一个IBinder
     *
     * @param intent
     * @return
     */
    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }

    /**
     * 创建一个Handler来处理客户端发来的消息
     * 通过传来的msg.what判断
     */
    private static class MessengerHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MyConstants.MSG_FROM_CLIENT:
                    Log.i(TAG, "接受来自客户端的消息" + msg.getData().get("msg"));
                    Messenger client = msg.replyTo;
                    Message replyMessage = Message.obtain(null, MyConstants.MSG_FROM_SERVICE);
                    Bundle bundle = new Bundle();
                    bundle.putString("replay", "渣渣,收到啦");
                    replyMessage.setData(bundle);
                    try {
                        client.send(replyMessage);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    super.handleMessage(msg);
            }

        }
    }
}
