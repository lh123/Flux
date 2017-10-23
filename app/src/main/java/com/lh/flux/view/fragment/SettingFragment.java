package com.lh.flux.view.fragment;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.widget.TimePicker;

import com.lh.flux.R;
import com.lh.flux.view.FluxActivity;

import java.util.Locale;

public class SettingFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.setting_aty);
        //((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        findPreference("theme").setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

            @Override
            public boolean onPreferenceChange(Preference p1, Object p2) {
                Intent i = new Intent();
                i.setClass(getActivity().getApplication(), FluxActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                return true;
            }
        });
        findPreference("default_time").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(final Preference preference) {
                if(preference.getKey().equals("default_time")){
                    String time = preference.getSharedPreferences().getString(preference.getKey(),"12:00");
                    String times[] = time.split(":");
                    TimePickerDialog dialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            String newTime = String.format(Locale.getDefault(),"%02d:%02d",hourOfDay,minute);
                            preference.getSharedPreferences().edit().putString(preference.getKey(),newTime).apply();
                            System.out.println("time :" + newTime);
                        }
                    },Integer.parseInt(times[0]),Integer.parseInt(times[1]),true);
                    dialog.show();
                    return true;
                }
                return false;
            }
        });
    }

}
