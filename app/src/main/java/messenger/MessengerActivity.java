package messenger;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.haclep.ipctest.R;

import utils.MyConstants;

public class MessengerActivity extends AppCompatActivity {


    private static final String TAG = "MessengerActivity";
    private Messenger mMessenger;//用于发送给Service Message的Messenger
    /**
     * 用于接收来自Service的message,并用Handler处理
     */
    private Messenger mGetReplyMessenger = new Messenger(new MessengerHandler());
    private ServiceConnection mConnection = new ServiceConnection() {

        /**
         * 当这个Activity与Service连接上后回调这个函数
         * 通过Service返回的IBinder创建Messenger
         * 用Message.obtain创建一个message,并且赋值message.what为 MyConstants.MSG_FROM_CLIENT)
         * 创建一个Bundle放入一段String,再把Bundle放到msg中
         * 用Messenger发送msg
         * @param name
         * @param service
         */

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mMessenger = new Messenger(service);

            /**
             *创建一个message用于存储Bundle发送给服务端
             *mMessage.replyTo是一个messenger,放入mGetReplyMessenger. 随message一起发送到服务端,服务端取出用于发回消息
             */
            Message mMessage = Message.obtain(null, MyConstants.MSG_FROM_CLIENT);
            Bundle bundle = new Bundle();
            bundle.putString("msg", "这里是客户端");
            mMessage.setData(bundle);


            mMessage.replyTo = mGetReplyMessenger;
            try {
                mMessenger.send(mMessage);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mConnection);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messenger);

        //Intent intent = new Intent("com.haclep.MessengerService.launch");
        Intent intent = new Intent(this, MessengerService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    private static class MessengerHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MyConstants.MSG_FROM_SERVICE:
                    Log.i(TAG, "收到啦来自服务端的消息" + msg.getData().get("reply"));
                    break;
                default:
                    super.handleMessage(msg);
            }


        }
    }

}
