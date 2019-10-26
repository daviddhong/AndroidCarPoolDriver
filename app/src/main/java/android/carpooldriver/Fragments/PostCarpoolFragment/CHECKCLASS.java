//BPostCarpoolFragment
//
//
//        package android.carpooldriver.AppFragment.BPostCarpool;
//
//import android.carpooldriver.AppFragment.ACarpoolRiderRequests.content.DriverRequestTicketClass;
//import android.carpooldriver.AppFragment.ESettings.content.Profile.ProfileActivity;
//import android.carpooldriver.AppFragment.BPostCarpool.content.PostCarpoolRouteActivity;
//import android.carpooldriver.R;
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.Fragment;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.firebase.ui.database.FirebaseRecyclerAdapter;
//import com.firebase.ui.database.FirebaseRecyclerOptions;
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.Query;
//import com.google.firebase.database.ValueEventListener;
//
//public class BPostCarpoolFragment extends Fragment {
//
//    private View mPostCarpoolView;
//    private RecyclerView FriendRecyclerView;
//    private DatabaseReference DriverTicketsRef;
//    private String currentUserID;
//    private String usersIDS;
//
//    public static final int ADD_TICKET_REQUEST = 1;
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        mPostCarpoolView = inflater.inflate(R.layout.app_bpostcarpool_fragment_post_carpool, container, false);
//        initPostNewCarpool();
//        initProfile();
//        displaysFriendsListbyRecyclerView();
//        return mPostCarpoolView;
//    }
//
//
//    //use recycler view and friend list adapter to display list of friends
//    private void displaysFriendsListbyRecyclerView() {
//        FriendRecyclerView = (RecyclerView) mPostCarpoolView.findViewById(R.id.myposteddrivertickets);
//        FriendRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        FirebaseAuth mAuth = FirebaseAuth.getInstance();
//        currentUserID = mAuth.getCurrentUser().getUid();
//        DriverTicketsRef = FirebaseDatabase.getInstance().getReference().child("DriverTickets");
////        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");
//    }
//
//    // create firebase recycler
//    @Override
//    public void onStart() {
//        super.onStart();
//
////        Query rreceiveriderQuery = FirebaseDatabase
////                .getInstance()
////                .getReference()
////                .child("DriverTickets")
////                .orderByChild("status")
////                .equalTo("0");
//
//        String UidToString = FirebaseAuth.getInstance().getUid().toString();
//        String status_uid = "0" + UidToString;
//        Query receiveriderQuery = FirebaseDatabase
//                .getInstance()
//                .getReference()
//                .child("DriverTickets")
//                .orderByChild("status_uid")
//                .equalTo(status_uid);
//
//        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<DriverRequestTicketClass>()
//                .setQuery(receiveriderQuery, DriverRequestTicketClass.class)
//                .build();
//
//        final FirebaseRecyclerAdapter<DriverRequestTicketClass, riderTicketHolder> adapter
//                = new FirebaseRecyclerAdapter<DriverRequestTicketClass, riderTicketHolder>(options) {
//            @Override
//            protected void onBindViewHolder(@NonNull riderTicketHolder riderticketholder,
//                                            int i, @NonNull DriverRequestTicketClass riderReqTickets) {
//                usersIDS = getRef(i).getKey();
//                DriverTicketsRef.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        if (dataSnapshot.exists()) {
//                            riderticketholder.riderTo.setText(riderReqTickets.getticketto());
//                            riderticketholder.riderFrom.setText(riderReqTickets.getticketfrom());
//                            riderticketholder.riderDate.setText(riderReqTickets.getticketdate());
//                            riderticketholder.riderTime.setText(riderReqTickets.gettickettime());
//                            riderticketholder.riderPrice.setText(riderReqTickets.getticketprice());
//                            riderticketholder.riderNumberOfSeats.setText(riderReqTickets.getticketnumberofseats());
//
//                            riderticketholder.xDeletingButton.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//
//                                    DriverTicketsRef.child(usersIDS).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
//                                        @Override
//                                        public void onComplete(@NonNull Task<Void> task) {
//                                            Toast.makeText(getContext(), "Deleted my posted ticket", Toast.LENGTH_LONG).show();
//
//                                        }
//                                    });
//
//                                }
//                            });
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//                    }
//                });
//
//            }
//
//
//            @NonNull
//            @Override
//            public riderTicketHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.app_bpostcarpool_layout_posted_ride_ticket_entity, parent, false);
//                riderTicketHolder viewHolder = new riderTicketHolder(view);
//                return viewHolder;
//            }
//        };
//        FriendRecyclerView.setAdapter(adapter);
//        adapter.startListening();
//    }
//
//
//    public static class riderTicketHolder extends RecyclerView.ViewHolder {
//        TextView riderTo, riderFrom, riderDate, riderTime, riderNumberOfSeats, riderPrice;
//        RelativeLayout xDeletingButton;
//
//        public riderTicketHolder(@NonNull View itemView) {
//            super(itemView);
//            riderFrom = itemView.findViewById(R.id.drivertext_origin_post);
//            riderTo = itemView.findViewById(R.id.drivertext_destination_post);
//            riderDate = itemView.findViewById(R.id.drivertext_date_post);
//            riderTime = itemView.findViewById(R.id.drivertext_time_post);
//            riderNumberOfSeats = itemView.findViewById(R.id.drivertext_passenger_number_post);
//            riderPrice = itemView.findViewById(R.id.drivertext_earnings_entity_post);
//            xDeletingButton = itemView.findViewById(R.id.drivermore_information_entity_request_post);
//        }
//
//    }
//
//    // EFFECTS: Initialize the post new carpool activity.
//    private void initPostNewCarpool() {
//        RelativeLayout mPostCarpoolRelativeLayout = (RelativeLayout) mPostCarpoolView.findViewById(R.id.post_new_carpool);
//        mPostCarpoolRelativeLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), PostCarpoolRouteActivity.class);
//                startActivity(intent);
//                // EFFECTS: Animation to Profile Activity
//                getActivity().overridePendingTransition(R.anim.slide_up, R.anim.slide_vertical_null);
//            }
//        });
//    }
//
//    private void initProfile() {
//        ImageView profileImageView = (ImageView) mPostCarpoolView.findViewById(R.id.profile_post_carpool);
//        profileImageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), ProfileActivity.class);
//                startActivity(intent);
//
//            }
//        });
//    }
//}
//
