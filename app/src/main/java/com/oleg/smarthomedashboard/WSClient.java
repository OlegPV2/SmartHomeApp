package com.oleg.smarthomedashboard;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.ColorStateList;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.core.widget.ImageViewCompat;

import com.google.android.material.slider.Slider;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.oleg.smarthomedashboard.model.ConnectionState;
import com.oleg.smarthomedashboard.widget.SettingsFieldClass;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;

public class WSClient {

	protected static WebSocketClient webSocketClient;
	private static final Toast connectionWait = Toast.makeText(MainActivity.getContext(), R.string.websocket_no_connection_to_server, Toast.LENGTH_SHORT);
	private static final Toast connectionWrong = Toast.makeText(MainActivity.getContext(), R.string.websocket_error, Toast.LENGTH_SHORT);

	static void createWebSocketClient(final MainActivity mainActivity, String Url) {
		URI uri;
		try {
			uri = new URI(Url);
		} catch (URISyntaxException e) {
			Log.e("createWebSocketClient", String.valueOf(e));
			return;
		}

		webSocketClient = new WebSocketClient(uri) {
			@Override
			public void onOpen(ServerHandshake serverHandshake) {
				ConnectionState.WEBSOCKET_CONNECTED = true;
				connectionWait.cancel();
				connectionWrong.cancel();
//                sendMessage("Update");
			}

			@Override
			public void onMessage(String s) {
				mainActivity.runOnUiThread(() -> {
					String[] msg = s.split("\n");
					if (mainActivity.getStartingPosition() == 1 && msg[0].charAt(0) != 'w') {
						OnMessageForDashboard(mainActivity, msg);
					}
					if (mainActivity.getStartingPosition() == 2 && msg[0].charAt(0) == 'w') {
						OnMessageForMeters(mainActivity, msg);
					}
					if (mainActivity.getStartingPosition() == 4 /*&& msg[0].charAt(0) == 'w'*/) {
						OnMessageForSettings(mainActivity, msg);
					}
				});
			}

			@Override
			public void onClose(int i, String s, boolean b) {
				ConnectionState.WEBSOCKET_CONNECTED = false;
			}

			@Override
			public void onError(Exception e) {
				Log.e("WebSocket", "Connection error:" + e);
			}
		};
		webSocketClient.connect();
	}

