package android.carpooldriver.StartFromLogIn;

import android.carpooldriver.R;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class InitActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial_screen);

        initSignIn();
    }

    private void initSignIn() {
        TextView signIn = (TextView) findViewById(R.id.init_screen_sign_in);
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InitActivity.this, LogInActivity.class);
                startActivity(intent);

            }
        });
    }

    private void initSignUp() {

    }
}
