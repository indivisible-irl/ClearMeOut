package com.indivisible.clearmeout.fragment;

import android.content.Context;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.PreferenceFragment;
import com.indivisible.clearmeout.R;


public class FiltersPreferenceFragment
        extends PreferenceFragment
{

    ///////////////////////////////////////////////////////
    ////    data
    ///////////////////////////////////////////////////////

    private CheckBoxPreference isExtensionsFilterActivePreference;
    private CheckBoxPreference isExtensionsFilterWhitelistPreference;
    private EditTextPreference extensionsFilterRawPreference;
    private CheckBoxPreference isFilenameFilterActivePreference;
    private CheckBoxPreference isFilenameFilterWhitelistPreference;
    private EditTextPreference filenameFilterRawPreference;

    private String isExtensionsFilterActiveKey;
    private String isExtensionsFilterWhitelistKey;
    private String extensionsFilterRawKey;
    private String isFilenameFilterActiveKey;
    private String isFilenameFilterWhitelistKey;
    private String filenameFilterRawKey;

    private static final String TAG = "FiltersPrefFrag";


    ///////////////////////////////////////////////////////
    ////    init
    ///////////////////////////////////////////////////////

    public static final FiltersPreferenceFragment newInstance()
    {
        return new FiltersPreferenceFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences_filters);
        initStrings();
        initPreferences();
    }

    private void initStrings()
    {
        Context cxt = getActivity();
        isExtensionsFilterActiveKey = cxt
                .getString(R.string.pref_filter_extensions_isActive_key);
        isExtensionsFilterWhitelistKey = cxt
                .getString(R.string.pref_filter_extensions_isWhitelist_key);
        extensionsFilterRawKey = cxt.getString(R.string.pref_filter_extensions_list_key);
        isFilenameFilterActiveKey = cxt.getString(R.string.pref_filter_filenames_isActive_key);
        isFilenameFilterWhitelistKey = cxt
                .getString(R.string.pref_filter_filenames_isWhitelist_key);
        filenameFilterRawKey = cxt.getString(R.string.pref_filter_filenames_patterns_key);
    }

    private void initPreferences()
    {
        isExtensionsFilterActivePreference = (CheckBoxPreference) findPreference(isExtensionsFilterActiveKey);
        isExtensionsFilterWhitelistPreference = (CheckBoxPreference) findPreference(isExtensionsFilterWhitelistKey);
        extensionsFilterRawPreference = (EditTextPreference) findPreference(extensionsFilterRawKey);
        isFilenameFilterActivePreference = (CheckBoxPreference) findPreference(isFilenameFilterActiveKey);
        isFilenameFilterWhitelistPreference = (CheckBoxPreference) findPreference(isFilenameFilterWhitelistKey);
        filenameFilterRawPreference = (EditTextPreference) findPreference(filenameFilterRawKey);
    }


}
