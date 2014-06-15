package com.indivisible.clearmeout.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceFragment;
import android.util.Log;
import com.indivisible.clearmeout.R;
import com.indivisible.clearmeout.activity.FiltersPreferenceActivity;


public class DeletePreferenceFragment
        extends PreferenceFragment
        implements OnPreferenceClickListener//, OnPreferenceChangeListener
{

    ///////////////////////////////////////////////////////
    ////    data
    ///////////////////////////////////////////////////////

    private EditTextPreference targetFolderPreference;
    private CheckBoxPreference doRecursiveDeletePreference;
    private CheckBoxPreference doDeleteFoldersPreference;
    private Preference filtersIntentPreference;

    private String targetFolderPrefKey;
    private String doRecursiveDeletePrefKey;
    private String doDeleteFoldersPrefKey;
    private String filtersIntentPrefKey;
    private String targetFolderPrefDefault;

    private static final String TAG = "DeletePrefFrag";
    private final int REQUEST_CODE_PICK_DIR = 1;


    ///////////////////////////////////////////////////////
    ////    init
    ///////////////////////////////////////////////////////

    public static final DeletePreferenceFragment newInstance()
    {
        return new DeletePreferenceFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences_delete);
        initStrings();
        initPreferences();
    }

    private void initStrings()
    {
        Context cxt = getActivity();
        targetFolderPrefKey = cxt.getString(R.string.pref_delete_targetFolder_key);
        doRecursiveDeletePrefKey = cxt.getString(R.string.pref_delete_doRecursiveDelete_key);
        doDeleteFoldersPrefKey = cxt.getString(R.string.pref_delete_doDeleteFolders_key);
        filtersIntentPrefKey = cxt.getString(R.string.pref_delete_intent_filters_key);
        targetFolderPrefDefault = cxt.getString(R.string.pref_delete_targetFolder_default);
    }

    private void initPreferences()
    {
        targetFolderPreference = (EditTextPreference) findPreference(targetFolderPrefKey);
        doRecursiveDeletePreference = (CheckBoxPreference) findPreference(doRecursiveDeletePrefKey);
        doDeleteFoldersPreference = (CheckBoxPreference) findPreference(doDeleteFoldersPrefKey);
        filtersIntentPreference = findPreference(filtersIntentPrefKey);
        filtersIntentPreference.setOnPreferenceClickListener(this);
        targetFolderPreference.setOnPreferenceClickListener(this);
    }

    @Override
    public void onResume()
    {
        super.onResume();
    }

    @Override
    public void onPause()
    {
        super.onPause();
    }


    ///////////////////////////////////////////////////////
    ////    override
    ///////////////////////////////////////////////////////

    @Override
    public boolean onPreferenceClick(Preference preference)
    {
        String key = preference.getKey();
        if (key.equals(filtersIntentPrefKey))
        {
            Log.v(TAG, "filtersIntentPref clicked");
            startFiltersPrefActivity();
            return true;
        }
        if (key.equals(targetFolderPrefKey))
        {
            Log.v(TAG, "targetFolderPref clicked");
            targetFolderPreference.getDialog().dismiss();
            startTargetFolderActivity();
            return true;
        }
        else
        {
            Log.w(TAG, "unhandled preference click: " + key);
            return false;
        }
    }

    //    @Override
    //    public boolean onPreferenceChange(Preference preference, Object newValue)
    //    {
    //        String key = preference.getKey();
    //        if (key.equals(targetFolderKey))
    //        {
    //            //TODO: validate targetFolder
    //            //setActivePrefs();
    //            return true;
    //        }
    //        else
    //        {
    //            Log.w(TAG, "unhandled preference change: " + key);
    //            Log.w(TAG, "newValue: " + newValue.toString());
    //            return false;
    //        }
    //    }


    ///////////////////////////////////////////////////////
    ////    intents
    ///////////////////////////////////////////////////////

    private void startFiltersPrefActivity()
    {
        Intent filtersPrefIntent = new Intent(getActivity(), FiltersPreferenceActivity.class);
        startActivity(filtersPrefIntent);
    }

    private void startTargetFolderActivity()
    {
        //TODO: write my own folder chooser
        Intent folderChooserIntent = new Intent(
                com.vassiliev.androidfilebrowser.FileBrowserActivity.INTENT_ACTION_SELECT_DIR,
                null, getActivity(),
                com.vassiliev.androidfilebrowser.FileBrowserActivity.class);
        startActivityForResult(folderChooserIntent, REQUEST_CODE_PICK_DIR);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_PICK_DIR)
        {
            if (resultCode == Activity.RESULT_OK)
            {
                updateFolderPreference(data);
            }
            else
            {
                Log.w(TAG, "Received no result from FileBrowser");
            }
        }
        else
        {
            Log.w(TAG, "Received code other than REQUEST_CODE_PICK_DIR from ActivityResult");
        }
    }

    private void updateFolderPreference(Intent receivedIntent)
    {
        String selectedFolderPath = receivedIntent
                .getStringExtra(com.vassiliev.androidfilebrowser.FileBrowserActivity.returnDirectoryParameter);
        targetFolderPreference.setSummary(selectedFolderPath);
        targetFolderPreference.setText(selectedFolderPath);
    }

}
