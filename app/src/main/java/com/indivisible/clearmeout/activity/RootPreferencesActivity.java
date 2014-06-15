package com.indivisible.clearmeout.activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import com.indivisible.clearmeout.R;
import com.indivisible.clearmeout.fragment.DeletePreferenceFragment;
import com.indivisible.clearmeout.fragment.ServicePreferenceFragment;


public class RootPreferencesActivity
        extends Activity
{

    ///////////////////////////////////////////////////////
    ////    data
    ///////////////////////////////////////////////////////

    private DeletePreferenceFragment deletePrefs;
    private ServicePreferenceFragment servicePrefs;


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
            ft.add(R.id.frag_top, deletePrefs);
            ft.add(R.id.frag_middle, servicePrefs);
            ft.commit();
        }
    }

    private void initFragments()
    {
        if (deletePrefs == null)
        {
            deletePrefs = DeletePreferenceFragment.newInstance();
        }
        if (servicePrefs == null)
        {
            servicePrefs = ServicePreferenceFragment.newInstance();
        }
    }
}
