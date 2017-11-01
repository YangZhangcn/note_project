package com.zhangyang.charpter2.book;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhang on 2017/11/1.
 */

public class BookService1 extends Service {

    private ArrayList<Book> books = new ArrayList<>();

    private MyBookManager.Stub mBinder = new MyBookManager.Stub() {
        @Override
        public void addBook(Book book) throws RemoteException {
            books.add(book);
        }

        @Override
        public List<Book> getBooks() throws RemoteException {
            return books;
        }
    };


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
}
