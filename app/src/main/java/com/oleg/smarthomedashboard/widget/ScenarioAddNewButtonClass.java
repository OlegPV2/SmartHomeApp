package com.oleg.smarthomedashboard.widget;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.oleg.smarthomedashboard.MainActivity;
import com.oleg.smarthomedashboard.R;
import com.oleg.smarthomedashboard.model.ButtonType;

public class ScenarioAddNewButtonClass {
    private final int buttonType;
    private final String buttonID;
    private final int buttonDrawable;
    private final int buttonTextId;
    private final int buttonPlaceId;
    private View.OnClickListener listener;

    public ScenarioAddNewButtonClass(String buttonID, int buttonType, int buttonDrawable, int buttonPlaceId, int buttonTextId) {
        this.buttonID = buttonID;
        this.buttonType = buttonType;
        this.buttonDrawable = buttonDrawable;
        this.buttonPlaceId = buttonPlaceId;
        this.buttonTextId = buttonTextId;
    }

    public String getButtonID() {
        return buttonID;
    }

    public int getButtonTextId() {
        return buttonTextId;
    }

    public int getButtonPlaceId() {
        return buttonPlaceId;
    }

    public int getButtonType() {
        return this.buttonType;
    }

    public int getButtonDrawable() {
        return buttonDrawable;
    }

    public void SetOnItemClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    public View getButton(boolean showText) {
        if (ButtonType.values()[this.buttonType] == ButtonType.WARM_FLOOR ||
                ButtonType.values()[this.buttonType] == ButtonType.LIGHT ||
                ButtonType.values()[this.buttonType] == ButtonType.AUTO ||
                ButtonType.values()[this.buttonType] == ButtonType.FAN) {
            View button = View.inflate(MainActivity.getContext(), R.layout.fragment_scenario_add_new_action_button, null);
            ImageView image = button.findViewById(R.id.scenario_add_new_button_image);
            if (ButtonType.values()[this.buttonType] == ButtonType.WARM_FLOOR) {
                image.setImageResource(R.drawable.wf);
                button.setTag(R.drawable.wf);
            } else {
                image.setImageResource(buttonDrawable);
                button.setTag(this.buttonDrawable);
            }
            TextView place = button.findViewById(R.id.scenario_add_new_button_place);
            TextView text = button.findViewById(R.id.scenario_add_new_button_text);
            if (showText) {
                place.setText(buttonPlaceId);
                text.setText(buttonTextId);
            } else {
                place.setVisibility(View.GONE);
                text.setVisibility(View.GONE);
            }
            button.setTag(this.buttonID);
            if (listener != null) button.setOnClickListener(listener);
            return button;
        } else return null;
    }
}
