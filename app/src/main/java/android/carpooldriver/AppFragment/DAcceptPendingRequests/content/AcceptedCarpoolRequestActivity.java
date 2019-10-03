package android.carpooldriver.AppFragment.DAcceptPendingRequests.content;

import android.carpooldriver.AppFragment.ACarpoolRiderRequests.content.RiderRequestTicketClass;
import android.carpooldriver.R;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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

public class AcceptedCarpoolRequestActivity extends AppCompatActivity {

    private DatabaseReference RiderTicketsRef, DriverRequestingRiderRef,UserRef,DriverTicketsRef;
    private String senderUIDme;
    private RecyclerView FriendRecyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_dacceptpendingrequests_content_activity_accepted_carpool_requests);
        initBack();
        initiateFields();
    }

    private void initiateFields() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        senderUIDme = mAuth.getCurrentUser().getUid();
        RiderTicketsRef = FirebaseDatabase.getInstance().getReference().child("RiderTickets");
        UserRef = FirebaseDatabase.getInstance().getReference().child("Users");

        DriverTicketsRef = FirebaseDatabase.getInstance().getReference().child("DriverTickets");

        FriendRecyclerView = (RecyclerView) findViewById(R.id.rrides_requested_recycler_view);
        FriendRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        DriverRequestingRiderRef = FirebaseDatabase.getInstance().getReference().child("DriverRequestingRider");

    }

    private void initBack() {
        RelativeLayout back = (RelativeLayout) findViewById(R.id.rl_back_accepted);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.app_dacceptpendingrequests_content_layout_pending_request_ticket_entity, parent, false);
                riderTicketHolder viewHolder = new riderTicketHolder(view);
                return viewHolder;
            }
        };
        FriendRecyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    public static class riderTicketHolder extends RecyclerView.ViewHolder {
        TextView riderTo, riderFrom, riderDate, riderTime, riderNumberOfSeats, riderPrice;
        RelativeLayout cancelButtonForRiderRequest;

        public riderTicketHolder(@NonNull View itemView) {
            super(itemView);
            riderFrom = itemView.findViewById(R.id.drivertext_origin_post_accepted_rider);
            riderTo = itemView.findViewById(R.id.drivertext_destination_post_accepted_rider);
            riderDate = itemView.findViewById(R.id.drivertext_date_post_accepted_rider);
            riderTime = itemView.findViewById(R.id.drivertext_time_post_accepted_rider);
            riderNumberOfSeats = itemView.findViewById(R.id.drivertext_passenger_number_post_accepted_rider);
            riderPrice = itemView.findViewById(R.id.drivertext_earnings_entity_post_accepted_rider);
            cancelButtonForRiderRequest = itemView.findViewById(R.id.confirm_carpool_accept_rider);
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
                                                Toast.makeText(AcceptedCarpoolRequestActivity.this, "Canceled ticket request", Toast.LENGTH_LONG).show();

                                            }
                                        }
                                    });
                        }
                    }
                });
    }


}
