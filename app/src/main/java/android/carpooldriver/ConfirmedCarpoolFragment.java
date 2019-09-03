package android.carpooldriver;

import android.carpooldriver.Settings.Profile.MoreProfileActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ConfirmedCarpoolFragment extends Fragment {

    private View mYourCarpoolView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mYourCarpoolView = inflater.inflate(R.layout.fragment_your_carpool, container, false);

        initProfile();
        return mYourCarpoolView;
    }

    private void initProfile() {
        ImageView profileImageView = (ImageView) mYourCarpoolView.findViewById(R.id.profile_your_carpool);
        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MoreProfileActivity.class);
                startActivity(intent);
            }
        });
    }

}
