package android.carpooldriver.Fragments.ConfirmedCarpoolFragment;

import android.carpooldriver.Fragments.CarpoolRiderRequestsFragment.Content.DriverRequestTicketClass;
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
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class ConfirmedCarpoolFragment extends Fragment {

    private String senderUIDme, receiverUID;
    private View mYourCarpoolView;
    private RecyclerView DriverRecyclerView;
    private DatabaseReference ConfirmedTicketsRef, RiderTicketsRef, DriverTicketsRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mYourCarpoolView = inflater.inflate(R.layout.fragment_confirmed_carpool, container, false);

        initializeFields();

        return mYourCarpoolView;
    }


    private void initializeFields() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        senderUIDme = mAuth.getCurrentUser().getUid();
        RiderTicketsRef = FirebaseDatabase.getInstance().getReference().child("RiderTickets");
        DriverTicketsRef = FirebaseDatabase.getInstance().getReference().child("DriverTickets");
        ConfirmedTicketsRef = FirebaseDatabase.getInstance().getReference().child("ConfirmedMatch");
        DriverRecyclerView = (RecyclerView) mYourCarpoolView.findViewById(R.id.confirmed_carpool_fragment_recycler_view);
        DriverRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }


    // Display the list of confirmed/matched rides with FireBase recycler
    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<DriverRequestTicketClass> options
                = new FirebaseRecyclerOptions.Builder<DriverRequestTicketClass>()
                .setQuery(ConfirmedTicketsRef.child(senderUIDme), DriverRequestTicketClass.class)
                .build();
        FirebaseRecyclerAdapter<DriverRequestTicketClass, driverTicketHolder> adapter
                = new FirebaseRecyclerAdapter<DriverRequestTicketClass, driverTicketHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull driverTicketHolder driverticketholder,
                                            int i, @NonNull DriverRequestTicketClass driverReqTickets) {

                String riderKeyID = getRef(i).getKey();

                RiderTicketsRef.child(riderKeyID).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            final String ticketTo = dataSnapshot.child("To").getValue().toString();
                            final String ticketFrom = dataSnapshot.child("From").getValue().toString();
                            final String ticketDate = dataSnapshot.child("Date").getValue().toString();
                            final String ticketTime = dataSnapshot.child("Time").getValue().toString();
                            final String ticketPrice = dataSnapshot.child("Price").getValue().toString();
                            final String ticketNumberOfSeats = dataSnapshot.child("NumberOfSeats").getValue().toString();
                            driverticketholder.riderTo.setText(ticketTo);
                            driverticketholder.riderFrom.setText(ticketFrom);
                            driverticketholder.riderDate.setText(ticketDate);
                            driverticketholder.riderTime.setText(ticketTime);
                            driverticketholder.riderPrice.setText(ticketPrice);
                            driverticketholder.riderNumberOfSeats.setText(ticketNumberOfSeats);

                            driverticketholder.cancelCarPoolButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    //todo cancel when button pressed
//                                    CarpoolRiderRequestsFragment.riderTicketHolder.FILTER = 2;
                                    RemoveSpecificContact(riderKeyID);
                                }
                            });
                        } else {
                            DriverTicketsRef.child(riderKeyID).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        final String ticketTo = dataSnapshot.child("To").getValue().toString();
                                        final String ticketFrom = dataSnapshot.child("From").getValue().toString();
                                        final String ticketDate = dataSnapshot.child("Date").getValue().toString();
                                        final String ticketTime = dataSnapshot.child("Time").getValue().toString();
                                        final String ticketPrice = dataSnapshot.child("Price").getValue().toString();
                                        final String ticketNumberOfSeats = dataSnapshot.child("NumberOfSeats").getValue().toString();
                                        driverticketholder.riderTo.setText(ticketTo);
                                        driverticketholder.riderFrom.setText(ticketFrom);
                                        driverticketholder.riderDate.setText(ticketDate);
                                        driverticketholder.riderTime.setText(ticketTime);
                                        driverticketholder.riderPrice.setText(ticketPrice);
                                        driverticketholder.riderNumberOfSeats.setText(ticketNumberOfSeats);

                                        driverticketholder.cancelCarPoolButton.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {


                                                ConfirmedTicketsRef.child(senderUIDme).child(riderKeyID).child("with").addValueEventListener(new ValueEventListener() {
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


                                                //todo cancel when button pressed
                                                RemoveSpecificContact(riderKeyID);
                                            }
                                        });
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

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
            public driverTicketHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_confirmed_ticket_entity, parent, false);
                return new driverTicketHolder(view);
            }
        };
        DriverRecyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    public static class driverTicketHolder extends RecyclerView.ViewHolder {
        TextView riderTo, riderFrom, riderDate, riderTime, riderNumberOfSeats, riderPrice;
        RelativeLayout cancelCarPoolButton;

        private driverTicketHolder(@NonNull View itemView) {
            super(itemView);
            riderFrom = itemView.findViewById(R.id.drivertext_origin_post_accepted_confirm);
            riderTo = itemView.findViewById(R.id.drivertext_destination_post_accepted_confirm);
            riderDate = itemView.findViewById(R.id.drivertext_date_post_accepted_confirm);
            riderTime = itemView.findViewById(R.id.drivertext_time_post_accepted_confirm);
            riderNumberOfSeats = itemView.findViewById(R.id.drivertext_passenger_number_post_accepted_confirm);
            riderPrice = itemView.findViewById(R.id.driver_text_earnings_entity_post_accepted_confirm);
            cancelCarPoolButton = itemView.findViewById(R.id.confirm_carpool_accept_confirm);
        }
    }

    // todo fix this so database removes the matchedRides realtime.
    private void RemoveSpecificContact(String receiverKeyID) {



        ConfirmedTicketsRef.child(senderUIDme).child(receiverKeyID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    receiverUID = dataSnapshot.child("with").getValue().toString();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });


        ConfirmedTicketsRef.child(senderUIDme).child(receiverKeyID)
                .removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                            ConfirmedTicketsRef.child(receiverUID).child(receiverKeyID)
                                    .removeValue()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                // toast deleted
                                                Toast.makeText(getContext(), "Canceled confirmed ticket", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                        }
                    }
                });

        RiderTicketsRef.child(receiverKeyID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Map<String, Object> profileMap = new HashMap<>();
                    String status = "0";
                    profileMap.put("status", status);
                    profileMap.put("status_uid", status + receiverUID);
                    RiderTicketsRef.child(receiverKeyID).updateChildren(profileMap);
                } else {
                    Map<String, Object> profileMap = new HashMap<>();
                    String status = "0";
                    profileMap.put("status", status);
                    profileMap.put("status_uid", status + senderUIDme);
                    DriverTicketsRef.child(receiverKeyID).updateChildren(profileMap);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }


}
