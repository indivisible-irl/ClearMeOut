package com.indivisible.clearmeout.interval;

import android.content.Context;
import android.preference.EditTextPreference;
import android.preference.Preference;
import com.indivisible.clearmeout.R;
import com.indivisible.clearmeout.preferences.TimePreference;


public class OnTheseDatesInterval
        implements Interval
{

    ///////////////////////////////////////////////////////
    ////    data
    ///////////////////////////////////////////////////////

    private Preference[] prefs = null;
    private String datesKey;
    private String datesTitle;
    private String datesSummary;
    private String timeKey;
    private String timeTitle;
    private String timeSummary;
    private static String DEFAULT_DATES_VALUE = "1,15";
    private static String DEFAULT_TIME_VALUE = "02:00";
    private static final IntervalType intervalType = IntervalType.EveryXDays;

    //private static final String TAG = "EveryXMins";


    ///////////////////////////////////////////////////////
    ////    init
    ///////////////////////////////////////////////////////

    public OnTheseDatesInterval(Context context)
    {
        initStrings(context);
    }

    private void initStrings(Context context)
    {
        datesKey = context.getString(R.string.pref_interval_onTheseDates_dates_key);
        datesTitle = context.getString(R.string.pref_interval_onTheseDates_dates_title);
        datesSummary = context.getString(R.string.pref_interval_onTheseDates_dates_text);
        timeKey = context.getString(R.string.pref_interval_onTheseDates_time_key);
        timeTitle = context.getString(R.string.pref_interval_onTheseDates_time_title);
        timeSummary = context.getString(R.string.pref_interval_onTheseDates_time_text);
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
            prefs[0] = makeDaysPreference(context);
            prefs[1] = makeTimePreference(context);
        }
        return prefs;
    }

    private EditTextPreference makeDaysPreference(Context context)
    {
        EditTextPreference pref = new EditTextPreference(context);
        pref.setKey(datesKey);
        pref.setPersistent(true);
        pref.setTitle(datesTitle);
        pref.setDefaultValue(DEFAULT_DATES_VALUE);
        pref.setSummary(String.format(datesSummary, pref.getText()));
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
