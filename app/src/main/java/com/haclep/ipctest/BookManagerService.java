package com.haclep.ipctest;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.util.Log;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;


/**
 * 服务端需要在被客户端连上的时候返回一个Binder
 * binder实现了了AIDL建立的接口,并且覆写了2个方法
 */


public class BookManagerService extends Service {
    private static final String TAG = "BookManagerService";
    private CopyOnWriteArrayList<Book> mBooks = new CopyOnWriteArrayList<Book>();
    private RemoteCallbackList<IOnNewBookArrivedListener> mListenersList = new RemoteCallbackList<>();
    Binder mBinder = new IBookManager.Stub() {

        @Override
        public List<Book> getBookList() throws RemoteException {
            return mBooks;
        }

        @Override
        public void addBook(Book book) throws RemoteException {
            mBooks.add(book);
        }

        @Override
        public void registerListener(IOnNewBookArrivedListener listener) throws RemoteException {

            mListenersList.register(listener);
        }

        @Override
        public void unregisterListener(IOnNewBookArrivedListener listener) throws RemoteException {
            mListenersList.unregister(listener);
        }
    };
    private AtomicBoolean mIsServiceDestroyed = new AtomicBoolean(false);

    public BookManagerService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mBooks.add(new Book(1, "android"));
        mBooks.add(new Book(2, "google"));
        new Thread(new ServiceWorker()).start();
    }

    /**
     * 每当新加了数,通知list中的Listener更新
     */
    private void onNewBookArrived(Book book) {
        mBooks.add(book);
        Log.i(TAG, "新书到啦");
        final int N = mListenersList.beginBroadcast();
        for (int i = 0; i <= N - 1; i++) {
            IOnNewBookArrivedListener listener = mListenersList.getBroadcastItem(i);
            try {
                listener.onNewBookArrived(book);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        mListenersList.finishBroadcast();

    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    /**
     * 每5秒新加一次数的线程
     * 这里用while 而不是if!!!
     */
    class ServiceWorker implements Runnable {

        @Override
        public void run() {
            while (!mIsServiceDestroyed.get()) {
                try {
                    Thread.sleep(5000);
                    int bookId = mBooks.size() + 1;
                    Book newBook = new Book(bookId, "新书#");
                    onNewBookArrived(newBook);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }
    }

}




