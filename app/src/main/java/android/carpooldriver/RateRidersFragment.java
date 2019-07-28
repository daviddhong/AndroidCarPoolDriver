package android.carpooldriver;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class RateRidersFragment extends Fragment {

    View mRateRidersView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRateRidersView = inflater.inflate(R.layout.fragment_rate_riders, container, false);
        return mRateRidersView;
    }
}
