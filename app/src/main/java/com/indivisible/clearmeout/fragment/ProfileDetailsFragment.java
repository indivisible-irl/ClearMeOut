package com.indivisible.clearmeout.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
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
        implements Preference.OnPreferenceClickListener, Preference.OnPreferenceChangeListener
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
        setPrefsActiveOrInactive();
        new PopulateTask().execute();
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

        profileNextRunPref.setEnabled(false);
        profileNextRunPref.setSelectable(false);
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
        targetDirPref.setSummary(context.getString(R.string.summary_target_rootDir,
                thisProfile.getTarget().getRootDirectory()));
        profileIsActivePref.setChecked(thisProfile.isActive());
        //TODO: calculate next interval trigger time
        profileNextRunPref.setSummary(context.getString(R.string.summary_profile_nextRun,
                "1d 2h 3m"));
        targetIsRecursivePref.setChecked(thisProfile.getTarget().isRecursive());
        targetDoDeleteDirsPref.setChecked(thisProfile.getTarget().doDeleteDirectories());

        targetIsRecursivePref.setOnPreferenceChangeListener(this);
        filtersIntentPref.setOnPreferenceClickListener(this);
        intervalsIntentPref.setOnPreferenceClickListener(this);

        long[] filterCounts = profileManager.getFiltersCount(profileId);
        long[] intervalCounts = profileManager.getIntervalsCount(profileId);
        filtersIntentPref.setSummary(context.getString(R.string.summary_intent_filters,
                filterCounts[1],
                filterCounts[0]));
        intervalsIntentPref.setSummary(context.getString(R.string.summary_intent_intervals,
                intervalCounts[1],
                intervalCounts[0]));
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

    @Override
    public boolean onPreferenceClick(Preference preference)
    {
        if (preference.getKey().equals(filtersIntentKey))
        {
            Log.v(TAG, "(click) filtersIntent clicked");
            return true;
        }
        else if (preference.getKey().equals(intervalsIntentKey))
        {
            Log.v(TAG, "(click) intervalsIntent clicked");
            return true;
        }
        else
        {
            Log.w(TAG, "(click) Unhandled click: " + preference.getKey());
            return false;
        }
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue)
    {
        if (preference.getKey().equals(targetIsRecursiveKey))
        {
            Log.v(TAG, "(change) isRecursive: " + newValue.toString());
            boolean newIsRecursive = (Boolean) newValue;
            targetDoDeleteDirsPref.setEnabled(newIsRecursive);
            return true;
        }
        else
        {
            Log.w(TAG, "(change) Unhandled change: " + preference.getKey());
            return false;
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


    ///////////////////////////////////////////////////////
    ////    async pref populate
    ///////////////////////////////////////////////////////

    private class PopulateTask
            extends AsyncTask<Void, Void, Void>
    {

        @Override
        protected Void doInBackground(Void... voidVar)
        {
            populatePrefs();
            return null;
        }
    }
}
