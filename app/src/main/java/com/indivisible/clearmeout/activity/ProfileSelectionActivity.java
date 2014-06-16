package com.indivisible.clearmeout.activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import com.indivisible.clearmeout.R;
import com.indivisible.clearmeout.fragment.BreadcrumbFragment;
import com.indivisible.clearmeout.fragment.ProfileListFragment;

/**
 * Created by indiv on 16/06/14.
 */
public class ProfileSelectionActivity
        extends Activity
{

    ///////////////////////////////////////////////////////
    ////    data
    ///////////////////////////////////////////////////////

    private BreadcrumbFragment breadcrumbFragment;
    private ProfileListFragment profileListFragment;

    private static final String THIS_CRUMB = "Profiles";
    private static final String TAG = "ProileSelectFrag";


    ///////////////////////////////////////////////////////
    ////    init
    ///////////////////////////////////////////////////////


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_selection);
        FragmentManager fm = getFragmentManager();

        profileListFragment = (ProfileListFragment) fm.findFragmentById(R.id.profile_listview);
        breadcrumbFragment = BreadcrumbFragment.newInstance(new String[] {
            THIS_CRUMB
        });

        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.generic_breadcrumbs, breadcrumbFragment);
        ft.commit();
    }
}
