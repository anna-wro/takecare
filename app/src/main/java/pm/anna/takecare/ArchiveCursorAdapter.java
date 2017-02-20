package pm.anna.takecare;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import pm.anna.takecare.data.ArchiveContract;

/**
 * Created by Anna on 19.02.2017.
 */


public class ArchiveCursorAdapter extends CursorAdapter {


    public ArchiveCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }

    /**
     * Makes a new blank list item view. No data is set (or bound) to the views yet.
     *
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already
     *                moved to the correct position.
     * @param parent  The parent to which the new view is attached to
     * @return the newly created list item view.
     */

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    /**
     * This method binds the  data (in the current row pointed to by cursor) to the given
     * list item layout.
     *
     * @param view    Existing view, returned earlier by newView() method
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already moved to the
     *                correct row.
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Find fields to populate in inflated template
        TextView tvDate = (TextView) view.findViewById(R.id.date);
        TextView tvPoints = (TextView) view.findViewById(R.id.points);
//        TextView tvAll = (TextView) view.findViewById(R.id.all);
//        TextView tvMind = (TextView) view.findViewById(R.id.mind);
//        TextView tvSoul = (TextView) view.findViewById(R.id.soul);

        int dateColumnIndex = cursor.getColumnIndex(ArchiveContract.ArchiveEntry.COLUMN_DATE);
        int pointsColumnIndex = cursor.getColumnIndex(ArchiveContract.ArchiveEntry.COLUMN_POINTS_ALL);
        int bodyColumnIndex = cursor.getColumnIndex(ArchiveContract.ArchiveEntry.COLUMN_POINTS_BODY);
        int mindColumnIndex = cursor.getColumnIndex(ArchiveContract.ArchiveEntry.COLUMN_POINTS_MIND);
        int soulColumnIndex = cursor.getColumnIndex(ArchiveContract.ArchiveEntry.COLUMN_POINTS_SOUL);

        String date = cursor.getString(dateColumnIndex);
        int points = cursor.getInt(pointsColumnIndex);
        int body = cursor.getInt(bodyColumnIndex);
        int mind = cursor.getInt(mindColumnIndex);
        int soul = cursor.getInt(soulColumnIndex);
        tvDate.setText(date);
        tvPoints.setText(String.valueOf(points + " pts"));
//        tvAll.setText(String.valueOf("(Body - " + body  + ", mind - " + mind + ", soul - " + soul + ")") );
//        tvMind.setText(String.valueOf(mind));
//        tvSoul.setText(String.valueOf(soul));

    }
}