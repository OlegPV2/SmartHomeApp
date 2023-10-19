package com.oleg.smarthomedashboard;

import static com.oleg.smarthomedashboard.MainActivity.FIRST_START;
import static com.oleg.smarthomedashboard.MainActivity.HOME_NETWORK;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.slider.Slider;
import com.google.android.material.switchmaterial.SwitchMaterial;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;

public class CreateWebSocketClient {

    protected static WebSocketClient webSocketClient;
    private static boolean WEBSOCKET_CONNECTED = false;
    private static final Toast connectionWait = Toast.makeText(MainActivity.getContext(), R.string.websocket_no_connection_to_server, Toast.LENGTH_SHORT);
    private static final Toast connectionWrong = Toast.makeText(MainActivity.getContext(), R.string.websocket_error, Toast.LENGTH_SHORT);

    static void createWebSocketClient(final MainActivity mainActivity) {
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
                WEBSOCKET_CONNECTED = true;
                FIRST_START = false;
                connectionWait.cancel();
                connectionWrong.cancel();
                sendMessage("Update");
            }

            @Override
            public void onMessage(String s) {
                mainActivity.runOnUiThread(() -> {
                    String[] msg = s.split("\n");
                    if (mainActivity.startingPosition == 1 && msg[0].charAt(0) != 'w') {
                        OnMessageForDashboard(mainActivity, msg);
                    }
                    if (mainActivity.startingPosition == 2 && msg[0].charAt(0) == 'w') {
                        OnMessageForMeters(mainActivity, msg);
                    }
                    if (mainActivity.startingPosition == 4 /*&& msg[0].charAt(0) == 'w'*/) {
                        OnMessageForSettings(mainActivity, msg);
                    }
                });
            }

            @Override
            public void onClose(int i, String s, boolean b) {
                WEBSOCKET_CONNECTED = false;
            }

            @Override
            public void onError(Exception e) {
                Log.e("WebSocket", "Connection error:" + e);
            }
        };
        webSocketClient.connect();
    }

    private static void OnMessageForDashboard(Activity activity, String[] message) {
        for (String value : message) {
            String[] command = value.split(":");
            if (command[0].equals("th")) {
                command[1] = command[1].replace("-", "_");
                @SuppressLint("DiscouragedApi")
                TextView txt = activity.findViewById(
                        activity.getResources().getIdentifier(command[1],
                                "id",
                                activity.getPackageName()
                        ));
                txt.setText(command[2]);
            } else {
                if (command[0].equals("rwc")) break;
                if (command[0].equals("lux")) break;
                @SuppressLint("DiscouragedApi")
                ViewGroup element = activity.findViewById(
                        activity.getResources().getIdentifier(
                                command[0],
                                "id",
                                activity.getPackageName()
                        ));
                if (command[1].equals("0")) {
                    element.setBackground(null);

                    if (command[0].equals("rentrance")) {
                        ((ImageView) element
                                .getChildAt(0))
                                .setImageResource(R.drawable.door_closed);
                    } else if (command[0].charAt(0) == 'r') {
                        ((ImageView) element
                                .getChildAt(0))
                                .setImageResource(R.drawable.closed_window);
                    }
                } else {
                    element.setBackgroundColor(activity.getColor(getBckColor(command[0].charAt(0))));

                    if (command[0].equals("rentrance")) {
                        ((ImageView) element
                                .getChildAt(0))
                                .setImageResource(R.drawable.door_opened);
                    } else if (command[0].charAt(0) == 'r') {
                        ((ImageView) element
                                .getChildAt(0))
                                .setImageResource(R.drawable.open_window);
                    }
                }
            }
        }
    }

    private static void OnMessageForMeters(Activity activity, String[] message) {
        for (String value : message) {
            String[] command = value.split(":");
            switch (command[1]) {
                case "hot":
                    TextView hot = activity.findViewById(R.id.value_hot_water);
                    hot.setText(command[2]);
                    break;
                case "cold":
                    TextView cold = activity.findViewById(R.id.value_cold_water);
                    cold.setText(command[2]);
                    break;
                case "t1":
                    TextView t1 = activity.findViewById(R.id.value_electric_t1);
                    t1.setText(command[2]);
                    break;
                case "t2":
                    TextView t2 = activity.findViewById(R.id.value_electric_t2);
                    t2.setText(command[2]);
                    break;
                case "t3":
                    TextView t3 = activity.findViewById(R.id.value_electric_t3);
                    t3.setText(command[2]);
                    break;
            }
        }
    }

    private static void OnMessageForSettings(Activity activity, String[] message) {
        for (String value : message) {
            String[] command = value.split(":");
            switch (command[0]) {
                case "dimming":
                    Slider sl = activity.findViewById(
                            activity.getResources().getIdentifier(
                                    command[0] + command[1] + command[2],
                                    "id",
                                    activity.getPackageName()
                            ));
                    sl.setValue(Float.parseFloat(command[3]));
                    break;
                case "meter:":
                    switch (command[1]) {
                        case "hot":
                            TextView hot = activity.findViewById(R.id.value_hot_water);
                            hot.setText(command[2]);
                            break;
                        case "cold":
                            TextView cold = activity.findViewById(R.id.value_cold_water);
                            cold.setText(command[2]);
                            break;
                        case "t1":
                            TextView t1 = activity.findViewById(R.id.value_electric_t1);
                            t1.setText(command[2]);
                            break;
                        case "t2":
                            TextView t2 = activity.findViewById(R.id.value_electric_t2);
                            t2.setText(command[2]);
                            break;
                        case "t3":
                            TextView t3 = activity.findViewById(R.id.value_electric_t3);
                            t3.setText(command[2]);
                            break;
                    }
                    break;
                case "dim":
                    SwitchMaterial sw;
                    for (int m = 0; m < 5; m++) {
                        for (int ch = 0; ch < 4; ch++) {
                            sw = activity.findViewById(
                                    activity.getResources().getIdentifier(
                                            "dimming_" + m + ch + "_switch",
                                            "id",
                                            activity.getPackageName()
                                    ));
                            sw.setEnabled(((Integer.parseInt(command[2]) >> ch) & 1) != 0);
                        }
                    }
                    break;
                case "warm":
                    // Do nothing yet. Temperature of warm floor set is not sent from the Main
                    break;
            }
        }
    }

    private static int getBckColor(char ch) {
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

    public static void sendMessage(View view) {
        String command = MainActivity.getContext().getResources().getResourceEntryName(view.getId()) + ":";
        if (command.charAt(0) == 'l') {
            command += (view.getBackground() == null) ? "255" : "0";
        } else {
            command += (view.getBackground() == null) ? "0" : "1";
        }
        sendMessage(command);
    }

    public static void sendMessage(String command) {
        Log.e("sendMessage", "HOME_NETWORK:" + HOME_NETWORK + ", WEBSOCKET_CONNECTED:" + WEBSOCKET_CONNECTED + ", Command:" + command);
        if (HOME_NETWORK && WEBSOCKET_CONNECTED) {
            try {
                webSocketClient.send(command);
            } catch (Exception e) {
                connectionWrong.show();
//                createWebSocketClient((MainActivity) MainActivity.getContext());
            }
        } else if (!HOME_NETWORK) {
            Toast.makeText(MainActivity.getContext(), R.string.websocket_not_home_network, Toast.LENGTH_SHORT).show();
        } else {
            connectionWait.show();
        }
    }

    public static void onClose() {
        if (WEBSOCKET_CONNECTED) {
            webSocketClient.close();
        }
    }
}
