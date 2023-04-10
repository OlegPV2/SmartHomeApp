package com.oleg.smarthomedashboard;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.oleg.smarthomedashboard.fragments.DashboardFragment;
import com.oleg.smarthomedashboard.fragments.MetersFragment;
import com.oleg.smarthomedashboard.fragments.ScenarioFragment;
import com.oleg.smarthomedashboard.fragments.SettingsFragment;
import com.oleg.smarthomedashboard.update.CheckUpdate;

import java.util.Objects;

import io.ak1.BubbleTabBar;

public class MainActivity extends AppCompatActivity {
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public static boolean HOME_NETWORK = false;
    int startingPosition;
    private BubbleTabBar bubbleTabBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadFragment(new DashboardFragment(), 1);
        // Bottom Nav
        initNavigation();
        checkLocationPermission();
        CreateWebSocketClient.createWebSocketClient(this);
        CheckUpdate.checkUpdate(this);
    }

    @SuppressLint("NonConstantResourceId")
    private void initNavigation() {
        bubbleTabBar = findViewById(R.id.bubbleTabBar);
        bubbleTabBar.addBubbleListener(item -> {
            Fragment fragment = new DashboardFragment();
            int newPosition = 0;
            switch (item) {
                case R.id.navigation_home:
                    fragment = new DashboardFragment();
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
        HOME_NETWORK = false;
        checkHomeConnection();
    }

    @Override
    public void onBackPressed() {
        if (startingPosition != 1) {
            loadFragment(new DashboardFragment(), 1);
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

    public void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("Location permission request")
                        .setMessage("Location permission is needed to receive current WiFi name and proper communicate with Home controller")
                        .setPositiveButton("OK", (dialogInterface, i) -> {
                            //Prompt the user once explanation has been shown
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    MY_PERMISSIONS_REQUEST_LOCATION);
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
        } else checkHomeConnection();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST_LOCATION) {// If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                // permission was granted, yay! Do the
                // location-related task you need to do.
                if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    checkHomeConnection();
                }
            } else {
                new AlertDialog.Builder(this)
                        .setTitle("Location permission request")
                        .setMessage("Please specify the location permission manually to be sure that the app work correctly")
                        .create()
                        .show();
            }
        }
    }

    private void checkHomeConnection() {
        WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo;

        wifiInfo = wifiManager.getConnectionInfo();
        if (wifiInfo.getSupplicantState() == SupplicantState.COMPLETED) {
            String ssid = wifiInfo.getSSID();
            if (Objects.equals(ssid, "Elo4k@") || Objects.equals(ssid, "Elo4k@5")) {
                HOME_NETWORK = true;
            } else
                Toast.makeText(this, "You are not in home network", Toast.LENGTH_SHORT).show();
        }
    }
}