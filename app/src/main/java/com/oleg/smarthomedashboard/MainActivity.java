package com.oleg.smarthomedashboard;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.oleg.smarthomedashboard.fragments.MainFragment;
import com.oleg.smarthomedashboard.fragments.MetersFragment;
import com.oleg.smarthomedashboard.fragments.ScenarioFragment;
import com.oleg.smarthomedashboard.fragments.SettingsFragment;

import org.json.JSONException;

import io.ak1.BubbleTabBar;

public class MainActivity extends AppCompatActivity {

    int startingPosition;
    private BubbleTabBar bubbleTabBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadFragment(new MainFragment(), 1);
        // Bottom Nav
        initNavigation();
        CreateWebSocketClient.createWebSocketClient(this);
        CheckUpdate.checkUpdate(this);

    }

    @SuppressLint("NonConstantResourceId")
    private void initNavigation() {
        bubbleTabBar = findViewById(R.id.bubbleTabBar);
        bubbleTabBar.addBubbleListener(item -> {
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
    }

    private void loadFragment(Fragment fragment, int newPosition) {
        if (fragment != null) {
            if (startingPosition > newPosition) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
                transaction.replace(R.id.container, fragment);
                transaction.commit();
            }
            if (startingPosition < newPosition) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
                transaction.replace(R.id.container, fragment);
                transaction.commit();
            }
            startingPosition = newPosition;
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        switch (startingPosition) {
            case 1:
                CreateWebSocketClient.sendMessage(this, "Update");
                break;
            case 2:
                CreateWebSocketClient.sendMessage(this, "Meters");
                break;
            case 3:
                CreateWebSocketClient.sendMessage(this, "Scenario");
                break;
            case 4:
//                sendMessage("Settings");
                break;
        }
    }

    @Override
    public void onBackPressed() {
//        Toast.makeText(this, "Finish:"+selectedItemId, Toast.LENGTH_SHORT).show();
        if (startingPosition != 1) {
            loadFragment(new MainFragment(), 1);
            bubbleTabBar.setSelectedWithId(R.id.navigation_home, true);
        } else {
            CreateWebSocketClient.onClose();
            finishAffinity();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}