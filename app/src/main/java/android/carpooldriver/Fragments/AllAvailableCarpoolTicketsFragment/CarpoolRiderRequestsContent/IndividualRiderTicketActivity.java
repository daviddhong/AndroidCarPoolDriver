package android.carpooldriver.Fragments.AllAvailableCarpoolTicketsFragment.CarpoolRiderRequestsContent;

import android.Manifest;
import android.carpooldriver.R;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

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

public class IndividualRiderTicketActivity extends AppCompatActivity {

    public static final String TAG = IndividualRiderTicketActivity.class.getSimpleName();
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;
    private TelephonyManager mTelephonyManager;
    private MyPhoneCallListener mListener;

    private String receiverKeyID, uuid, senderUID, current_state, receiverUID, currentUID, confirmed;
    private RelativeLayout confirmButton, backButton;
    private DatabaseReference UserRef, DriverRequestingRiderRef, ConfirmedMatchRef, RiderTicketsRef, RootRef;
    private TextView confirm_carpool_button_word, RiderPhoneNumber, riderTo, riderFrom, riderDate, riderTime, riderNumberOfSeats, riderPrice, riderName;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_individual_rider_ticket);
        initializeFields();
        backButtonInit();
        RetrieveTicketStatusInformation();
        extractReceiverUID();
        fillTicketInformationFromDatabase();
        gettingPhoneNumberFromFirebase();

        createATelephonyManager();
    }

    private void createATelephonyManager() {
        mTelephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        if (isTelephonyEnabled()) {
            Log.d(TAG, "Telephony is enabled");
            // Check for phone permission.
            checkForPhonePermission();
            // Register the PhoneStateListener to monitor phone activity.
            mListener = new MyPhoneCallListener();
            mTelephonyManager.listen(mListener, PhoneStateListener.LISTEN_CALL_STATE);
        } else {
            Toast.makeText(this, "TELEPHONY NOT ENABLED!", Toast.LENGTH_LONG).show();
            Log.d(TAG, "TELEPHONY NOT ENABLED!");
            // Disable the call button.
            disableCallButton();
        }
    }

    public void callNumber(View view) {
        // Use format with "tel:" and phone number to create phoneNumber.
        String phoneNumber = String.format("tel: %s", RiderPhoneNumber.getText().toString());
        // Log the concatenated phone number for dialing.
        Log.d(TAG, "Phone Status: DIALING: " + phoneNumber);
        Toast.makeText(this, "Phone Status: DIALING: " + phoneNumber, Toast.LENGTH_LONG).show();
        // Create the intent.
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        // Set the data for the intent as the phone number.
        callIntent.setData(Uri.parse(phoneNumber));
        // If package resolves to an app, send intent.
        if (callIntent.resolveActivity(getPackageManager()) != null) {
            checkForPhonePermission();
            startActivity(callIntent);
        } else {
            Log.e(TAG, "Can't resolve app for ACTION_CALL Intent.");
        }
    }

    private boolean isTelephonyEnabled() {
        if (mTelephonyManager != null) {
            if (mTelephonyManager.getSimState() == TelephonyManager.SIM_STATE_READY) {
                return true;
            }
        }
        return false;
    }

    private void disableCallButton() {
        Toast.makeText(this, "Phone calling disabled", Toast.LENGTH_LONG).show();
    }

    private void checkForPhonePermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "PERMISSION NOT GRANTED!");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL_PHONE);
        } else {
            // Permission already granted. Enable the call button.
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CALL_PHONE: {
                if (permissions[0].equalsIgnoreCase(Manifest.permission.CALL_PHONE)
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission was granted
                } else {
                    // Permission denied.
                    Log.d(TAG, "Failure to obtain permission!");
                    Toast.makeText(this, "Failure to obtain permission!",
                            Toast.LENGTH_LONG).show();
                    // Disable the call button.
                    disableCallButton();
                }
            }
        }
    }

    // Inner class
    private class MyPhoneCallListener extends PhoneStateListener {
        private boolean returningFromOffHook = false;

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            // Define a string for the message to use in a toast.
            String message = "Phone Status: ";
            switch (state) {
                case TelephonyManager.CALL_STATE_RINGING:
                    // Incoming call is ringing (not used for outgoing call).
                    message = message + "RINGING, number: " + incomingNumber;
                    Toast.makeText(IndividualRiderTicketActivity.this, message, Toast.LENGTH_SHORT).show();
                    Log.i(TAG, message);
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    // Phone call is active -- off the hook.
                    message = message + "OFFHOOK";
                    Toast.makeText(IndividualRiderTicketActivity.this, message, Toast.LENGTH_SHORT).show();
                    Log.i(TAG, message);
                    returningFromOffHook = true;
                    break;
                case TelephonyManager.CALL_STATE_IDLE:
                    // Phone is idle before and after phone call.
                    // If running on version older than 19 (KitKat), restart activity when phone
                    // call ends.
                    message = message + "IDLE";
                    Toast.makeText(IndividualRiderTicketActivity.this, message, Toast.LENGTH_SHORT).show();
                    Log.i(TAG, message);
                    if (returningFromOffHook) {
                        // No need to do anything if >= version KitKat.
                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                            Log.i(TAG, "Restarting app");
                            // Restart the app.
                            Intent intent = getPackageManager()
                                    .getLaunchIntentForPackage(getApplicationContext().getPackageName());
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                    }
                    break;
                default:
                    // Must be an error. Raise an exception or just log it.
                    message = message + "Phone off";
                    Toast.makeText(IndividualRiderTicketActivity.this, message,
                            Toast.LENGTH_SHORT).show();
                    Log.i(TAG, message);
                    break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isTelephonyEnabled()) {
            mTelephonyManager.listen(mListener,
                    PhoneStateListener.LISTEN_NONE);
        }
    }

    public void smsSendMessage(View view) {
//        TextView RiderPhoneNumber = (TextView) findViewById(R.id.phone_number_data);

        // Use format with "smsto:" and phone number to create smsNumber.
        String smsNumber = String.format("smsto: %s", RiderPhoneNumber.getText().toString());

        // Find the sms_message view.
//        EditText smsEditText = (EditText) findViewById(R.id.text_message);
        // Get the text of the SMS message.
//        String sms = smsEditText.getText().toString();

        // Create the intent.
        Intent smsIntent = new Intent(Intent.ACTION_SENDTO);
        // Set the data for the intent as the phone number.
        smsIntent.setData(Uri.parse(smsNumber));
        // Add the message (sms) with the key ("sms_body")
//        smsIntent.putExtra("sms_body", sms);

        // If package resolves (target app installed), send intent.
        if (smsIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(smsIntent);
        } else {
            Log.e(TAG, "Can't resolve app for ACTION_SENDTO Intent");
        }
    }


    private void initializeFields() {
        // initialize fields
        confirm_carpool_button_word = findViewById(R.id.confirm_carpool_button_word);
        mAuth = FirebaseAuth.getInstance();
        UserRef = FirebaseDatabase.getInstance().getReference().child("Users");
        DriverRequestingRiderRef = FirebaseDatabase.getInstance().getReference().child("DriverRequestingRider");
        ConfirmedMatchRef = FirebaseDatabase.getInstance().getReference().child("ConfirmedMatch");
        Bundle gotname = getIntent().getExtras();
        confirmed = gotname.getString("CONFIRMED");
        if (!(confirmed == null)) {
            current_state = confirmed;
        } else {
            current_state = "new_dontknoweachother";
        }
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
        currentUID = mAuth.getCurrentUser().getUid();
        RiderPhoneNumber = (TextView) findViewById(R.id.phone_number_data);
        RootRef = FirebaseDatabase.getInstance().getReference();
    }

    private void backButtonInit() {
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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

//                            if (!(confirmed == null)) {
////                                current_state = confirmed;
//                                confirmButton.setVisibility(View.INVISIBLE);
//                            }
//                            if (current_state.equals("CONFIRMED")) {
//                                confirmButton.setVisibility(View.INVISIBLE);
//                            }

                            if (requestStatus.equals("sent")) {
                                current_state = "requestissent";
//                                todo

                                Map<String, Object> profileMap = new HashMap<>();
                                String status = "1";
                                profileMap.put("status", status);
                                profileMap.put("status_uid", status + receiverUID);

                                RiderTicketsRef.child(receiverKeyID).updateChildren(profileMap);
                                confirm_carpool_button_word.setText("Cancel Carpool Request");
//                                confirmButton.setBackgroundColor(Color.parseColor("#FF0000"));
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });

        if (current_state.equals("CONFIRMED")) {
            confirmButton.setVisibility(View.INVISIBLE);
        }

        // double check for senderUID.equals(receiverKeyID) should be differnt!!
        if (!senderUID.equals(receiverUID)) {
            confirmButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    confirmButton.setVisibility(View.INVISIBLE);
                    confirmButton.setEnabled(false);

//                    if (current_state.equals("CONFIRMED")) {
//                        confirmButton.setVisibility(View.INVISIBLE);
//                    }

                    if (current_state.equals("new_dontknoweachother")) {
                        confirmButton.setVisibility(View.VISIBLE);
                        SendRequestToPickUpRider();
                    }
                    if (current_state.equals("requestissent")) {
                        confirmButton.setVisibility(View.VISIBLE);
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


                                                // todo

                                                Map<String, Object> profileMap = new HashMap<>();
                                                String status = "1";
                                                profileMap.put("status", status);
                                                profileMap.put("status_uid", status + receiverUID);

                                                RiderTicketsRef.child(receiverKeyID).updateChildren(profileMap);

                                                confirm_carpool_button_word.setText("Cancel Carpool Request");
//                                                confirmButton.setBackgroundColor(Color.parseColor("#FF0000"));
//                                                AllAvailableCarpoolTicketsFragment.riderTicketHolder.FILTER = 2;
                                                Toast.makeText(IndividualRiderTicketActivity.this, "Sent request to driver", Toast.LENGTH_LONG).show();


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

                            DriverRequestingRiderRef.child(receiverUID).child(receiverKeyID)
                                    .removeValue()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                confirmButton.setEnabled(true);
                                                current_state = "new_dontknoweachother";

                                                // todo

                                                Map<String, Object> profileMap = new HashMap<>();
                                                String status = "0";
                                                profileMap.put("status", status);
                                                profileMap.put("status_uid", status + receiverUID);
                                                RiderTicketsRef.child(receiverKeyID).updateChildren(profileMap);

                                                confirm_carpool_button_word.setText("Request to Pickup Rider");
//                                                confirmButton.setBackgroundColor(Color.parseColor("#2A2E43"));
//                                                AllAvailableCarpoolTicketsFragment.riderTicketHolder.FILTER = 1;
                                                Toast.makeText(IndividualRiderTicketActivity.this, "Canceled request to driver", Toast.LENGTH_LONG).show();

                                            }
                                        }
                                    });
                        }
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

    private void gettingPhoneNumberFromFirebase() {
        RiderTicketsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String receiveruID = dataSnapshot
                            .child(receiverKeyID).child("uid").getValue().toString();
                    receiverUID = receiveruID;

                    RootRef.child("Users").child(receiverUID).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                RiderPhoneNumber.setText(dataSnapshot.child("phone_number").getValue().toString());
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
}
