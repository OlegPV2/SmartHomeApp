package com.oleg.smarthomedashboard;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;

public class CreateWebSocketClient {

    protected static WebSocketClient webSocketClient;

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
            }

            @Override
            public void onMessage(String s) {
                mainActivity.runOnUiThread(() -> {
//                    Log.d("WS.Incoming message", s);
                    String[] msg = s.split("\n");
                    if (mainActivity.startingPosition == 1 && msg[0].charAt(0) != 'w') {
                        for (String value : msg) {
                            String[] cmd = value.split(":");
                            if (cmd[0].equals("th")) {
                                cmd[1] = cmd[1].replace("-", "_");
                                @SuppressLint("DiscouragedApi") TextView txt = mainActivity.findViewById(
                                        mainActivity.getResources().getIdentifier(cmd[1],
                                                "id",
                                                mainActivity.getPackageName()
                                        ));
                                txt.setText(cmd[2]);
                            } else {
                                if (cmd[0].equals("rwc")) break;
                                if (cmd[0].equals("lux")) break;
                                @SuppressLint("DiscouragedApi")
                                ViewGroup element = mainActivity.findViewById(
                                        mainActivity.getResources().getIdentifier(
                                                cmd[0],
                                                "id",
                                                mainActivity.getPackageName()
                                        ));
                                if (cmd[1].equals("0")) {
                                    element.setBackground(null);

                                    if (cmd[0].equals("rentrance")) {
                                        ((ImageView) element
                                                .getChildAt(0))
                                                .setImageResource(R.drawable.door_closed);
                                    } else if (cmd[0].charAt(0) == 'r') {
                                        ((ImageView) element
                                                .getChildAt(0))
                                                .setImageResource(R.drawable.closed_window);
                                    }
                                } else {
                                    element.setBackgroundColor(mainActivity.getColor(getBckColor(cmd[0].charAt(0))));

                                    if (cmd[0].equals("rentrance")) {
                                        ((ImageView) element
                                                .getChildAt(0))
                                                .setImageResource(R.drawable.door_opened);
                                    } else if (cmd[0].charAt(0) == 'r') {
                                        ((ImageView) element
                                                .getChildAt(0))
                                                .setImageResource(R.drawable.open_window);
                                    }
                                }
                            }
                        }
                    }
                    if (mainActivity.startingPosition == 2 && msg[0].charAt(0) == 'w') {
                        for (String value : msg) {
                            String[] cmd = value.split(":");
                            switch (cmd[1]) {
                                case "hot":
                                    TextView hot = mainActivity.findViewById(R.id.value_hot_water);
                                    hot.setText(cmd[2]);
                                    break;
                                case "cold":
                                    TextView cold = mainActivity.findViewById(R.id.value_cold_water);
                                    cold.setText(cmd[2]);
                                    break;
                                case "t1":
                                    TextView t1 = mainActivity.findViewById(R.id.value_electric_t1);
                                    t1.setText(cmd[2]);
                                    break;
                                case "t2":
                                    TextView t2 = mainActivity.findViewById(R.id.value_electric_t2);
                                    t2.setText(cmd[2]);
                                    break;
                                case "t3":
                                    TextView t3 = mainActivity.findViewById(R.id.value_electric_t3);
                                    t3.setText(cmd[2]);
                                    break;
                            }
                        }
                    }
                });
            }

            @Override
            public void onClose(int i, String s, boolean b) {
                Toast.makeText(mainActivity, "Connection closed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Exception e) {
//                Toast.makeText(MainActivity.this, "Connection error:"+e, Toast.LENGTH_SHORT).show();
            }
        };
        webSocketClient.connect();
    }

    protected static int getBckColor(char ch) {
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
        if (MainActivity.HOME_NETWORK) {
            try {
                Log.d("sendMessage", command);
                webSocketClient.send(command);
            } catch (Exception e) {
                Toast.makeText(MainActivity.getContext(), "No connection to server. Try to connect again", Toast.LENGTH_SHORT).show();
                createWebSocketClient((MainActivity) MainActivity.getContext());
            }
        }
    }

    public static void onClose() {
        webSocketClient.close();
    }
}
