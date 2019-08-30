package android.carpooldriver.PostCarpool;

import android.carpooldriver.R;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.shawnlin.numberpicker.NumberPicker;

public class PostCarpoolTimeActivity extends AppCompatActivity {

    private NumberPicker hour;
    private NumberPicker minutes;
    private NumberPicker period;
    String hourString;
    String minutesString;
    String periodString;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_new_carpool_time);
        initHourPicker();
        initMinutePicker();
        initPeriodPicker();
        initNext();
    }

    // EFFECTS: Initialize the hour number picker.
    private void initHourPicker() {
        hour = (NumberPicker) findViewById(R.id.hour_picker);
        String[] data = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};
        hour.setMinValue(1);
        hour.setMaxValue(data.length);
        hour.setDisplayedValues(data);
        hour.setFadingEdgeEnabled(false);

        hourString = String.valueOf(hour.getValue());

    }

    // EFFECTS: Initialize the minute number picker.
    private void initMinutePicker() {
        minutes = (NumberPicker) findViewById(R.id.minute_picker);
        String[] data = {"00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11",
                "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25",
                "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39",
                "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53",
                "54", "55", "56", "57", "58", "59"};
        minutes.setMinValue(1);
        minutes.setMaxValue(data.length);
        minutes.setDisplayedValues(data);
        minutes.setFadingEdgeEnabled(false);

        minutesString = String.valueOf(minutes.getValue());
    }

    // EFFECTS: Initialize the period number picker.
    private void initPeriodPicker() {
        period = (NumberPicker) findViewById(R.id.am_pm_picker);
        String[] data = {"AM", "PM"};
        period.setMinValue(1);
        period.setMaxValue(data.length);
        period.setDisplayedValues(data);
        period.setFadingEdgeEnabled(false);

        periodString = String.valueOf(period.getValue());
    }

    // EFFECTS: Initialize the next activity.
    private void initNext() {
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.next_carpool_time_post);
        relativeLayout.setOnClickListener(new View.OnClickListener() {

            Bundle bundle = getIntent().getExtras();
            String origin = bundle.getString("ORIGIN_LOCATION_STRING_KEY");
            String destination = bundle.getString("DESTINATION_LOCATION_STRING_KEY");
            String earnings = bundle.getString("EARNINGS_STRING_KEY");
            String seats = bundle.getString("SEATS_STRING");
            String month = bundle.getString("MONTH_STRING");
            String day = bundle.getString("DAY_STRING");
            String year = bundle.getString("YEAR_STRING");

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PostCarpoolTimeActivity.this, PostCarpoolConfirmActivity.class);

                intent.putExtra("ORIGIN_LOCATION_STRING_KEY", origin);
                intent.putExtra("DESTINATION_LOCATION_STRING_KEY", destination);
                intent.putExtra("EARNINGS_STRING_KEY", earnings);
                intent.putExtra("SEATS_STRING", seats);
                intent.putExtra("MONTH_STRING", month);
                intent.putExtra("DAY_STRING", day);
                intent.putExtra("YEAR_STRING", year);
                intent.putExtra("HOUR_STRING", hourString);
                intent.putExtra("MINUTES_STRING", minutesString);
                intent.putExtra("PERIOD_STRING", periodString);

                startActivity(intent);

                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
    }
}
