// IOnNewBookArrivedListener.aidl
package com.haclep.ipctest;

import com.haclep.ipctest.Book;

interface IOnNewBookArrivedListener {

void onNewBookArrived(in Book newbook);
}
