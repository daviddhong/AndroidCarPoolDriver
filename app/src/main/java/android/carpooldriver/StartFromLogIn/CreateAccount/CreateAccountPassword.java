package android.carpooldriver.StartFromLogIn.CreateAccount;

import android.carpooldriver.R;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class CreateAccountPassword extends AppCompatActivity {

    private String fname, lname, uemail;
    private EditText userpw;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account_password);
        userpw = findViewById(R.id.editText_password_sign_up);



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
                Intent intent = new Intent(CreateAccountPassword.this, CreateAccountPhoneNumber.class);

                String upw = userpw.getText().toString();
                Bundle dataBundle = new Bundle();
                dataBundle.putString("first_name", fname);
                dataBundle.putString("last_name", lname);
                dataBundle.putString("user_email", uemail);
                dataBundle.putString("user_pw", upw);
                intent.putExtras(dataBundle);

                startActivity(intent);
            }
        });
    }

    private void initBack() {
        RelativeLayout back = (RelativeLayout) findViewById(R.id.rl_back_settings_password);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
