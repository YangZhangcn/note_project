package com.zhangyang.charpter2.book;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhang on 2017/10/31.
 */

public class BookService extends Service {

    private ArrayList<Book> books = new ArrayList<>();

    private RemoteCallbackList<IOnNewBookArrivedListener> listeners = new RemoteCallbackList<>();

    IBookManager.Stub mBinder = new IBookManager.Stub(){

        @Override
        public void addBook(Book book) throws RemoteException {
            books.add(book);
            final int n = listeners.beginBroadcast();
            for (int i = 0; i< n; i++){
                IOnNewBookArrivedListener broadcastItem = listeners.getBroadcastItem(i);
                broadcastItem.onNewBookArrived(book);
            }
            listeners.finishBroadcast();
        }

        @Override
        public List<Book> getBookList() throws RemoteException {
            return books;
        }

        @Override
        public void registerListener(IOnNewBookArrivedListener listener) throws RemoteException {
            listeners.register(listener);
        }

        @Override
        public void unRegisterListener(IOnNewBookArrivedListener listener) throws RemoteException {
            listeners.unregister(listener);
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
}
