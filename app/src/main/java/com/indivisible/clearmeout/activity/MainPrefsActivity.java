package com.indivisible.clearmeout.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;
import com.indivisible.clearmeout.R;
import com.indivisible.clearmeout.preferences.TimePreference;
import com.indivisible.clearmeout.service.UpdateAlarmsService;
import com.mburman.fileexplore.FileExplore;


@SuppressWarnings("deprecation")
//FIXME comment this to view where to update to newer functionality
public class MainPrefsActivity
        extends PreferenceActivity
        implements OnPreferenceClickListener, OnSharedPreferenceChangeListener
{

    //// data

    private static final String TAG = "CMO:PreferencesActivity";
    private final int REQUEST_CODE_PICK_DIR = 1;


    // preferences
    //	private CheckBoxPreference pCbServiceActive;
    private EditTextPreference pEtFolder;
    //	private CheckBoxPreference pCbRecursiveDelete, pCbDeleteFolders, pCbNotifyOnDelete;
    private CheckBoxPreference pCbIntervalType;										//TODO intervalType --> list
    private TimePreference pTpDailyTime;
    private EditTextPreference pEtIntervalMinutes;
    //	private CheckBoxPreference pCbStrictInterval;
    private CheckBoxPreference pCbUseExtensionFilter, pCbUsePatternFilter;
    private CheckBoxPreference pCbDeleteExtensionMatches, pCbDeletePatternMatches;
    private EditTextPreference pEtExtensionsFilter, pEtPatternFilter;


    //// default Activity methods

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        //FIXME: addPreferencesFromResource(R.xml.preferences_main);
        //		prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        initPreferences();

        // disable non functional preferences
        setUnimplemenetedUnselectable();

        //		// testing SharedPreference access
        //		Toast.makeText(this, "Folder is:\n" +prefs.getString(pEtFolder.getKey(), "missed"), Toast.LENGTH_SHORT).show();
    }

    // register as a sharedPreferenceListener
    @Override
    protected void onResume()
    {
        super.onResume();
        // register pref change listener
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);

        //TODO updatePreferences() containing following and others
        pEtFolder.setSummary(pEtFolder.getText());
        setFolderPreferenceClickIntent();
        updateIntervalSummary();
        updateIntervalTypesEnabled();
        updatePTpDailyTime();
    }

    // release the sharedPreferenceListener
    @Override
    protected void onPause()
    {
        super.onPause();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onStop()
    {
        super.onStop();

        Intent updateIntent = new Intent(getApplicationContext(),
                UpdateAlarmsService.class);
        startService(updateIntent);
    }


    //// custom handling of some preferences

    private void initPreferences()
    {
        //pCbServiceActive = (CheckBoxPreference) findPreference(getString(R.string.pref_key_service_active));

        pEtFolder = (EditTextPreference) findPreference(getString(R.string.pref_delete_targetFolder_key));
        //pCbRecursiveDelete = (CheckBoxPreference) findPreference(getString(R.string.pref_key_use_recursive_delete));
        //pCbDeleteFolders = (CheckBoxPreference) findPreference(getString(R.string.pref_key_delete_folders));
        //pCbNotifyOnDelete = (CheckBoxPreference) findPreference(getString(R.string.pref_key_notify_on_delete));

        pCbIntervalType = (CheckBoxPreference) findPreference("pref_interval_type");
        pTpDailyTime = (TimePreference) findPreference(getString(R.string.pref_interval_daily_key));
        pEtIntervalMinutes = (EditTextPreference) findPreference(getString(R.string.pref_interval_everyXMinutes_key));
        //pCbStrictInterval =  (CheckBoxPreference) findPreference(getString(R.string.pref_key_use_strict_alarms));

    }


    //// override onPreferenceClick and onPreferenceChange

    @Override
    public boolean onPreferenceClick(Preference preference)
    {
        // Folder preference
        if (preference.equals(pEtFolder))
        {
            //			// disable the EditText dialog (and soft keyboard) as we're using a file picking intent instead
            pEtFolder.getDialog().dismiss();

            // pick folder
            pEtFolderGetFolder();
            return true;
        }

        // Interval Preference
        else if (preference.equals(pEtIntervalMinutes))
        {
            // update the preference summary with the new value
            updateIntervalSummary();
            return true;
        }

        // for all other clicks
        return true;
    }

    @Override
    public void
            onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key)
    {
        if (key.equals(pEtIntervalMinutes.getKey()))
        {
            updateIntervalSummary();
        }
        else if (key.equals(pCbIntervalType.getKey()))
        {
            updateIntervalTypesEnabled();
        }
        else if (key.equals(pTpDailyTime.getKey()))
        {
            updatePTpDailyTime();
        }

    }

    //// custom preference updates and actions
    //TODO see what the common actions are and method(pref) them

    private void setFolderPreferenceClickIntent()
    {
        Intent folderIntent = new Intent(this, FileExplore.class);
        pEtFolder.setIntent(folderIntent);
        pEtFolder.setOnPreferenceClickListener(this);
    }

    private void updateFolderPreference(Intent receivedIntent)
    {
        String selectedFolderPath = receivedIntent
                .getStringExtra(com.vassiliev.androidfilebrowser.FileBrowserActivity.returnDirectoryParameter);


        pEtFolder.setSummary(selectedFolderPath);
        pEtFolder.setText(selectedFolderPath);

        Toast.makeText(this, "SET AS:\n\n" + pEtFolder.getText(), Toast.LENGTH_SHORT)
                .show();
    }

    private void updateIntervalSummary()
    {
        int interval = Integer.parseInt(pEtIntervalMinutes.getText());
        pEtIntervalMinutes.setSummary(String
                .format(getString(R.string.pref_interval_everyXMinutes_text), interval));
    }

    private void updateIntervalTypesEnabled()
    {
        if (PreferenceManager.getDefaultSharedPreferences(this)
                .getBoolean(pCbIntervalType.getKey(), true))
        {
            // set to daily
            pTpDailyTime.setEnabled(true);
            pEtIntervalMinutes.setEnabled(false);
        }
        else
        {
            // set to periodic
            pTpDailyTime.setEnabled(false);
            pEtIntervalMinutes.setEnabled(true);
        }
    }

    /**
     * Update the time value within pTpDailyTime's summary
     */
    private void updatePTpDailyTime()
    {
        String summ = String.format(getString(R.string.pref_interval_daily_text),
                pTpDailyTime.toString());
        pTpDailyTime.setSummary(summ);
    }

    /**
     * Disable interactions for currently unimplemented preferences
     */
    private void setUnimplemenetedUnselectable()
    {
        pCbUseExtensionFilter.setSelectable(false);
        pCbDeleteExtensionMatches.setSelectable(false);
        pEtExtensionsFilter.setSelectable(false);

        pCbUsePatternFilter.setSelectable(false);
        pCbDeletePatternMatches.setSelectable(false);
        pEtPatternFilter.setSelectable(false);
    }

    //// special case action: Get target folder - startActivityForResult() and onActivityResult()

    /**
     * StartActivityForResult() to get user's folder choice
     */
    private void pEtFolderGetFolder()
    {
        Log.d(TAG, "Starting FileBrowserActivity to get target folder...");
        Intent fileExploreIntent = new Intent(
                com.vassiliev.androidfilebrowser.FileBrowserActivity.INTENT_ACTION_SELECT_DIR,
                null, this, com.vassiliev.androidfilebrowser.FileBrowserActivity.class);
        //If the parameter below is not provided the Activity will try to start from sdcard(external storage),
        // if fails, then will start from roor "/"
        // Do not use "/sdcard" instead as base address for sdcard use Environment.getExternalStorageDirectory() 
        //		fileExploreIntent.putExtra(
        //				ua.com.vassiliev.androidfilebrowser.FileBrowserActivity.startDirectoryParameter, 
        //				"/sdcard"
        //				);
        startActivityForResult(fileExploreIntent, REQUEST_CODE_PICK_DIR);
    }


    /// triggered activity result handling

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_PICK_DIR)
        {
            if (resultCode == RESULT_OK)
            {
                // set pEtFolder value and summary
                updateFolderPreference(data);
            }
            else
            {
                Log.d(TAG, "Received NO result from file browser");
            }
        }
        else
        {
            Log.w(TAG,
                    "Received code other than REQUEST_CODE_PICK_DIR from ActivityResult");
        }
    }

    //TODO add a help menu option here?
    //	@Override
    //	public boolean onCreateOptionsMenu(Menu menu) {
    //		// Inflate the menu; this adds items to the action bar if it is present.
    //		getMenuInflater().inflate(R.menu.settings, menu);
    //		return true;
    //	}

}
