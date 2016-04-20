package com.haclep.ipctest;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.List;

public class BookManagerActivity extends AppCompatActivity {

    public static final String TAG = "BookManagerActivity";
    private static final int NEW_BOOK_ARRIVED = 1;
    private IBookManager remoteIBookManager;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case NEW_BOOK_ARRIVED:
                    Log.i(TAG, "新书到了" + msg.obj);
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    };
    //Stub继承了Binder也实现了IOnNewBookArrivedListener
    IOnNewBookArrivedListener mListener = new IOnNewBookArrivedListener.Stub() {
        @Override
        public void onNewBookArrived(Book newBook) throws RemoteException {

            mHandler.obtainMessage(NEW_BOOK_ARRIVED, newBook).sendToTarget();
        }
    };
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            IBookManager bookManager = IBookManager.Stub.asInterface(service);
            try {
                remoteIBookManager = bookManager;
                List<Book> books = bookManager.getBookList();
                Log.i(TAG, "查询图书列表,列表类型:" + books.getClass().getCanonicalName());
                Log.i(TAG, "图书列表" + books.toString());
                bookManager.registerListener(mListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }

        }


        @Override
        public void onServiceDisconnected(ComponentName name) {
            remoteIBookManager = null;
            Log.e(TAG, "binder死亡");
        }
    };

    /**
     * 绑定远程service
     * 成功连接后,将返回的Binder转换为AIDL接口,通过接口去调用服务端的方法
     * <p>
     * 创建Listener
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_manager);
        Intent intent = new Intent(this, BookManagerService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);


    }

    @Override
    protected void onDestroy() {

        if (remoteIBookManager != null && remoteIBookManager.asBinder().isBinderAlive()) {
            Log.i(TAG, "没有取消接收" + mListener);
            try {
                remoteIBookManager.unregisterListener(mListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        unbindService(mConnection);
        super.onDestroy();
    }


}
