package com.indivisible.clearmeout.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.indivisible.clearmeout.R;
import com.indivisible.clearmeout.data.Profile;
import com.indivisible.clearmeout.data.ProfileManager;

public class ProfileDetailsFragment
        extends PreferenceFragment
{

    ///////////////////////////////////////////////////////
    ////    data
    ///////////////////////////////////////////////////////

    private ProfileManager profileManager;
    private long profileId;
    private Profile profile;

    private EditTextPreference profileNamePref;
    private CheckBoxPreference profileIsActivePref;
    private Preference profileNextRunPref;
    private EditTextPreference targetDirPref;
    private CheckBoxPreference targetIsRecursivePref;
    private CheckBoxPreference targetDoDeleteDirsPref;
    private Preference filtersIntentPref;
    private Preference intervalsIntentPref;

    private String profileNameKey;
    private String profileIsActiveKey;
    private String profileNextRunKey;
    private String targetDirKey;
    private String targetIsRecursiveKey;
    private String targetDoDeleteDirsKey;
    private String filtersIntentKey;
    private String intervalsIntentKey;

    private OnIntentClickListener onIntentClickListener;

    private static final String ARG_PROFILEID = "profile_id";
    private static final String TAG = "ProfileDetailsFrag";

    ///////////////////////////////////////////////////////
    ////    init
    ///////////////////////////////////////////////////////

    public static ProfileDetailsFragment newInstance(long profileId)
    {
        ProfileDetailsFragment fragment = new ProfileDetailsFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_PROFILEID, profileId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        verifyParentActivityInterface(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.profile_details_prefs);
        if (getArguments() != null)
        {
            profileId = getArguments().getLong(ARG_PROFILEID);
            if (profileId < 0)
            {
                Log.w(TAG, "(onCreate) Invalid profileId: " + profileId);
            }
        }
        else
        {
            Log.e(TAG, "(onCreate) Never received a profileId");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        profileManager = new ProfileManager(getActivity());
        profile = profileManager.getProfile(profileId);

        //Context context = getActivity();
        //initKeys(context);
        //initPrefs();
        //populatePrefs();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        //TODO: load temp state
    }

    @Override
    public void onPause()
    {
        super.onPause();
        //TODO: save temp state
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        onIntentClickListener = null;
        profile = null;
        profileManager = null;
    }

    private void initKeys(Context context)
    {
        profileNameKey = context.getString(R.string.key_profile_name);
        profileIsActiveKey = context.getString(R.string.key_profile_isActive);
        profileNextRunKey = context.getString(R.string.key_profile_nextRun);
        targetDirKey = context.getString(R.string.key_target_rootDirectory);
        targetIsRecursiveKey = context.getString(R.string.key_target_isRecursive);
        targetDoDeleteDirsKey = context.getString(R.string.key_target_doDeleteDirs);
        filtersIntentKey = context.getString(R.string.key_intent_filters);
        intervalsIntentKey = context.getString(R.string.key_intent_intervals);
    }

    private void initPrefs()
    {
        profileNamePref = (EditTextPreference) findPreference(profileNameKey);
        profileIsActivePref = (CheckBoxPreference) findPreference(profileIsActiveKey);
        profileNextRunPref = findPreference(profileNextRunKey);
        targetDirPref = (EditTextPreference) findPreference(targetDirKey);
        targetIsRecursivePref = (CheckBoxPreference) findPreference(targetIsRecursiveKey);
        targetDoDeleteDirsPref = (CheckBoxPreference) findPreference(targetDoDeleteDirsKey);
        filtersIntentPref = findPreference(filtersIntentKey);
        intervalsIntentPref = findPreference(intervalsIntentKey);
    }

    private void populatePrefs()
    {
        profileNamePref.setTitle(profile.getName());
        profileIsActivePref.setChecked(profile.isActive());
    }

    private void verifyParentActivityInterface(Activity activity)
    {
        try
        {
            onIntentClickListener = (OnIntentClickListener) activity;
        }
        catch (ClassCastException e)
        {
            Log.e(TAG, "Parent Activity must implement 'OnIntentClickListener'");
            e.printStackTrace();
            throw e;
        }
    }

    ///////////////////////////////////////////////////////
    ////    communication
    ///////////////////////////////////////////////////////

    public interface OnIntentClickListener
    {

        public void onChildClick(Intent intent);
    }

}
