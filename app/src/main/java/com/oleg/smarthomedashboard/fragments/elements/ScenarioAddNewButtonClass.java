package com.oleg.smarthomedashboard.fragments.elements;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.oleg.smarthomedashboard.MainActivity;
import com.oleg.smarthomedashboard.R;

public class ScenarioAddNewButtonClass {
    private final int buttonType;
    private final int buttonID;
    private final int buttonDrawable;
    private final int buttonTextId;
    private final int buttonPlaceId;
    private View.OnClickListener listener;

    public ScenarioAddNewButtonClass(int buttonID, int buttonType, int buttonDrawable, int buttonPlaceId, int buttonTextId) {
        this.buttonID = buttonID;
        this.buttonType = buttonType;
        this.buttonDrawable = buttonDrawable;
        this.buttonPlaceId = buttonPlaceId;
        this.buttonTextId = buttonTextId;
    }

    public int getButtonID() {
        return buttonID;
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
        if (ButtonTypes.values()[this.buttonType] == ButtonTypes.WARM_FLOOR ||
                ButtonTypes.values()[this.buttonType] == ButtonTypes.LIGHT ||
                ButtonTypes.values()[this.buttonType] == ButtonTypes.AUTO ||
                ButtonTypes.values()[this.buttonType] == ButtonTypes.FAN) {
            View button = View.inflate(MainActivity.getContext(), R.layout.fragment_scenario_add_new_action_button, null);
            String s1 = MainActivity.getContext().getResources().getResourceEntryName(this.buttonID);
            ImageView image = button.findViewById(R.id.scenario_add_new_button_image);
            if (s1.charAt(0) == 'w') {
//                int id = MainActivity.getContext().getResources().getIdentifier("wf", "drawable", MainActivity.getContext().getPackageName());
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
            button.setId(this.buttonID);
            if (listener != null) button.setOnClickListener(listener);
            return button;
        } else return null;
    }
}
