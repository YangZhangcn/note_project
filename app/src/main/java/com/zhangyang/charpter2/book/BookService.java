package com.zhangyang.charpter2.book;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhang on 2017/10/31.
 */

public class BookService extends Service {

    private ArrayList<Book> books = new ArrayList<>();

    IBookManager.Stub mBinder = new IBookManager.Stub(){

        @Override
        public void addBook(Book book) throws RemoteException {
            books.add(book);
        }

        @Override
        public List<Book> getBookList() throws RemoteException {
            return books;
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
}
