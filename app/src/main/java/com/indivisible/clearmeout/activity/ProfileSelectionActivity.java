package com.indivisible.clearmeout.activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.indivisible.clearmeout.R;
import com.indivisible.clearmeout.data.Profile;
import com.indivisible.clearmeout.fragment.BreadcrumbFragment;
import com.indivisible.clearmeout.fragment.ProfileListFragment;

/**
 * Created by indiv on 16/06/14.
 */
public class ProfileSelectionActivity
        extends Activity
        implements ProfileListFragment.OnListProfileItemClickListener
{

    ///////////////////////////////////////////////////////
    ////    data
    ///////////////////////////////////////////////////////

    private BreadcrumbFragment breadcrumbFragment;
    private ProfileListFragment profileListFragment;

    private static final String THIS_CRUMB = "Home";
    private static final String KEY_PROFILEID = "profile_id";
    private static final String TAG = "ProfileSelectFrag";


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
        breadcrumbFragment = BreadcrumbFragment.newInstance(THIS_CRUMB);

        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.generic_breadcrumbs, breadcrumbFragment);
        ft.commit();
    }


    ///////////////////////////////////////////////////////
    ////    communication
    ///////////////////////////////////////////////////////

    @Override
    public void onClickShort(Profile profile)
    {
        Intent profileDetailsIntent = new Intent(getApplicationContext(),
                ProfileDetailsActivity.class);
        profileDetailsIntent.putExtra(KEY_PROFILEID, profile.getId());
        profileDetailsIntent.putExtra(BreadcrumbFragment.KEY_CRUMB_ARRAY, new String[] {
            THIS_CRUMB
        });
        startActivity(profileDetailsIntent);
    }

    @Override
    public void onClickLong(Profile profile)
    {
        Log.v(TAG, "Retrieved longClick on: (" + profile.getId() + ") " + profile.getName());
    }
}
