package pm.anna.takecare.data;

/**
 * Created by Anna on 19.02.2017.
 */

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import pm.anna.takecare.data.ArchiveContract.ArchiveEntry;

import static pm.anna.takecare.data.ArchiveContract.CONTENT_AUTHORITY;

/**
 * {@link ContentProvider}
 */

public class ArchiveProvider extends ContentProvider {

    public static final String LOG_TAG = ArchiveProvider.class.getSimpleName();
    private static final int ARCHIVE = 100;
    private static final int ARCHIVE_ID = 101;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(CONTENT_AUTHORITY, ArchiveContract.PATH_ARCHIVE, ARCHIVE);
        sUriMatcher.addURI(CONTENT_AUTHORITY, ArchiveContract.PATH_ARCHIVE + "#", ARCHIVE_ID);
    }

    private ArchiveDbHelper mDbHelper;

    @Override
    public boolean onCreate() {
        mDbHelper = new ArchiveDbHelper(getContext());
        return true;
    }

    /**
     * Perform the query for the given URI. Use the given projection, selection, selection arguments, and sort order.
     */

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        SQLiteDatabase database = mDbHelper.getReadableDatabase();
        Cursor cursor = null;

        int match = sUriMatcher.match(uri);
        switch (match) {
            case ARCHIVE:
                cursor = database.query(ArchiveEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case ARCHIVE_ID:
                selection = ArchiveContract.ArchiveEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(ArchiveEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case ARCHIVE:
                return insertArchiveItem(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    private Uri insertArchiveItem(Uri uri, ContentValues values) {
        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        long id = database.insert(ArchiveEntry.TABLE_NAME, null, values);
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id);
    }

    /**
     * Updates the data at the given selection and selection arguments, with the new ContentValues.
     */

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection,
                      String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case ARCHIVE:
                return updateArchiveItem(uri, contentValues, selection, selectionArgs);
            case ARCHIVE_ID:
                selection = ArchiveEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateArchiveItem(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    private int updateArchiveItem(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        String date = values.getAsString(ArchiveEntry.COLUMN_DATE);
        Integer all = values.getAsInteger(ArchiveEntry.COLUMN_POINTS_ALL);
        Integer body = values.getAsInteger(ArchiveEntry.COLUMN_POINTS_BODY);
        Integer mind = values.getAsInteger(ArchiveEntry.COLUMN_POINTS_MIND);
        Integer soul = values.getAsInteger(ArchiveEntry.COLUMN_POINTS_SOUL);
        if (values.size() == 0) {
            return 0;
        }
        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        int rowsUpdated = database.update(ArchiveEntry.TABLE_NAME, values, selection, selectionArgs);
        // If 1 or more rows were updated, then notify all listeners that the data at the
        // given URI has changed
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return database.update(ArchiveEntry.TABLE_NAME, values, selection, selectionArgs);
    }

    /**
     * Delete the data at the given selection and selection arguments.
     */

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Get writeable database
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Track the number of rows that were deleted
        int rowsDeleted;

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case ARCHIVE:
                // Delete all rows that match the selection and selection args
                rowsDeleted = database.delete(ArchiveEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case ARCHIVE_ID:
                // Delete a single row given by the ID in the URI
                selection = ArchiveEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                rowsDeleted = database.delete(ArchiveEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }
        // If 1 or more rows were deleted, then notify all listeners that the data at the
        // given URI has changed
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        // Return the number of rows deleted
        return rowsDeleted;
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case ARCHIVE:
                return ArchiveEntry.CONTENT_LIST_TYPE;
            case ARCHIVE_ID:
                return ArchiveEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }
}