	private static void OnMessageForDashboard(Activity activity, String[] message) {
		View mainView = activity.findViewById(R.id.main_fragment_container);
		for (String value : message) {
			String[] command = value.split(":");
			if (command[0].equals("th")) {
				command[1] = command[1].replace("-", "_");
				@SuppressLint("DiscouragedApi")
				TextView txt = mainView.findViewById(
						activity.getResources().getIdentifier(
								command[1],
								"id",
								activity.getPackageName()
						)
				);
				if (txt == null) continue;

				txt.setText(command[2]);
			} else {
				if (command[0].equals("rwc")) continue;
				if (command[0].equals("lux")) continue;
				ViewGroup element = mainView.findViewWithTag(command[0]);
				if (element == null) continue;

				if (command[1].equals("0")) {
					element.setBackground(null);
					if (command[0].equals("rentrance")) {
						((ImageView) element
								.getChildAt(0))
								.setImageResource(R.drawable.door_closed);
					} else if (command[0].charAt(0) == 'r') {
						((ImageView) element
								.getChildAt(0))
								.setImageResource(R.drawable.window_closed);
					} else if (!element.isClickable()) {
						element.setClickable(true);
						element.setFocusable(true);
						ImageView image = element.findViewById(R.id.image);
						ImageViewCompat.setImageTintList(image, null);
					}
				} else if (command[1].equals("-1")) {
					element.setClickable(false);
					element.setFocusable(false);
					int tint = ContextCompat.getColor(MainActivity.getContext(), R.color.button_not_clickable);
					ImageView image = element.findViewById(R.id.image);
					ImageViewCompat.setImageTintList(image, ColorStateList.valueOf(tint));
				} else {
					element.setBackgroundColor(activity.getColor(getBckColor(command[0].charAt(0))));

					if (command[0].equals("rentrance")) {
						((ImageView) element
								.getChildAt(0))
								.setImageResource(R.drawable.door_opened);
					} else if (command[0].charAt(0) == 'r') {
						((ImageView) element
								.getChildAt(0))
								.setImageResource(R.drawable.window_open);
					} else if (!element.isClickable()) {
						element.setClickable(true);
						element.setFocusable(true);
						ImageView image = element.findViewById(R.id.image);
						ImageViewCompat.setImageTintList(image, null);
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
		View mainView = activity.findViewById(R.id.settings_fragment_container);
		for (String value : message) {
			String[] command = value.split(":");
			Slider sl;
			switch (command[0]) {
				case "dimming":
					sl = mainView.findViewWithTag("dimming_" + command[1] + "_slider");
					if (sl == null) continue;
					if (Integer.parseInt(command[3]) != 255) {
						SettingsFieldClass.setSlideListenerEnabled(false);
						sl.setValue(200 - Integer.parseInt(command[3]));
						SettingsFieldClass.setSlideListenerEnabled(true);
					}
					break;
				case "meter":
					switch (command[1]) {
						case "hot":
						case "cold":
							TextView hot = mainView.findViewWithTag("value_water_" + command[1] + "_field");
							hot.setText(command[2]);
							break;
						case "t1":
						case "t2":
						case "t3":
							TextView t = mainView.findViewWithTag("value_electric_" + command[1] + "_field");
							t.setText(command[2]);
							break;
					}
					break;
				case "dim":
					SwitchMaterial sw1, sw2, sw3, sw4;
					sw1 = mainView.findViewWithTag("dimming_" + command[1] + "0_switch");
					sw2 = mainView.findViewWithTag("dimming_" + command[1] + "1_switch");
					sw3 = mainView.findViewWithTag("dimming_" + command[1] + "2_switch");
					sw4 = mainView.findViewWithTag("dimming_" + command[1] + "3_switch");
					if (sw1 != null) {
						SettingsFieldClass.setSwitchListenerEnabled(false);
						boolean state = ((Integer.parseInt(command[2])) & 1) == 1;
						sw1.setChecked(state);
						SettingsFieldClass.setSwitchListenerEnabled(true);
						sl = mainView.findViewWithTag("dimming_" + command[1] + "0_slider");
						sl.setEnabled(state);
					}
					if (sw2 != null) {
						SettingsFieldClass.setSwitchListenerEnabled(false);
						boolean state = ((Integer.parseInt(command[2]) >> 1) & 1) == 1;
						sw2.setChecked(state);
						SettingsFieldClass.setSwitchListenerEnabled(true);
						sl = mainView.findViewWithTag("dimming_" + command[1] + "1_slider");
						sl.setEnabled(state);
					}
					if (sw3 != null) {
						SettingsFieldClass.setSwitchListenerEnabled(false);
						boolean state = ((Integer.parseInt(command[2]) >> 2) & 1) == 1;
						sw3.setChecked(state);
						SettingsFieldClass.setSwitchListenerEnabled(true);
						sl = mainView.findViewWithTag("dimming_" + command[1] + "2_slider");
						sl.setEnabled(state);
					}
					if (sw4 != null) {
						SettingsFieldClass.setSwitchListenerEnabled(false);
						boolean state = ((Integer.parseInt(command[2]) >> 3) & 1) == 1;
						sw4.setChecked(state);
						SettingsFieldClass.setSwitchListenerEnabled(true);
						sl = mainView.findViewWithTag("dimming_" + command[1] + "3_slider");
						sl.setEnabled(state);
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
		String command = view.getTag() + ":";
		if (command.charAt(0) == 'l') {
			command += (view.getBackground() == null) ? "255" : "0";
		} else {
			command += (view.getBackground() == null) ? "0" : "1";
		}
		sendMessage(command);
	}

	public static void sendMessage(String command) {
		Log.e("sendMessage", "HOME_NETWORK:" + ConnectionState.HOME_NETWORK + ", WEBSOCKET_CONNECTED:" + ConnectionState.WEBSOCKET_CONNECTED + ", Command:" + command);
		if (ConnectionState.HOME_NETWORK && ConnectionState.WEBSOCKET_CONNECTED) {
			try {
				webSocketClient.send(command);
			} catch (Exception e) {
				connectionWrong.show();
//                createWebSocketClient((MainActivity) MainActivity.getContext());
			}
		} else if (!ConnectionState.HOME_NETWORK) {
			Toast.makeText(MainActivity.getContext(), R.string.websocket_not_home_network, Toast.LENGTH_SHORT).show();
		} else {
			connectionWait.show();
		}
	}

	public static void onClose() {
		if (ConnectionState.WEBSOCKET_CONNECTED) {
			webSocketClient.close();
		}
	}
}
