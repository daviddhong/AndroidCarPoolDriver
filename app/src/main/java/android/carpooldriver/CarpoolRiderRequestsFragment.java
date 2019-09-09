package android.carpooldriver;

import android.carpooldriver.CarpoolRiderRequests.RiderRequestTicketClass;
import android.carpooldriver.CarpoolRiderRequests.IndividualRiderTicketActivity;
import android.carpooldriver.Settings.Profile.ProfileActivity;
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

import java.util.ArrayList;
import java.util.List;

public class CarpoolRiderRequestsFragment extends Fragment {

    private View mCarpoolRequestsView;
    private RecyclerView FriendRecyclerView;
    private DatabaseReference RiderTicketsRef, UsersRef, DriverTicketsRef, reff;
    private FirebaseAuth mAuth;
    private String currentUserID, clicked_user_id, clicked_user_uid, usersIDS, uniquekey, THEUID;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mCarpoolRequestsView = inflater.inflate(R.layout.fragment_carpool_requests, container, false);
        displaysFriendsListbyRecyclerView();
        initProfile();
        return mCarpoolRequestsView;
    }

    //use recycler view and friend list adapter to display list of friends
    private void displaysFriendsListbyRecyclerView() {

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();

        // todo check to get all rider tickets
        RiderTicketsRef = FirebaseDatabase.getInstance().getReference().child("RiderTickets");
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");
//        DriverTicketsRef = FirebaseDatabase.getInstance().getReference().child("DriverTickets");

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
//              String usersIDS = getRef(i).getKey();
                RiderTicketsRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
//                            if (!(clicked_user_id == null)){
//                                THEUID = dataSnapshot.child(clicked_user_id).child("uid").getValue().toString();
//                            } else {
//                            List<String> keys = new ArrayList<>();
//                            for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
//                                keys.add(childSnapshot.getKey());
//                            }
//                            final String clicked_user_uid = getRef(i).getKey();
//                                uniquekey = childSnapshot.getKey();
//                                uniquekey = childSnapshot.child(clicked_user_uid).child("uid").getValue().toString();
//                                String type = dataSnapshot.child(uniquekey).child("uid").getValue().toString();
//                                final String riderTo = dataSnapshot.child("uid").getValue().toString();
//                            final String riderFrom = dataSnapshot.child("From").getValue().toString();
                            riderticketholder.riderTo.setText(riderReqTickets.getticketto());
                            riderticketholder.riderFrom.setText(riderReqTickets.getticketfrom());
                            riderticketholder.riderDate.setText(riderReqTickets.getticketdate());
                            riderticketholder.riderTime.setText(riderReqTickets.gettickettime());
                            riderticketholder.riderPrice.setText(riderReqTickets.getticketprice());
                            riderticketholder.riderNumberOfSeats.setText(riderReqTickets.getticketnumberofseats());
                            riderticketholder.itemView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String usersIDS = getRef(i).getKey();
                                    // todo get the UID
                                    clicked_user_uid = getRef(i).getKey();
//                                    for (String key: keys) {
//                                        if (clicked_user_id.equals(key)){
//                                            THEUID = dataSnapshot.child(clicked_user_id).child("uid").getValue().toString();
//                                        }
//                                    }
//                                        clicked_user_id = getRef(i).getParent().getKey();
//                                         THEUID = RiderTicketsRef.child(clicked_user_id).child("uid").getValue().toString();
//                                        clicked_user_uid = ;
//                                        RiderTicketsRef.addValueEventListener(new ValueEventListener() {
//                                            @Override
//                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                                uniquekey = getRef(i).child("uid").getValue().toString();
//                                            }
//                                            @Override
//                                            public void onCancelled(@NonNull DatabaseError databaseError) {
//                                            }
//                                        });
                                    Intent intent = new Intent(getActivity(), IndividualRiderTicketActivity.class);
                                    //send currentUserID or usersIDS
                                    intent.putExtra("clicked_user_id", clicked_user_uid);
                                    startActivity(intent);
                                }
                            });
                        }
                        // remove here for getchildren()
//                        }
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
    private void initProfile() {
        ImageView profileImageView = (ImageView) mCarpoolRequestsView.findViewById(R.id.profile_carpool_requests);
        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ProfileActivity.class);
                startActivity(intent);
            }
        });
    }
}
