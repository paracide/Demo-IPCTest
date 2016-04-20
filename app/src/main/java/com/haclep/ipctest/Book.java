package com.haclep.ipctest;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Paracide on 2016/4/13.
 * 对象是无法跨进程传说U盾,所以需要实现Parcelable
 */
public class Book implements Parcelable {
    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };
    public String bookName;
    private int bookId;

    public Book(int id, String name) {
        bookId = id;
        bookName = name;
    }

    protected Book(Parcel in) {
        bookName = in.readString();
        bookId = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(bookName);
        dest.writeInt(bookId);
    }

    @Override
    public String toString() {
        return String.format("[bookId:%s, bookName:%s]", bookId, bookName);
    }

}
