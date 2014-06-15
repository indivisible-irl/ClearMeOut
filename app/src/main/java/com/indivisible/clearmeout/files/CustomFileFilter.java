package com.indivisible.clearmeout.files;

import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import com.indivisible.clearmeout.R;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

public class CustomFileFilter
        implements FileFilter
{

    private static final String TAG = "CMO:CustFileFilter";

    // filter files by their extension
    private boolean useExtensionFilter;
    private boolean deleteExtensionMatches;
    private String[] extensionsToFilter;

    // filter files with regex matches
    //	private boolean usePatternFilter;
    //	private boolean deletePatternMatches;
    //	private String[] patternsToFilter;


    // use a constructor to populate the user's settings' vars
    public CustomFileFilter(Context context)
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        useExtensionFilter = prefs.getBoolean(context
                .getString(R.string.pref_filter_extensions_isActive_key), false);
        if (useExtensionFilter)
        {
            deleteExtensionMatches = prefs.getBoolean(context
                    .getString(R.string.pref_filter_extensions_isWhitelist_key), true);	//IDEA declare these defaults somewhere more central?

            String rawExtensionFilters = prefs.getString(context
                    .getString(R.string.pref_filter_extensions_list_key), null);
            if (rawExtensionFilters == null)
            {
                Log.w(TAG,
                        "Extensions choices was made null as could not access saved value. disabling filter");
                useExtensionFilter = false;
                //TODO change useExtensionFilter value to false in actual preferences
            }
            else
            {
                // split extension choices up
                extensionsToFilter = rawExtensionFilters.replace(" ", "").split(",");
                Log.d(TAG, String.format("Split %s extensions: %s",
                        extensionsToFilter.length,
                        Arrays.toString(extensionsToFilter)));
            }
        }
        else
        {
            Log.i(TAG, "filter: extensions filter inactive");
        }

        //TODO throw in the usePatternFilter bits here


    }


    @Override
    public boolean accept(File pathname)
    {

        //TODO: use the stored (and future) prefs to determine if accept file (means delete)


        return false;
    }

}
