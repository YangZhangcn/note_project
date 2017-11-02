// IBookManager.aidl
package com.zhangyang.charpter2.book;

import com.zhangyang.charpter2.book.Book;
import com.zhangyang.charpter2.book.IOnNewBookArrivedListener;

// Declare any non-default types here with import statements

interface IBookManager {

    void addBook(in Book book);

    List<Book> getBookList();

    void registerListener( IOnNewBookArrivedListener listener);

    void unRegisterListener( IOnNewBookArrivedListener listener);
}
