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

public class CreateAccountPassword extends AppCompatActivity {

    private String fname, lname, uemail;
    private EditText userpw, confirmpw;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account_password);
        userpw = findViewById(R.id.editText_password_sign_up);
        confirmpw = findViewById(R.id.editText_password_sign_up_confirm_pw);


        Bundle gotname = getIntent().getExtras();
        fname = gotname.getString("first_name");
        lname = gotname.getString("last_name");
        uemail = gotname.getString("user_email");

        initContinue();
        initBack();
    }

    private void initContinue() {
        RelativeLayout continueActivity = (RelativeLayout) findViewById(R.id.continue_sign_up_password);
        continueActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String upw = userpw.getText().toString();
                String cpw = confirmpw.getText().toString();

                //   todo:
                //    if (not pwd =~ /[0-9]/) return false;
                //    if (not pwd =~ /[a-z]/) return false;
                //    if (not pwd =~ /[A-Z]/) return false;
                //    if (not pwd =~ /[%@$^]/) return false;
                //    if (pwd =~ /\s/) return false;
                //    if (pw.contains(" ")) return false;
                //    https://stackoverflow.com/questions/3802192/regexp-java-for-password-validation

                if (upw.isEmpty()) {
                    Toast.makeText(CreateAccountPassword.this, "Please Enter Your Password", Toast.LENGTH_LONG).show();
                } else if (cpw.isEmpty()) {
                    Toast.makeText(CreateAccountPassword.this, "Please Confirm Your Password", Toast.LENGTH_LONG).show();
                } else if (!(upw.equals(cpw))) {
                    Toast.makeText(CreateAccountPassword.this, "Passwords do not match", Toast.LENGTH_LONG).show();
                } else if (upw.length() < 7) {
                    Toast.makeText(CreateAccountPassword.this,
                            "Password must be at least 7 characters long", Toast.LENGTH_LONG).show();
                } else {
                    Intent intent = new Intent(CreateAccountPassword.this, CreateAccountCarType.class);
                    Bundle dataBundle = new Bundle();

                    dataBundle.putString("first_name", fname);
                    dataBundle.putString("last_name", lname);
                    dataBundle.putString("user_email", uemail);
                    dataBundle.putString("user_pw", upw);

                    intent.putExtras(dataBundle);
                    startActivity(intent);
                }
            }
        });
    }

    private void initBack() {
        RelativeLayout back = (RelativeLayout) findViewById(R.id.rl_back_settings_password_account);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
