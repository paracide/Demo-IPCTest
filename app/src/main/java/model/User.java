package model;

import android.os.Parcel;
import android.os.Parcelable;

import com.haclep.ipctest.Book;

/**
 * Created by Paracide on 2016/4/13.
 */
public class User implements Parcelable {
    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
    public int userId;
    public String userName;
    public Boolean isMale;
    public Book book;

    protected User(Parcel in) {
        userId = in.readInt();
        userName = in.readString();
        book = in.readParcelable(Book.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(userId);
        dest.writeString(userName);
        dest.writeParcelable(book, flags);
    }
}
