package android.carpooldriver.PostCarpool;


import android.carpooldriver.R;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.shawnlin.numberpicker.NumberPicker;

import java.util.Calendar;

public class PostCarpoolDateActivity extends AppCompatActivity {

    private NumberPicker month;
    private NumberPicker day;
    private NumberPicker year;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_new_carpool_date);
        initMonthNumberPicker();
        initDayNumberPicker();
        initYearNumberPicker();
        initNext();
    }

    // EFFECTS: Initialize the month number picker.
    private void initMonthNumberPicker() {
        month = (NumberPicker) findViewById(R.id.month_picker);
        String[] data = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug",
                "Sept", "Oct", "Nov", "Dec"};
        month.setMinValue(1);
        month.setMaxValue(data.length);
        month.setDisplayedValues(data);
        month.setFadingEdgeEnabled(false);
    }

    // EFFECTS: Initialize the day number picker.
    private void initDayNumberPicker() {
        day = (NumberPicker) findViewById(R.id.day_picker);
        String[] data = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12",
                "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26",
                "27", "28", "29", "30", "31"};
        day.setMinValue(1);
        day.setMaxValue(data.length);
        day.setDisplayedValues(data);
        day.setFadingEdgeEnabled(false);
    }

    // EFFECTS: Initialize the year number picker.
    private void initYearNumberPicker() {
        year = (NumberPicker) findViewById(R.id.year_picker);
        Calendar yearCalendar = Calendar.getInstance();
        int yearInt = yearCalendar.get(Calendar.YEAR);
        int yearIntTwo = yearCalendar.get(Calendar.YEAR) + 1;
        String yearString = String.valueOf(yearInt);
        String yearStringTwo = String.valueOf(yearIntTwo);
        String[] data = {yearString, yearStringTwo};
        year.setMinValue(1);
        year.setMaxValue(data.length);
        year.setDisplayedValues(data);
        year.setFadingEdgeEnabled(false);
    }

    // EFFECTS: Initialize the next activity.
    private void initNext() {

    }
}
