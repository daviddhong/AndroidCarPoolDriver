package android.carpooldriver.StartFromLogIn.CreateAccount;

import android.carpooldriver.R;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class DCreateAccountCarType extends AppCompatActivity {
    private EditText car_make_model;
    private String fname, lname, uemail, upw;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.startfromlogin_createaccount_activity_create_account_car_type);
        car_make_model = findViewById(R.id.editText_car_name);


        Bundle gotname = getIntent().getExtras();
        fname = gotname.getString("first_name");
        lname = gotname.getString("last_name");
        uemail = gotname.getString("user_email");
        upw = gotname.getString("user_pw");


        initContinue();
        initBack();
    }

    private void initContinue() {
        RelativeLayout continueActivity = (RelativeLayout) findViewById(R.id.continue_sign_up_car_name);
        continueActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String carmakemodel = car_make_model.getText().toString();
                if (!(carmakemodel.isEmpty())) {
                    Intent intent = new Intent(DCreateAccountCarType.this, ECreateAccountDriversLicense.class);

                    Bundle dataBundle = new Bundle();
                    dataBundle.putString("first_name", fname);
                    dataBundle.putString("last_name", lname);
                    dataBundle.putString("user_email", uemail);
                    dataBundle.putString("user_pw", upw);
                    dataBundle.putString("user_car", carmakemodel);
                    intent.putExtras(dataBundle);
                    startActivity(intent);

                } else {
                    Toast.makeText(DCreateAccountCarType.this, "Please Fill in the model and year of your car", Toast.LENGTH_LONG).show();

                }
            }
        });
    }

    private void initBack() {
        RelativeLayout back = (RelativeLayout) findViewById(R.id.rl_back_create_account_car_type);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
