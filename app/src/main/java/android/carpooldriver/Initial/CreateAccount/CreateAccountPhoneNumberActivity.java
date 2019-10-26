package android.carpooldriver.Initial.CreateAccount;

import android.carpooldriver.R;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class CreateAccountPhoneNumberActivity extends AppCompatActivity {

    private String fname, lname, uemail;
    private EditText phoneNumb;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account_phone_number);

        phoneNumb = findViewById(R.id.editText_phonenumber_sign_up);

        Bundle gotname = getIntent().getExtras();
        fname = gotname.getString("first_name");
        lname = gotname.getString("last_name");
        uemail = gotname.getString("user_email");

        initContinue();
        initBack();
    }

    private void initContinue() {
        RelativeLayout continueActivity = (RelativeLayout) findViewById(R.id.continue_sign_up_phone_number);
        continueActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String phone_num = phoneNumb.getText().toString();

                if (phone_num.isEmpty()) {
                    Toast.makeText(CreateAccountPhoneNumberActivity.this, "Please Enter Phone Number", Toast.LENGTH_SHORT).show();
                } else if ((phone_num.length() < 10) || (phone_num.length() > 11)) {
                    Toast.makeText(CreateAccountPhoneNumberActivity.this, "Please Enter Valid Phone Number", Toast.LENGTH_SHORT).show();
                } else {

                    Intent intent = new Intent(CreateAccountPhoneNumberActivity.this, CreateAccountPasswordActivity.class);
                    Bundle dataBundle = new Bundle();
                    dataBundle.putString("first_name", fname);
                    dataBundle.putString("last_name", lname);
                    dataBundle.putString("user_email", uemail);
                    dataBundle.putString("phone_number", phone_num);
                    intent.putExtras(dataBundle);
                    startActivity(intent);
                }
            }
        });
    }

    private void initBack() {
        RelativeLayout back = (RelativeLayout) findViewById(R.id.rl_back_create_account_phone_number);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
