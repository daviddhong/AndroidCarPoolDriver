package android.carpooldriver;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class PostCarpoolFragment extends Fragment {

    View mPostCarpoolView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mPostCarpoolView = inflater.inflate(R.layout.fragment_post_carpool, container, false);
        return mPostCarpoolView;
    }
}
