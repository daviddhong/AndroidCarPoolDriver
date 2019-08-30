package android.carpooldriver.PostCarpool;

import android.carpooldriver.R;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.shawnlin.numberpicker.NumberPicker;

public class PostCarpoolSeatsActivity extends AppCompatActivity {

    NumberPicker seats;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_new_carpool_seats);
        initSeatNumberPicker();
        initNext();
    }

    // EFFECTS: Initialize the number picker for the number of seats.
    private void initSeatNumberPicker() {
        seats = (NumberPicker) findViewById(R.id.pn_picker);
        seats.setFadingEdgeEnabled(false);
    }

    // EFFECTS: Initialize the next activity.
    private void initNext() {
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.next_carpool_seat_post);
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PostCarpoolSeatsActivity.this, PostCarpoolDateActivity.class);

                startActivity(intent);

                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
    }
}
