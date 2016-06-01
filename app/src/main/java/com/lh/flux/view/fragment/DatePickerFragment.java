package com.lh.flux.view.fragment;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.TimePicker;

import com.lh.flux.R;
import com.lh.flux.crash.LogUtil;
import com.lh.flux.domain.utils.ThemeUtil;
import com.lh.flux.service.WelfareService;
import com.lh.flux.view.FluxActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DatePickerFragment extends DialogFragment {
    public static final String TAG = "DatePickerFragment";

    //private DatePickerDialog mDialog;
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final SharedPreferences sp = getActivity().getSharedPreferences("auto_grab", Context.MODE_PRIVATE);
        final long advanceTime = Long.parseLong(PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext()).getString("advance_time", "3")) * 60 * 1000;
        final Calendar ca = Calendar.getInstance();
        ca.setTimeInMillis(System.currentTimeMillis());
        int theme = ThemeUtil.getInstance().getCurrentTheme() == 0 ? R.style.DialogBlue : R.style.DialogRed;
        return new TimePickerDialog(getActivity(), theme, new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker p1, int p2, int p3) {
                AlarmManager am = (AlarmManager) getActivity().getApplicationContext().getSystemService(Context.ALARM_SERVICE);
                ca.set(Calendar.HOUR_OF_DAY, p2);
                ca.set(Calendar.MINUTE, p3);
                ca.set(Calendar.SECOND, 0);
                ca.set(Calendar.MILLISECOND, 0);
                if (ca.getTimeInMillis() <= System.currentTimeMillis()) {
                    ((FluxActivity) getActivity()).showToast("时间设定为明天");
                    ca.add(Calendar.DAY_OF_MONTH, 1);
                }
                Intent si = new Intent();
                si.putExtra("mode", WelfareService.START_GRAB_DELY);
                si.putExtra("act", ca.getTimeInMillis());
                si.setClass(getActivity().getApplicationContext(), WelfareService.class);
                PendingIntent pi = PendingIntent.getService(getActivity().getApplicationContext(), 0, si, PendingIntent.FLAG_UPDATE_CURRENT);
                am.cancel(pi);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, ca.getTimeInMillis() - advanceTime, pi);
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    am.setExact(AlarmManager.RTC_WAKEUP, ca.getTimeInMillis() - advanceTime, pi);
                } else {
                    am.set(AlarmManager.RTC_WAKEUP, ca.getTimeInMillis() - advanceTime, pi);
                }
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                ((FluxActivity) getActivity()).setWelfareServiceStatus("自动抢红包:" + df.format(ca.getTime()), false);
                LogUtil.getInstance().logE("FluxPresenter", "auto grab " + df.format(ca.getTime()));
                sp.edit().putString("time", "自动抢红包:" + df.format(ca.getTime())).apply();
            }
        }, ca.get(Calendar.HOUR_OF_DAY), ca.get(Calendar.MINUTE), true);
    }

}
