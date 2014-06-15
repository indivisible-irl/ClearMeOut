package com.indivisible.clearmeout.fragment;

import static com.indivisible.clearmeout.R.xml.pref_interval_daily;
import static com.indivisible.clearmeout.R.xml.pref_interval_everyxdays;
import static com.indivisible.clearmeout.R.xml.pref_interval_everyxhours;
import static com.indivisible.clearmeout.R.xml.pref_interval_everyxminutes;
import static com.indivisible.clearmeout.R.xml.pref_interval_onthesedates;
import static com.indivisible.clearmeout.R.xml.pref_interval_ontheseweekdays;
import static com.indivisible.clearmeout.R.xml.pref_interval_weekly;
import android.content.Context;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.PreferenceFragment;
import android.util.Log;
import com.indivisible.clearmeout.R;
import com.indivisible.clearmeout.data.IntervalType;
import com.indivisible.clearmeout.preferences.TimePreference;


public class IntervalChildPreferencesFragment
        extends PreferenceFragment
{

    ///////////////////////////////////////////////////////
    ////    data
    ///////////////////////////////////////////////////////

    private IntervalType intervalType = null;

    private EditTextPreference everyXMinutesPreference = null;
    private EditTextPreference everyXHoursPreference = null;
    private EditTextPreference everyXDaysDaysPreference = null;
    private TimePreference everyXDaysTimePreference = null;
    private TimePreference dailyTimePreference = null;
    private EditTextPreference weeklyDayPreference = null;
    private TimePreference weeklyTimePreference = null;
    private EditTextPreference onTheseWeekdaysDaysPreference = null;
    private TimePreference onTheseWeekdaysTimePreference = null;
    private EditTextPreference onTheseDatesDatesPreference = null;
    private TimePreference onTheseDatesTimePreference = null;

    private String everyXMinutesKey = null;
    private String everyXHoursKey = null;
    private String everyXDaysDaysKey = null;
    private String everyXDaysTimeKey = null;
    private String dailyTimeKey = null;
    private String weeklyDayKey = null;
    private String weeklyTimeKey = null;
    private String onTheseWeekdaysDaysKey = null;
    private String onTheseWeekdaysTimeKey = null;
    private String onTheseDatesDatesKey = null;
    private String onTheseDatesTimeKey = null;

    private static final String KEY_INTERVAL_EXTRA = "IntervalExtra";
    private static final String TAG = "IntChildPrefFrag";


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
        intervalType = (IntervalType) getArguments().getSerializable(KEY_INTERVAL_EXTRA);
        initStrings();
        loadPreferences();
    }

    private void initStrings()
    {
        Context cxt = getActivity();
        switch (intervalType)
        {
            case EveryXMinutes:
                everyXMinutesKey = cxt.getString(R.string.pref_interval_everyXMinutes_key);
                break;
            case EveryXHours:
                everyXHoursKey = cxt.getString(R.string.pref_interval_everyXHours_key);
                break;
            case EveryXDays:
                everyXDaysDaysKey = cxt.getString(R.string.pref_interval_everyXDays_days_key);
                everyXDaysTimeKey = cxt.getString(R.string.pref_interval_everyXDays_time_key);
                break;
            case Daily:
                dailyTimeKey = cxt.getString(R.string.pref_interval_daily_key);
                break;
            case Weekly:
                weeklyDayKey = cxt.getString(R.string.pref_interval_weekly_day_key);
                weeklyTimeKey = cxt.getString(R.string.pref_interval_weekly_time_key);
                break;
            case OnTheseWeekdays:
                onTheseWeekdaysDaysKey = cxt
                        .getString(R.string.pref_interval_onTheseWeekdays_days_key);
                onTheseWeekdaysTimeKey = cxt
                        .getString(R.string.pref_interval_onTheseWeekdays_time_key);
                break;
            case OnTheseDates:
                onTheseDatesDatesKey = cxt
                        .getString(R.string.pref_interval_onTheseDates_dates_key);
                onTheseDatesTimeKey = cxt
                        .getString(R.string.pref_interval_onTheseDates_time_key);
                break;
            case INVALID:
                Log.e(TAG, "initStrings: INVALID IntervalType");
                break;
            default:
                Log.e(TAG, "initStrings: Unhandled IntervalType: " + intervalType.name());
                break;
        }
    }

    public void loadPreferences()
    {
        Log.d(TAG, "loadPrefs: intervalType: " + intervalType.name());
        switch (intervalType)
        {
            case EveryXMinutes:
                addPreferencesFromResource(pref_interval_everyxminutes);
                everyXMinutesPreference = (EditTextPreference) findPreference(everyXMinutesKey);
                break;
            case EveryXHours:
                addPreferencesFromResource(pref_interval_everyxhours);
                everyXHoursPreference = (EditTextPreference) findPreference(everyXHoursKey);
                break;
            case EveryXDays:
                addPreferencesFromResource(pref_interval_everyxdays);
                everyXDaysDaysPreference = (EditTextPreference) findPreference(everyXDaysDaysKey);
                everyXDaysTimePreference = (TimePreference) findPreference(everyXDaysTimeKey);
                break;
            case Daily:
                addPreferencesFromResource(pref_interval_daily);
                dailyTimePreference = (TimePreference) findPreference(dailyTimeKey);
                break;
            case Weekly:
                addPreferencesFromResource(pref_interval_weekly);
                weeklyDayPreference = (EditTextPreference) findPreference(weeklyDayKey);
                weeklyTimePreference = (TimePreference) findPreference(weeklyTimeKey);
                break;
            case OnTheseWeekdays:
                addPreferencesFromResource(pref_interval_ontheseweekdays);
                onTheseWeekdaysDaysPreference = (EditTextPreference) findPreference(onTheseWeekdaysDaysKey);
                onTheseWeekdaysTimePreference = (TimePreference) findPreference(onTheseWeekdaysTimeKey);

                break;
            case OnTheseDates:
                addPreferencesFromResource(pref_interval_onthesedates);
                onTheseDatesDatesPreference = (EditTextPreference) findPreference(onTheseDatesDatesKey);
                onTheseDatesTimePreference = (TimePreference) findPreference(onTheseDatesTimeKey);
                break;
            case INVALID:
                Log.e(TAG, "loadPrefs: INVALID IntervalType");
                break;
            default:
                Log.e(TAG, "loadPrefs: Unhandled IntervalType: " + intervalType.name());
                break;
        }
    }
}
