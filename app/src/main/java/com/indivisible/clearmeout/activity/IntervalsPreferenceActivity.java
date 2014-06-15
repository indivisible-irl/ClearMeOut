package com.indivisible.clearmeout.activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import com.indivisible.clearmeout.R;
import com.indivisible.clearmeout.fragment.IntervalChildPreferencesFragment;
import com.indivisible.clearmeout.fragment.IntervalParentPreferencesFragment;
import com.indivisible.clearmeout.fragment.IntervalParentPreferencesFragment.ParentIntervalPreferenceInterface;
import com.indivisible.clearmeout.data.IntervalType;
import com.indivisible.clearmeout.interval.IntervalUtil;


public class IntervalsPreferenceActivity
        extends Activity
        implements ParentIntervalPreferenceInterface
{

    ///////////////////////////////////////////////////////
    ////    data
    ///////////////////////////////////////////////////////

    private IntervalParentPreferencesFragment intervalParentFragment;
    private IntervalChildPreferencesFragment intervalChildFragment;

    private static final String TAG = "IntPrefActivity";


    ///////////////////////////////////////////////////////
    ////    init
    ///////////////////////////////////////////////////////

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_interval_preferences);
        initFragments();
    }

    private void initFragments()
    {
        intervalParentFragment = IntervalParentPreferencesFragment.newInstance();
        IntervalType intervalType = IntervalUtil.getCurentIntervalType(this);
        addChildPreferenceFragment(intervalType, false);
    }

    private void addChildPreferenceFragment(IntervalType intervalType,
                                            boolean isReplaceTransaction)
    {
        intervalChildFragment = IntervalChildPreferencesFragment.newInstance(intervalType);
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if (isReplaceTransaction)
        {
            ft.replace(R.id.frag_intervals_child, intervalChildFragment);
        }
        else
        {
            ft.add(R.id.frag_intervals_child, intervalChildFragment);
        }
        ft.commit();
    }


    ///////////////////////////////////////////////////////
    ////    fragment communication
    ///////////////////////////////////////////////////////

    @Override
    public void onIntervalChoiceChanged(IntervalType newIntervalType)
    {
        Log.v(TAG,
                "IntChoice (before): "
                        + PreferenceManager.getDefaultSharedPreferences(this)
                                .getString(getString(R.string.pref_interval_choice_key),
                                        getString(R.string.pref_interval_choice_default)));
        addChildPreferenceFragment(newIntervalType, true);
        Log.v(TAG,
                "IntChoice (after): "
                        + PreferenceManager.getDefaultSharedPreferences(this)
                                .getString(getString(R.string.pref_interval_choice_key),
                                        getString(R.string.pref_interval_choice_default)));
    }
}
