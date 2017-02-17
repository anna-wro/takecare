package pm.anna.takecare;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.Date;

/**
 * Created by Anna on 17.02.2017.
 */

public class ArchiveDbAdapter {
    private static final String DEBUG_TAG = "SqLiteArchiveManager";
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "database.db";

    private static final String DB_ARCHIVE_TABLE = "archive";
    public static final String KEY_ID = "_id";
    public static final String ID_OPTIONS = "INTEGER PRIMARY KEY AUTOINCREMENT";
    public static final int ID_COLUMN = 0;
    public static final String KEY_DATE = "date";
    public static final String DATE_OPTIONS = "DATE DEFAULT CURRENT_DATE NOT NULL";
    public static final int DATE_COLUMN = 1;
    public static final String KEY_POINTS = "points";
    public static final String POINTS_OPTIONS = "INTEGER DEFAULT 0 NOT NULL";
    public static final int POINTS_COLUMN = 2;

    private SQLiteDatabase db;
    private Context context;
    private DatabaseHelper dbHelper;

    private static final String DB_CREATE_ARCHIVE_TABLE =
            "CREATE TABLE " + DB_ARCHIVE_TABLE + "( " +
                    KEY_ID + " " + ID_OPTIONS + ", " +
                    KEY_DATE + " " + DATE_OPTIONS + ", " +
                    KEY_POINTS + " " + POINTS_OPTIONS +
                    ");";
    private static final String DROP_ARCHIVE_TABLE =
            "DROP TABLE IF EXISTS " + DB_ARCHIVE_TABLE;

    private static class DatabaseHelper extends SQLiteOpenHelper {
        public DatabaseHelper(Context context, String name,
                              SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DB_CREATE_ARCHIVE_TABLE);

            Log.d(DEBUG_TAG, "Database creating...");
            Log.d(DEBUG_TAG, "Table " + DB_ARCHIVE_TABLE + " ver." + DB_VERSION + " created");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(DROP_ARCHIVE_TABLE);
            Log.d(DEBUG_TAG, "Database updating...");
            Log.d(DEBUG_TAG, "Table " + DB_ARCHIVE_TABLE + " updated from ver." + oldVersion + " to ver." + newVersion);
            Log.d(DEBUG_TAG, "All data is lost.");
            onCreate(db);
        }
    }

    public ArchiveDbAdapter(Context context) {
        this.context = context;
    }
    public ArchiveDbAdapter open(){
        dbHelper = new DatabaseHelper(context, DB_NAME, null, DB_VERSION);
        try {
            db = dbHelper.getWritableDatabase();
        } catch (SQLException e) {
            db = dbHelper.getReadableDatabase();
        }
        return this;
    }
    public void close() {
        dbHelper.close();
    }

    public long insertArchiveItem(Date date, int points) {
        ContentValues newArchiveValues = new ContentValues();
        newArchiveValues.put(KEY_DATE, String.valueOf(date));
        newArchiveValues.put(KEY_POINTS, points);
        return db.insert(DB_ARCHIVE_TABLE, null, newArchiveValues);
    }

    public boolean updateArchive(ArchiveItem item) {
        long id = item.getId();
        String date  = item.getDate();
        int points = item.getPoints();
        return updateArchive(id, date, points);
    }

    public boolean updateArchive(long id, String date, int points) {
        String where = KEY_ID + "=" + id;
        ContentValues updateArchiveValues = new ContentValues();
        updateArchiveValues.put(KEY_DATE, date);
        updateArchiveValues.put(KEY_POINTS, points);
        return db.update(DB_ARCHIVE_TABLE, updateArchiveValues, where, null) > 0;
    }
    public boolean deleteArchiveItem(long id){
        String where = KEY_ID + "=" + id;
        return db.delete(DB_ARCHIVE_TABLE, where, null) > 0;
    }
    public Cursor getAllItems() {
        String[] columns = {KEY_ID, KEY_DATE, KEY_POINTS};
        return db.query(DB_ARCHIVE_TABLE, columns, null, null, null, null, null);
    }

    public ArchiveItem getItem(long id) {
        String[] columns = {KEY_ID, KEY_DATE, KEY_POINTS};
        String where = KEY_ID + "=" + id;
        Cursor cursor = db.query(DB_ARCHIVE_TABLE, columns, where, null, null, null, null);
        ArchiveItem item = null;
        if(cursor != null && cursor.moveToFirst()) {
            String date = cursor.getString(DATE_COLUMN);
            int points = cursor.getInt(POINTS_COLUMN);
            item = new ArchiveItem(id, date, points);
        }
        return item;
    }
}
