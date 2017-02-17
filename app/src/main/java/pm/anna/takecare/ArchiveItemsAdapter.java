package pm.anna.takecare;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ArchiveItemsAdapter extends ArrayAdapter<ArchiveItem> {
    private Activity context;
    private List<ArchiveItem> items;
    public ArchiveItemsAdapter(Activity context, List<ArchiveItem> items) {
        super(context, R.layout.archive, items);
        this.context = context;
        this.items = items;
    }

    static class ViewHolder {
        public TextView mArchiveItems;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mViewHolder;
        View rowView = convertView;
        if(rowView == null) {
            LayoutInflater layoutInflater = context.getLayoutInflater();
            rowView = layoutInflater.inflate(R.layout.archive, null, true);
            mViewHolder = new ViewHolder();
            mViewHolder.mArchiveItems = (TextView) rowView.findViewById(R.id.archiveListItem);
            rowView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) rowView.getTag();
        }
        ArchiveItem item = items.get(position);
        mViewHolder.mArchiveItems.setText(item.getPoints());
        return rowView;
    }
}