package android.carpooldriver.More.Profile;

import android.carpooldriver.R;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MoreProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initEditCar();
        initEditName();
        initEditPhoneNumber();
        initClose();
    }

    private void initEditCar() {
        RelativeLayout editCar = (RelativeLayout) findViewById(R.id.profile_car_type_rl);
        editCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MoreProfileActivity.this, ProfileEditCarModelActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initEditName() {
        RelativeLayout name = (RelativeLayout) findViewById(R.id.profile_name_rl);
        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MoreProfileActivity.this, ProfileEditNameActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initEditPhoneNumber() {
        RelativeLayout phoneNumber = (RelativeLayout) findViewById(R.id.profile_phone_number_rl);
        phoneNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MoreProfileActivity.this, ProfileEditPhoneNumberActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initClose() {
        RelativeLayout close = (RelativeLayout) findViewById(R.id.close_button_profile);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
