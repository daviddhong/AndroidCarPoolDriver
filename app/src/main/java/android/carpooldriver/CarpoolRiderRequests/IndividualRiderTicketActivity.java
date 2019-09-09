package android.carpooldriver.CarpoolRiderRequests;

import android.carpooldriver.R;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

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

public class IndividualRiderTicketActivity extends AppCompatActivity {

    private String receiverKeyID, senderUID, current_state, receiverUID, requestStatus;
    private TextView ticketID, confirm_carpool_button_word;
    private RelativeLayout confirmButton;
    private FirebaseAuth mAuth;
    private DatabaseReference UserRef, DriverRequestingRiderRef, ConfirmedMatchRef, reff, asd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_requested_ride_ticket_expand_entity_post);
        // initialize fields
        confirm_carpool_button_word = findViewById(R.id.request_cancel_button_word);
        confirmButton = findViewById(R.id.request_cancel_carpool_post);
        mAuth = FirebaseAuth.getInstance();
        UserRef = FirebaseDatabase.getInstance().getReference().child("Users");
        DriverRequestingRiderRef = FirebaseDatabase.getInstance().getReference().child("DriverRequestingRider");
        ConfirmedMatchRef = FirebaseDatabase.getInstance().getReference().child("ConfirmedMatch");
        current_state = "new_dontknoweachother";

//        receiverUID = dataSnapshot.child("RiderTicekts").child(receiverKeyID).child("uid").getValue().toString();
        //should be the receiver's uid
        receiverKeyID = getIntent().getExtras().get("clicked_user_id").toString();
        senderUID = mAuth.getCurrentUser().getUid();

//        reff = FirebaseDatabase.getInstance().getReference().child("RiderTicekts").child(receiverKeyID);

        reff = FirebaseDatabase.getInstance().getReference().child("RiderTickets");
//         asd  = reff.child("RiderTicekts").child(receiverKeyID).child("uid");
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String receiveruID = dataSnapshot
                        .child(receiverKeyID).child("uid").getValue().toString();
                receiverUID = receiveruID;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });


        RetrieveUserInformation();
    }

    private void RetrieveUserInformation() {
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
                            // might not need this one because different layout will be for received possible
//                            } else if (requestStatus.equals("received")) {
//                                current_state = "requestisreceived";
//                                confirm_carpool_button_word.setText("You have request! Confirm!");
//                                sendfriendrequest.setText("Accept Friend Request");

//                                DeclineFriendRequestButton.setVisibility(View.VISIBLE);
//                                DeclineFriendRequestButton.setEnabled(true);
//                                DeclineFriendRequestButton.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        CancelCarpoolRequest();
//                                    }
//                                });
//
//                            }
                        } else {
                            ConfirmedMatchRef.child(senderUID)
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.hasChild(receiverKeyID)) {
                                                current_state = "friendstatus";
                                                confirm_carpool_button_word.setText("Remove Friend from Contacts");
//                                                sendfriendrequest.setText("Remove Friend from Contacts");

                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {
                                        }
                                    });
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
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
                    if (current_state.equals("requestisreceived")) {
                        ConfirmCarpoolRequest();
                    }
                    if (current_state.equals("friendstatus")) {
                        RemoveSpecificContact();
                    }
                }
            });
        } else {
//            confirmButton.setVisibility(View.INVISIBLE);
            confirm_carpool_button_word.setText("My own request... cannot request!");
            confirmButton.setEnabled(false);
        }


    }


    private void RemoveSpecificContact() {
        ConfirmedMatchRef.child(senderUID).child(receiverKeyID)
                .removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            ConfirmedMatchRef.child(receiverKeyID).child(senderUID)
                                    .removeValue()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                confirmButton.setEnabled(true);
                                                current_state = "new_dontknoweachother";
                                                confirm_carpool_button_word.setText("Send a request");
//                                                sendfriendrequest.setText("Send a Friend Request");
//
//                                                DeclineFriendRequestButton.setVisibility(View.INVISIBLE);
//                                                DeclineFriendRequestButton.setEnabled(false);
                                            }
                                        }
                                    });
                        }
                    }
                });
    }

    private void ConfirmCarpoolRequest() {

        ConfirmedMatchRef.child(senderUID).child(receiverKeyID)
                .child("Friends").setValue("Saved")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            ConfirmedMatchRef.child(receiverKeyID).child(senderUID)
                                    .child("Friends").setValue("Saved")
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {

                                                // remove chat requests
                                                DriverRequestingRiderRef.child(senderUID).child(receiverKeyID)
                                                        .removeValue()
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    DriverRequestingRiderRef.child(receiverKeyID).child(senderUID)
                                                                            .removeValue()
                                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                @Override
                                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                                    if (task.isSuccessful()) {
                                                                                        confirmButton.setEnabled(true);
                                                                                        current_state = "friendstatus";
                                                                                        confirm_carpool_button_word.setText("Remove Friend from Contacts");
//                                                                                        sendfriendrequest.setText("Remove Friend from Contacts");
//                                                                                        DeclineFriendRequestButton.setVisibility(View.INVISIBLE);
//                                                                                        DeclineFriendRequestButton.setEnabled(false);
                                                                                    }
                                                                                }
                                                                            });
                                                                }
                                                            }
                                                        });
                                            }
                                        }
                                    });
                        }
                    }
                });
    }

    private void CancelCarpoolRequest() {
        DriverRequestingRiderRef.child(senderUID).child(receiverKeyID)
                .removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            DriverRequestingRiderRef.child(receiverKeyID).child(senderUID)
                                    .removeValue()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                confirmButton.setEnabled(true);
                                                current_state = "new_dontknoweachother";
                                                confirm_carpool_button_word.setText("Request to Pickup Rider");
                                                confirmButton.setBackgroundColor(Color.parseColor("#2A2E43"));

//                                                sendfriendrequest.setText("Send a Friend Request");
//                                                DeclineFriendRequestButton.setVisibility(View.INVISIBLE);
//                                                DeclineFriendRequestButton.setEnabled(false);
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
                            //recieved should be the UID
                            DriverRequestingRiderRef.child(receiverKeyID).child(senderUID)
                                    .child("requeststatus").setValue("received")
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                confirmButton.setEnabled(true);
                                                current_state = "requestissent";
                                                confirm_carpool_button_word.setText("Cancel Carpool Request");
                                                confirmButton.setBackgroundColor(Color.parseColor("#FF0000"));
//                                                sendfriendrequest.setText("Cancel Friend Request");

                                            }
                                        }
                                    });
                        }
                    }
                });
    }
}
