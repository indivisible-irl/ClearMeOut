package com.indivisible.clearmeout.interval;

import android.content.Context;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.text.InputType;
import com.indivisible.clearmeout.R;
import com.indivisible.clearmeout.preferences.TimePreference;


public class WeeklyInterval
        implements Interval
{

    ///////////////////////////////////////////////////////
    ////    data
    ///////////////////////////////////////////////////////

    private Preference[] prefs = null;
    private String weekdayKey;
    private String weekdayTitle;
    private String weekdaySummary;
    private String timeKey;
    private String timeTitle;
    private String timeSummary;
    private static String DEFAULT_WEEKDAY_VALUE = "0";
    private static String DEFAULT_TIME_VALUE = "02:00";
    private static final IntervalType intervalType = IntervalType.Weekly;

    //private static final String TAG = "Weekly";


    ///////////////////////////////////////////////////////
    ////    init
    ///////////////////////////////////////////////////////

    public WeeklyInterval(Context context)
    {
        initStrings(context);
    }

    private void initStrings(Context context)
    {
        weekdayKey = context.getString(R.string.pref_interval_weekly_day_key);
        weekdayTitle = context.getString(R.string.pref_interval_weekly_day_title);
        weekdaySummary = context.getString(R.string.pref_interval_weekly_day_text);
        timeKey = context.getString(R.string.pref_interval_weekly_time_key);
        timeTitle = context.getString(R.string.pref_interval_weekly_time_title);
        timeSummary = context.getString(R.string.pref_interval_weekly_time_text);
    }


    ///////////////////////////////////////////////////////
    ////    preferences
    ///////////////////////////////////////////////////////

    @Override
    public IntervalType getType()
    {
        return intervalType;
    }

    @Override
    public Preference[] getPreferences(Context context)
    {
        if (prefs == null)
        {
            Preference[] prefs = new Preference[2];
            prefs[0] = makeWeekdayPreference(context);
            prefs[1] = makeTimePreference(context);
        }
        return prefs;
    }

    private EditTextPreference makeWeekdayPreference(Context context)
    {
        EditTextPreference pref = new EditTextPreference(context);
        pref.setKey(weekdayKey);
        pref.setPersistent(true);
        pref.getEditText().setInputType(InputType.TYPE_CLASS_NUMBER);
        pref.setTitle(weekdayTitle);
        pref.setDefaultValue(DEFAULT_WEEKDAY_VALUE);
        pref.setSummary(String.format(weekdaySummary, pref.getText()));
        return pref;
    }

    private TimePreference makeTimePreference(Context context)
    {
        TimePreference pref = new TimePreference(context);
        pref.setKey(timeKey);
        pref.setPersistent(true);
        pref.setTitle(timeTitle);
        pref.setDefaultValue(DEFAULT_TIME_VALUE);
        pref.setSummary(String.format(timeSummary, pref.toString()));
        return pref;
    }
}
