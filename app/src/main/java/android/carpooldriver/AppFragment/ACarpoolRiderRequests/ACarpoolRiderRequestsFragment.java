package android.carpooldriver.AppFragment.ACarpoolRiderRequests;

import android.carpooldriver.AppFragment.ACarpoolRiderRequests.content.RiderRequestTicketClass;
import android.carpooldriver.AppFragment.ACarpoolRiderRequests.content.IndividualRiderTicketActivity;
import android.carpooldriver.AppFragment.ACarpoolRiderRequests.content.AcceptedCarpoolRequestActivity;
import android.carpooldriver.AppFragment.ESettings.content.Profile.ProfileActivity;
import android.carpooldriver.R;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ACarpoolRiderRequestsFragment extends Fragment {

    private View mCarpoolRequestsView;
    private RecyclerView FriendRecyclerView;
    private DatabaseReference RiderTicketsRef;
    private String currentUserID, clicked_user_uid;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mCarpoolRequestsView = inflater.inflate(R.layout.app_acarpoolriderrequest_fragment_carpool_rider_requests, container, false);
        initializeFields();
        initProfile();
        initAcceptedCarpoolRequestActivity();
        return mCarpoolRequestsView;
    }

    private void initAcceptedCarpoolRequestActivity() {
        TextView clickHere = (TextView) mCarpoolRequestsView.findViewById(R.id.text_pending_request_threee);
        clickHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AcceptedCarpoolRequestActivity.class);
                startActivity(intent);
            }
        });
    }

    //use recycler view and friend list adapter to display list of friends
    private void initializeFields() {
        // initialize FireBase
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        RiderTicketsRef = FirebaseDatabase.getInstance().getReference().child("RiderTickets");
        // initialize recylerView
        FriendRecyclerView = (RecyclerView) mCarpoolRequestsView.findViewById(R.id.rides_requested_recycler_view);
        FriendRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    // create firebase recycler
    @Override
    public void onStart() {
        super.onStart();

        Query rreceiveriderQuery = FirebaseDatabase
                .getInstance()
                .getReference()
                .child("RiderTickets")
                .orderByChild("status")
                .equalTo("0");

        FirebaseRecyclerOptions<RiderRequestTicketClass> options
                = new FirebaseRecyclerOptions.Builder<RiderRequestTicketClass>()
                .setQuery(rreceiveriderQuery, RiderRequestTicketClass.class)
                .build();

        FirebaseRecyclerAdapter<RiderRequestTicketClass, riderTicketHolder> adapter
                = new FirebaseRecyclerAdapter<RiderRequestTicketClass, riderTicketHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull riderTicketHolder riderticketholder,
                                            int i, @NonNull RiderRequestTicketClass riderReqTickets) {
                RiderTicketsRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
//                            if (riderticketholder.FILTER == 1) {
                                riderticketholder.riderTo.setText(riderReqTickets.getticketto());
                                riderticketholder.riderFrom.setText(riderReqTickets.getticketfrom());
                                riderticketholder.riderDate.setText(riderReqTickets.getticketdate());
                                riderticketholder.riderTime.setText(riderReqTickets.gettickettime());
                                riderticketholder.riderPrice.setText(riderReqTickets.getticketprice());
                                riderticketholder.riderNumberOfSeats.setText(riderReqTickets.getticketnumberofseats());
                                riderticketholder.itemView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        clicked_user_uid = getRef(i).getKey();
                                        Intent intent = new Intent(getActivity(), IndividualRiderTicketActivity.class);
                                        intent.putExtra("clicked_user_id", clicked_user_uid);
                                        startActivity(intent);
                                    }
                                });
                            }
//                            if (riderticketholder.FILTER == 2) {
//                                riderticketholder.itemView.setVisibility(View.GONE);
//                            }
                        }
//                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
            }

            @NonNull
            @Override
            public riderTicketHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.app_acarpoolriderrequest_layout_requested_ride_ticket_entity, parent, false);
                riderTicketHolder viewHolder = new riderTicketHolder(view);
                return viewHolder;
            }
        };
        FriendRecyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    public static class riderTicketHolder extends RecyclerView.ViewHolder {
        TextView riderTo, riderFrom, riderDate, riderTime, riderNumberOfSeats, riderPrice;
//        public static Integer FILTER = 1;

        public riderTicketHolder(@NonNull View itemView) {
            super(itemView);
            riderFrom = itemView.findViewById(R.id.text_origin);
            riderTo = itemView.findViewById(R.id.text_destination);
            riderDate = itemView.findViewById(R.id.text_date);
            riderTime = itemView.findViewById(R.id.text_time);
            riderNumberOfSeats = itemView.findViewById(R.id.text_passenger_number);
            riderPrice = itemView.findViewById(R.id.text_earnings_entity);
        }

    }

    private void initProfile() {
        ImageView profileImageView = (ImageView) mCarpoolRequestsView.findViewById(R.id.profile_carpool_requests);
        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ProfileActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_up, R.anim.slide_vertical_null);
            }
        });
    }
}
