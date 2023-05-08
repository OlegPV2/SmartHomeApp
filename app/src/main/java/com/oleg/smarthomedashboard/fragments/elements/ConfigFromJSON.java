package com.oleg.smarthomedashboard.fragments.elements;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

public class ConfigFromJSON {
    private final Activity activity;

    private final List<ConfigurationInfo> configurationInfoList;
    private final List<MetersInfo> metersInfoList;

    public ConfigFromJSON(Activity activity, String jsonUrl, List<ConfigurationInfo> configurationInfoList, List<MetersInfo> metersInfoList) {
        this.activity = activity;
        this.configurationInfoList = configurationInfoList;
        this.metersInfoList = metersInfoList;
        JSONObject jsonObject = loadJSONData(jsonUrl);
        if (jsonObject != null) parsingData(jsonObject);
    }

    private void parsingData(JSONObject jsonObject) {
        try {
            JSONArray jsonCardsArray = jsonObject.getJSONArray("card");
            ConfigurationInfo configurationInfo;
            for (int i = 0; i < jsonCardsArray.length(); i++) {
                JSONObject jsonCard = jsonCardsArray.getJSONObject(i);
                configurationInfo = new ConfigurationInfo(jsonCard.getString("titleTextID"),
                        jsonCard.getString("titleImageID"),
                        jsonCard.getBoolean("showTemperature"),
                        jsonCard.getString("titleTemperatureID"),
                        jsonCard.getBoolean("showHumidity"),
                        jsonCard.getString("titleHumidityID"),
                        getButton(jsonCard.getJSONObject("button1")),
                        getButton(jsonCard.getJSONObject("button2")),
                        getButton(jsonCard.getJSONObject("button3")),
                        getButton(jsonCard.getJSONObject("button4"))
                );
                configurationInfoList.add(configurationInfo);
            }
            jsonCardsArray = jsonObject.getJSONArray("meters");
            MetersInfo metersInfo;
            for (int i = 0; i < jsonCardsArray.length(); i++) {
                JSONObject jsonMeters = jsonCardsArray.getJSONObject(i);
                metersInfo = new MetersInfo(
                        jsonMeters.getString("titleTextID"),
                        jsonMeters.getString("meterCorrectionDecreaseID"),
                        jsonMeters.getString("meterCorrectionValueID"),
                        jsonMeters.getString("meterCorrectionIncreaseID")
                );
                metersInfoList.add(metersInfo);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private ConfigurationButtonsInfo getButton(JSONObject jsonObject) {
        ConfigurationButtonsInfo configurationButtonsInfo = null;
        try {
            switch (ButtonTypes.values()[jsonObject.getInt("buttonType")]) {
                case LIGHT:
                    configurationButtonsInfo = new ConfigurationButtonsInfo(
                            jsonObject.getInt("buttonType"),
                            jsonObject.getString("buttonID"),
                            jsonObject.getString("buttonDrawableID"),
                            jsonObject.getBoolean("buttonClickable"),
                            jsonObject.getBoolean("dimming"),
                            jsonObject.getString("dimmingSwitchID"),
                            jsonObject.getString("dimmingSliderID"),
                            jsonObject.getString("note")
                    );
                    break;
                case FAN:
                case AUTO:
                case DOOR:
                case WINDOW:
                case NOTHING:
                    configurationButtonsInfo = new ConfigurationButtonsInfo(
                            jsonObject.getInt("buttonType"),
                            jsonObject.getString("buttonID"),
                            jsonObject.getString("buttonDrawableID"),
                            jsonObject.getBoolean("buttonClickable"),
                            jsonObject.getString("note")
                    );
                    break;
                case WARM_FLOOR:
                    configurationButtonsInfo = new ConfigurationButtonsInfo(
                            jsonObject.getInt("buttonType"),
                            jsonObject.getString("buttonID"),
                            jsonObject.getString("buttonDrawableID"),
                            jsonObject.getBoolean("buttonClickable"),
                            jsonObject.getInt("warmFloorPreset"),
                            jsonObject.getString("warmFloorDecreaseID"),
                            jsonObject.getString("warmFloorTemperatureID"),
                            jsonObject.getString("warmFloorIncreaseID"),
                            jsonObject.getString("note")
                    );
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return configurationButtonsInfo;
    }

    private boolean isNetworkAvailable(Context ctx) {
        ConnectivityManager connectivityManager = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        Network nw = connectivityManager.getActiveNetwork();
        if (nw == null) return false;
        NetworkCapabilities actNw = connectivityManager.getNetworkCapabilities(nw);
        return actNw != null && (actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI));
    }

    private JSONObject loadJSONData(String jsonUrl) {
        try {
            StringBuilder sb = new StringBuilder();
            if (Objects.equals(jsonUrl, "") || !isNetworkAvailable(activity)) {
                sb.append(loadJSONFromAsset());
            } else {
                Log.d("ConfigFromJSON", "Load json");
                URL url = new URL(jsonUrl);
                InputStream is = url.openStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));

                int cp;
                while ((cp = bufferedReader.read()) != -1) {
                    sb.append((char) cp);
                }
                is.close();
            }
            return new JSONObject(sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private String loadJSONFromAsset() {
        String json;
        try {
            InputStream is = activity.getAssets().open("config.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}