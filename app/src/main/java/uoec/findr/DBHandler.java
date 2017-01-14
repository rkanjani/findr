package uoec.findr;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tmetade on 2017-01-14.
 */
public class DBHandler extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "FindrDB";

    // Table name
    private static final String TABLE_POINT = "point";
    private static final String TABLE_RELATIONSHIP = "relationship";


    // Point Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_X_CORD = "xCord";
    private static final String KEY_Y_CORD = "yCord";
    private static final String KEY_FLOOR = "floor";

    //Relationship Table Columns names
    private static final String KEY_ID1 = "id1";
    private static final String KEY_ID2 = "id2";


    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_VERTEX_TABLE = "CREATE TABLE " + TABLE_POINT + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTO_INCREMENT,"
                + KEY_NAME + "TEXT,"
                + KEY_X_CORD + " INTEGER,"
                + KEY_Y_CORD + " INTEGER,"
                + KEY_FLOOR + "INTEGER" + ")";

        String CREATE_RELATIONSHIP_TABLE = "CREATE TABLE " + TABLE_RELATIONSHIP + "("
                + KEY_ID1 + "INTEGER,"
                + KEY_ID2 + "INTEGER,"
                + "FOREIGN KEY(" + KEY_ID1 + ") REFERENCES " + TABLE_POINT + "(" + KEY_ID + "),"
                + "FOREIGN KEY(" + KEY_ID2 + ") REFERENCES " + TABLE_POINT + "(" + KEY_ID + ") )";

        db.execSQL(CREATE_VERTEX_TABLE);
        db.execSQL(CREATE_RELATIONSHIP_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
// Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_POINT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RELATIONSHIP);
// Creating tables again
        onCreate(db);
    }

    void addPoint(Point point) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, point.getName()); // Point Name
        values.put(KEY_X_CORD, point.getXCord()); // Point xCord
        values.put(KEY_Y_CORD, point.getYCord()); // Point yCord
        values.put(KEY_FLOOR, point.getFloor()); // Point floor

        // Inserting Row
        db.insert(TABLE_POINT, null, values);
        db.close(); // Closing database connection
    }

    void addNeighbour(int id1, int id2) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID1, id1); // ID1
        values.put(KEY_ID2, id2); // ID2

        // Inserting Row
        db.insert(TABLE_RELATIONSHIP, null, values);
        db.close(); // Closing database connection
    }

    Point getPoint(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_POINT, new String[]{KEY_ID,
                        KEY_NAME, KEY_X_CORD, KEY_Y_CORD, KEY_FLOOR}, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }

        Point point = new Point(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getInt(2), cursor.getInt(3), cursor.getInt(4));
        // return contact
        return point;
    }

    public List<Point> getAllPoints() {
        List<Point> pointList = new ArrayList<Point>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_POINT;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Point point = new Point(0, null, 0, 0, 0);
                point.setId(Integer.parseInt(cursor.getString(0)));
                point.setName(cursor.getString(1));
                point.setXCord(cursor.getInt(2));
                point.setYCord(cursor.getInt(3));
                point.setFloor(cursor.getInt(4));
                // Adding contact to list
                pointList.add(point);
            } while (cursor.moveToNext());
        }

        // return contact list
        return pointList;
    }

    public void getAllNeighbours() {
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_RELATIONSHIP;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                int id1;
                int id2;
                id1 = Integer.parseInt(cursor.getString(0));
                id2 = Integer.parseInt(cursor.getString(1));

                Point point1 = getPoint(id1);
                point1.addNeighbour(id2);

                Point point2 = getPoint(id2);
                point1.addNeighbour(id1);

            } while (cursor.moveToNext());
        }
    }

}
