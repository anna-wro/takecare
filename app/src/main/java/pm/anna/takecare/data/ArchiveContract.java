package pm.anna.takecare.data;

import android.provider.BaseColumns;

/**
 * Created by Anna on 18.02.2017.
 */

public final class ArchiveContract {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private ArchiveContract() {
    }

    /* Inner class that defines the table contents */
    public static class ArchiveEntry implements BaseColumns {
        public static final String TABLE_NAME = "archive";

        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_POINTS_ALL = "points";
        public static final String COLUMN_POINTS_BODY = "body";
        public static final String COLUMN_POINTS_MIND = "mind";
        public static final String COLUMN_POINTS_SOUL = "soul";
    }
}
