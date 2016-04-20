package provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Paracide on 2016/4/16.
 */
public class DbOpenHelper extends SQLiteOpenHelper {

    //这里定义了全局的String是为了方便管理,在Provider中也需要NAME的值
    public static final String BOOK_TABLE_NAME = "book";
    public static final String USER_TABLE_NAME = "user";
    private static final String DB_NAME = "bookProvider.db";
    private static final int DB_VERSiON = 1;
    /**
     * Book表和USER表的SQL的创建语句
     */

    private static final String CREATE_BOOK_TABLE = "CREATE TABLE IF NOT EXISTS " + BOOK_TABLE_NAME + "(_id INTEGER PRIMARY KEY,name TEXT)";
    private static final String CREATE_USER_TABLE = "CREATE TABLE IF NOT EXISTS " + USER_TABLE_NAME + "(_id INTEGER PRIMARY KEY,name TEXT)";


    public DbOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSiON);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_BOOK_TABLE);
        db.execSQL(CREATE_USER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
