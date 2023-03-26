package com.oleg.smarthomedashboard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;
import com.oleg.smarthomedashboard.fragments.MainFragment;
import com.oleg.smarthomedashboard.fragments.MetersFragment;
import com.oleg.smarthomedashboard.fragments.ScenarioFragment;
import com.oleg.smarthomedashboard.fragments.SettingsFragment;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView navigation;
    private ChipNavigationBar chipNavigationBar;
    int startingPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadFragment(new MainFragment(), 1);
        // Bottom Nav
        initNavigation();
        chipNavigationBar = findViewById(R.id.navigation);
        chipNavigationBar.setItemSelected(1, true);
        chipNavigationBar.setOnItemSelectedListener(item -> {
            Fragment fragment = new MainFragment();
            int newPosition = 0;

            switch (item) {
                case R.id.navigation_home:
                    fragment = new MainFragment();
                    newPosition = 1;
                    break;
                case R.id.navigation_meters:
                    fragment = new MetersFragment();
                    newPosition = 2;
                    break;
                case R.id.navigation_scenario:
                    fragment = new ScenarioFragment();
                    newPosition = 3;
                    break;
                case R.id.navigation_settings:
                    fragment = new SettingsFragment();
                    newPosition = 4;
                    break;
            }
            loadFragment(fragment, newPosition);
        });
        // TODO: retrieve data from server via websocket
    }

    private void initNavigation() {
/*        navigation = findViewById(R.id.navigation);
        navigation.setOnItemSelectedListener(item -> {
                    Fragment fragment = null;
                    int newPosition = 0;

                    switch (item.getItemId()) {
                        case R.id.navigation_home:
                            fragment = new MainFragment();
                            newPosition = 1;
                            navigation.setBackgroundColor(getResources().getColor(R.color.blue_grey_700, this.getTheme()));
                            break;
                        case R.id.navigation_meters:
                            fragment = new MetersFragment();
                            newPosition = 2;
                            navigation.setBackgroundColor(getResources().getColor(R.color.indigo_500, this.getTheme()));
                            break;
                        case R.id.navigation_scenario:
                            fragment = new ScenarioFragment();
                            newPosition = 3;
                            navigation.setBackgroundColor(getResources().getColor(R.color.grey_700, this.getTheme()));
                            break;
                        case R.id.navigation_settings:
                            fragment = new SettingsFragment();
                            newPosition = 4;
                            navigation.setBackgroundColor(getResources().getColor(R.color.teal_800, this.getTheme()));
                            break;
                    }
                    return loadFragment(fragment, newPosition);
                }
        );*/
    }

    private boolean loadFragment(Fragment fragment, int newPosition) {
        if(fragment != null) {
            if(startingPosition > newPosition) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right );
                transaction.replace(R.id.container, fragment);
                transaction.commit();
            }
            if(startingPosition < newPosition) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
                transaction.replace(R.id.container, fragment);
                transaction.commit();
            }
            startingPosition = newPosition;
            return true;
        }

        return false;
    }

    @Override
    public void onBackPressed() {
        int selectedItemId = navigation.getSelectedItemId();
        if (R.id.navigation_home != selectedItemId) {
            loadFragment(new MainFragment(), 1);
            navigation.setSelectedItemId(R.id.navigation_home);
        } else {
            super.onBackPressed();
        }
    }

}