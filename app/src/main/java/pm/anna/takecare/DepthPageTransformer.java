package pm.anna.takecare;

import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * Created by Anna on 15.02.2017.
 */
public class DepthPageTransformer implements ViewPager.PageTransformer {

    public void transformPage(View view, float position) {
        int pageWidth = view.getWidth();

        if (position <= 0) { // [-1,0]
            // Use the default slide transition when moving to the left page
            view.setTranslationX(0);

        } else if (position <= 1) { // (0,1]
            // Counteract the default slide transition
            view.setTranslationX(pageWidth * -position);

        }
    }
}