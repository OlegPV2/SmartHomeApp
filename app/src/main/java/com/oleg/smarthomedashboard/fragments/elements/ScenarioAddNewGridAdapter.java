package com.oleg.smarthomedashboard.fragments.elements;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;

import com.oleg.smarthomedashboard.MainActivity;
import com.oleg.smarthomedashboard.R;

import java.util.ArrayList;

public class ScenarioAddNewGridAdapter extends BaseAdapter {
    private final ArrayList<Integer> drawable;
    private final ArrayList<Integer> place;
    private final ArrayList<Integer> text;
    private final ArrayList<Boolean> isOn;

    private final ArrayList<Integer> ids;
    private final ArrayList<Integer> buttonTypes;

    View.OnClickListener listener;

    public ScenarioAddNewGridAdapter(ArrayList<Integer> drawable, ArrayList<Integer> place, ArrayList<Integer> text, ArrayList<Boolean> isOn, ArrayList<Integer> ids, ArrayList<Integer> buttonTypes) {
        this.drawable = drawable;
        this.place = place;
        this.text = text;
        this.isOn = isOn;
        this.ids = ids;
        this.buttonTypes = buttonTypes;
    }

    public void setListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    public int getIds(int position) {
        return ids.get(position);
    }

    @Override
    public int getCount() {
        return ids.size();
    }

    @Override
    public Object getItem(int position) {
        return drawable.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        View item;
        LayoutInflater layoutInflater = (LayoutInflater) MainActivity.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (view == null) {
            item = layoutInflater.inflate(R.layout.fragment_scenario_add_new_action_button, null);
            ImageView imageView = item.findViewById(R.id.scenario_add_new_button_image);
            TextView placeView = item.findViewById(R.id.scenario_add_new_button_place);
            TextView textView = item.findViewById(R.id.scenario_add_new_button_text);
            imageView.setImageResource(drawable.get(position));
            placeView.setText(place.get(position));
            textView.setText(text.get(position));
            if (isOn.get(position)) {
                item.setBackground(
                        ResourcesCompat.getDrawable(
                                item.getResources(),
                                getBckColor(buttonTypes.get(position)),
                                item.getContext().getTheme()
                        )
                );
            }
        } else {
            item = view;
        }
        item.setTag(position);
        item.setOnClickListener(this.listener);
        return item;
    }

    private int getBckColor(int buttonType) {
        switch (buttonType) {
            case 1:
                return R.drawable.rounded_corners_yellow;
            case 4:
                return R.drawable.rounded_corners_amber;
            case 2:
                return R.drawable.rounded_corners_green;
            case 3:
                return R.drawable.rounded_corners_blue;
        }
        return R.color.error;
    }
}
