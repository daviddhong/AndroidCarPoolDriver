package android.carpooldriver;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class PendingRequestsFragment extends Fragment {

    View mRequestView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRequestView = inflater.inflate(R.layout.fragment_my_request, container, false);
        return mRequestView;


    }
}
