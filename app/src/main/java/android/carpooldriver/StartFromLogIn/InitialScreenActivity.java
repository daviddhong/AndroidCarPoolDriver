package android.carpooldriver.StartFromLogIn;

import android.carpooldriver.R;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class InitialScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial_screen);
        initSignUp();
        initLogin();
    }

    // MODIFIES: this
    // EFFECTS: Initialize the sign up activity when relative layout is selected.
    private void initSignUp() {
        RelativeLayout signUpRelativeLayout = findViewById(R.id.init_screen_sign_up);
        signUpRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InitialScreenActivity.this, MakeAccountActivity.class);
                startActivity(intent);
            }
        });
    }

    // MODIFIES: this
    // EFFECTS: Initialize the log in activity when the text view is selected.
    private void initLogin() {
        TextView logInTextView = findViewById(R.id.init_screen_sign_in);
        logInTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InitialScreenActivity.this, LogInActivity.class);
                startActivity(intent);
            }
        });
    }
}
