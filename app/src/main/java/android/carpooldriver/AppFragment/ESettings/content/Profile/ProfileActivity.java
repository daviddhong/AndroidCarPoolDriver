package android.carpooldriver.AppFragment.ESettings.content.Profile;

import android.carpooldriver.R;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    private TextView Name, PhoneNumber, CAR;
    private DatabaseReference UsersRef;
    private String userUID;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_esettings_content_profile_activity_profile);
        initFields();
        setFields();
        initEditCar();
        initEditName();
        initEditPhoneNumber();
        initClose();
    }

    private void initFields() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        userUID = mAuth.getCurrentUser().getUid();
        Name = findViewById(R.id.name_profile);
        PhoneNumber = findViewById(R.id.phone_number_profile);
        CAR = findViewById(R.id.name_car_type);

        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");
    }

    private void setFields() {
        UsersRef.child(userUID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Name.setText(dataSnapshot.child("firstname").getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void initEditCar() {
        RelativeLayout editCar = (RelativeLayout) findViewById(R.id.profile_car_type_rl);
        editCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, ProfileEditCarModelActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initEditName() {
        RelativeLayout name = (RelativeLayout) findViewById(R.id.profile_name_rl);
        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, ProfileEditNameActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initEditPhoneNumber() {
        RelativeLayout phoneNumber = (RelativeLayout) findViewById(R.id.profile_phone_number_rl);
        phoneNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, ProfileEditPhoneNumberActivity.class);
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
