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
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_care);
        mBodyList = (GridLayout) findViewById(R.id.body_list);
        // hide until its title is clicked
        mBodyList.setVisibility(View.GONE);

    }
    /**
     * onClick handler
     */
    public void toggle_contents(View v){
        if(mBodyList.isShown()){
            Slide_animation.slide_up(this, mBodyList);
            mBodyList.setVisibility(View.GONE);
        }
        else{
            mBodyList.setVisibility(View.VISIBLE);
            Slide_animation.slide_down(this, mBodyList);
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
