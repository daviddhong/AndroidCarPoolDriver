package android.carpooldriver.Fragments.SettingsFragment.SettingsContent.ProfileActivities;

import android.carpooldriver.R;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ProfileEditPhoneNumberActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_phone_number);

        initBack();
    }

    private void initBack() {
        RelativeLayout back = (RelativeLayout) findViewById(R.id.rl_back_profile_phone_number);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
