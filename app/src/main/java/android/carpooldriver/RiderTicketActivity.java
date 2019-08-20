package android.carpooldriver;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class RiderTicketActivity extends AppCompatActivity {

    private TextView ticketID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider_ticket);

        ticketID = findViewById(R.id.textView2);

        ticketID.setText(getIntent().getExtras().get("clicked_user_id").toString());
    }
}
