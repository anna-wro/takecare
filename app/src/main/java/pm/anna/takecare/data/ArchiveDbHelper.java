package pm.anna.takecare.data;

/**
 * Created by Anna on 18.02.2017.
 */


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import pm.anna.takecare.data.ArchiveContract.ArchiveEntry;


public class ArchiveDbHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = ArchiveDbHelper.class.getSimpleName();

    private static final String DATABASE_NAME = "archive.db";
    private static final int DATABASE_VERSION = 1;

    public ArchiveDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_ARCHIVE_TABLE = "CREATE TABLE " + ArchiveEntry.TABLE_NAME + " ("
                + ArchiveEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ArchiveEntry.COLUMN_DATE + " TEXT NOT NULL, "
                + ArchiveEntry.COLUMN_POINTS_ALL + " INTEGER NOT NULL DEFAULT 0, "
                + ArchiveEntry.COLUMN_POINTS_BODY + " INTEGER DEFAULT 0, "
                + ArchiveEntry.COLUMN_POINTS_MIND + " INTEGER DEFAULT 0, "
                + ArchiveEntry.COLUMN_POINTS_SOUL + " INTEGER DEFAULT 0);";

        db.execSQL(SQL_CREATE_ARCHIVE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}