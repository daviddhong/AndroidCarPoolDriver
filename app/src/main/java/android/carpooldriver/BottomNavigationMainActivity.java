package android.carpooldriver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BottomNavigationMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initFragment();

        bottomNavigationView();
    }

    private void initFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_layout, new CarpoolRiderRequestsFragment()).commit();
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
                    fragment = new CarpoolRiderRequestsFragment();
                    break;
                case R.id.post_carpool:
                    fragment = new PostCarpoolFragment();
                    break;
                case R.id.your_carpool:
                    fragment = new ConfirmedCarpoolFragment();
                    break;
                case R.id.chat:
                    fragment = new PendingRequestsFragment();
                    break;
                case R.id.more:
                    fragment = new SettingsFragment();
                    break;
            }
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_layout, fragment).commit();
            return true;
        }
    };
}
