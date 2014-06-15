package com.indivisible.clearmeout.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;
import com.indivisible.clearmeout.R;
import com.indivisible.clearmeout.interval.IntervalType;
import com.indivisible.clearmeout.interval.IntervalUtil;


public class IntervalParentPreferencesFragment
        extends PreferenceFragment
        implements OnPreferenceChangeListener
{

    ///////////////////////////////////////////////////////
    ////    data
    ///////////////////////////////////////////////////////

    private ListPreference intervalChoicesPreference;
    private CheckBoxPreference isStrictAlarmPreference;

    private String intervalChoicesKey;
    private String isStrictAlarmKey;

    private ParentIntervalPreferenceInterface parentInterface;

    private static final String TAG = "IntervalPrefFrag";


    ///////////////////////////////////////////////////////
    ////    init
    ///////////////////////////////////////////////////////

    public static final IntervalParentPreferencesFragment newInstance()
    {
        return new IntervalParentPreferencesFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences_intervals_parent);
        initStrings();
        initPreferences();
    }

    private void initStrings()
    {
        Context context = getActivity();
        intervalChoicesKey = context.getString(R.string.pref_interval_choice_key);
        isStrictAlarmKey = context.getString(R.string.pref_interval_isStrictAlarm_key);
    }

    private void initPreferences()
    {
        intervalChoicesPreference = (ListPreference) findPreference(intervalChoicesKey);
        intervalChoicesPreference.setOnPreferenceChangeListener(this);
        if(intervalChoicesPreference.getValue() == null)
        {
            intervalChoicesPreference.setValueIndex(0);
        }
        isStrictAlarmPreference = (CheckBoxPreference) findPreference(isStrictAlarmKey);
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        try
        {
            parentInterface = (ParentIntervalPreferenceInterface) activity;
        }
        catch (ClassCastException e)
        {
            Log.e(TAG, "Parent Activity deosn't implement 'ParentIntervalPreferenceInterface'");
            throw new ClassCastException(activity.toString()
                    + " must implement ParentIntervalPreferenceInterface");
        }
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        parentInterface = null;
    }


    public interface ParentIntervalPreferenceInterface
    {

        public void onIntervalChoiceChanged(IntervalType newIntervalType);
    }


    ///////////////////////////////////////////////////////
    ////    prefs
    ///////////////////////////////////////////////////////

    public IntervalType getIntervalType(Context context)
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String intervalTypeString = prefs.getString(intervalChoicesKey,
                context.getString(R.string.pref_interval_choice_default));
        return IntervalUtil.calcIntervalType(context, intervalTypeString);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue)
    {
        if (preference.getKey().equals(intervalChoicesKey))
        {
            try
            {
                String newIntervalChoice = (String) newValue;
                IntervalType intervalType = IntervalUtil.calcIntervalType(getActivity(),
                        newIntervalChoice);
                parentInterface.onIntervalChoiceChanged(intervalType);
                return true;
            }
            catch (ClassCastException e)
            {
                Log.e(TAG,
                        "Failed to cast new intervalChoice to String: " + newValue.toString());
            }
        }
        return false;
    }

}