package android.carpooldriver.More;

import android.carpooldriver.More.Password.MoreCurrentPasswordActivity;
import android.carpooldriver.More.Profile.MoreProfileActivity;
import android.carpooldriver.R;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class MoreFragmentActivity extends Fragment {

    View mMoreView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mMoreView = inflater.inflate(R.layout.fragment_more, container, false);
        initProfile();
        initEmail();
        initAccountPassword();
        initOpenSourceLicense();
        return mMoreView;
    }

    // MODIFIES: this
    // EFFECTS: Initialize the Profile Activity from the Settings fragment.
    private void initProfile() {
        RelativeLayout settingsProfile = (RelativeLayout) mMoreView.findViewById(R.id.settings_profile);
        settingsProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MoreProfileActivity.class);
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
                Intent intent = new Intent(getActivity(), MoreEmailActivity.class);
                startActivity(intent);

                // EFFECTS: Animation to EmailActivity.
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }

    private void initPhone() {

    }

    // MODIFIES: this
    // EFFECTS: Initialize the AccountPassword from the Settings fragment.
    private void initAccountPassword() {
        RelativeLayout settingsAccountPassword = (RelativeLayout) mMoreView.findViewById(R.id.settings_password);
        settingsAccountPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MoreCurrentPasswordActivity.class);
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
                Intent intent = new Intent(getActivity(), MoreOpenSourceLicenseActivity.class);
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
