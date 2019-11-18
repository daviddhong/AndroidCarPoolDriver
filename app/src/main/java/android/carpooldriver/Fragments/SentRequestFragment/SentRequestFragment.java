package android.carpooldriver.Fragments.SentRequestFragment;

import android.carpooldriver.Fragments.AllAvailableCarpoolTicketsFragment.CarpoolRiderRequestsContent.IndividualRiderTicketActivity;
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


public class SentRequestFragment extends Fragment {
    private View mySentRequestView;
    private RecyclerView FriendRecyclerView;
    private String senderUIDme, clicked_user_uid;
    private DatabaseReference RiderTicketsRef, DriverRequestingRiderRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mySentRequestView = inflater.inflate(R.layout.fragment_accepted_carpool_requests, container, false);
        initiateFields();
        return mySentRequestView;
    }

    private void initiateFields() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        senderUIDme = mAuth.getCurrentUser().getUid();
        RiderTicketsRef = FirebaseDatabase.getInstance().getReference().child("RiderTickets");
        DriverRequestingRiderRef = FirebaseDatabase.getInstance().getReference().child("DriverRequestingRider");
        FriendRecyclerView = (RecyclerView) mySentRequestView.findViewById(R.id.rrides_requested_recycler_view);
        FriendRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

    }

    @Override
    public void onStart() {
        super.onStart();
        Query rreceiveriderQuery = FirebaseDatabase
                .getInstance()
                .getReference()
                .child("DriverRequestingRider")
                .child(senderUIDme)
                .orderByChild("requeststatus")
                .equalTo("sent");
        FirebaseRecyclerOptions options =
                new FirebaseRecyclerOptions.Builder<RiderRequestTicketClass>()
                        .setQuery(rreceiveriderQuery, RiderRequestTicketClass.class)
                        .build();
        final FirebaseRecyclerAdapter<RiderRequestTicketClass, riderTicketHolder> adapter
                = new FirebaseRecyclerAdapter<RiderRequestTicketClass, riderTicketHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull riderTicketHolder holder,
                                            int i, @NonNull RiderRequestTicketClass riderReqTickets) {
                //get all friend request list and then get their information from Users node
                final String receiverKeyID = getRef(i).getKey();
                DatabaseReference getTypeRef = getRef(i).child("requeststatus").getRef();
                getTypeRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            String type = dataSnapshot.getValue().toString();
                            if (type.equals("sent")) {
                                RiderTicketsRef.child(receiverKeyID).addValueEventListener(new ValueEventListener() {
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
                                            holder.moreTicketInfo.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    clicked_user_uid = getRef(i).getKey();
                                                    Intent intent = new Intent(getActivity(), IndividualRiderTicketActivity.class);
                                                    intent.putExtra("clicked_user_id", clicked_user_uid);
                                                    startActivity(intent);
                                                }
                                            });
                                            holder.cancelButtonForRiderRequest.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    //todo delete firebase
                                                    final String receiverUID = dataSnapshot.child("uid").getValue().toString();
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
                            if (type.equals("received")) {
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
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_pending_request_ticket_entity, parent, false);
                riderTicketHolder viewHolder = new riderTicketHolder(view);
                return viewHolder;
            }
        };
        FriendRecyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    public static class riderTicketHolder extends RecyclerView.ViewHolder {
        TextView riderTo, riderFrom, riderDate, riderTime, riderNumberOfSeats, riderPrice;
        RelativeLayout cancelButtonForRiderRequest, moreTicketInfo;

        public riderTicketHolder(@NonNull View itemView) {
            super(itemView);
            riderFrom = itemView.findViewById(R.id.drivertext_origin_post_accepted_rider);
            riderTo = itemView.findViewById(R.id.drivertext_destination_post_accepted_rider);
            riderDate = itemView.findViewById(R.id.drivertext_date_post_accepted_rider);
            riderTime = itemView.findViewById(R.id.drivertext_time_post_accepted_rider);
            riderNumberOfSeats = itemView.findViewById(R.id.drivertext_passenger_number_post_accepted_rider);
            riderPrice = itemView.findViewById(R.id.drivertext_earnings_entity_post_accepted_rider);
            cancelButtonForRiderRequest = itemView.findViewById(R.id.cancel_carpool_accept_rider);
            moreTicketInfo = itemView.findViewById(R.id.more_information_entity_request2);
        }
    }

    private void CancelCarpoolRequest(String receiverUID, String receiverKeyID) {
        DriverRequestingRiderRef.child(senderUIDme).child(receiverKeyID)
                .removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            DriverRequestingRiderRef.child(receiverUID).child(receiverKeyID)
                                    .removeValue()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Map<String, Object> profileMap = new HashMap<>();
                                                String status = "0";
                                                profileMap.put("status", status);
                                                profileMap.put("status_uid", status + receiverUID);
                                                RiderTicketsRef.child(receiverKeyID).updateChildren(profileMap);
                                                Toast.makeText(getContext(), "Canceled ticket request", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                        }
                    }
                });
    }
}
