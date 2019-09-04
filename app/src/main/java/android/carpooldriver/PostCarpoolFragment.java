package android.carpooldriver;

import android.carpooldriver.CarpoolRiderRequests.RiderRequestTicketClass;
import android.carpooldriver.Settings.Profile.ProfileActivity;
import android.carpooldriver.PostCarpool.PostCarpoolRouteActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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

public class PostCarpoolFragment extends Fragment {

    View mPostCarpoolView;
    RelativeLayout mPostCarpoolRelativeLayout;
    private RecyclerView FriendRecyclerView;
    private DatabaseReference DriverTicketsRef, UsersRef;
    private FirebaseAuth mAuth;
    private String currentUserID;

    public static final int ADD_TICKET_REQUEST = 1;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mPostCarpoolView = inflater.inflate(R.layout.fragment_post_carpool, container, false);
        initPostNewCarpool();
        initProfile();
        displaysFriendsListbyRecyclerView();
        return mPostCarpoolView;
    }

    //use recycler view and friend list adapter to display list of friends
    private void displaysFriendsListbyRecyclerView() {
        FriendRecyclerView = (RecyclerView) mPostCarpoolView.findViewById(R.id.myposteddrivertickets);
        FriendRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        // todo this is getting all the driver tickets from database
        DriverTicketsRef = FirebaseDatabase.getInstance().getReference().child("DriverTickets");
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");
    }


    // create firebase recycler
    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<RiderRequestTicketClass>()
                .setQuery(DriverTicketsRef, RiderRequestTicketClass.class)
                .build();

        final FirebaseRecyclerAdapter<RiderRequestTicketClass, riderTicketHolder> adapter
                = new FirebaseRecyclerAdapter<RiderRequestTicketClass, riderTicketHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull riderTicketHolder riderticketholder, int i, @NonNull RiderRequestTicketClass riderReqTickets) {
                String usersIDS = getRef(i).getKey();
                UsersRef.child(usersIDS).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (dataSnapshot.exists()) {
                            final String riderTo = dataSnapshot.child("To").getValue().toString();
                            final String riderFrom = dataSnapshot.child("From").getValue().toString();
                            riderticketholder.riderTo.setText(riderTo);
                            riderticketholder.riderFrom.setText(riderFrom);
                            riderticketholder.itemView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    // does nothing when clicked yet (probably want to delete it)
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
            public riderTicketHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_posted_ride_ticket_entity, parent, false);
                riderTicketHolder viewHolder = new riderTicketHolder(view);
                return viewHolder;
            }
        };
        FriendRecyclerView.setAdapter(adapter);
        adapter.startListening();
    }


    public static class riderTicketHolder extends RecyclerView.ViewHolder {
        TextView riderTo, riderFrom;
        public riderTicketHolder(@NonNull View itemView) {
            super(itemView);
            riderFrom = itemView.findViewById(R.id.text_origin);
            riderTo = itemView.findViewById(R.id.text_destination);
        }
    }


    // EFFECTS: Initialize the post new carpool activity.
    private void initPostNewCarpool() {
        mPostCarpoolRelativeLayout = (RelativeLayout) mPostCarpoolView.findViewById(R.id.post_new_carpool);
        mPostCarpoolRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PostCarpoolRouteActivity.class);
                startActivity(intent);
                // EFFECTS: Animation to Profile Activity
                getActivity().overridePendingTransition(R.anim.slide_up, R.anim.slide_vertical_null);
            }
        });
    }

    private void initProfile() {
        ImageView profileImageView = (ImageView) mPostCarpoolView.findViewById(R.id.profile_post_carpool);
        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ProfileActivity.class);
                startActivity(intent);

            }
        });
    }
}