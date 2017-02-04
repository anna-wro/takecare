package pm.anna.takecare;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class CareActivity extends BaseActivity  {
    private TextView mHowMany;
    private Button mButton;
    int howMany = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_care);
    }
    public void addThreePoints(View v) {
        Button b = (Button)v;
        b.setTextColor(getResources().getColor(R.color.colorPrimary));
        b.setBackgroundResource(R.drawable.button_used);
        b.setEnabled(false);
        howMany +=3;
        addCare(howMany);
    }
    public void addTwoPoints(View v) {
        Button b = (Button)v;
        b.setTextColor(getResources().getColor(R.color.colorPrimary));
        b.setBackgroundResource(R.drawable.button_used);
        b.setEnabled(false);
        howMany +=2;
        addCare(howMany);
    }
    public void addOnePoint(View v) {
        Button b = (Button)v;
        b.setTextColor(getResources().getColor(R.color.colorPrimary));
        b.setBackgroundResource(R.drawable.button_used);
        b.setEnabled(false);
        howMany +=1;
        addCare(howMany);
    }
    public void resetCare (View v){
        howMany = 0;
        addCare(howMany);

    }
    public void addCare (int num) {
        mHowMany = (TextView) findViewById(R.id.howMany);
        mHowMany.setText(String.valueOf(num));
    }
}
