package com.indivisible.clearmeout.activity;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.indivisible.clearmeout.R;
import com.indivisible.clearmeout.data.Profile;
import com.indivisible.clearmeout.data.ProfileManager;
import com.indivisible.clearmeout.fragment.BreadcrumbFragment;
import com.indivisible.clearmeout.fragment.ProfileDetailsFragment;
import com.indivisible.clearmeout.fragment.SaveCancelFragment;
import com.indivisible.clearmeout.util.Utils;

/**
 * Created by indiv on 16/06/14.
 */
public class ProfileDetailsActivity
        extends Activity
        implements SaveCancelFragment.SaveCancelInterface,
        ProfileDetailsFragment.OnIntentClickListener
{

    ///////////////////////////////////////////////////////
    ////    data
    ///////////////////////////////////////////////////////

    private long profileId;
    private String[] crumbs;
    private ProfileManager profileManager;
    private Profile profile;

    private BreadcrumbFragment breadcrumbFragment;
    private SaveCancelFragment saveCancelFragment;
    private ProfileDetailsFragment profileDetailsFragment;

    private static final String THIS_CRUMB = "Profile";     //TODO: res string
    private static final String KEY_PROFILEID = "profile_id";
    private static final String TAG = "ProfileDetailsFrag";


    ///////////////////////////////////////////////////////
    ////    init
    ///////////////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_details);
        loadExtras(getIntent().getExtras());
        profileManager = new ProfileManager(this);
        loadProfile();
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        loadFragments();
    }

    private void loadExtras(Bundle extras)
    {
        if (extras != null)
        {
            profileId = extras.getLong(KEY_PROFILEID);
            if (profileId < 0)
            {
                Log.e(TAG, "received invalid ID from bundle extras: " + profileId);
            }
            String[] parentCrumbs = extras.getStringArray(BreadcrumbFragment.KEY_CRUMB_ARRAY);
            if (parentCrumbs != null)
            {
                crumbs = Utils.appendToArray(parentCrumbs, THIS_CRUMB);
            }
            else
            {
                crumbs = new String[] {
                    THIS_CRUMB
                };
            }
        }
        else
        {
            Log.e(TAG, "No extras passed with Intent!");
            profileId = -2;
        }
    }

    private void loadProfile()
    {
        profile = profileManager.getProfile(profileId);
        Log.v(TAG, "Loaded profile: " + profile.getName());
    }

    private void loadFragments()
    {
        breadcrumbFragment = BreadcrumbFragment.newInstance(crumbs);
        //saveCancelFragment = SaveCancelFragment.newInstance();
        profileDetailsFragment = ProfileDetailsFragment.newInstance(profileId);

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(R.id.profile_details_breadcrumbs, breadcrumbFragment);
        ft.add(R.id.profile_details_preferenceFragment, profileDetailsFragment);
        //ft.add(R.id.profile_details_savecancel, saveCancelFragment);
        ft.commit();
    }

    ///////////////////////////////////////////////////////
    ////    communication
    ///////////////////////////////////////////////////////

    @Override
    public void onSave()
    {
        Log.v(TAG, "onSave");
    }

    @Override
    public void onCancel()
    {
        Log.v(TAG, "onCancel");
    }

    @Override
    public void onChildClick(Intent intent)
    {
        Log.v(TAG, "Received intent from ProfileDetailsFragment");
    }
}
