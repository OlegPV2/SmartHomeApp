package com.oleg.smarthomedashboard.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.oleg.smarthomedashboard.R;

import java.util.ArrayList;

public class ScenarioAddNewGridAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final ArrayList<Integer> drawable;
    private final ArrayList<Integer> place;
    private final ArrayList<Integer> text;
    private final ArrayList<Boolean> isOn;

    private final ArrayList<Integer> ids;
    private final ArrayList<Integer> buttonTypes;

    View.OnClickListener listener;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_scenario_add_new_action_button, parent, false);

        return new ViewHolder(itemView);
    }

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

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {
            ViewHolder view = (ViewHolder) holder;
            view.imageView.setImageResource(drawable.get(position));
            view.placeView.setText(place.get(position));
            view.textView.setText(text.get(position));
            if (isOn.get(position)) {
                view.item.setBackground(
                        ResourcesCompat.getDrawable(
                                view.item.getResources(),
                                getBckColor(buttonTypes.get(position)),
                                view.item.getContext().getTheme()
                        )
                );
            }
            view.item.setTag(position);
            view.item.setOnClickListener(this.listener);
        }
    }

    @Override
    public int getItemCount() {
        return ids.size();
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

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView placeView;
        public TextView textView;

        public View item;

        public ViewHolder(View view) {
            super(view);
            item = view;
            imageView = view.findViewById(R.id.scenario_add_new_button_image);
            placeView = view.findViewById(R.id.scenario_add_new_button_place);
            textView = view.findViewById(R.id.scenario_add_new_button_text);
        }
    }
}
