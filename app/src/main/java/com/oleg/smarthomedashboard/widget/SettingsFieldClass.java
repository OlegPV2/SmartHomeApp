package com.oleg.smarthomedashboard.widget;

import android.annotation.SuppressLint;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.slider.Slider;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.oleg.smarthomedashboard.WSClient;
import com.oleg.smarthomedashboard.MainActivity;
import com.oleg.smarthomedashboard.R;
import com.oleg.smarthomedashboard.model.SettingsFieldType;

public class SettingsFieldClass {
	private final SettingsFieldType fieldType;
	private final int fieldNameId;
	private String switchId;
	private String sliderId;
	private String buttonIncreaseId;
	private String buttonDecreaseId;
	private String fieldTextId;
	private static boolean isSwitchListenerEnabled = true;
	private static boolean isSlideListenerEnabled = true;

	public SettingsFieldClass(int fieldType, int fieldNameId, String switchId, String sliderId) {
		this.fieldType = SettingsFieldType.values()[fieldType];
		this.fieldNameId = fieldNameId;
		this.switchId = switchId;
		this.sliderId = sliderId;
	}

	public SettingsFieldClass(int fieldType, int fieldNameId, String buttonDecreaseId, String fieldTextId, String buttonIncreaseId) {
		this.fieldType = SettingsFieldType.values()[fieldType];
		this.fieldNameId = fieldNameId;
		this.buttonDecreaseId = buttonDecreaseId;
		this.fieldTextId = fieldTextId;
		this.buttonIncreaseId = buttonIncreaseId;
	}

	public View getField() {
		View field = null;
		switch (fieldType) {
			case DIMMING:
				field = View.inflate(MainActivity.getContext(), R.layout.fragment_settings_card_element_dimming, null);
				TextView head = field.findViewById(R.id.settings_dimming_head);
				head.setText(fieldNameId);
				SwitchMaterial sw = field.findViewById(R.id.dimming_switch);
				sw.setTag(switchId);
				sw.setOnCheckedChangeListener(listener_switch);
				Slider sl = field.findViewById(R.id.dimming_slider);
				sl.setTag(sliderId);
				sl.addOnChangeListener(listener_slider);
				break;
			case TEMPERATURE:
				field = View.inflate(MainActivity.getContext(), R.layout.fragment_settings_card_element_temperature, null);
				TextView text = field.findViewById(R.id.settings_element_temperature_text);
				text.setTag(fieldTextId);
				ImageView btn = field.findViewById(R.id.settings_element_button_decrease);
				btn.setTag(buttonDecreaseId);
				setOnClick(btn, text);
				btn = field.findViewById(R.id.settings_element_button_increase);
				btn.setTag(buttonIncreaseId);
				setOnClick(btn, text);
				break;
			case METERS_CORRECTION:
				field = View.inflate(MainActivity.getContext(), R.layout.fragment_settings_card_element_meters_correction, null);
				TextView name = field.findViewById(R.id.settings_element_meters_text);
				name.setText(fieldNameId);
				EditText nameEdit = field.findViewById(R.id.settings_element_meters_value);
				nameEdit.setTag(fieldTextId);
				final String[] oldText = new String[1];
				nameEdit.setOnEditorActionListener((textView, i, keyEvent) -> {
					boolean handled = false;
					if (i == EditorInfo.IME_ACTION_SEND) {
						String[] cmd = textView.getTag().toString().split("_");
						WSClient.sendMessage("s:" + cmd[2] + ":" + textView.getText());
						oldText[0] = nameEdit.getText().toString();
						handled = true;
					}
					return handled;
				});
				nameEdit.setOnFocusChangeListener((v, hasFocus) -> {
					if (hasFocus) {
						oldText[0] = nameEdit.getText().toString();
					} else {
						nameEdit.setText(oldText[0]);
					}
				});
				ImageView incDec = field.findViewById(R.id.settings_element_meters_decrease);
				incDec.setTag(buttonDecreaseId);
				setOnClick(incDec, nameEdit);
				incDec = field.findViewById(R.id.settings_element_meters_increase);
				incDec.setTag(buttonIncreaseId);
				setOnClick(incDec, nameEdit);
				break;
		}
		return field;
	}

	private final CompoundButton.OnCheckedChangeListener listener_switch = (compoundButton, b) -> {
		if (isSwitchListenerEnabled) {
			String[] cmd = compoundButton.getTag().toString().split("_");
			Slider sl = compoundButton.getRootView().findViewWithTag("dimming_" + cmd[1] + "_slider");
			sl.setEnabled(b);
			WSClient.sendMessage("s:" + cmd[1] + ":dimming:" + (b ? 1 : 0));
		}
	};

	private final Slider.OnChangeListener listener_slider = (slider, value, fromUser) -> {
		if (isSlideListenerEnabled) {
			String[] cmd = slider.getTag().toString().split("_");
			WSClient.sendMessage("s:" + cmd[1] + ":dim-set:" + (200 - (int) slider.getValue()));
		}
	};

	@SuppressLint("NonConstantResourceId")
	private void setOnClick(final View view, final TextView textView) {
		view.setOnClickListener(view1 -> {
//			double val;
			String[] tag = view1.getTag().toString().split("_");
			if (view1.getResources().getString(R.string.settings_button_tag_increase).equals(tag[tag.length - 1])) {
				if (tag[2].equals("hot") || tag[2].equals("cold")) {
					double val = Double.parseDouble(textView.getText().toString());
					val += 0.01;
					textView.setText(String.valueOf(val));
				} else {
					int val = Integer.parseInt(textView.getText().toString());
					val++;
					textView.setText(String.valueOf(val));
				}
			} else if (view1.getResources().getString(R.string.settings_button_tag_decrease).equals(tag[tag.length - 1])) {
				if (tag[2].equals("hot") || tag[2].equals("cold")) {
					double val = Double.parseDouble(textView.getText().toString());
					val -= 0.01;
					textView.setText(String.valueOf(val));
				} else {
					int val = Integer.parseInt(textView.getText().toString());
					val--;
					textView.setText(String.valueOf(val));
				}
			}
			WSClient.sendMessage("s:" + tag[tag.length - 2] + ":" + textView.getText());
		});
	}

	public static void setSlideListenerEnabled(boolean slideListenerEnabled) {
		isSlideListenerEnabled = slideListenerEnabled;
	}

	public static void setSwitchListenerEnabled(boolean switchListenerEnabled) {
		isSwitchListenerEnabled = switchListenerEnabled;
	}
}
