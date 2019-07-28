package android.carpooldriver.YourCarpool;

import android.carpooldriver.R;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class YourCarpoolFragment extends Fragment {

    private View mYourCarpoolView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mYourCarpoolView = inflater.inflate(R.layout.fragment_your_carpool, container, false);
        return mYourCarpoolView;
    }



}
