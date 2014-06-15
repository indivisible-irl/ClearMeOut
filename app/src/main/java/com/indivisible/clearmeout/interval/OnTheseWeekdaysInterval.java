package com.indivisible.clearmeout.interval;

import android.content.Context;
import android.preference.EditTextPreference;
import android.preference.Preference;
import com.indivisible.clearmeout.R;
import com.indivisible.clearmeout.preferences.TimePreference;


public class OnTheseWeekdaysInterval
        implements Interval
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
    private static String DEFAULT_VALUE = "1001000";
    private static String DEFAULT_TIME = "02:00";
    private static final IntervalType intervalType = IntervalType.OnTheseWeekdays;

    //private static final String TAG = "EveryXMins";


    ///////////////////////////////////////////////////////
    ////    init
    ///////////////////////////////////////////////////////

    public OnTheseWeekdaysInterval(Context context)
    {
        initStrings(context);
    }

    private void initStrings(Context context)
    {
        daysKey = context.getString(R.string.pref_interval_onTheseWeekdays_days_key);
        daysTitle = context.getString(R.string.pref_interval_onTheseWeekdays_days_title);
        daysSummary = context.getString(R.string.pref_interval_onTheseWeekdays_days_text);
        timeKey = context.getString(R.string.pref_interval_onTheseWeekdays_time_key);
        timeTitle = context.getString(R.string.pref_interval_onTheseWeekdays_time_title);
        timeSummary = context.getString(R.string.pref_interval_onTheseWeekdays_time_text);
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
            prefs[0] = makeWeekdaysPreference(context);
            prefs[1] = makeTimePreference(context);
        }
        return prefs;
    }

    private EditTextPreference makeWeekdaysPreference(Context context)
    {
        EditTextPreference pref = new EditTextPreference(context);
        pref.setKey(daysKey);
        pref.setPersistent(true);
        pref.setTitle(daysTitle);
        pref.setDefaultValue(DEFAULT_VALUE);
        pref.setSummary(String.format(daysSummary, pref.getText()));
        return pref;
    }

    private TimePreference makeTimePreference(Context context)
    {
        TimePreference pref = new TimePreference(context);
        pref.setKey(timeKey);
        pref.setPersistent(true);
        pref.setTitle(timeTitle);
        pref.setDefaultValue(DEFAULT_TIME);
        pref.setSummary(String.format(timeSummary, pref.toString()));
        return pref;
    }

}
