package com.indivisible.clearmeout.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceFragment;
import android.util.Log;
import com.indivisible.clearmeout.R;
import com.indivisible.clearmeout.activity.IntervalsPreferenceActivity;


public class ServicePreferenceFragment
        extends PreferenceFragment
        implements OnPreferenceClickListener
{

    ///////////////////////////////////////////////////////
    ////    data
    ///////////////////////////////////////////////////////

    private CheckBoxPreference isActivePreference;
    private CheckBoxPreference notifyOnDeletePreference;
    private Preference intervalsIntentPreference;

    private String isActivePrefKey;
    private String notifyOnDeletePrefKey;
    private String intervalsIntentPrefKey;

    private static final String TAG = "ServicePrefFrag";


    ///////////////////////////////////////////////////////
    ////    init
    ///////////////////////////////////////////////////////

    public static final ServicePreferenceFragment newInstance()
    {
        return new ServicePreferenceFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences_service);
        initStrings();
        initPreferences();
    }

    private void initStrings()
    {
        Context cxt = getActivity();
        isActivePrefKey = cxt.getString(R.string.pref_service_isActive_key);
        notifyOnDeletePrefKey = cxt.getString(R.string.pref_service_notifyOnDelete_key);
        intervalsIntentPrefKey = cxt.getString(R.string.pref_service_intent_intervals_key);
    }

    private void initPreferences()
    {
        isActivePreference = (CheckBoxPreference) findPreference(isActivePrefKey);
        notifyOnDeletePreference = (CheckBoxPreference) findPreference(notifyOnDeletePrefKey);
        intervalsIntentPreference = findPreference(intervalsIntentPrefKey);
        intervalsIntentPreference.setOnPreferenceClickListener(this);
    }


    ///////////////////////////////////////////////////////
    ////    override
    ///////////////////////////////////////////////////////

    @Override
    public boolean onPreferenceClick(Preference preference)
    {
        String key = preference.getKey();
        if (key.equals(intervalsIntentPrefKey))
        {
            Log.v(TAG, "intervalsIntentPref clicked");
            Intent intervalsIntent = new Intent(getActivity(),
                    IntervalsPreferenceActivity.class);
            startActivity(intervalsIntent);
            return true;
        }
        else
        {
            Log.w(TAG, "unhandled pref click: " + key);
            return false;
        }
    }

}
