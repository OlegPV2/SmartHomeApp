package com.oleg.smarthomedashboard.fragments.elements;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.widget.ImageViewCompat;

import com.oleg.smarthomedashboard.CreateWebSocketClient;
import com.oleg.smarthomedashboard.MainActivity;
import com.oleg.smarthomedashboard.R;

public class DashboardButtonClass {
    Context context;
    private final int buttonId;
    private final int buttonDrawableOrTextId;
    private final DashboardButtonTypes buttonType;
    private final boolean buttonClickable;

    public DashboardButtonClass(Context context, int buttonType, int buttonId, int buttonDrawableOrTextId, boolean buttonTouchable) {
        this.context = context;
        this.buttonType = DashboardButtonTypes.values()[buttonType];
        this.buttonId = buttonId;
        this.buttonDrawableOrTextId = buttonDrawableOrTextId;
        this.buttonClickable = buttonTouchable;
    }

    @SuppressLint("ResourceAsColor")
    public View getButton() {
        View button;
        if (buttonType == DashboardButtonTypes.NOTHING) return null;
        if (buttonType == DashboardButtonTypes.WARM_FLOOR) {
            button = View.inflate(context, R.layout.fragment_dashboard_image_button_with_text, null);
            button.setId(buttonId);
            TextView text = button.findViewById(R.id.temp);
            text.setId(buttonDrawableOrTextId);
            if (buttonId == R.id.w2) {
                TextView textMark = button.findViewById(R.id.temp_mark);
                textMark.setText("");
            }
        } else {
            button = View.inflate(context, R.layout.fragment_dashboard_image_button, null);
            button.setId(buttonId);
            ImageView image = button.findViewById(R.id.image);
            image.setImageResource(buttonDrawableOrTextId);
            if (!buttonClickable) {
                button.setClickable(false);
                button.setFocusable(false);
                int tint = ContextCompat.getColor(context, R.color.button_not_clickable);
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
//        if (CreateWebSocketClient.getReadyState() != ReadyState.OPEN) {
        if (view.getBackground() == null) {
            String t = view.getResources().getResourceEntryName(view.getId());
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
            if (view.getResources().getResourceEntryName(view.getId()).charAt(0) == 'w') {
                View v = ((ViewGroup) view).getChildAt(1);
                View tv = ((LinearLayout) v).getChildAt(0);
                ((TextView) tv).setTextColor(view.getResources().getColor(R.color.text_low, view.getContext().getTheme()));
                tv = ((LinearLayout) v).getChildAt(1);
                ((TextView) tv).setTextColor(view.getResources().getColor(R.color.text_low, view.getContext().getTheme()));
            }
        }
//        }
        CreateWebSocketClient.sendMessage(view);
    };

}