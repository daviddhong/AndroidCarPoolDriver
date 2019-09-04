package android.carpooldriver;

import android.carpooldriver.Settings.EmailActivity;
import android.carpooldriver.Settings.OpenSourceLicenseActivity;
import android.carpooldriver.Settings.PhoneNumberActivity;
import android.carpooldriver.Settings.Password.CurrentPasswordActivity;
import android.carpooldriver.Settings.Profile.ProfileActivity;
import android.carpooldriver.StartFromLogIn.LogInActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;

public class SettingsFragment extends Fragment {

    View mMoreView;
    private RelativeLayout sign_out_button;
    FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mMoreView = inflater.inflate(R.layout.fragment_more, container, false);
        mAuth = FirebaseAuth.getInstance();
        initProfile();
        initEmail();
        initAccountPassword();
        initOpenSourceLicense();
        initPhone();
        // EFFECTS: Call setLogoutRelativeLayout;
        setLogoutRelativeLayout();
        return mMoreView;
    }

    // MODIFIES: this
    // EFFECTS: Set OnClickActivity for relative_layout_logout_settings
    private void setLogoutRelativeLayout() {
        sign_out_button = (RelativeLayout) mMoreView.findViewById(R.id.sign_out_auth_button);
        sign_out_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent intent = new Intent(getActivity(), LogInActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                // EFFECTS: Animation from SettingsActivity to EditProfileActivity.
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

    }

    // MODIFIES: this
    // EFFECTS: Initialize the Profile Activity from the Settings fragment.
    private void initProfile() {
        RelativeLayout settingsProfile = (RelativeLayout) mMoreView.findViewById(R.id.settings_profile);
        settingsProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ProfileActivity.class);
                startActivity(intent);

                // EFFECTS: Animation to Profile Activity
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }

    // MODIFIES: this
    // EFFECTS: Initialize the EmailActivity from the Settings fragment.
    private void initEmail() {
        RelativeLayout settingsEmail = (RelativeLayout) mMoreView.findViewById(R.id.settings_email);
        settingsEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EmailActivity.class);
                startActivity(intent);

                // EFFECTS: Animation to EmailActivity.
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }

    private void initPhone() {
        RelativeLayout settingsPhone = (RelativeLayout) mMoreView.findViewById(R.id.settings_phone);
        settingsPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PhoneNumberActivity.class);
                startActivity(intent);

                // EFFECTS: Animation to CurrentPasswordActivity.
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }

    // MODIFIES: this
    // EFFECTS: Initialize the AccountPassword from the Settings fragment.
    private void initAccountPassword() {
        RelativeLayout settingsAccountPassword = (RelativeLayout) mMoreView.findViewById(R.id.settings_password);
        settingsAccountPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CurrentPasswordActivity.class);
                startActivity(intent);

                // EFFECTS: Animation to CurrentPasswordActivity.
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }

    private void initPayment() {

    }

    private void initHelp() {

    }

    private void initOpenSourceLicense() {
        RelativeLayout settingsOSL = (RelativeLayout) mMoreView.findViewById(R.id.settings_open_source);
        settingsOSL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), OpenSourceLicenseActivity.class);
                startActivity(intent);

                // EFFECTS: Animation to OpenSourceLicenseActivity.
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }

    private void initTermsPolicies() {

    }

    private void initLegal() {

    }

    private void initSignOut() {

    }
}