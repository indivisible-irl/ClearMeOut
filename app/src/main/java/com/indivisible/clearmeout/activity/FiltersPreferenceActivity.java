package com.indivisible.clearmeout.activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import com.indivisible.clearmeout.R;
import com.indivisible.clearmeout.fragment.FiltersPreferenceFragment;


public class FiltersPreferenceActivity
        extends Activity
{

    ///////////////////////////////////////////////////////
    ////    data
    ///////////////////////////////////////////////////////

    private FiltersPreferenceFragment filterPrefs;

    private static final String TAG = "FiltersPrefAct";


    ///////////////////////////////////////////////////////
    ////    init
    ///////////////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_container);
        initFragments();

        if (savedInstanceState == null)
        {
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(R.id.frag_top, filterPrefs);
            ft.commit();
        }
    }

    private void initFragments()
    {
        if (filterPrefs == null)
        {
            filterPrefs = FiltersPreferenceFragment.newInstance();
        }
    }
}
