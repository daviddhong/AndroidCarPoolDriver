package android.carpooldriver.StartFromLogIn.CreateAccount;

import android.carpooldriver.R;
import android.carpooldriver.StartFromLogIn.LogInActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class CreateAccountDriversLicense extends AppCompatActivity {

    private String fname, lname, uemail, upw;
    private RelativeLayout create_account_button;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account_drivers_license);
        create_account_button = findViewById(R.id.continue_sign_up_id_picture);

        Bundle gotname = getIntent().getExtras();
        fname = gotname.getString("first_name");
        lname = gotname.getString("last_name");
        uemail = gotname.getString("user_email");
        upw = gotname.getString("user_pw");

        initBack();
        initContinue();
    }

    private void initContinue() {
        RelativeLayout continueActivity = (RelativeLayout) findViewById(R.id.continue_sign_up_car_name);
        continueActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateAccountDriversLicense.this, LogInActivity.class);
                startActivity(intent);
            }

        });
    }

    private void initBack() {
        RelativeLayout back = (RelativeLayout) findViewById(R.id.rl_back_create_account_drivers_license);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
