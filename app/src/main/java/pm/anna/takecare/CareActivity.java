package pm.anna.takecare;


import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
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
    TextView mPointsNumber;
    TextView mCommentDays;
    EditText mDateEdit;
    EditText mBodyPointsNumber;
    EditText mMindPointsNumber;
    EditText mSoulPointsNumber;
    EqualWidthHeightTextView mHowManyDays;
    ImageButton mAddButton;
    EqualWidthHeightTextView mDeleteButton;
    LinearLayout mAddPanel;
    ListView mItemsList;
    ImageButton mYesButton;
    ImageButton mNoButton;
    ImageButton mShowDetailsButton;
    RelativeLayout mEmptyView;
    String mDoneArray[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUiElements();
        makeSlides();
        setDate();
        addListeners();
        initDatabase();

        mItemsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {

                View toolbar = view.findViewById(R.id.details);

                // Creating the expand animation for the item
                ExpandAnimation expandAni = new ExpandAnimation(toolbar, 500);

                // Start the animation on the toolbar
                toolbar.startAnimation(expandAni);
            }
        });
    }


    private void initUiElements() {
        setContentView(R.layout.activity_care);
        mBodyList = (GridLayout) findViewById(R.id.body_list);
        mMindList = (GridLayout) findViewById(R.id.mind_list);
        mSoulList = (GridLayout) findViewById(R.id.soul_list);
        mHowMany = (TextView) findViewById(R.id.howMany);
        mHowManyBody = (TextView) findViewById(R.id.howManyBody);
        mHowManyMind = (TextView) findViewById(R.id.howManyMind);
        mHowManySoul = (TextView) findViewById(R.id.howManySoul);
        mHowManyDays = (EqualWidthHeightTextView) findViewById(R.id.howManyDays);
        mBodyDots = (TextView) findViewById(R.id.bodyDots);
        mMindDots = (TextView) findViewById(R.id.mindDots);
        mSoulDots = (TextView) findViewById(R.id.soulDots);
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mItemsList = (ListView) findViewById(R.id.list);
        mPointsNumber = (TextView) findViewById(R.id.pointsNumber);
        mDateEdit = (EditText) findViewById(R.id.editDate);
        mAddButton = (ImageButton) findViewById(addButton);
        mCommentDays = (TextView) findViewById(R.id.comment_days);
        mBodyPointsNumber = (EditText) findViewById(bodyPointsNumber);
        mMindPointsNumber = (EditText) findViewById(R.id.mindPointsNumber);
        mSoulPointsNumber = (EditText) findViewById(R.id.soulPointsNumber);
        mAddPanel = (LinearLayout) findViewById(R.id.addPanel);
        mYesButton = (ImageButton) findViewById(R.id.yesButton);
        mNoButton = (ImageButton) findViewById(R.id.noButton);
        mEmptyView = (RelativeLayout) findViewById(R.id.empty);
        mDeleteButton = (EqualWidthHeightTextView) findViewById(R.id.deleteButton);
        mDoneArray = getResources().getStringArray(R.array.done);
        mShowDetailsButton = (ImageButton) findViewById(R.id.showDetails);
    }

    private void makeSlides() {
        WizardPagerAdapter adapter = new WizardPagerAdapter();
        mViewPager.setAdapter(adapter);
        mViewPager.setOffscreenPageLimit(5);
        mViewPager.setPageTransformer(true, new DepthPageTransformer());
        mAddPanel.setVisibility(View.INVISIBLE);
        mItemsList.setEmptyView(mEmptyView);
    }

    private void setDate() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy (EEE)", Locale.US);
        String formattedDate = df.format(c.getTime());
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
                ;
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
                ;
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
                ;
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
                mAddPanel.setVisibility(View.INVISIBLE);
            }
        });
        mDeleteButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showDeleteConfirmationDialog();
            }
        });
    }

    /* * * ARCHIVE * * */

    private void initDatabase() {
        mCursorAdapter = new ArchiveCursorAdapter(this, null);
        mItemsList.setAdapter(mCursorAdapter);
        getSupportLoaderManager().initLoader(ARCHIVE_LOADER, null, this);
    }

    public void insertArchiveItem(View v) {

        String date = mDateEdit.getText().toString();

        ContentValues values = new ContentValues();
        values.put(ArchiveEntry.COLUMN_DATE, date);
        values.put(ArchiveEntry.COLUMN_POINTS_ALL, pointsNum);
        values.put(ArchiveEntry.COLUMN_POINTS_BODY, bodyPoints);
        values.put(ArchiveEntry.COLUMN_POINTS_MIND, mindPoints);
        values.put(ArchiveEntry.COLUMN_POINTS_SOUL, soulPoints);

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
        negativeButton.setTextColor(getResources().getColor(R.color.green));

        Button positiveButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        positiveButton.setTextColor(getResources().getColor(R.color.circle_delete));
    }

    private void deleteArchiveItem() {
        Cursor c = getContentResolver().query(ArchiveEntry.CONTENT_URI, null, null, null, null);
        c.moveToLast();
        String dayToDelete = c.getString(c.getColumnIndex(ArchiveEntry._ID));
        getContentResolver().delete(ArchiveEntry.CONTENT_URI, ArchiveEntry._ID + "=?", new String[]{dayToDelete});
    }

    /* * * CHANGE POINTS - BODY  * * */

    public void changeFivePointsBody(View v) {
        boolean isChecked = ((ToggleButton) v).isChecked();
        if (isChecked) {
            howMany += 5;
            bodyPoints += 5;
            howManyBody += "● ● ● ● ● ";
        } else {
            howMany -= 5;
            bodyPoints -= 5;
            howManyBody = howManyBody.substring(0, howManyBody.length() - 10);
        }
        changeCareBody(howManyBody);
        changeCare(howMany);
    }

    public void changeThreePointsBody(View v) {
        boolean isChecked = ((ToggleButton) v).isChecked();
        if (isChecked) {
            howMany += 3;
            bodyPoints += 3;
            howManyBody += "● ● ● ";
        } else {
            howMany -= 3;
            bodyPoints -= 3;
            howManyBody = howManyBody.substring(0, howManyBody.length() - 6);
        }
        changeCareBody(howManyBody);
        changeCare(howMany);
    }

    public void changeTwoPointsBody(View v) {
        boolean isChecked = ((ToggleButton) v).isChecked();
        if (isChecked) {
            howMany += 2;
            bodyPoints += 2;
            howManyBody += "● ● ";
        } else {
            howMany -= 2;
            bodyPoints -= 2;
            howManyBody = howManyBody.substring(0, howManyBody.length() - 4);
        }
        changeCareBody(howManyBody);
        changeCare(howMany);
    }

    public void changeOnePointBody(View v) {
        boolean isChecked = ((ToggleButton) v).isChecked();
        if (isChecked) {
            howMany += 1;
            bodyPoints += 1;
            howManyBody += "● ";
        } else {
            howMany -= 1;
            bodyPoints -= 1;
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
        mBodyPointsNumber.setText(Integer.toString(bodyPoints));
    }

    /* * * CHANGE POINTS - MIND * * */

    public void changeFivePointsMind(View v) {
        boolean isChecked = ((ToggleButton) v).isChecked();
        if (isChecked) {
            howMany += 5;
            mindPoints += 5;
            howManyMind += "● ● ● ● ● ";
        } else {
            howMany -= 5;
            mindPoints -= 5;
            howManyMind = howManyMind.substring(0, howManyMind.length() - 10);
        }
        changeCareMind(howManyMind);
        changeCare(howMany);
    }

    public void changeThreePointsMind(View v) {
        boolean isChecked = ((ToggleButton) v).isChecked();
        if (isChecked) {
            howMany += 3;
            mindPoints += 3;
            howManyMind += "● ● ● ";
        } else {
            howMany -= 3;
            mindPoints -= 3;
            howManyMind = howManyMind.substring(0, howManyMind.length() - 6);
        }
        changeCareMind(howManyMind);
        changeCare(howMany);
    }

    public void changeTwoPointsMind(View v) {
        boolean isChecked = ((ToggleButton) v).isChecked();
        if (isChecked) {
            howMany += 2;
            mindPoints += 2;
            howManyMind += "● ● ";
        } else {
            howMany -= 2;
            mindPoints -= 2;
            howManyMind = howManyMind.substring(0, howManyMind.length() - 4);
        }
        changeCareMind(howManyMind);
        changeCare(howMany);
    }

    public void changeOnePointMind(View v) {
        boolean isChecked = ((ToggleButton) v).isChecked();
        if (isChecked) {
            howMany += 1;
            mindPoints += 1;
            howManyMind += "● ";
        } else {
            howMany -= 1;
            mindPoints -= 1;
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
        mMindPointsNumber.setText(Integer.toString(mindPoints));
    }

    /* * * CHANGE POINTS - SOUL * * */

    public void changeFivePointsSoul(View v) {
        boolean isChecked = ((ToggleButton) v).isChecked();
        if (isChecked) {
            howMany += 5;
            soulPoints += 5;
            howManySoul += "● ● ● ● ● ";
        } else {
            howMany -= 5;
            soulPoints -= 5;
            howManySoul = howManySoul.substring(0, howManySoul.length() - 10);
        }
        changeCareSoul(howManySoul);
        changeCare(howMany);
    }

    public void changeThreePointsSoul(View v) {
        boolean isChecked = ((ToggleButton) v).isChecked();
        if (isChecked) {
            howMany += 3;
            soulPoints += 3;
            howManySoul += "● ● ● ";
        } else {
            howMany -= 3;
            soulPoints -= 3;
            howManySoul = howManySoul.substring(0, howManySoul.length() - 6);
        }
        changeCareSoul(howManySoul);
        changeCare(howMany);
    }

    public void changeTwoPointsSoul(View v) {
        boolean isChecked = ((ToggleButton) v).isChecked();
        if (isChecked) {
            howMany += 2;
            soulPoints += 2;
            howManySoul += "● ● ";
        } else {
            howMany -= 2;
            soulPoints -= 2;
            howManySoul = howManySoul.substring(0, howManySoul.length() - 4);
        }
        changeCareSoul(howManySoul);
        changeCare(howMany);
    }

    public void changeOnePointSoul(View v) {
        boolean isChecked = ((ToggleButton) v).isChecked();
        if (isChecked) {
            howMany += 1;
            soulPoints += 1;
            howManySoul += "● ";
        } else {
            howMany -= 1;
            soulPoints -= 1;
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
        mSoulPointsNumber.setText(Integer.toString(soulPoints));
    }

    /* * * POINTS-RELATED * * */

    public void changeCare(int num) {
        pointsNum = bodyPoints + soulPoints + mindPoints;
        mHowMany.setText(String.valueOf(num));
        mPointsNumber.setText(String.valueOf(pointsNum));
    }

    public void changeSum() {
        pointsNum = bodyPoints + soulPoints + mindPoints;
        mPointsNumber.setText(String.valueOf(pointsNum));
    }

    public void resetPoints() {
        pointsNum = bodyPoints = mindPoints = soulPoints = 0;
        mSoulPointsNumber.setText(Integer.toString(soulPoints));
        mBodyPointsNumber.setText(Integer.toString(bodyPoints));
        mMindPointsNumber.setText(Integer.toString(mindPoints));
    }

    private void hidePanel() {
        mAddPanel.setVisibility(View.INVISIBLE);
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    ;

     /* * * CURSOR * * */

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {
                ArchiveEntry._ID,
                ArchiveEntry.COLUMN_DATE,
                ArchiveEntry.COLUMN_POINTS_ALL,
                ArchiveEntry.COLUMN_POINTS_BODY,
                ArchiveEntry.COLUMN_POINTS_MIND,
                ArchiveEntry.COLUMN_POINTS_SOUL
        };

        return new CursorLoader(this, ArchiveEntry.CONTENT_URI, projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCursorAdapter.swapCursor(data);
        int days = mCursorAdapter.getCount();
        mHowManyDays.setText("" + days);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }

};

