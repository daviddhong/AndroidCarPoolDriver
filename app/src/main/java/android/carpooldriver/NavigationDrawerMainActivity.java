package android.carpooldriver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.carpooldriver.Fragments.AllAvailableCarpoolTicketsFragment.AllAvailableCarpoolTicketsFragment;
import android.carpooldriver.Fragments.ConfirmedCarpoolFragment.ConfirmedCarpoolFragment;
import android.carpooldriver.Fragments.AcceptPendingRequestsFragment.AcceptPendingRequestsFragment;
import android.carpooldriver.Fragments.PostCarpoolFragment.PostCarpoolFragment;
import android.carpooldriver.Fragments.SentRequestFragment.SentRequestFragment;
import android.carpooldriver.Fragments.SettingsFragment.SettingsFragment;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

public class NavigationDrawerMainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_drawer);

        initToolbar();

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
                .replace(R.id.frame_layout, new AllAvailableCarpoolTicketsFragment()).commit();
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
                fragment = new AllAvailableCarpoolTicketsFragment();
                break;
            case R.id.nav_sent_carpool_requests:
                fragment = new SentRequestFragment();
                break;
            case R.id.nav_post_carpool:
                fragment = new PostCarpoolFragment();
                break;
            case R.id.nav_your_carpool:
                fragment = new ConfirmedCarpoolFragment();
                break;
            case R.id.nav_reply:
                fragment = new AcceptPendingRequestsFragment();
                break;
            case R.id.nav_more:
                fragment = new SettingsFragment();
                break;
        }
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_layout, fragment).commit();
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

}

