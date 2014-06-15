package com.indivisible.clearmeout.interval;

import android.content.Context;
import android.preference.Preference;
import com.indivisible.clearmeout.R;
import com.indivisible.clearmeout.preferences.TimePreference;


public class DailyInterval
        implements Interval
{

    ///////////////////////////////////////////////////////
    ////    data
    ///////////////////////////////////////////////////////

    private Preference[] prefs = null;
    private String key;
    private String title;
    private String summary;
    private static String DEFAULT_TIME = "02:00";
    private static final IntervalType intervalType = IntervalType.Daily;

    //private static final String TAG = "Daily";


    ///////////////////////////////////////////////////////
    ////    init
    ///////////////////////////////////////////////////////

    public DailyInterval(Context context)
    {
        initStrings(context);
    }

    private void initStrings(Context context)
    {
        key = context.getString(R.string.pref_interval_daily_key);
        title = context.getString(R.string.pref_interval_daily_title);
        summary = context.getString(R.string.pref_interval_daily_text);
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
            Preference[] prefs = new Preference[1];
            prefs[0] = makePreference(context);
        }
        return prefs;
    }

    private TimePreference makePreference(Context context)
    {
        TimePreference pref = new TimePreference(context);
        pref.setKey(key);
        pref.setPersistent(true);
        pref.setTitle(title);
        pref.setDefaultValue(DEFAULT_TIME);
        pref.setSummary(String.format(summary, pref.toString()));
        return pref;
    }

}
