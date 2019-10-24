package android.carpooldriver.AppFragment.DAcceptPendingRequests;

import android.carpooldriver.AppFragment.DAcceptPendingRequests.content.AcceptedCarpoolRequestActivity;
import android.carpooldriver.AppFragment.ACarpoolRiderRequests.content.RiderRequestTicketClass;
import android.carpooldriver.R;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class DAcceptPendingRequestsFragment extends Fragment {
    private RecyclerView receivedFriendRequest;
    private DatabaseReference DriverTicketsRef, DriverRequestingRiderRef, ConfirmedMatchRef, RiderRequestingDriverRef;
    private String senderUIDme, receiverUID;
    private View mRequestView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRequestView = inflater.inflate(R.layout.app_dacceptpendingrequests_fragment_accept_pending_request, container, false);
        initializeFields();
        initAcceptedCarpoolRequestActivity();
        goToMyProfileByProfileImageView();
        return mRequestView;
    }

    private void initializeFields() {
        //initialize fields
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        senderUIDme = mAuth.getCurrentUser().getUid();
        DriverTicketsRef = FirebaseDatabase.getInstance().getReference().child("DriverTickets");
        DriverRequestingRiderRef = FirebaseDatabase.getInstance().getReference().child("DriverRequestingRider");
        RiderRequestingDriverRef = FirebaseDatabase.getInstance().getReference().child("RiderRequestingDriver");
        ConfirmedMatchRef = FirebaseDatabase.getInstance().getReference().child("ConfirmedMatch");
        //initialize recycler view for received
        receivedFriendRequest = (RecyclerView) mRequestView.findViewById(R.id.arides_requested_recycler_view);
        receivedFriendRequest.setLayoutManager(new LinearLayoutManager(getContext()));
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

    // EFFECTS: Set OnClickActivity for ProfileActivity.
    private void goToMyProfileByProfileImageView() {
        ImageView profileImageView = (ImageView) mRequestView.findViewById(R.id.profile_rate_riders);
        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // todo name of profile activity
//                Intent intent = new Intent(getActivity(), ProfileActivity.class);
//                startActivity(intent);
                // EFFECTS: Animation to Profile Activity
//                getActivity().overridePendingTransition(R.anim.slide_up, R.anim.slide_vertical_null);
            }
        });
    }

    // Display the list of all my sent carpool requests with FireBase recycler
    @Override
    public void onStart() {
        super.onStart();
        Query mySentRequestQuery = FirebaseDatabase
                .getInstance()
                .getReference()
                .child("RiderRequestingDriver")
                .child(senderUIDme)
                .orderByChild("requeststatus")
                .equalTo("received");



        FirebaseRecyclerOptions options =
                new FirebaseRecyclerOptions.Builder<RiderRequestTicketClass>()
                        .setQuery(mySentRequestQuery, RiderRequestTicketClass.class)
                        .build();
        final FirebaseRecyclerAdapter<RiderRequestTicketClass, riderTicketHolder> adapter
                = new FirebaseRecyclerAdapter<RiderRequestTicketClass, riderTicketHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull riderTicketHolder holder,
                                            int i, @NonNull RiderRequestTicketClass riderReqTickets) {
                //get all friend request list and then get their information from FireBase Users node to tickets
                final String receiverKeyID = getRef(i).getKey();
                DatabaseReference getTypeRef = getRef(i).child("requeststatus").getRef();
                getTypeRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            String type = dataSnapshot.getValue().toString();
                            if (type.equals("received")) {
                                DriverTicketsRef.child(receiverKeyID).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()){

                                        final String ticketTo = dataSnapshot.child("To").getValue().toString();
                                        final String ticketFrom = dataSnapshot.child("From").getValue().toString();
                                        final String ticketDate = dataSnapshot.child("Date").getValue().toString();
                                        final String ticketTime = dataSnapshot.child("Time").getValue().toString();
                                        final String ticketPrice = dataSnapshot.child("Price").getValue().toString();
                                        final String ticketNumberOfSeats = dataSnapshot.child("NumberOfSeats").getValue().toString();
                                        holder.riderTo.setText(ticketTo);
                                        holder.riderFrom.setText(ticketFrom);
                                        holder.riderDate.setText(ticketDate);
                                        holder.riderTime.setText(ticketTime);
                                        holder.riderPrice.setText(ticketPrice);
                                        holder.riderNumberOfSeats.setText(ticketNumberOfSeats);


                                        RiderRequestingDriverRef.child(senderUIDme).child(receiverKeyID).child("senderUID").addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                if (dataSnapshot.exists()) {
                                                    receiverUID = dataSnapshot.getValue().toString();
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });


                                        holder.confirmCarPoolButton.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                //todo confirm the carpool
                                                createCarpoolConfirmMatchNodeInFireBase(receiverUID, receiverKeyID);
                                                //todo ask are you sure before finalizing finish
//                                                deletingDatabase(receiverKeyID);
                                                CancelCarpoolRequest(receiverUID, receiverKeyID);

                                            }
                                        });

                                        holder.declineCarPoolButton.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {

                                                // todo delete the carpool
                                                CancelCarpoolRequest(receiverUID, receiverKeyID);

                                            }
                                        });

                                    }
                                }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                    }
                                });
                            }
                            if (type.equals("sent")) {
                                holder.itemView.setVisibility(View.GONE);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
            }

            @NonNull
            @Override
            public riderTicketHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.app_dacceptpendingrequests_layout_accepted_request_carpool_ticket_entity, parent, false);
                riderTicketHolder viewHolder = new riderTicketHolder(view);
                return viewHolder;
            }
        };
        receivedFriendRequest.setAdapter(adapter);
        adapter.startListening();
    }

    public static class riderTicketHolder extends RecyclerView.ViewHolder {
        TextView riderTo, riderFrom, riderDate, riderTime, riderNumberOfSeats, riderPrice;
        RelativeLayout confirmCarPoolButton, declineCarPoolButton;

        public riderTicketHolder(@NonNull View itemView) {
            super(itemView);
            riderFrom = itemView.findViewById(R.id.drivertext_origin_post_accepted);
            riderTo = itemView.findViewById(R.id.drivertext_destination_post_accepted);
            riderDate = itemView.findViewById(R.id.drivertext_date_post_accepted);
            riderTime = itemView.findViewById(R.id.drivertext_time_post_accepted);
            riderNumberOfSeats = itemView.findViewById(R.id.drivertext_passenger_number_post_accepted);
            riderPrice = itemView.findViewById(R.id.drivertext_earnings_entity_post_accepted);
            confirmCarPoolButton = itemView.findViewById(R.id.confirm_carpool_accept);
            declineCarPoolButton = itemView.findViewById(R.id.confirm_carpool_decline);

        }
    }

    private void createCarpoolConfirmMatchNodeInFireBase(String receiverUID, String receiverKeyID) {
        ConfirmedMatchRef.child(senderUIDme).child(receiverKeyID)
                .child("with").setValue(receiverUID)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            ConfirmedMatchRef.child(receiverUID).child(receiverKeyID)
                                    .child("with").setValue(senderUIDme);

                            Toast.makeText(getContext(), "Accepted ticket offer", Toast.LENGTH_LONG).show();

                        }
                    }
                });
    }


    private void CancelCarpoolRequest(String receiverUID, String receiverKeyID) {
        RiderRequestingDriverRef.child(senderUIDme).child(receiverKeyID)
                .removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                            RiderRequestingDriverRef.child(receiverUID).child(receiverKeyID)
                                    .removeValue()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(getContext(), "Declined ticket offer", Toast.LENGTH_LONG).show();

                                            }
                                        }
                                    });
                        }
                    }
                });
    }

}
