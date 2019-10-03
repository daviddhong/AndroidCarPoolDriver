package android.carpooldriver.AppFragment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.carpooldriver.AppFragment.ACarpoolRiderRequests.ACarpoolRiderRequestsFragment;
import android.carpooldriver.AppFragment.BPostCarpool.BPostCarpoolFragment;
import android.carpooldriver.AppFragment.CConfirmedCarpoolFragment.CConfirmedCarpoolFragment;
import android.carpooldriver.AppFragment.DAcceptpendingRequests.DAcceptPendingRequestsFragment;
import android.carpooldriver.AppFragment.ESettings.ESettingsFragment;
import android.carpooldriver.R;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ZBottomNavigationMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carpooldriver_main);

        initFragment();

        bottomNavigationView();
    }

    private void initFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_layout, new ACarpoolRiderRequestsFragment()).commit();
    }

    private void bottomNavigationView() {
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener
            onNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment = null;

            switch (item.getItemId()) {
                case R.id.carpool_requests:
                    fragment = new ACarpoolRiderRequestsFragment();
                    break;
                case R.id.post_carpool:
                    fragment = new BPostCarpoolFragment();
                    break;
                case R.id.your_carpool:
                    fragment = new CConfirmedCarpoolFragment();
                    break;
                case R.id.reply:
                    fragment = new DAcceptPendingRequestsFragment();
                    break;
                case R.id.more:
                    fragment = new ESettingsFragment();
                    break;
            }
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_layout, fragment).commit();
            return true;
        }
    };
}

