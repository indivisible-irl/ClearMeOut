package com.indivisible.clearmeout.interval;

import android.content.Context;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.text.InputType;
import com.indivisible.clearmeout.R;
import com.indivisible.clearmeout.preferences.TimePreference;


public class EveryXDaysInterval
{

    ///////////////////////////////////////////////////////
    ////    data
    ///////////////////////////////////////////////////////

    private Preference[] prefs = null;
    private String daysKey;
    private String daysTitle;
    private String daysSummary;
    private String timeKey;
    private String timeTitle;
    private String timeSummary;
    private static String DEFAULT_DAYS_VALUE = "30";
    private static String DEFAULT_TIME_VALUE = "02:00";
    private static final IntervalType intervalType = IntervalType.EveryXDays;

    //private static final String TAG = "EveryXMins";


    ///////////////////////////////////////////////////////
    ////    init
    ///////////////////////////////////////////////////////

    public EveryXDaysInterval(Context context)
    {
        initStrings(context);
    }

    private void initStrings(Context context)
    {
        daysKey = context.getString(R.string.pref_interval_everyXDays_days_key);
        daysTitle = context.getString(R.string.pref_interval_everyXDays_days_title);
        daysSummary = context.getString(R.string.pref_interval_everyXDays_days_text);
        timeKey = context.getString(R.string.pref_interval_everyXDays_time_key);
        timeTitle = context.getString(R.string.pref_interval_everyXDays_time_title);
        timeSummary = context.getString(R.string.pref_interval_everyXDays_time_text);
    }


    ///////////////////////////////////////////////////////
    ////    preferences
    ///////////////////////////////////////////////////////

    public IntervalType getType()
    {
        return intervalType;
    }

    public Preference[] getPreferences(Context context)
    {
        if (prefs == null)
        {
            Preference[] prefs = new Preference[2];
            prefs[0] = makeDaysPreference(context);
            prefs[1] = makeTimePreference(context);
        }
        return prefs;
    }

    private EditTextPreference makeDaysPreference(Context context)
    {
        EditTextPreference pref = new EditTextPreference(context);
        pref.setKey(daysKey);
        pref.setPersistent(true);
        pref.getEditText().setInputType(InputType.TYPE_CLASS_NUMBER);
        pref.setTitle(daysTitle);
        pref.setDefaultValue(DEFAULT_DAYS_VALUE);
        pref.setSummary(String.format(daysSummary, pref.getText()));
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
