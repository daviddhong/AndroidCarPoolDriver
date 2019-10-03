package android.carpooldriver.AppFragment.ACarpoolRiderRequests.content;

import android.carpooldriver.AppFragment.DAcceptPendingRequests.content.AcceptedCarpoolRequestActivity;
import android.carpooldriver.R;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class IndividualRiderTicketActivity extends AppCompatActivity {

    private String receiverKeyID, senderUID, current_state, receiverUID, requestStatus;
    private RelativeLayout confirmButton, backButton;
    private DatabaseReference UserRef, DriverRequestingRiderRef, ConfirmedMatchRef, RiderTicketsRef, asd;
    private TextView confirm_carpool_button_word, riderPhoneNumber, riderTo, riderFrom, riderDate, riderTime, riderNumberOfSeats, riderPrice, riderName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_acarpoolriderrequest_content_layout_individual_rider_ticket);
        initializeFields();
        backButtonInit();
        RetrieveTicketStatusInformation();

        extractReceiverUID();
        fillTicketInformationFromDatabase();

        RiderTicketsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String receiveruID = dataSnapshot
                            .child(receiverKeyID).child("uid").getValue().toString();
                    receiverUID = receiveruID;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }

    private void backButtonInit() {
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void extractReceiverUID() {

        RiderTicketsRef.child(receiverKeyID).child("uid").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    receiverUID = dataSnapshot.getValue().toString();

                    // setting name in ticket
                    UserRef.child(receiverUID).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                final String ticketname = dataSnapshot.child("firstname").getValue().toString();
                                riderName.setText(ticketname);
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

    private void fillTicketInformationFromDatabase() {
        RiderTicketsRef.child(receiverKeyID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    final String ticketTo = dataSnapshot.child("To").getValue().toString();
                    final String ticketFrom = dataSnapshot.child("From").getValue().toString();
                    final String ticketDate = dataSnapshot.child("Date").getValue().toString();
                    final String ticketTime = dataSnapshot.child("Time").getValue().toString();
                    final String ticketPrice = dataSnapshot.child("Price").getValue().toString();
                    final String ticketNumberOfSeats = dataSnapshot.child("NumberOfSeats").getValue().toString();

                    riderTo.setText(ticketTo);
                    riderFrom.setText(ticketFrom);
                    riderDate.setText(ticketDate);
                    riderTime.setText(ticketTime);
                    riderPrice.setText(ticketPrice);
                    riderNumberOfSeats.setText(ticketNumberOfSeats);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    private void initializeFields() {
        // initialize fields
        confirm_carpool_button_word = findViewById(R.id.confirm_carpool_button_word);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        UserRef = FirebaseDatabase.getInstance().getReference().child("Users");
        DriverRequestingRiderRef = FirebaseDatabase.getInstance().getReference().child("DriverRequestingRider");
        ConfirmedMatchRef = FirebaseDatabase.getInstance().getReference().child("ConfirmedMatch");
        current_state = "new_dontknoweachother";
//        receiverUID = dataSnapshot.child("RiderTicekts").child(receiverKeyID).child("uid").getValue().toString();
        //should be the receiver's uid
        receiverKeyID = getIntent().getExtras().get("clicked_user_id").toString();
        senderUID = mAuth.getCurrentUser().getUid();
        RiderTicketsRef = FirebaseDatabase.getInstance().getReference().child("RiderTickets");
        backButton = (RelativeLayout) findViewById(R.id.backButtonOniTicket);

        confirmButton = (RelativeLayout) findViewById(R.id.confirm_carpool);
        riderFrom = findViewById(R.id.origin_data);
        riderTo = findViewById(R.id.destination_data);
        riderDate = findViewById(R.id.date_data);
        riderTime = findViewById(R.id.time_data);
        riderNumberOfSeats = findViewById(R.id.passenger_seat_data);
        riderPrice = findViewById(R.id.earnings_text_confirm);
        riderName = findViewById(R.id.profile_name);
        riderPhoneNumber = findViewById(R.id.phone_number_data);


    }

    //todo check the USERREF.recieverkeyID
    private void RetrieveTicketStatusInformation() {
        UserRef.child(receiverKeyID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ManageCarpoolRequest();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void ManageCarpoolRequest() {
        DriverRequestingRiderRef.child(senderUID)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild(receiverKeyID)) {

                            String requestStatus = dataSnapshot.child(receiverKeyID)
                                    .child("requeststatus").getValue().toString();

                            if (requestStatus.equals("sent")) {
                                current_state = "requestissent";
                                confirm_carpool_button_word.setText("Cancel Carpool Request");
                                confirmButton.setBackgroundColor(Color.parseColor("#FF0000"));
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });


        // double check for senderUID.equals(receiverKeyID) should be differnt!!
        if (!senderUID.equals(receiverUID)) {
            confirmButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    confirmButton.setEnabled(false);
                    if (current_state.equals("new_dontknoweachother")) {
                        SendRequestToPickUpRider();
                    }
                    if (current_state.equals("requestissent")) {
                        CancelCarpoolRequest();
                    }
                }
            });
        } else {
//            confirmButton.setVisibility(View.INVISIBLE);
            // todo shouldnt be called
            confirm_carpool_button_word.setText("My own request... cannot request!");
            confirmButton.setEnabled(false);
        }
    }

    private void CancelCarpoolRequest() {
        DriverRequestingRiderRef.child(senderUID).child(receiverKeyID)
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
                                                confirmButton.setEnabled(true);
                                                current_state = "new_dontknoweachother";
                                                confirm_carpool_button_word.setText("Request to Pickup Rider");
                                                confirmButton.setBackgroundColor(Color.parseColor("#2A2E43"));
                                                Toast.makeText(IndividualRiderTicketActivity.this, "Canceled request to driver", Toast.LENGTH_LONG).show();

                                            }
                                        }
                                    });
                        }
                    }
                });
    }

    private void SendRequestToPickUpRider() {
        DriverRequestingRiderRef.child(senderUID).child(receiverKeyID)
                .child("requeststatus").setValue("sent")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                            //putting the receivers uid
                            HashMap<String, Object> ticketuserMap = new HashMap<>();
                            ticketuserMap.put("receiverUID", receiverUID);
                            DriverRequestingRiderRef.child(senderUID)
                                    .child(receiverKeyID).updateChildren(ticketuserMap);

                            //recieved should be the UID
                            DriverRequestingRiderRef.child(receiverUID).child(receiverKeyID)
                                    .child("requeststatus").setValue("received")
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {

                                                //putting the sender uid
                                                HashMap<String, Object> ticketuserMap = new HashMap<>();
                                                ticketuserMap.put("senderUID", senderUID);
                                                DriverRequestingRiderRef.child(receiverUID)
                                                        .child(receiverKeyID).updateChildren(ticketuserMap);

                                                confirmButton.setEnabled(true);
                                                current_state = "requestissent";
                                                confirm_carpool_button_word.setText("Cancel Carpool Request");
                                                confirmButton.setBackgroundColor(Color.parseColor("#FF0000"));
                                                Toast.makeText(IndividualRiderTicketActivity.this, "Sent request to driver", Toast.LENGTH_LONG).show();


                                            }
                                        }
                                    });
                        }
                    }
                });
    }
}
