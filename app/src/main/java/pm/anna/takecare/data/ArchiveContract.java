package pm.anna.takecare.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Anna on 18.02.2017.
 */

public final class ArchiveContract {

    static final String CONTENT_AUTHORITY = "pm.anna.takecare";
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    static final String PATH_ARCHIVE = "archive";

    private ArchiveContract() {
    }

    /* Inner class that defines the table contents */
    public static class ArchiveEntry implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_ARCHIVE);
        static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ARCHIVE;
        static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ARCHIVE;

        static final String TABLE_NAME = "archive";
        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_POINTS_ALL = "points";
        public static final String COLUMN_POINTS_BODY = "body";
        public static final String COLUMN_POINTS_MIND = "mind";
        public static final String COLUMN_POINTS_SOUL = "soul";
        public static final String COLUMN_DESCRIPTION = "description";
    }
}
