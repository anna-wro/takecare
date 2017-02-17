package pm.anna.takecare;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.List;

public class CareActivity extends BaseActivity {
    int howMany = 0;
    String howManyBody = "";
    String howManyMind = "";
    String howManySoul = "";
    GridLayout mBodyList;
    GridLayout mMindList;
    GridLayout mSoulList;
    ViewPager mViewPager;
    TextView mHowMany;
    TextView mHowManyBody;
    TextView mHowManyMind;
    TextView mHowManySoul;
    TextView mBodyDots;
    TextView mMindDots;
    TextView mSoulDots;
    ListView mItemsList;
    EditText mPointsEdit;
    EditText mDateEdit;
    EqualWidthHeightTextView mAddButton;
    private ArchiveDbAdapter mArchiveDbAdapter;
    private Cursor mArchiveCursor;
    private List<ArchiveItem> items;
    private ArchiveItemsAdapter mListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_care);
        initUiElements();
        WizardPagerAdapter adapter = new WizardPagerAdapter();
        mViewPager.setAdapter(adapter);
        mViewPager.setOffscreenPageLimit(5);
        mViewPager.setPageTransformer(true, new DepthPageTransformer());
        initListView();
        initButtonsOnClickListeners();
    }
    private void initUiElements() {
        mBodyList = (GridLayout) findViewById(R.id.body_list);
        mMindList = (GridLayout) findViewById(R.id.mind_list);
        mSoulList = (GridLayout) findViewById(R.id.soul_list);
        mHowMany = (TextView) findViewById(R.id.howMany);
        mHowManyBody = (TextView) findViewById(R.id.howManyBody);
        mHowManyMind = (TextView) findViewById(R.id.howManyMind);
        mHowManySoul = (TextView) findViewById(R.id.howManySoul);
        mBodyDots = (TextView) findViewById(R.id.bodyDots);
        mMindDots = (TextView) findViewById(R.id.mindDots);
        mSoulDots = (TextView) findViewById(R.id.soulDots);
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mItemsList = (ListView) findViewById(R.id.list);
        mPointsEdit = (EditText) findViewById(R.id.editPoints);
        mDateEdit = (EditText) findViewById(R.id.editDate);
        mAddButton = (EqualWidthHeightTextView) findViewById(R.id.addButton);
    }

    private void initListView() {
        fillListViewData();

    }

    private void fillListViewData() {
        mArchiveDbAdapter = new ArchiveDbAdapter(getApplicationContext());
        mArchiveDbAdapter.open();
        getAllItems();
        mListAdapter = new ArchiveItemsAdapter(this, items);
        mItemsList.setAdapter(mListAdapter);
    }

    private void getAllItems() {
        items = new ArrayList<ArchiveItem>();
        mArchiveCursor = getAllEntriesFromDb();
        updateTaskList();
    }

    private Cursor getAllEntriesFromDb() {
        mArchiveCursor = mArchiveDbAdapter.getAllItems();
        if(mArchiveCursor != null) {
            startManagingCursor(mArchiveCursor);
            mArchiveCursor.moveToFirst();
        }
        return mArchiveCursor;
    }

    private void updateTaskList() {
        if(mArchiveCursor != null && mArchiveCursor.moveToFirst()) {
            do {
                long id = mArchiveCursor.getLong(mArchiveDbAdapter.ID_COLUMN);
                String date = mArchiveCursor.getString(mArchiveDbAdapter.DATE_COLUMN);
                int points = mArchiveCursor.getInt(mArchiveDbAdapter.POINTS_COLUMN);
                items.add(new ArchiveItem(id, date, points));
            } while(mArchiveCursor.moveToNext());
        }
    }

    @Override
    protected void onDestroy() {
        if(mArchiveDbAdapter != null)
            mArchiveDbAdapter.close();
        super.onDestroy();
    }
    private void initButtonsOnClickListeners() {
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.addButton:
                        saveNewTask();
                        break;
                    default:
                        break;
                }
            }
        };
        mAddButton.setOnClickListener(onClickListener);
    }

    private void saveNewTask(){
        String chosenDate = mDateEdit.getText().toString();
        int pointsNumber = Integer.valueOf(mPointsEdit.getText().toString());
        if(chosenDate.equals("") || pointsNumber < 0){
            mDateEdit.setError("This field can't be empty");
        } else {
            mArchiveDbAdapter.insertArchiveItem(chosenDate, pointsNumber);
            mDateEdit.setText("");
            mPointsEdit.setText("0");
        }
        updateListViewData();
    }

    private void updateListViewData() {
        mArchiveCursor.requery();
        items.clear();
        updateTaskList();
        mListAdapter.notifyDataSetChanged();
    }

    /* * * CHANGE POINTS - BODY  * * */

    public void changeFivePointsBody(View v) {
        boolean isChecked = ((ToggleButton) v).isChecked();
        if (isChecked) {
            howMany += 5;
            howManyBody += "● ● ● ● ● ";
        } else {
            howMany -= 5;
            howManyBody = howManyBody.substring(0, howManyBody.length() - 10);
        }
        changeCareBody(howManyBody);
        changeCare(howMany);
    }

    public void changeThreePointsBody(View v) {
        boolean isChecked = ((ToggleButton) v).isChecked();
        if (isChecked) {
            howMany += 3;
            howManyBody += "● ● ● ";
        } else {
            howMany -= 3;
            howManyBody = howManyBody.substring(0, howManyBody.length() - 6);
        }
        changeCareBody(howManyBody);
        changeCare(howMany);
    }

    public void changeTwoPointsBody(View v) {
        boolean isChecked = ((ToggleButton) v).isChecked();
        if (isChecked) {
            howMany += 2;
            howManyBody += "● ● ";
        } else {
            howMany -= 2;
            howManyBody = howManyBody.substring(0, howManyBody.length() - 4);
        }
        changeCareBody(howManyBody);
        changeCare(howMany);
    }

    public void changeOnePointBody(View v) {
        boolean isChecked = ((ToggleButton) v).isChecked();
        if (isChecked) {
            howMany += 1;
            howManyBody += "● ";
        } else {
            howMany -= 1;
            howManyBody = howManyBody.substring(0, howManyBody.length() - 2);
        }
        changeCareBody(howManyBody);
        changeCare(howMany);
    }

    public void changeCareBody(String s) {
        if (s.length() > 0) {
            mBodyDots.setText(s);
        } else {
            mBodyDots.setText("");
        }
        if (s.length() == 0) s = "◦ ◦ ◦";
        mHowManyBody.setText(s);
    }

    /* * * CHANGE POINTS - MIND * * */

    public void changeFivePointsMind(View v) {
        boolean isChecked = ((ToggleButton) v).isChecked();
        if (isChecked) {
            howMany += 5;
            howManyMind += "● ● ● ● ● ";
        } else {
            howMany -= 5;
            howManyMind = howManyMind.substring(0, howManyMind.length() - 10);
        }
        changeCareMind(howManyMind);
        changeCare(howMany);
    }

    public void changeThreePointsMind(View v) {
        boolean isChecked = ((ToggleButton) v).isChecked();
        if (isChecked) {
            howMany += 3;
            howManyMind += "● ● ● ";
        } else {
            howMany -= 3;
            howManyMind = howManyMind.substring(0, howManyMind.length() - 6);
        }
        changeCareMind(howManyMind);
        changeCare(howMany);
    }

    public void changeTwoPointsMind(View v) {
        boolean isChecked = ((ToggleButton) v).isChecked();
        if (isChecked) {
            howMany += 2;
            howManyMind += "● ● ";
        } else {
            howMany -= 2;
            howManyMind = howManyMind.substring(0, howManyMind.length() - 4);
        }
        changeCareMind(howManyMind);
        changeCare(howMany);
    }

    public void changeOnePointMind(View v) {
        boolean isChecked = ((ToggleButton) v).isChecked();
        if (isChecked) {
            howMany += 1;
            howManyMind += "● ";
        } else {
            howMany -= 1;
            howManyMind = howManyMind.substring(0, howManyMind.length() - 2);
        }
        changeCareMind(howManyMind);
        changeCare(howMany);
    }

    public void changeCareMind(String s) {
        if (s.length() > 0) {
            mMindDots.setText(s);
        } else {
            mMindDots.setText("");
        }
        if (s.length() == 0) s = "◦ ◦ ◦";
        mHowManyMind.setText(s);
    }

    /* * * CHANGE POINTS - SOUL * * */
    public void changeFivePointsSoul(View v) {
        boolean isChecked = ((ToggleButton) v).isChecked();
        if (isChecked) {
            howMany += 5;
            howManySoul += "● ● ● ● ● ";
        } else {
            howMany -= 5;
            howManySoul = howManySoul.substring(0, howManySoul.length() - 10);
        }
        changeCareSoul(howManySoul);
        changeCare(howMany);
    }

    public void changeThreePointsSoul(View v) {
        boolean isChecked = ((ToggleButton) v).isChecked();
        if (isChecked) {
            howMany += 3;
            howManySoul += "● ● ● ";
        } else {
            howMany -= 3;
            howManySoul = howManySoul.substring(0, howManySoul.length() - 6);
        }
        changeCareSoul(howManySoul);
        changeCare(howMany);
    }

    public void changeTwoPointsSoul(View v) {
        boolean isChecked = ((ToggleButton) v).isChecked();
        if (isChecked) {
            howMany += 2;
            howManySoul += "● ● ";
        } else {
            howMany -= 2;
            howManySoul = howManySoul.substring(0, howManySoul.length() - 4);
        }
        changeCareSoul(howManySoul);
        changeCare(howMany);
    }

    public void changeOnePointSoul(View v) {
        boolean isChecked = ((ToggleButton) v).isChecked();
        if (isChecked) {
            howMany += 1;
            howManySoul += "● ";
        } else {
            howMany -= 1;
            howManySoul = howManySoul.substring(0, howManySoul.length() - 2);
        }
        changeCareSoul(howManySoul);
        changeCare(howMany);
    }

    public void changeCareSoul(String s) {
        if (s.length() > 0) {
            mSoulDots.setText(s);
        } else {
            mSoulDots.setText("");
        }
        if (s.length() == 0) s = "◦ ◦ ◦";
        mHowManySoul.setText(s);
    }

    public void changeCare(int num) {
        mHowMany.setText(String.valueOf(num));
        mPointsEdit.setText(Integer.toString(num));
    }


}