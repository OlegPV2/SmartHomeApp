package com.oleg.smarthomedashboard.widget;

import android.content.res.ColorStateList;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.widget.ImageViewCompat;

import com.oleg.smarthomedashboard.WSClient;
import com.oleg.smarthomedashboard.MainActivity;
import com.oleg.smarthomedashboard.R;
import com.oleg.smarthomedashboard.model.ButtonType;

import java.util.Objects;

public class DashboardButtonClass {
    private final String buttonId;
    private final int buttonDrawableOrTextId;
    private final ButtonType buttonType;
    private final boolean buttonClickable;

    public DashboardButtonClass(int buttonType, String buttonId, int buttonDrawableOrTextId, boolean buttonTouchable) {
        this.buttonType = ButtonType.values()[buttonType];
        this.buttonId = buttonId;
        this.buttonDrawableOrTextId = buttonDrawableOrTextId;
        this.buttonClickable = buttonTouchable;
    }

    public View getButton() {
        View button;
        if (buttonType == ButtonType.NOTHING) return null;
        if (buttonType == ButtonType.WARM_FLOOR) {
            button = View.inflate(MainActivity.getContext(), R.layout.fragment_dashboard_image_button_with_text, null);
            button.setTag(buttonId);
            TextView text = button.findViewById(R.id.temp);
            text.setId(buttonDrawableOrTextId);
            if (Objects.equals(buttonId, "w2")) {
                TextView textMark = button.findViewById(R.id.temp_mark);
                textMark.setText("");
            }
        } else {
            button = View.inflate(MainActivity.getContext(), R.layout.fragment_dashboard_image_button, null);
            button.setTag(buttonId);
            ImageView image = button.findViewById(R.id.image);
            image.setImageResource(buttonDrawableOrTextId);
            if (!buttonClickable) {
                button.setClickable(false);
                button.setFocusable(false);
                int tint = ContextCompat.getColor(MainActivity.getContext(), R.color.button_not_clickable);
                ImageViewCompat.setImageTintList(image, ColorStateList.valueOf(tint));
            }
        }
        if (buttonClickable) button.setOnClickListener(listener);
        return button;
    }

    private int getBckColor(String s) {
        switch (s.charAt(0)) {
            case 'l':
            case 'b':
            case 't':
                return R.drawable.rounded_corners_yellow;
            case 'w':
                return R.drawable.rounded_corners_amber;
            case 'a':
                return R.drawable.rounded_corners_green;
            case 'f':
                return R.drawable.rounded_corners_blue;
        }
        return R.color.error;
    }

    private final View.OnClickListener listener = view -> {
        if (view.getBackground() == null) {
            String t = view.getTag().toString();
            view.setBackground(ResourcesCompat.getDrawable(view.getResources(), getBckColor(t), view.getContext().getTheme()));
            if (t.charAt(0) == 'w') {
                View v = ((ViewGroup) view).getChildAt(1);
                View tv = ((LinearLayout) v).getChildAt(0);
                ((TextView) tv).setTextColor(view.getResources().getColor(R.color.white, view.getContext().getTheme()));
                tv = ((LinearLayout) v).getChildAt(1);
                ((TextView) tv).setTextColor(view.getResources().getColor(R.color.white, view.getContext().getTheme()));
            }
        } else {
            view.setBackground(null);
            if (view.getTag().toString().charAt(0) == 'w') {
                View v = ((ViewGroup) view).getChildAt(1);
                View tv = ((LinearLayout) v).getChildAt(0);
                ((TextView) tv).setTextColor(view.getResources().getColor(R.color.text_low, view.getContext().getTheme()));
                tv = ((LinearLayout) v).getChildAt(1);
                ((TextView) tv).setTextColor(view.getResources().getColor(R.color.text_low, view.getContext().getTheme()));
            }
        }
        WSClient.sendMessage(view);
    };

}
