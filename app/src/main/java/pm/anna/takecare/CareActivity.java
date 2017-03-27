package pm.anna.takecare;


import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Random;

import pm.anna.takecare.data.ArchiveContract.ArchiveEntry;

import static pm.anna.takecare.R.id.addButton;
import static pm.anna.takecare.R.id.bodyPointsNumber;
import static pm.anna.takecare.R.id.pager;

public class CareActivity extends BaseActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int ARCHIVE_LOADER = 0;

    ArchiveCursorAdapter mCursorAdapter;
    int howMany = 0;
    int bodyPoints = 0;
    int mindPoints = 0;
    int soulPoints = 0;
    int pointsNum = 0;
    String howManyBody = "";
    String howManyMind = "";
    String howManySoul = "";
    String formattedDate;
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
    TextView mBodyDots_f;
    TextView mMindDots_f;
    TextView mSoulDots_f;
    TextView mPointsNumber;
    TextView mCommentDays;
    TextView mComment;
    TextView mComment_f;
    EditText mDateEdit;
    EditText mBodyPointsNumber;
    EditText mMindPointsNumber;
    EditText mSoulPointsNumber;
    EditText mThoughtsEdit;
    EqualWidthHeightTextView mHowMany_f;
    EqualWidthHeightTextView mHowManyDays;
    EqualWidthHeightTextView mHowManyDays_f;
    ImageButton mAddButton;
    EqualWidthHeightTextView mDeleteButton;
    LinearLayout mAddPanel;
    RelativeLayout mArchive;
    ListView mItemsList;
    ListView mItemsList_f;
    ImageButton mYesButton;
    ImageButton mNoButton;
    ImageButton mShowDetailsButton;
    ImageButton mShowSimpleButton;
    RelativeLayout mEmptyView;
    String mDoneArray[];
    boolean detailsVisible = false;
    boolean savedToday = false;
    int wantsToAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUiElements();
        makeSlides();
        fakeSlides();
        setDate();
        addListeners();
        initDatabase();
        checkLastTime();
    }

    private void initUiElements() {

        setContentView(R.layout.activity_care);
        mBodyList = (GridLayout) findViewById(R.id.body_list);
        mMindList = (GridLayout) findViewById(R.id.mind_list);
        mSoulList = (GridLayout) findViewById(R.id.soul_list);
        mHowMany = (TextView) findViewById(R.id.howMany);
        mComment = (TextView) findViewById(R.id.careText);
        mComment_f = (TextView) findViewById(R.id.careText_f);
        mHowManyBody = (TextView) findViewById(R.id.howManyBody);
        mHowManyMind = (TextView) findViewById(R.id.howManyMind);
        mHowManySoul = (TextView) findViewById(R.id.howManySoul);
        mHowManyDays = (EqualWidthHeightTextView) findViewById(R.id.howManyDays);
        mBodyDots = (TextView) findViewById(R.id.bodyDots);
        mMindDots = (TextView) findViewById(R.id.mindDots);
        mSoulDots = (TextView) findViewById(R.id.soulDots);
        mViewPager = (ViewPager) findViewById(pager);
        mItemsList = (ListView) findViewById(R.id.list);
        mPointsNumber = (TextView) findViewById(R.id.pointsNumber);
        mDateEdit = (EditText) findViewById(R.id.editDate);
        mAddButton = (ImageButton) findViewById(addButton);
        mCommentDays = (TextView) findViewById(R.id.comment_days);
        mBodyPointsNumber = (EditText) findViewById(bodyPointsNumber);
        mMindPointsNumber = (EditText) findViewById(R.id.mindPointsNumber);
        mSoulPointsNumber = (EditText) findViewById(R.id.soulPointsNumber);
        mAddPanel = (LinearLayout) findViewById(R.id.addPanel);
        mArchive = (RelativeLayout) findViewById(R.id.archive);
        mYesButton = (ImageButton) findViewById(R.id.yesButton);
        mNoButton = (ImageButton) findViewById(R.id.noButton);
        mEmptyView = (RelativeLayout) findViewById(R.id.empty);
        mDeleteButton = (EqualWidthHeightTextView) findViewById(R.id.deleteButton);
        mDoneArray = getResources().getStringArray(R.array.done);
        mShowDetailsButton = (ImageButton) findViewById(R.id.showDetails);
        mShowSimpleButton = (ImageButton) findViewById(R.id.showSimple);
        mThoughtsEdit = (EditText) findViewById(R.id.anyThoughts);
    }

    private void makeSlides() {
        WizardPagerAdapter adapter = new WizardPagerAdapter();
        mViewPager.setAdapter(adapter);
        mViewPager.setOffscreenPageLimit(7);
        mViewPager.setCurrentItem(1, false);
        mViewPager.setPageTransformer(true, new DepthPageTransformer());
        mAddPanel.setVisibility(View.INVISIBLE);
        mItemsList.setEmptyView(mEmptyView);
    }

    private void fakeSlides() {
        mHowMany_f = (EqualWidthHeightTextView) findViewById(R.id.howMany_f);
        mHowManyDays_f = (EqualWidthHeightTextView) findViewById(R.id.howManyDays_f);
        mBodyDots_f = (TextView) findViewById(R.id.bodyDots_f);
        mMindDots_f = (TextView) findViewById(R.id.mindDots_f);
        mSoulDots_f = (TextView) findViewById(R.id.soulDots_f);
        mItemsList_f = (ListView) findViewById(R.id.list_f);
    }

    private void setDate() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy (EEE)", Locale.US);
        formattedDate = df.format(c.getTime());
        mDateEdit.setText(formattedDate);
        try {
            EditTextDatePicker fromDate = new EditTextDatePicker(this, mDateEdit);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void addListeners() {

        mBodyPointsNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (mBodyPointsNumber.getText().toString().equals(""))
                        mBodyPointsNumber.setText("0");
                }
            }
        });
        mBodyPointsNumber.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(final Editable s) {
                if ((!mBodyPointsNumber.getText().toString().equals(""))
                        && mBodyPointsNumber.getText().toString().equals(s.toString())) {
                    bodyPoints = Integer.parseInt(s.toString());
                    changeSum();
                }
            }
        });
        mMindPointsNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (mMindPointsNumber.getText().toString().equals(""))
                        mMindPointsNumber.setText("0");
                }
            }
        });
        mMindPointsNumber.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(final Editable s) {
                if ((!mMindPointsNumber.getText().toString().equals(""))
                        && mMindPointsNumber.getText().toString().equals(s.toString())) {
                    mindPoints = Integer.parseInt(s.toString());
                    changeSum();
                }
            }
        });
        mSoulPointsNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (mSoulPointsNumber.getText().toString().equals(""))
                        mSoulPointsNumber.setText("0");
                }
            }
        });
        mSoulPointsNumber.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(final Editable s) {
                if ((!mSoulPointsNumber.getText().toString().equals(""))
                        && mSoulPointsNumber.getText().toString().equals(s.toString())) {
                    soulPoints = Integer.parseInt(s.toString());
                    changeSum();
                }
            }
        });

        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAddPanel.setVisibility(View.VISIBLE);
            }
        });

        mNoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hidePanel();
            }
        });

        mDeleteButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showDeleteConfirmationDialog();
            }
        });

        mItemsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {

                View details = view.findViewById(R.id.details);

                // Creating the expand animation for the item
                ExpandAnimation expandAni = new ExpandAnimation(details, 500);

                // Start the animation on the toolbar
                details.startAnimation(expandAni);
            }
        });
        mShowDetailsButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!detailsVisible) {
                    for (int i = 0; i < mItemsList.getAdapter().getCount(); i++) {
                        mItemsList.performItemClick(
                                getViewByPosition(i),
                                i,
                                mItemsList.getAdapter().getItemId(i));
                    }
                    detailsVisible = true;
                }

            }
        });
        mShowSimpleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemsList.setAdapter(mCursorAdapter);
                detailsVisible = false;
            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {

                // skip fake page (first), go to last page
                if (position == 0) {
                    mViewPager.setCurrentItem(5, false);
                }

                // skip fake page (last), go to first page
                if (position == 6) {
                    mViewPager.setCurrentItem(1, false);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    /* * * ARCHIVE * * */

    private void initDatabase() {
        mCursorAdapter = new ArchiveCursorAdapter(this, null);
        mItemsList.setAdapter(mCursorAdapter);
        mItemsList_f.setAdapter(mCursorAdapter); // fake list
        getSupportLoaderManager().initLoader(ARCHIVE_LOADER, null, this);
    }

    private void checkLastTime() {
        Cursor c = getContentResolver().query(ArchiveEntry.CONTENT_URI, null, null, null, null);
        assert c != null;
        c.moveToLast();
        String lastDay = c.getString(c.getColumnIndex(ArchiveEntry.COLUMN_DATE));
        if (lastDay.equals(formattedDate)) {

            savedToday = true;
            resetDots();
            mComment.setText(getResources().getString(R.string.comment));
            mComment_f.setText(getResources().getString(R.string.comment));

            pointsNum = c.getInt(c.getColumnIndex(ArchiveEntry.COLUMN_POINTS_ALL));

            howMany = pointsNum;
            changeCare(pointsNum);

            bodyPoints = c.getInt(c.getColumnIndex(ArchiveEntry.COLUMN_POINTS_BODY));
            for (int i = 0; i < bodyPoints; i++) {
                howManyBody += "● ";
            }

            mindPoints = c.getInt(c.getColumnIndex(ArchiveEntry.COLUMN_POINTS_MIND));
            for (int i = 0; i < mindPoints; i++) {
                howManyMind += "● ";
            }

            soulPoints = c.getInt(c.getColumnIndex(ArchiveEntry.COLUMN_POINTS_SOUL));
            for (int i = 0; i < soulPoints; i++) {
                howManySoul += "● ";
            }

            changeCareBody(howManyBody);
            changeCareMind(howManyMind);
            changeCareSoul(howManySoul);
            wantsToAdd = 2;
        }
        c.close();
    }

    private View getViewByPosition(int position) {
        int firstItemPosition = mItemsList.getFirstVisiblePosition();
        int lastItemPosition = firstItemPosition + mItemsList.getChildCount() - 1;

        if (position < firstItemPosition || position > lastItemPosition) {//is invisible
            return mItemsList.getAdapter().getView(position, null, mItemsList);
        } else {
            int childIndex = position - firstItemPosition;//is visible
            return mItemsList.getChildAt(childIndex);
        }
    }

    public void insertArchiveItem(View v) {

        String date = mDateEdit.getText().toString();
        String thought = mThoughtsEdit.getText().toString();
        ContentValues values = new ContentValues();
        values.put(ArchiveEntry.COLUMN_DATE, date);
        values.put(ArchiveEntry.COLUMN_POINTS_ALL, pointsNum);
        values.put(ArchiveEntry.COLUMN_POINTS_BODY, bodyPoints);
        values.put(ArchiveEntry.COLUMN_POINTS_MIND, mindPoints);
        values.put(ArchiveEntry.COLUMN_POINTS_SOUL, soulPoints);
        values.put(ArchiveEntry.COLUMN_DESCRIPTION, thought);

        if (savedToday && date.equals(formattedDate)) deleteArchiveItem();
        Uri newUri = getContentResolver().insert(ArchiveEntry.CONTENT_URI, values);
        if (newUri == null) {
            Toast.makeText(this, getString(R.string.save_error), Toast.LENGTH_SHORT).show();
        } else {
            Random generator = new Random();
            int num = generator.nextInt(mDoneArray.length);
            Toast.makeText(this, mDoneArray[num], Toast.LENGTH_SHORT).show();
        }

        resetPoints();
        changeSum();
        hidePanel();
        checkLastTime();
    }

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg);

        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                deleteArchiveItem();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Cancel" button, so dismiss the dialog
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        Button negativeButton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        negativeButton.setTextColor(ContextCompat.getColor(this, R.color.green));

        Button positiveButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        positiveButton.setTextColor(ContextCompat.getColor(this, R.color.circle_delete));
    }

    private void deleteArchiveItem() {
        Cursor c = getContentResolver().query(ArchiveEntry.CONTENT_URI, null, null, null, null);
        assert c != null;
        c.moveToLast();
        String dayToDelete = c.getString(c.getColumnIndex(ArchiveEntry._ID));
        getContentResolver().delete(ArchiveEntry.CONTENT_URI, ArchiveEntry._ID + "=?", new String[]{dayToDelete});
        c.close();
    }

    /* * * CHANGE POINTS - BODY  * * */

    public void changePointsBody(View v) {
        int pointsToChange = Integer.parseInt((String) v.getTag());
        ToggleButton selected = ((ToggleButton) v);
        boolean isChecked = selected.isChecked();
        if (savedToday && wantsToAdd == 2) {
            selected.toggle();
            checkIfWantsToAdd();
            return;
        }
        if (isChecked) {
            howMany += pointsToChange;
            bodyPoints += pointsToChange;

            for (int i = 0; i < pointsToChange; i++) {
                howManyBody += "● ";
            }
        } else {
            howMany -= pointsToChange;
            bodyPoints -= pointsToChange;
            howManyBody = howManyBody.substring(0, howManyBody.length() - (pointsToChange * 2));
        }
        changeCareBody(howManyBody);
        changeCare(howMany);
    }

    private void changeCareBody(String s) {
        if (s.length() > 0) {
            mBodyDots.setText(s);
            mBodyDots_f.setText(s);
        } else {
            mBodyDots.setText("");
            mBodyDots_f.setText("");
        }
        if (s.length() == 0) s = "◦ ◦ ◦";
        mHowManyBody.setText(s);
        mBodyPointsNumber.setText(String.valueOf(bodyPoints));
    }

    /* * * CHANGE POINTS - MIND * * */

    public void changePointsMind(View v) {
        int pointsToChange = Integer.parseInt((String) v.getTag());
        ToggleButton selected = ((ToggleButton) v);
        boolean isChecked = selected.isChecked();
        if (savedToday && wantsToAdd == 2) {
            selected.toggle();
            checkIfWantsToAdd();
            return;
        }

        if (isChecked) {
            howMany += pointsToChange;
            mindPoints += pointsToChange;

            for (int i = 0; i < pointsToChange; i++) {
                howManyMind += "● ";
            }
        } else {
            howMany -= pointsToChange;
            mindPoints -= pointsToChange;
            howManyMind = howManyMind.substring(0, howManyMind.length() - (pointsToChange * 2));
        }
        changeCareMind(howManyMind);
        changeCare(howMany);
    }

    private void changeCareMind(String s) {
        if (s.length() > 0) {
            mMindDots.setText(s);
            mMindDots_f.setText(s);
        } else {
            mMindDots.setText("");
            mMindDots_f.setText("");
        }
        if (s.length() == 0) s = "◦ ◦ ◦";
        mHowManyMind.setText(s);
        mMindPointsNumber.setText(String.valueOf(mindPoints));
    }

    /* * * CHANGE POINTS - SOUL * * */

    public void changePointsSoul(View v) {
        int pointsToChange = Integer.parseInt((String) v.getTag());
        ToggleButton selected = ((ToggleButton) v);
        boolean isChecked = selected.isChecked();
        if (savedToday && wantsToAdd == 2) {
            selected.toggle();
            checkIfWantsToAdd();
            return;
        }
        if (isChecked) {
            howMany += pointsToChange;
            soulPoints += pointsToChange;

            for (int i = 0; i < pointsToChange; i++) {
                howManySoul += "● ";
            }
        } else {
            howMany -= pointsToChange;
            soulPoints -= pointsToChange;
            howManySoul = howManySoul.substring(0, howManySoul.length() - (pointsToChange * 2));
        }
        changeCareSoul(howManySoul);
        changeCare(howMany);
    }

    private void changeCareSoul(String s) {
        if (s.length() > 0) {
            mSoulDots.setText(s);
            mSoulDots_f.setText(s);
        } else {
            mSoulDots.setText("");
            mSoulDots.setText("");
        }
        if (s.length() == 0) s = "◦ ◦ ◦";
        mHowManySoul.setText(s);
        mSoulPointsNumber.setText(String.valueOf(soulPoints));
    }

    /* * * POINTS-RELATED * * */

    private void changeCare(int num) {
        preventNegative();
        pointsNum = bodyPoints + soulPoints + mindPoints;
        mHowMany.setText(String.valueOf(num));
        mHowMany_f.setText(String.valueOf(num));
        mPointsNumber.setText(String.valueOf(pointsNum));
    }

    private void checkIfWantsToAdd() {
        switch (wantsToAdd) {
            case 0:
                resetEverything();
                wantsToAdd = 1;
                break;
            case 1:
                break;
            case 2:
                askWhatToDo();
                break;
        }

    }

    private void askWhatToDo() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.today_dialog_msg);

        builder.setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                wantsToAdd = 1;
            }
        });
        builder.setNegativeButton(R.string.reset, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                resetEverything();
                wantsToAdd = 1;
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        TextView messageView = (TextView) alertDialog.findViewById(android.R.id.message);
        assert messageView != null;
        messageView.setGravity(Gravity.CENTER);

        Button negativeButton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        negativeButton.setTextColor(ContextCompat.getColor(this, R.color.circle_delete));

        Button positiveButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        positiveButton.setTextColor(ContextCompat.getColor(this, R.color.green));
    }

    private void resetEverything() {
        resetPoints();
        resetDots();
        mHowMany.setText(String.valueOf(0));
        mHowMany_f.setText(String.valueOf(0));
        howMany = 0;
    }

    private void resetDots(){
        howManyMind = "";
        howManyBody = "";
        howManySoul = "";
        changeCareBody(howManyBody);
        changeCareMind(howManyMind);
        changeCareSoul(howManySoul);
    };

    private void preventNegative() {
        if (howMany < 0) howMany = 0;
        if (bodyPoints < 0) {
            bodyPoints = 0;
            mBodyPointsNumber.setText(String.valueOf(bodyPoints));
        }
        if (mindPoints < 0) {
            mindPoints = 0;
            mMindPointsNumber.setText(String.valueOf(mindPoints));
        }
        if (soulPoints < 0) {
            soulPoints = 0;
            mSoulPointsNumber.setText(String.valueOf(soulPoints));
        }
    }

    private void changeSum() {
        pointsNum = bodyPoints + soulPoints + mindPoints;
        mPointsNumber.setText(String.valueOf(pointsNum));
    }

    private void resetPoints() {
        pointsNum = bodyPoints = mindPoints = soulPoints = 0;
        mSoulPointsNumber.setText(String.valueOf(soulPoints));
        mBodyPointsNumber.setText(String.valueOf(bodyPoints));
        mMindPointsNumber.setText(String.valueOf(mindPoints));
        mThoughtsEdit.setText("");
    }

    private void hidePanel() {
        mAddPanel.setVisibility(View.INVISIBLE);
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

     /* * * CURSOR * * */

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {
                ArchiveEntry._ID,
                ArchiveEntry.COLUMN_DATE,
                ArchiveEntry.COLUMN_POINTS_ALL,
                ArchiveEntry.COLUMN_POINTS_BODY,
                ArchiveEntry.COLUMN_POINTS_MIND,
                ArchiveEntry.COLUMN_POINTS_SOUL,
                ArchiveEntry.COLUMN_DESCRIPTION
        };

        return new CursorLoader(this, ArchiveEntry.CONTENT_URI, projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCursorAdapter.swapCursor(data);
        int days = mCursorAdapter.getCount();
        mHowManyDays.setText(String.valueOf(days));
        mHowManyDays_f.setText(String.valueOf(days));
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }

}