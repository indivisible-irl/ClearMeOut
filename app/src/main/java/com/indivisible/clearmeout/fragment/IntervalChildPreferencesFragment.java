package com.indivisible.clearmeout.fragment;

import android.content.Context;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.PreferenceFragment;
import android.util.Log;
import com.indivisible.clearmeout.R;
import com.indivisible.clearmeout.interval.IntervalType;
import com.indivisible.clearmeout.preferences.NumberPreference;
import com.indivisible.clearmeout.preferences.TimePreference;


public class IntervalChildPreferencesFragment
        extends PreferenceFragment
{

    ///////////////////////////////////////////////////////
    ////    data
    ///////////////////////////////////////////////////////

    private NumberPreference everyXMinutesPreference;
    private EditTextPreference everyXHoursPreference;
    private EditTextPreference everyXDaysDaysPreference;
    private TimePreference everyXDaysTimePreference;
    private TimePreference dailyTimePreference;
    private EditTextPreference weeklyDayPreference;
    private TimePreference weeklyTimePreference;
    private EditTextPreference onTheseWeekdaysDaysPreference;
    private TimePreference onTheseWeekdaysTimePreference;
    private EditTextPreference onTheseDatesDatesPreference;
    private TimePreference onTheseDatesTimePreference;

    private String everyXMinutesKey;
    private String everyXHoursKey;
    private String everyXDaysDaysKey;
    private String everyXDaysTimeKey;
    private String dailyTimeKey;
    private String weeklyDayKey;
    private String weeklyTimeKey;
    private String onTheseWeekdaysDaysKey;
    private String onTheseWeekdaysTimeKey;
    private String onTheseDatesDatesKey;
    private String onTheseDatesTimeKey;

    private static final String KEY_INTERVAL_EXTRA = "IntervalExtra";
    private static final String TAG = "IntervalChildPrefFrag";


    ///////////////////////////////////////////////////////
    ////    init
    ///////////////////////////////////////////////////////

    public static final IntervalChildPreferencesFragment
            newInstance(IntervalType intervalType)
    {
        IntervalChildPreferencesFragment fragment = new IntervalChildPreferencesFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_INTERVAL_EXTRA, intervalType);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        initStrings();
        initPreferences();
    }

    private void initStrings()
    {
        Context cxt = getActivity();
        everyXMinutesKey = cxt.getString(R.string.pref_interval_everyXMinutes_key);
        everyXHoursKey = cxt.getString(R.string.pref_interval_everyXHours_key);
        everyXDaysDaysKey = cxt.getString(R.string.pref_interval_everyXDays_days_key);
        everyXDaysTimeKey = cxt.getString(R.string.pref_interval_everyXDays_time_key);
        dailyTimeKey = cxt.getString(R.string.pref_interval_daily_key);
        weeklyDayKey = cxt.getString(R.string.pref_interval_weekly_day_key);
        weeklyTimeKey = cxt.getString(R.string.pref_interval_weekly_time_key);
        onTheseWeekdaysDaysKey = cxt
                .getString(R.string.pref_interval_onTheseWeekdays_days_key);
        onTheseWeekdaysTimeKey = cxt
                .getString(R.string.pref_interval_onTheseWeekdays_time_key);
        onTheseDatesDatesKey = cxt.getString(R.string.pref_interval_onTheseDates_dates_key);
        onTheseDatesTimeKey = cxt.getString(R.string.pref_interval_onTheseDates_time_key);
    }

    private void initPreferences()
    {
        IntervalType intervalType = (IntervalType) getArguments()
                .getSerializable(KEY_INTERVAL_EXTRA);
        Log.d(TAG, "initPrefs: intervalType: " + intervalType.name());
        loadPreferences(intervalType);
    }

    public void loadPreferences(IntervalType intervalType)
    {
        Log.d(TAG, "loadPrefs: intervalType: " + intervalType.name());
        try
        {
            this.getPreferenceScreen().removeAll();
        }
        catch (NullPointerException e)
        {}
        switch (intervalType)
        {
            case EveryXMinutes:
                addPreferencesFromResource(R.xml.pref_interval_everyxminutes);
                if (everyXMinutesPreference == null)
                {
                    everyXMinutesPreference = (NumberPreference) findPreference(everyXMinutesKey);
                }
                break;
            case EveryXHours:
                addPreferencesFromResource(R.xml.pref_interval_everyxhours);
                if (everyXHoursPreference == null)
                {
                    everyXHoursPreference = (EditTextPreference) findPreference(everyXHoursKey);
                }
                break;
            case EveryXDays:
                addPreferencesFromResource(R.xml.pref_interval_everyxdays);
                if (everyXDaysDaysPreference == null)
                {
                    everyXDaysDaysPreference = (EditTextPreference) findPreference(everyXDaysDaysKey);
                }
                if (everyXDaysTimePreference == null)
                {
                    everyXDaysTimePreference = (TimePreference) findPreference(everyXDaysTimeKey);
                }
                break;
            case Daily:
                addPreferencesFromResource(R.xml.pref_interval_daily);
                if (dailyTimePreference == null)
                {
                    dailyTimePreference = (TimePreference) findPreference(dailyTimeKey);
                }
                break;
            case Weekly:
                addPreferencesFromResource(R.xml.pref_interval_weekly);
                if (weeklyDayPreference == null)
                {
                    weeklyDayPreference = (EditTextPreference) findPreference(weeklyDayKey);
                }
                if (weeklyTimePreference == null)
                {
                    weeklyTimePreference = (TimePreference) findPreference(weeklyTimeKey);
                }
                break;
            case OnTheseWeekdays:
                addPreferencesFromResource(R.xml.pref_interval_ontheseweekdays);
                if (onTheseWeekdaysDaysPreference == null)
                {
                    onTheseWeekdaysDaysPreference = (EditTextPreference) findPreference(onTheseWeekdaysDaysKey);
                }
                if (onTheseWeekdaysTimePreference == null)
                {
                    onTheseWeekdaysTimePreference = (TimePreference) findPreference(onTheseWeekdaysTimeKey);
                }
                break;
            case OnTheseDates:
                addPreferencesFromResource(R.xml.pref_interval_onthesedates);
                if (onTheseDatesDatesPreference == null)
                {
                    onTheseDatesDatesPreference = (EditTextPreference) findPreference(onTheseDatesDatesKey);
                }
                if (onTheseDatesTimePreference == null)
                {
                    onTheseDatesTimePreference = (TimePreference) findPreference(onTheseDatesTimeKey);
                }
                break;
            default:
                Log.e(TAG, "loadPrefs: Unhandled IntervalType: " + intervalType.name());
                break;
        }
    }


}
