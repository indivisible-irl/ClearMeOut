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
    private Profile thisProfile;
    private Context context;

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
        context = activity;
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
        thisProfile = profileManager.getProfile(profileId);

        initStrings();
        initPrefs();
        populatePrefs();
        setPrefsActiveOrInactive();
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        context = null;
        onIntentClickListener = null;
        thisProfile = null;
        profileManager = null;
    }

    private void initStrings()
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

    private void verifyParentActivityInterface(Activity activity)
    {
        try
        {
            onIntentClickListener = (OnIntentClickListener) activity;
        }
        catch (ClassCastException e)
        {
            Log.e(TAG, "Parent Activity must implement 'OnIntentClickListener'");
            throw e;
        }
    }


    ///////////////////////////////////////////////////////
    ////    handle prefs
    ///////////////////////////////////////////////////////

    private void populatePrefs()
    {
        profileNamePref.setTitle(context.getString(R.string.title_profile_name,
                thisProfile.getName()));
        profileNamePref.setDialogTitle(context.getString(R.string.dialog_profile_name_title));
        targetDirPref.setSummary(context.getString(R.string.summary_target_rootDir,
                thisProfile.getTarget().getRootDirectory()));
        profileIsActivePref.setChecked(thisProfile.isActive());
        //TODO: calculate next interval trigger time
        profileNextRunPref.setSummary(context.getString(R.string.summary_profile_nextRun,
                "1d 2h 3m"));
        profileNextRunPref.setEnabled(false);
        profileNextRunPref.setSelectable(false);
        targetIsRecursivePref.setChecked(thisProfile.getTarget().isRecursive());
        targetDoDeleteDirsPref.setChecked(thisProfile.getTarget().doDeleteDirectories());
        filtersIntentPref.setSummary(context.getString(R.string.summary_intent_filters,
                -1,
                thisProfile.getFilters().size()));
        intervalsIntentPref.setSummary(context.getString(R.string.summary_intent_intervals,
                -1,
                thisProfile.getIntervals().size()));
    }

    private void setPrefsActiveOrInactive()
    {
        if (thisProfile.getTarget().isRecursive())
        {
            targetDoDeleteDirsPref.setEnabled(true);
        }
        else
        {
            targetDoDeleteDirsPref.setEnabled(false);
        }
    }


    ///////////////////////////////////////////////////////
    ////    save and restore instance
    ///////////////////////////////////////////////////////

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

    private void syncProfile(Profile profile)
    {

    }


    ///////////////////////////////////////////////////////
    ////    communication
    ///////////////////////////////////////////////////////

    public interface OnIntentClickListener
    {

        public void onChildClick(Intent intent);
    }

}
