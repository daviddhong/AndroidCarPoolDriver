package android.carpooldriver.Fragments.CarpoolRiderRequestsFragment;

import android.carpooldriver.Fragments.CarpoolRiderRequestsFragment.Content.RiderRequestTicketClass;
import android.carpooldriver.Fragments.CarpoolRiderRequestsFragment.Content.IndividualRiderTicketActivity;
import android.carpooldriver.Fragments.SettingsFragment.Content.ProfileActivities.ProfileActivity;
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
import com.google.firebase.database.ValueEventListener;

public class CarpoolRiderRequestsFragment extends Fragment {
    private View mCarpoolRequestsView;
    private RecyclerView FriendRecyclerView;
    private DatabaseReference RiderTicketsRef;
    private String currentUserID, clicked_user_uid;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mCarpoolRequestsView = inflater.inflate(R.layout.fragment_carpool_rider_requests, container, false);
        initializeFields();
        return mCarpoolRequestsView;
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
        FirebaseRecyclerOptions<RiderRequestTicketClass> options
                = new FirebaseRecyclerOptions.Builder<RiderRequestTicketClass>()
                .setQuery(RiderTicketsRef, RiderRequestTicketClass.class)
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
//                                    String clicked_uid = getRef(i).child("uid").toString();
                                    Intent intent = new Intent(getActivity(), IndividualRiderTicketActivity.class);
                                    intent.putExtra("clicked_user_id", clicked_user_uid);
//                                    intent.putExtra("clicked_uid", clicked_uid);

                                    startActivity(intent);
                                }
                            });
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
            }
            @NonNull
            @Override
            public riderTicketHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_requested_ride_ticket_entity, parent, false);
                riderTicketHolder viewHolder = new riderTicketHolder(view);
                return viewHolder;
            }
        };
        FriendRecyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    public static class riderTicketHolder extends RecyclerView.ViewHolder {
        TextView riderTo, riderFrom, riderDate, riderTime, riderNumberOfSeats, riderPrice;
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
}
