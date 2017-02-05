package pm.anna.takecare;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;


public class Slide_animation {
    public static void fade_in(Context ctx, View v){
        Animation a = AnimationUtils.loadAnimation(ctx, R.anim.fade_in);
        if(a != null){
            a.reset();
            if(v != null){
                v.clearAnimation();
                v.startAnimation(a);
            }
        }
    }
    public static void fade_out(Context ctx, View v){
        Animation a = AnimationUtils.loadAnimation(ctx, R.anim.fade_out);
        if(a != null){
            a.reset();
            if(v != null){
                v.clearAnimation();
                v.startAnimation(a);
            }
        }
    }
}