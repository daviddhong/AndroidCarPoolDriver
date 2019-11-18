package android.carpooldriver.Fragments.AcceptPendingRequestsFragment;

import android.carpooldriver.Fragments.AllAvailableCarpoolTicketsFragment.CarpoolRiderRequestsContent.RiderRequestTicketClass;
import android.carpooldriver.R;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.util.HashMap;
import java.util.Map;

public class AcceptPendingRequestsFragment extends Fragment {

    private View mRequestView;
    private RecyclerView receivedFriendRequest;
    private String CurrentUserUID, senderUID, clicked_user_uid;
    private DatabaseReference DriverTicketsRef, ConfirmedMatchRef, RiderRequestingDriverRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRequestView = inflater.inflate(R.layout.fragment_accept_pending_request, container, false);
        initializeFields();
        return mRequestView;
    }

    private void initializeFields() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        CurrentUserUID = mAuth.getCurrentUser().getUid();
        DriverTicketsRef = FirebaseDatabase.getInstance().getReference().child("DriverTickets");
        ConfirmedMatchRef = FirebaseDatabase.getInstance().getReference().child("ConfirmedMatch");
        RiderRequestingDriverRef = FirebaseDatabase.getInstance().getReference().child("RiderRequestingDriver");
        receivedFriendRequest = (RecyclerView) mRequestView.findViewById(R.id.arides_requested_recycler_view);
        receivedFriendRequest.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    // Display the list of all my sent carpool requests with FireBase recycler
    @Override
    public void onStart() {
        super.onStart();
        Query mySentRequestQuery = FirebaseDatabase
                .getInstance()
                .getReference()
                .child("RiderRequestingDriver")
                .child(CurrentUserUID)
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
                final String ReceiverKeyIDme = getRef(i).getKey();
                DatabaseReference getTypeRef = getRef(i).child("requeststatus").getRef();
                getTypeRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            String type = dataSnapshot.getValue().toString();
                            if (type.equals("received")) {
                                DriverTicketsRef.child(ReceiverKeyIDme).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()) {
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
                                            RiderRequestingDriverRef.child(CurrentUserUID).child(ReceiverKeyIDme).child("senderUID").addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    if (dataSnapshot.exists()) {
                                                        senderUID = dataSnapshot.getValue().toString();
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                                }
                                            });
                                            holder.moreInformationTicket.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    // todo
                                                    clicked_user_uid = getRef(i).getKey();
                                                    Intent intent = new Intent(getActivity(), IndividualAcceptedDriverTicketActivity.class);
                                                    intent.putExtra("clicked_user_id", clicked_user_uid);
                                                    startActivity(intent);
                                                }
                                            });
                                            holder.confirmCarPoolButton.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    //todo confirm the carpool
                                                    createCarpoolConfirmMatchNodeInFireBase(senderUID, ReceiverKeyIDme);
                                                    //todo ask are you sure before finalizing finish
                                                    DeleteSentRequestDatabase(senderUID, ReceiverKeyIDme);
                                                    Toast.makeText(getContext(), "Accepted ticket offer", Toast.LENGTH_LONG).show();
                                                }
                                            });
                                            holder.declineCarPoolButton.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    Map<String, Object> profileMap = new HashMap<>();
                                                    String status = "0";
                                                    profileMap.put("status", status);
                                                    profileMap.put("status_uid", status + CurrentUserUID);
                                                    DriverTicketsRef.child(ReceiverKeyIDme).updateChildren(profileMap);
                                                    // todo delete the carpool
                                                    DeleteSentRequestDatabase(senderUID, ReceiverKeyIDme);
                                                    Toast.makeText(getContext(), "Declined ticket offer", Toast.LENGTH_LONG).show();
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
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_accepted_request_carpool_ticket_entity, parent, false);
                riderTicketHolder viewHolder = new riderTicketHolder(view);
                return viewHolder;
            }
        };
        receivedFriendRequest.setAdapter(adapter);
        adapter.startListening();
    }

    public static class riderTicketHolder extends RecyclerView.ViewHolder {
        TextView riderTo, riderFrom, riderDate, riderTime, riderNumberOfSeats, riderPrice;
        RelativeLayout confirmCarPoolButton, declineCarPoolButton, moreInformationTicket;

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
            moreInformationTicket = itemView.findViewById(R.id.more_information_entity_request4);


        }
    }

    private void createCarpoolConfirmMatchNodeInFireBase(String receiverUID, String receiverKeyID) {
        ConfirmedMatchRef.child(CurrentUserUID).child(receiverKeyID)
                .child("with").setValue(receiverUID)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            ConfirmedMatchRef.child(receiverUID).child(receiverKeyID)
                                    .child("with").setValue(CurrentUserUID);
                        }
                    }
                });
    }

    private void DeleteSentRequestDatabase(String receiverUID, String receiverKeyID) {
        RiderRequestingDriverRef.child(CurrentUserUID).child(receiverKeyID)
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
                                            }
                                        }
                                    });
                        }
                    }
                });
    }

}
