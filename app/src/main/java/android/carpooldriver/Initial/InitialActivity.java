package android.carpooldriver.Initial;

import android.carpooldriver.NavigationDrawerMainActivity;
import android.carpooldriver.R;
import android.carpooldriver.Initial.CreateAccount.CreateAccountNameActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class InitialActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        initSignUp();
        initLogin();
        AutomaticLoginFunctionIfUserIsAlreadySignedIn();
    }

    private void initSignUp() {
        RelativeLayout signUpRelativeLayout = findViewById(R.id.init_screen_sign_up);
        signUpRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InitialActivity.this, CreateAccountNameActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initLogin() {
        TextView logInTextView = findViewById(R.id.init_screen_sign_in);
        logInTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InitialActivity.this, LogInActivity.class);
                startActivity(intent);
            }
        });
    }

    // Must check if email is also verified && is logged in already.
    private void AutomaticLoginFunctionIfUserIsAlreadySignedIn() {
        if (currentUser != null) {
            boolean emailVerified = currentUser.isEmailVerified();
            if (emailVerified) {
                SendToMainActivity();
            }
        }
    }

    private void SendToMainActivity() {
        Intent intent = new Intent(InitialActivity.this, NavigationDrawerMainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        // EFFECTS: Animation from LogInActivity to NavigationDrawerMainActivity.
        InitialActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        //why is there finnish
        finish();
    }
}
