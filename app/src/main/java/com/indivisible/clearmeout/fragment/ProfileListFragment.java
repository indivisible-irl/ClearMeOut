package com.indivisible.clearmeout.fragment;

import java.util.List;
import android.app.Activity;
import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import com.indivisible.clearmeout.R;
import com.indivisible.clearmeout.data.Profile;
import com.indivisible.clearmeout.data.ProfileAdapter;
import com.indivisible.clearmeout.data.ProfileManager;

/**
 * Created by indiv on 16/06/14.
 */
public class ProfileListFragment
        extends ListFragment
        implements AdapterView.OnItemLongClickListener
{

    ///////////////////////////////////////////////////////
    ////    data
    ///////////////////////////////////////////////////////

    private Context context;
    private ProfileManager profileManager;
    private List<Profile> profiles;
    private ProfileAdapter adapter;

    private OnListProfileItemClickListener onClickListener;

    private static final String TAG = "ProfileListFrag";


    ///////////////////////////////////////////////////////
    ////    init
    ///////////////////////////////////////////////////////

    public static final ProfileListFragment newInstance()
    {
        return new ProfileListFragment();
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        try
        {
            onClickListener = (OnListProfileItemClickListener) activity;
        }
        catch (ClassCastException e)
        {
            throw new ClassCastException(
                    "Parent Activity must implement 'OnListProfileItemClickListener'");
        }
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


    ///////////////////////////////////////////////////////
    ////    click handling
    ///////////////////////////////////////////////////////

    @Override
    public void onListItemClick(ListView l, View v, int position, long id)
    {
        Log.v(TAG, "Short click: " + profiles.get(position).getName());
        onClickListener.onClickShort(profiles.get(position));
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
    {
        Log.v(TAG, "Long click: " + profiles.get(position).getName());
        onClickListener.onClickLong(profiles.get(position));
        return true;
    }


    ///////////////////////////////////////////////////////
    ////    communication
    ///////////////////////////////////////////////////////

    public interface OnListProfileItemClickListener
    {

        public void onClickShort(Profile profile);

        public void onClickLong(Profile profile);
    }

}
