package android.carpooldriver.AppFragment.BPostCarpool.content;

import android.carpooldriver.AppFragment.BottomNavigationMainActivity;
import android.carpooldriver.R;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class PostCarpoolConfirmActivity extends AppCompatActivity {

    RelativeLayout mConfirmActivityRelativeLayout;
    String currentUserName;
    TextView carpoolOriginText;
    TextView carpoolDestinationText;
    TextView seatsText;
    TextView earningsText;
    TextView dateText;
    TextView timeText;
    private String seats, time, date, origin, destination, earnings;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DatabaseReference RootRef, RootKeyRef;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bpostcarpool_content_activity_carpool_confirm);

        initBack();

        initOriginText();
        initDestinationText();
        initSeats();
        initDateText();
        initTimeText();
        initEarningsText();

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        currentUserName = currentUser.getUid();
        RootRef = FirebaseDatabase.getInstance().getReference().child("DriverTickets");
        setConfirmActivityRelativeLayout();
    }

    private void initBack() {
        RelativeLayout back = (RelativeLayout) findViewById(R.id.rl_back_confirm);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initSeats() {
        Bundle bundle = getIntent().getExtras();
        seats = bundle.getString("SEATS_STRING");
        seatsText = (TextView) findViewById(R.id.seats_confirm);
        seatsText.setText(seats);
    }

    private void initOriginText() {
        Bundle bundle = getIntent().getExtras();
        origin = bundle.getString("ORIGIN_LOCATION_STRING_KEY");
        carpoolOriginText = (TextView) findViewById(R.id.origin_data);
        carpoolOriginText.setText(origin);
    }

    private void initDestinationText() {
        Bundle bundle = getIntent().getExtras();
        destination = bundle.getString("DESTINATION_LOCATION_STRING_KEY");
        carpoolDestinationText = (TextView) findViewById(R.id.destination_data);
        carpoolDestinationText.setText(destination);
    }

    private void initEarningsText() {
        Bundle bundle = getIntent().getExtras();
        earnings = bundle.getString("EARNINGS_STRING_KEY");
        earningsText = (TextView) findViewById(R.id.earnings_text_confirm);
        earningsText.setText(earnings);
    }

    private void initDateText() {
        Bundle bundle = getIntent().getExtras();
        String month = bundle.getString("MONTH_STRING");
        String day = bundle.getString("DAY_STRING");
        String year = bundle.getString("YEAR_STRING");
        dateText = (TextView) findViewById(R.id.date_confirm);
        date = month + " " + day + ", " + year;
        dateText.setText(date);
    }

    private void initTimeText() {
        Bundle bundle = getIntent().getExtras();
        String hour = bundle.getString("HOUR_STRING");
        String minutes = bundle.getString("MINUTES_STRING");
        String period = bundle.getString("PERIOD_STRING");
        timeText = (TextView) findViewById(R.id.time_confirm);
        time = hour + " " + ":" + " " + minutes + " " + period;
        timeText.setText(time);
    }

    // EFFECTS: Set ConfirmNewRideRequest.
    private void setConfirmActivityRelativeLayout() {
        mConfirmActivityRelativeLayout = (RelativeLayout) findViewById(R.id.confirm_carpool);
        mConfirmActivityRelativeLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //save to realtime database
                Toast.makeText(PostCarpoolConfirmActivity.this, "clicked", Toast.LENGTH_LONG).show();
                saveToRealTimeDatabase();
                Intent intent = new Intent(PostCarpoolConfirmActivity.this, BottomNavigationMainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                // EFFECTS: Animation from ()Activity to ()Activity.
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }

    private void saveToRealTimeDatabase() {
        String messageKey = RootRef.push().getKey();
        HashMap<String, Object> riderTicketKey = new HashMap<>();
        RootRef.updateChildren(riderTicketKey);
        RootKeyRef = RootRef.child(messageKey);
        String currentUserID = mAuth.getCurrentUser().getUid();
        HashMap<String, Object> profileMap = new HashMap<>();
        profileMap.put("uid", currentUserID);
        profileMap.put("From", origin);
        profileMap.put("To", destination);
        profileMap.put("NumberOfSeats", seats);
        profileMap.put("Price", earnings);
        profileMap.put("Date", date);
        profileMap.put("Time", time);
        RootKeyRef.updateChildren(profileMap);
    }

}
