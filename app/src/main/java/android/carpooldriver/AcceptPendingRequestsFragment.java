package android.carpooldriver;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class AcceptPendingRequestsFragment extends Fragment {

    View mRequestView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRequestView = inflater.inflate(R.layout.fragment_my_request, container, false);
        initAcceptedCarpoolRequestActivity();
        return mRequestView;
    }

    private void initAcceptedCarpoolRequestActivity() {
        TextView clickHere = (TextView) mRequestView.findViewById(R.id.text_pending_request_three);
        clickHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AcceptedCarpoolRequestActivity.class);
                startActivity(intent);
            }
        });
    }
}
