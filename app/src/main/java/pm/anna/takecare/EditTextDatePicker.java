package pm.anna.takecare;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Anna on 17.02.2017.
 */

public class EditTextDatePicker implements View.OnClickListener, DatePickerDialog.OnDateSetListener {
    EditText _editText;
    public int _day;
    public int _month;
    public int _year;
    public Context _context;
    public String _dayOfWeek;
    public String _monthWithZero;
    public String chosenDate;

    public EditTextDatePicker(Context context, EditText editTextViewID) throws ParseException {
        Activity act = (Activity) context;
        this._editText = editTextViewID;
        this._editText.setOnClickListener(this);
        this._context = context;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        _year = year;
        _month = monthOfYear;
        _day = dayOfMonth;
        SimpleDateFormat dateformat = new SimpleDateFormat("EEE", Locale.US);
        Date date = new Date(_year, _month, _day - 1);
        _dayOfWeek = dateformat.format(date);
        SimpleDateFormat betterMonth = new SimpleDateFormat("MM", Locale.US);
        _monthWithZero = betterMonth.format(date);
        DisplayDate();
    }

    @Override
    public void onClick(View v) {
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());

        DatePickerDialog dialog = new DatePickerDialog(_context, this,
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    // updates the date in the EditText
    private void DisplayDate(){
      chosenDate = _day + "." + (_monthWithZero) + "." + _year + " (" + _dayOfWeek + ")";
        _editText.setText(chosenDate);
    }
}