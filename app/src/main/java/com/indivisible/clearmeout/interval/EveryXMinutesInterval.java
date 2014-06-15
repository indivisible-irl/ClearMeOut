package com.indivisible.clearmeout.interval;

import android.content.Context;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.text.InputType;
import com.indivisible.clearmeout.R;


public class EveryXMinutesInterval
        implements Interval
{

    ///////////////////////////////////////////////////////
    ////    data
    ///////////////////////////////////////////////////////

    private Preference[] prefs = null;
    private String key;
    private String title;
    private String summary;
    private static String DEFAULT_VALUE = "30";
    private static final IntervalType intervalType = IntervalType.EveryXMinutes;

    //private static final String TAG = "EveryXMins";


    ///////////////////////////////////////////////////////
    ////    init
    ///////////////////////////////////////////////////////

    public EveryXMinutesInterval(Context context)
    {
        initStrings(context);
    }

    private void initStrings(Context context)
    {
        key = context.getString(R.string.pref_interval_everyXMinutes_key);
        title = context.getString(R.string.pref_interval_everyXMinutes_title);
        summary = context.getString(R.string.pref_interval_everyXMinutes_text);
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

    private EditTextPreference makePreference(Context context)
    {
        EditTextPreference pref = new EditTextPreference(context);
        pref.setKey(key);
        pref.setPersistent(true);
        pref.getEditText().setInputType(InputType.TYPE_CLASS_NUMBER);
        pref.setTitle(title);
        pref.setDefaultValue(DEFAULT_VALUE);
        pref.setSummary(String.format(summary, pref.getText()));
        return pref;
    }

}
