package android.carpooldriver;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class PostCarpoolFragment extends Fragment {

    View mPostCarpoolView;
    RelativeLayout mPostCarpoolRelativeLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mPostCarpoolView = inflater.inflate(R.layout.fragment_post_carpool, container, false);
        initPostNewCarpool();
        return mPostCarpoolView;
    }

    // EFFECTS: Initialize the post new carpool activity.
    private void initPostNewCarpool() {
        mPostCarpoolRelativeLayout = (RelativeLayout) mPostCarpoolView.findViewById(R.id.post_new_carpool);
        mPostCarpoolRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PostCarpoolOneActivity.class);
                startActivity(intent);
                // EFFECTS: Animation to Profile Activity
                getActivity().overridePendingTransition(R.anim.slide_up, R.anim.slide_vertical_null);
            }
        });
    }
}
