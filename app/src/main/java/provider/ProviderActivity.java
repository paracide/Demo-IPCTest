package provider;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.haclep.ipctest.Book;
import com.haclep.ipctest.R;

public class ProviderActivity extends AppCompatActivity {

    private static final String TAG = "ProviderActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider);

        Uri BookUri = Uri.parse("content://com.haclep.ipctest.book.provider/book");
        ContentValues values = new ContentValues();
        values.put("_id", 5);
        values.put("name", "湖马传奇");
        getContentResolver().insert(BookUri, values);
        Cursor cursor = getContentResolver().query(BookUri, new String[]{"_id", "name"}, null, null, null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            Book book = new Book(id, name);
            Log.i(TAG, "当前书:" + book.toString());
        }
        cursor.close();

    }
}
