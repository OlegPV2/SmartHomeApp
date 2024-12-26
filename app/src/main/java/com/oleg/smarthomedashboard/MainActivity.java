package com.oleg.smarthomedashboard;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.oleg.smarthomedashboard.fragment.DashboardFragment;
import com.oleg.smarthomedashboard.fragment.MetersFragment;
import com.oleg.smarthomedashboard.fragment.ScenarioFragment;
import com.oleg.smarthomedashboard.fragment.SettingsFragment;
import com.oleg.smarthomedashboard.helper.ConfigurationHelper;
import com.oleg.smarthomedashboard.helper.SettingsMetersHelper;
import com.oleg.smarthomedashboard.model.ConnectionState;
import com.oleg.smarthomedashboard.update.CheckUpdate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.ak1.BubbleTabBar;

public class MainActivity extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private static final List<ConfigurationHelper> configurationHelperList = new ArrayList<>();
    private static final List<SettingsMetersHelper> settingsMetersHelperList = new ArrayList<>();
    private int startingPosition;
    private static boolean APP_PAUSED = false;
    private static AppCompatActivity instance;
    private BubbleTabBar bubbleTabBar;
    private final String serverAddress = ConnectionState.SERVER_URL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        setContentView(R.layout.activity_main);
        checkLocationPermission();
        CheckUpdate.checkUpdate(this);
        new ConfigFromJSON(
                this,
                "https://raw.githubusercontent.com/OlegPV2/SmartHomeApp/master/config.json",
                configurationHelperList,
                settingsMetersHelperList);
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (startingPosition != 1) {
                    loadFragment(new DashboardFragment(), 1);
                    bubbleTabBar.setSelectedWithId(R.id.navigation_home, true);
                } else {
                    WSClient.onClose();
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }
            }
        });
        loadFragment(new DashboardFragment(), 1);
        initNavigation();
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
        if (APP_PAUSED) {
            checkHomeConnection();
            switch (startingPosition) {
                case 1:
                    WSClient.sendMessage("Update");
                    break;
                case 2:
                    WSClient.sendMessage("Meters");
                    break;
                case 3:
                    WSClient.sendMessage("Scenario");
                    break;
                case 4:
//                sendMessage("Settings");
                    break;
            }
            APP_PAUSED = false;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        APP_PAUSED = true;
    }

/*    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (startingPosition != 1) {
            loadFragment(new DashboardFragment(), 1);
            bubbleTabBar.setSelectedWithId(R.id.navigation_home, true);
        } else {
            WSClient.onClose();
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }*/

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
                        .setTitle(R.string.app_updater_permission_dialog_header)
                        .setMessage(R.string.app_updater_permission_dialog_message)
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
                        .setTitle(R.string.app_updater_permission_canceled_dialog_header)
                        .setMessage(R.string.app_updater_permission_canceled_dialog_message)
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
            ssid = ssid.substring(1, ssid.length() - 1);
            if (Objects.equals(ssid, "Elo4k@") ||
                    Objects.equals(ssid, "Elo4k@5") ||
                    serverAddress.equals(ConnectionState.TEST_URL)) {
                ConnectionState.HOME_NETWORK = true;
                WSClient.createWebSocketClient(this, serverAddress);
            }
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            View v = getCurrentFocus();
            if ( v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent( event );
    }

    public static List<ConfigurationHelper> getConfigurationHelperList() {
        return configurationHelperList;
    }

    public static List<SettingsMetersHelper> getSettingsMetersHelperList() {
        return settingsMetersHelperList;
    }

    public static Context getContext() {
        return instance.getApplicationContext();
    }

    public int getStartingPosition() {
        return startingPosition;
    }
}
