// IBookManager.aidl
package com.haclep.ipctest;

// Declare any non-default types here with import statements

import com.haclep.ipctest.Book;
import com.haclep.ipctest.IOnNewBookArrivedListener;

interface IBookManager {

    List<Book> getBookList();
    void addBook(in Book book);
    void registerListener(IOnNewBookArrivedListener listener);
    void unregisterListener(IOnNewBookArrivedListener listener);

}
