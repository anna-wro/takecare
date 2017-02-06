package pm.anna.takecare;


import android.os.Bundle;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

public class CareActivity extends BaseActivity {
    int howMany = 0;

    private TextView mHowMany;
    GridLayout mBodyList;
    GridLayout mMindList;
    GridLayout mSoulList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_care);


        mBodyList = (GridLayout) findViewById(R.id.body_list);
        mMindList = (GridLayout) findViewById(R.id.mind_list);
        mSoulList = (GridLayout) findViewById(R.id.soul_list);
        // hide until its title is clicked
        mBodyList.setVisibility(View.GONE);
        mMindList.setVisibility(View.GONE);
        mSoulList.setVisibility(View.GONE);

    }
    /**
     * onClick handler
     */
    public void toggle_body(View v) throws InterruptedException {
        if(mBodyList.isShown()){
            Fade_animation.fade_out(this, mBodyList);
            v.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mBodyList.setVisibility(View.GONE);
                }
            }, 450);

        }
        else{
            mBodyList.setVisibility(View.VISIBLE);
            Fade_animation.fade_in(this, mBodyList);
        }
    }
    public void toggle_mind(View v) throws InterruptedException {
        if(mMindList.isShown()){
            Fade_animation.fade_out(this, mMindList);
            v.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mMindList.setVisibility(View.GONE);
                }
            }, 450);

        }
        else{
            mMindList.setVisibility(View.VISIBLE);
            Fade_animation.fade_in(this, mMindList);
        }
    }
    public void toggle_soul(View v) throws InterruptedException {
        if(mSoulList.isShown()){
            Fade_animation.fade_out(this, mSoulList);
            v.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mSoulList.setVisibility(View.GONE);
                }
            }, 450);

        }
        else{
            mSoulList.setVisibility(View.VISIBLE);
            Fade_animation.fade_in(this, mSoulList);
        }
    }

    public void changeThreePoints(View v) {
        boolean isChecked = ((ToggleButton) v).isChecked();
        if (isChecked) {
            howMany += 3;
        } else {
            howMany -= 3;
        }
        changeCare(howMany);
    }

    public void changeTwoPoints(View v) {
        boolean isChecked = ((ToggleButton) v).isChecked();
        if (isChecked) {
            howMany += 2;
        } else {
            howMany -= 2;
        }
        changeCare(howMany);
    }

    public void changeOnePoint(View v) {
        boolean isChecked = ((ToggleButton) v).isChecked();
        if (isChecked) {
            howMany += 1;
        } else {
            howMany -= 1;
        }
        changeCare(howMany);
    }

    public void resetCare(View v) {
        howMany = 0;
        changeCare(howMany);
    }

    public void changeCare(int num) {
        mHowMany = (TextView) findViewById(R.id.howMany);
        mHowMany.setText(String.valueOf(num));
    }


}
