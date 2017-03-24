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

class EditTextDatePicker implements View.OnClickListener, DatePickerDialog.OnDateSetListener {
    private EditText _editText;
    private int _day;
    private int _month;
    private int _year;
    private Context _context;
    private String _dayOfWeek;
    private String _monthWithZero;

    EditTextDatePicker(Context context, EditText editTextViewID) throws ParseException {
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

        DatePickerDialog dialog = new DatePickerDialog(_context,  R.style.dialog,  this,
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        DatePicker datePicker = dialog.getDatePicker();
        datePicker.setMaxDate(calendar.getTimeInMillis());
        dialog.show();
    }

    // updates the date in the EditText
    private void DisplayDate() {
        String chosenDate = _day + "." + (_monthWithZero) + "." + _year + " (" + _dayOfWeek + ")";
        _editText.setText(chosenDate);
    }
}