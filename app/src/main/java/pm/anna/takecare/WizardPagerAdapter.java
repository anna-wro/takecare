package pm.anna.takecare;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
/**
 * Created by Anna on 13.02.2017.
 */

class WizardPagerAdapter extends PagerAdapter {

    public Object instantiateItem(ViewGroup collection, int position) {

        int resId = 0;
        switch (position) {
            case 0:
                resId = R.id.activity_care;
                break;
            case 1:
                resId = R.id.layout_body;
                break;
            case 2:
                resId = R.id.layout_mind;
                break;
            case 3:
                resId = R.id.layout_soul;
                break;
            case 4:
                resId = R.id.archive;
                break;
        }
        return collection.findViewById(resId);
    }

    @Override
    public int getCount() {
        return 5;
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == ((View) arg1);
    }
}