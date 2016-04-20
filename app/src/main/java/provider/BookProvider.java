package provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class BookProvider extends ContentProvider {


    private static final String AUTHORITY = "com.haclep.ipctest.book.provider";
    public static final Uri BOOK_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/book");
    public static final Uri USER_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/user");
    private static final int BOOK_URI_CODE = 0;
    private static final int USER_URI_CODE = 1;
    private static final UriMatcher mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        mUriMatcher.addURI(AUTHORITY, "book", BOOK_URI_CODE);
        mUriMatcher.addURI(AUTHORITY, "user", USER_URI_CODE);
    }

    private SQLiteDatabase mDB;
    private Context mContext;

    public BookProvider() {
    }

    private String getTableName(Uri uri) {
        String TableName = null;
        switch (mUriMatcher.match(uri)) {
            case BOOK_URI_CODE:
                TableName = DbOpenHelper.BOOK_TABLE_NAME;
                break;
            case USER_URI_CODE:
                TableName = DbOpenHelper.USER_TABLE_NAME;
                break;
            default:
                break;
        }
        return TableName;
    }

    private void initProviderData() {
        mDB = new DbOpenHelper(mContext).getWritableDatabase();
        mDB.execSQL("DELETE FROM " + DbOpenHelper.BOOK_TABLE_NAME);
        mDB.execSQL("DELETE FROM " + DbOpenHelper.USER_TABLE_NAME);
        mDB.execSQL("INSERT into Book VALUES(3,'胖轩是怎么练成的');");
        mDB.execSQL("INSERT INTO Book VALUES(4,'胖轩是胖');");
        mDB.execSQL("INSERT INTO User VALUES(1,'Paracide');");
        mDB.execSQL("INSERT INTO User VALUES(2,'haclep');");
    }

    @Override
    public boolean onCreate() {
        // TODO: Implement this to initialize your content provider on startup.
        mContext = getContext();
        initProviderData();
        return true;

    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        String TableName = getTableName(uri);
        if (TableName == null) {
            throw new IllegalArgumentException("无法解析URI" + uri);
        }
        int count = mDB.delete(TableName, selection, selectionArgs);
        if (count > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return count;
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        String TableName = getTableName(uri);
        if (TableName == null) {
            throw new IllegalArgumentException("无法解析URI" + uri);
        }
        mDB.insert(TableName, null, values);
        getContext().getContentResolver().notifyChange(uri, null);
        return uri;
    }


    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        String TableName = getTableName(uri);
        if (TableName == null) {
            throw new IllegalArgumentException("无法解析URI" + uri);
        }
        return mDB.query(TableName, projection, selection, selectionArgs, null, null, sortOrder, null);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        String TableName = getTableName(uri);
        if (TableName == null) {
            throw new IllegalArgumentException("无法解析URI" + uri);
        }
        int count = mDB.update(TableName, values, selection, selectionArgs);
        if (count > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return count;
    }
}
