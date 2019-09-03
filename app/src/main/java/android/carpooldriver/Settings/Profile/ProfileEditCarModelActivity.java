package android.carpooldriver.Settings.Profile;

import android.carpooldriver.R;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ProfileEditCarModelActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit_car_model_year);

        backButton();
    }

    private void backButton() {
        RelativeLayout back = (RelativeLayout) findViewById(R.id.rl_back_profile_edit_car);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
