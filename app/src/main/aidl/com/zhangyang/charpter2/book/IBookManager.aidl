// IBookManager.aidl
package com.zhangyang.charpter2.book;

import com.zhangyang.charpter2.book.Book;

// Declare any non-default types here with import statements

interface IBookManager {

    void addBook(in Book book);

    List<Book> getBookList();
}
