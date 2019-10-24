package android.carpooldriver.Initial.CreateAccount;

import android.carpooldriver.R;
import android.carpooldriver.Initial.LogInActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class CreateAccountDriversLicenseActivity extends AppCompatActivity {

    private String fname, lname, uemail, upw, ucar;
    private RelativeLayout create_account_button;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DatabaseReference RootRef;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account_drivers_license);
        create_account_button = findViewById(R.id.continue_sign_up_id_picture);

        mAuth = FirebaseAuth.getInstance();
        RootRef = FirebaseDatabase.getInstance().getReference();
        currentUser = mAuth.getCurrentUser();

        Bundle gotname = getIntent().getExtras();
        fname = gotname.getString("first_name");
        lname = gotname.getString("last_name");
        uemail = gotname.getString("user_email");
        upw = gotname.getString("user_pw");
        ucar = gotname.getString("user_car");

        initBack();
        initContinue();
    }

    private void initContinue() {
        RelativeLayout continueActivity = (RelativeLayout) findViewById(R.id.continue_sign_up_id_picture);
        continueActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                createAccount_createUser_SendVerificationEmail();

            }

        });
    }

    private void createAccount_createUser_SendVerificationEmail() {
        mAuth.createUserWithEmailAndPassword(uemail, upw).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    CollectFirstLastNameEmailIntoRealTimeDatabase(fname, lname, uemail, ucar);
                    SendVerificationEmail();


                } else {
                    // if account is not made
                    Toast.makeText(CreateAccountDriversLicenseActivity.this,
                            task.getException().toString(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    private void SendVerificationEmail() {

//        try {
//            final FirebaseUser currentUser = mAuth.getCurrentUser();
            currentUser.sendEmailVerification()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {

                                Intent intent = new Intent(CreateAccountDriversLicenseActivity.this, LogInActivity.class);
                                startActivity(intent);

                                Toast.makeText(CreateAccountDriversLicenseActivity.this,
                                        "Verification email sent to \n" + uemail, Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(CreateAccountDriversLicenseActivity.this,
                                        "NOT sent!!" + task.getException().toString(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });

//        } catch (NullPointerException e) {
//            Toast.makeText(CreateAccountDriversLicenseActivity.this,
//                    "NOT sent!!" + e.toString(), Toast.LENGTH_LONG).show();
//
//        }
    }

    private void CollectFirstLastNameEmailIntoRealTimeDatabase(String firstN, String lastN, String userEmail, String userCar) {
        String currentUserID = mAuth.getCurrentUser().getUid();
        HashMap<String, String> profileMap = new HashMap<>();
        profileMap.put("uid", currentUserID);
        profileMap.put("firstname", firstN);
        profileMap.put("lastname", lastN);
        profileMap.put("email", userEmail);
        profileMap.put("carmodelmake", userCar);
        RootRef.child("Users").child(currentUserID).setValue(profileMap);
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
