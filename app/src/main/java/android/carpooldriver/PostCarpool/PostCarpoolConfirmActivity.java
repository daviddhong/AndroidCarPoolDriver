package android.carpooldriver.PostCarpool;

import android.carpooldriver.MainActivity;
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
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DatabaseReference RootRef, RootKeyRef;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_new_carpool_confirm);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        currentUserName = currentUser.getUid();
        RootRef = FirebaseDatabase.getInstance().getReference().child("DriverTickets");
        setConfirmActivityRelativeLayout();
    }

    // EFFECTS: Set ConfirmNewRideRequest.
    private void setConfirmActivityRelativeLayout() {
        mConfirmActivityRelativeLayout = (RelativeLayout) findViewById(R.id.confirm_carpool);
        mConfirmActivityRelativeLayout.setOnClickListener(new View.OnClickListener() {
//            Bundle bundle = getIntent().getExtras();
//            String origin = bundle.getString("ORIGIN");
//            String destination = bundle.getString("DESTINATION");
//            String date = bundle.getString("DATE_VALUE");
//            String time = bundle.getString("TIME_VALUE");
//            String passengerNumber = bundle.getString("PASSENGER_NUMBER_SELECTED");
            @Override
            public void onClick(View v) {
                //save to realtime database
                Toast.makeText(PostCarpoolConfirmActivity.this, "clicked", Toast.LENGTH_LONG).show();
                savetorealtimedatabase();
                Intent intent = new Intent(PostCarpoolConfirmActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                // EFFECTS: Animation from ()Activity to ()Activity.
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }

    private void savetorealtimedatabase() {
        String messageKey = RootRef.push().getKey();
        HashMap<String, Object> riderTicketKey = new HashMap<>();
        RootRef.updateChildren(riderTicketKey);

        RootKeyRef = RootRef.child(messageKey);

        String currentUserID = mAuth.getCurrentUser().getUid();
        HashMap<String, Object> profileMap = new HashMap<>();
        profileMap.put("uid", currentUserID);
        profileMap.put("From", "Coquitlam");
        profileMap.put("To", "UBC");
        profileMap.put("NumberOfSeats", "2");
        profileMap.put("Price", "$4");
        profileMap.put("Date", "Jan/02/20");
        RootKeyRef.updateChildren(profileMap);
    }

}
