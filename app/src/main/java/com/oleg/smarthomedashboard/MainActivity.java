package com.oleg.smarthomedashboard;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.github.javiersantos.appupdater.AppUpdater;
import com.github.javiersantos.appupdater.enums.Display;
import com.github.javiersantos.appupdater.enums.UpdateFrom;
import com.oleg.smarthomedashboard.fragments.MainFragment;
import com.oleg.smarthomedashboard.fragments.MetersFragment;
import com.oleg.smarthomedashboard.fragments.ScenarioFragment;
import com.oleg.smarthomedashboard.fragments.SettingsFragment;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import io.ak1.BubbleTabBar;

public class MainActivity extends AppCompatActivity {

    int startingPosition;
    private BubbleTabBar bubbleTabBar;
    private WebSocketClient webSocketClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadFragment(new MainFragment(), 1);
        // Bottom Nav
        initNavigation();
        createWebSocketClient();
        AppUpdater appUpdater = new AppUpdater(this)
                .setDisplay(Display.SNACKBAR)
                .setUpdateFrom(UpdateFrom.GITHUB)
                .setGitHubUserAndRepo("OlegPV2", "SmartHomeApp")
                .setUpdateJSON("https://github.com/OlegPV2/SmartHomeApp/tree/master/app/update-changelog.json");
        appUpdater.start();
    }

    private void createWebSocketClient() {
        URI uri;
        try {
            uri = new URI("ws://192.168.1.80:9090/ws");
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }

        webSocketClient = new WebSocketClient(uri) {
            @Override
            public void onOpen(ServerHandshake serverHandshake) {
                webSocketClient.send("Update");
            }

            @Override
            public void onMessage(String s) {
                final String message = s;
                runOnUiThread(() -> {
                    String[] msg = message.split("\n");
                    if (startingPosition == 1 && msg[0].charAt(0) != 'w') {
                        for (String value : msg) {
                            String[] cmd = value.split(":");
                            if (cmd[0].equals("th")) {
                                cmd[1] = cmd[1].replace("-", "_");
                                TextView txt = findViewById(getResources().getIdentifier(cmd[1], "id", getPackageName()));
                                txt.setText(cmd[2]);
                            } else {
                                RelativeLayout element = (RelativeLayout) findViewById(getResources().getIdentifier(cmd[0], "id", getPackageName()));
                                if (cmd[1].equals("0")) {
                                    try {
                                        element.setBackgroundColor(0);
                                    } catch (Exception e) {
                                        Toast.makeText(MainActivity.this, "Exception:" + cmd[0] + ":" + cmd[1], Toast.LENGTH_SHORT).show();
                                    }
                                    if (cmd[0].equals("rentrance")) {
                                        ((ImageView) element.getChildAt(0)).setImageResource(R.drawable.door_closed);
                                    } else if (cmd[0].charAt(0) == 'r') {
                                        ((ImageView) element.getChildAt(0)).setImageResource(R.drawable.closed_window);
                                    }
                                } else {
                                    try {
                                        element.setBackgroundColor(getColor(getBckColor(cmd[0].charAt(0))));
                                    } catch (Exception e) {
                                        Toast.makeText(MainActivity.this, "Exception:" + cmd[0] + ":" + cmd[1], Toast.LENGTH_SHORT).show();
                                    }
                                    if (cmd[0].equals("rentrance")) {
                                        ((ImageView) element.getChildAt(0)).setImageResource(R.drawable.door_opened);
                                    } else if (cmd[0].charAt(0) == 'r') {
                                        ((ImageView) element.getChildAt(0)).setImageResource(R.drawable.open_window);
                                    }
                                }
                            }
                        }
                    }
                    if (startingPosition == 2 && msg[0].charAt(0) == 'w'){
                        for (String value : msg) {
                            String[] cmd = value.split(":");
                            switch (cmd[1]) {
                                case "hot":
                                    TextView hot = findViewById(R.id.value_hot_water);
                                    hot.setText(cmd[2]);
                                    break;
                                case "cold":
                                    TextView cold = findViewById(R.id.value_cold_water);
                                    cold.setText(cmd[2]);
                                    break;
                                case "t1":
                                    TextView t1 = findViewById(R.id.value_electric_t1);
                                    t1.setText(cmd[2]);
                                    break;
                                case "t2":
                                    TextView t2 = findViewById(R.id.value_electric_t2);
                                    t2.setText(cmd[2]);
                                    break;
                                case "t3":
                                    TextView t3 = findViewById(R.id.value_electric_t3);
                                    t3.setText(cmd[2]);
                                    break;
                            }
                        }
                    }
                });
            }

            @Override
            public void onClose(int i, String s, boolean b) {
                Toast.makeText(MainActivity.this, "Connection closed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Exception e) {
//                Toast.makeText(MainActivity.this, "Connection error:"+e, Toast.LENGTH_SHORT).show();
            }
        };
        webSocketClient.connect();
    }

    private int getBckColor(char ch) {
        switch (ch) {
            case 'l':
            case 't':
                return R.color.background_button_light;
            case 'w':
            case 'r':
                return R.color.background_button_wf;
            case 'a':
                return R.color.background_button_auto;
            case 'f':
                return R.color.background_button_fan;
        }
        return R.color.error;
    }

    public void sendMessage(View view) {
        String command = getResources().getResourceEntryName(view.getId()) + ":";
        if (command.charAt(0) == 'l') {
            command += (view.getBackground() == null) ? "255" : "0";
        } else {
            command += (view.getBackground() == null) ? "0" : "1";
        }
        try {
            webSocketClient.send(command);
        } catch (Exception e) {
            Toast.makeText(this, "No connection to server", Toast.LENGTH_SHORT).show();
        }
    }

    public void sendMessage(String command) {
        try {
            webSocketClient.send(command);
        } catch (Exception e) {
            Toast.makeText(this, "No connection to server", Toast.LENGTH_SHORT).show();
        }
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
//                    chipNavigationBar.setBackgroundColor(getResources().getColor(R.color.blue_grey_700, this.getTheme()));
                    newPosition = 1;
                    break;
                case R.id.navigation_meters:
                    fragment = new MetersFragment();
//                    chipNavigationBar.setBackgroundColor(getResources().getColor(R.color.indigo_500, this.getTheme()));
                    newPosition = 2;
                    break;
                case R.id.navigation_scenario:
                    fragment = new ScenarioFragment();
//                    chipNavigationBar.setBackgroundColor(getResources().getColor(R.color.grey_700, this.getTheme()));
                    newPosition = 3;
                    break;
                case R.id.navigation_settings:
                    fragment = new SettingsFragment();
//                    chipNavigationBar.setBackgroundColor(getResources().getColor(R.color.teal_800, this.getTheme()));
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
                sendMessage("Update");
                break;
            case 2:
                sendMessage("Meters");
                break;
            case 3:
                sendMessage("Scenario");
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
            webSocketClient.close();
            finishAffinity();
//            super.onBackPressed();
        }
    }

}