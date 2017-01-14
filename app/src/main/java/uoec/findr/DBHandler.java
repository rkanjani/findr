package uoec.findr;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by tmetade on 2017-01-14.
 */
public class DBHandler extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "FindrDB";
    // Contacts table name
    private static final String TABLE_POINT = "point";
    // Shops Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_X_CORD = "xCord";
    private static final String KEY_Y_CORD = "yCord";
    private static final String KEY_QR_HASH = "qrHash";
    private static final String KEY_FLOOR = "floor";

    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_POINT + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + "TEXT,"
                + KEY_X_CORD + " INTEGER" + KEY_Y_CORD + " INTEGER" + KEY_QR_HASH + " TEXT" + KEY_FLOOR + "INTEGER" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
// Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_POINT);
// Creating tables again
        onCreate(db);
    }
}
