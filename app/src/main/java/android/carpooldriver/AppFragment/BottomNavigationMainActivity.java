package android.carpooldriver.AppFragment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.carpooldriver.AppFragment.ACarpoolRiderRequests.ACarpoolRiderRequestsFragment;
import android.carpooldriver.AppFragment.BPostCarpool.BPostCarpoolFragment;
import android.carpooldriver.AppFragment.CConfirmedCarpoolFragment.CConfirmedCarpoolFragment;
import android.carpooldriver.AppFragment.DAcceptPendingRequests.DAcceptPendingRequestsFragment;
import android.carpooldriver.AppFragment.ESettings.ESettingsFragment;
import android.carpooldriver.AppFragment.ESettings.content.Profile.ProfileActivity;
import android.carpooldriver.R;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class BottomNavigationMainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_activity_bottom_navigation_main);

        initToolbar();

        initProfile();

        initFragment();

        NavigationView();
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.main));
        toggle.syncState();
    }

    private void initProfile() {
        ImageView profileImageView = findViewById(R.id.profile_toolbar);
        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BottomNavigationMainActivity.this, ProfileActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_up, R.anim.slide_vertical_null);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void initFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_layout, new ACarpoolRiderRequestsFragment()).commit();
    }

    private void NavigationView() {
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;
        switch (item.getItemId()) {
            case R.id.nav_carpool_requests:
                fragment = new ACarpoolRiderRequestsFragment();
                break;
            case R.id.nav_post_carpool:
                fragment = new BPostCarpoolFragment();
                break;
            case R.id.nav_your_carpool:
                fragment = new CConfirmedCarpoolFragment();
                break;
            case R.id.nav_reply:
                fragment = new DAcceptPendingRequestsFragment();
                break;
            case R.id.nav_more:
                fragment = new ESettingsFragment();
                break;
        }
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_layout, fragment).commit();
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

}

