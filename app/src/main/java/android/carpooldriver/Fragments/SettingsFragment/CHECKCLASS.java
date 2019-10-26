//ESettingsFragment
//
//        package android.carpooldriver.AppFragment.ESettings;
//
//import android.carpooldriver.AppFragment.ESettings.content.EmailActivity;
//import android.carpooldriver.AppFragment.ESettings.content.OpenSourceLicenseActivity;
//import android.carpooldriver.AppFragment.ESettings.content.PhoneNumberActivity;
//import android.carpooldriver.AppFragment.ESettings.content.Password.CurrentPasswordActivity;
//import android.carpooldriver.AppFragment.ESettings.content.Profile.ProfileActivity;
//import android.carpooldriver.R;
//import android.carpooldriver.StartFromLogIn.InitialScreenActivity;
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.Fragment;
//
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//
//public class ESettingsFragment extends Fragment {
//
//    View mMoreView;
//    private RelativeLayout sign_out_button;
//    FirebaseAuth mAuth;
//
//    private DatabaseReference RootRef;
//    String currentUID;
//    private TextView firstName, lastName;
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        mMoreView = inflater.inflate(R.layout.app_esettings_fragment_settings, container, false);
//        mAuth = FirebaseAuth.getInstance();
//
//
//
//        firstName = mMoreView.findViewById(R.id.firstnameofuser);
//        lastName = mMoreView.findViewById(R.id.lastnameofuser);
//        currentUID = mAuth.getCurrentUser().getUid();
//        RootRef = FirebaseDatabase.getInstance().getReference();
//        RootRef.child("Users").child(currentUID).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if (dataSnapshot.exists()) {
//
//                    firstName.setText(dataSnapshot.child("firstname").getValue().toString());
//                    lastName.setText(dataSnapshot.child("lastname").getValue().toString());
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//
//
//        initProfile();
//        initEmail();
//        initAccountPassword();
//        initOpenSourceLicense();
//        initPhone();
//        // EFFECTS: Call setLogoutRelativeLayout;
//        setLogoutRelativeLayout();
//        return mMoreView;
//    }
//
//    // MODIFIES: this
//    // EFFECTS: Set OnClickActivity for relative_layout_logout_settings
//    private void setLogoutRelativeLayout() {
//        sign_out_button = (RelativeLayout) mMoreView.findViewById(R.id.sign_out_auth_button);
//        sign_out_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mAuth.signOut();
//                Intent intent = new Intent(getActivity(), InitialScreenActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                startActivity(intent);
//                // EFFECTS: Animation from SettingsActivity to EditProfileActivity.
//                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//            }
//        });
//
//    }
//
//    // MODIFIES: this
//    // EFFECTS: Initialize the Profile Activity from the Settings fragment.
//    private void initProfile() {
//        RelativeLayout settingsProfile = (RelativeLayout) mMoreView.findViewById(R.id.settings_profile);
//        settingsProfile.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), ProfileActivity.class);
//                startActivity(intent);
//
//                // EFFECTS: Animation to Profile Activity
//                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//            }
//        });
//    }
//
//    // MODIFIES: this
//    // EFFECTS: Initialize the EmailActivity from the Settings fragment.
//    private void initEmail() {
//        RelativeLayout settingsEmail = (RelativeLayout) mMoreView.findViewById(R.id.settings_email);
//        settingsEmail.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), EmailActivity.class);
//                startActivity(intent);
//
//                // EFFECTS: Animation to EmailActivity.
//                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//            }
//        });
//    }
//
//    private void initPhone() {
//        RelativeLayout settingsPhone = (RelativeLayout) mMoreView.findViewById(R.id.settings_phone);
//        settingsPhone.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), PhoneNumberActivity.class);
//                startActivity(intent);
//
//                // EFFECTS: Animation to CurrentPasswordActivity.
//                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//            }
//        });
//    }
//
//    // MODIFIES: this
//    // EFFECTS: Initialize the AccountPassword from the Settings fragment.
//    private void initAccountPassword() {
//        RelativeLayout settingsAccountPassword = (RelativeLayout) mMoreView.findViewById(R.id.settings_password);
//        settingsAccountPassword.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), CurrentPasswordActivity.class);
//                startActivity(intent);
//
//                // EFFECTS: Animation to CurrentPasswordActivity.
//                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//            }
//        });
//    }
//
//    private void initPayment() {
//
//    }
//
//    private void initHelp() {
//
//    }
//
//    private void initOpenSourceLicense() {
//        RelativeLayout settingsOSL = (RelativeLayout) mMoreView.findViewById(R.id.settings_open_source);
//        settingsOSL.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), OpenSourceLicenseActivity.class);
//                startActivity(intent);
//
//                // EFFECTS: Animation to OpenSourceLicenseActivity.
//                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//            }
//        });
//    }
//
//    private void initTermsPolicies() {
//
//    }
//
//    private void initLegal() {
//
//    }
//
//    private void initSignOut() {
//
//    }
//}
//
