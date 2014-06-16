package com.indivisible.clearmeout.fragment;

import java.util.List;
import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.indivisible.clearmeout.R;
import com.indivisible.clearmeout.data.Profile;
import com.indivisible.clearmeout.data.ProfileAdapter;
import com.indivisible.clearmeout.data.ProfileManager;

/**
 * Created by indiv on 16/06/14.
 */
public class ProfileListFragment
        extends ListFragment
{

    ///////////////////////////////////////////////////////
    ////    data
    ///////////////////////////////////////////////////////

    private Context context;
    private ProfileManager profileManager;
    private List<Profile> profiles;
    private ProfileAdapter adapter;

    private static final String TAG = "ProfileListFrag";


    ///////////////////////////////////////////////////////
    ////    init
    ///////////////////////////////////////////////////////

    public static final ProfileListFragment newInstance()
    {
        return new ProfileListFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.frag_profile_list, container, false);
        this.context = getActivity();
        this.profileManager = new ProfileManager(context);
        this.profiles = profileManager.getAllProfiles();
        if (profiles.size() == 0)
        {
            this.profileManager.loadTestData();
            this.profiles = profileManager.getAllProfiles();
        }
        this.adapter = new ProfileAdapter(context, profiles);
        setListAdapter(adapter);
        return view;
    }

}
