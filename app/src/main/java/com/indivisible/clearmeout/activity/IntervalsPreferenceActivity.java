package com.indivisible.clearmeout.activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import com.indivisible.clearmeout.R;
import com.indivisible.clearmeout.fragment.IntervalChildPreferencesFragment;
import com.indivisible.clearmeout.fragment.IntervalParentPreferencesFragment;
import com.indivisible.clearmeout.fragment.IntervalParentPreferencesFragment.ParentIntervalPreferenceInterface;
import com.indivisible.clearmeout.interval.IntervalType;


public class IntervalsPreferenceActivity
        extends Activity
        implements ParentIntervalPreferenceInterface
{

    ///////////////////////////////////////////////////////
    ////    data
    ///////////////////////////////////////////////////////

    private IntervalParentPreferencesFragment intervalParentFragment;
    private IntervalChildPreferencesFragment intervalChildFragment;


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
        intervalChildFragment = IntervalChildPreferencesFragment
                .newInstance(intervalParentFragment.getIntervalType(this));

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.frag_intervals_child, intervalChildFragment);
        ft.commit();
    }


    ///////////////////////////////////////////////////////
    ////    fragment communication
    ///////////////////////////////////////////////////////

    @Override
    public void onIntervalChoiceChanged(IntervalType newIntervalType)
    {
        intervalChildFragment.loadPreferences(newIntervalType);
    }

}
