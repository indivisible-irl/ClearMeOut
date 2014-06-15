package com.indivisible.clearmeout.service;

import java.io.File;
import java.io.FilenameFilter;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;
import com.indivisible.clearmeout.R;

public class DeleteService
        extends Service
{

    //// data

    private String folder;
    private File root;
    private boolean recursiveDelete;
    private boolean deleteFolders;
    private boolean notifyOnDelete;

    private static final String TAG = "CMO:DeleteService";


    //// perform on intent calls for this service

    @Override
    public void onCreate()
    {
        super.onCreate();
        Log.d(TAG, "DeleteService started...");

        // get needed settings and test folder pref saved
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext());
        folder = prefs.getString(getString(R.string.pref_delete_targetFolder_key), "---");

        if (folder.equals("---"))
        {
            Log.w(TAG,
                    "Default folder value found. Shutting down (and disabling service)");
            disableService();
        }
        else
        {
            Log.d(TAG, "ClearMeOut emptying folder: " + folder);
            recursiveDelete = prefs
                    .getBoolean(getString(R.string.pref_delete_doRecursiveDelete_key),
                            false);
            deleteFolders = prefs
                    .getBoolean(getString(R.string.pref_delete_doDeleteFolders_key),
                            false);
            notifyOnDelete = prefs
                    .getBoolean(getString(R.string.pref_service_notifyOnDelete_key),
                            false);

            performDelete();
            stopSelf();
        }
    }


    /**
     * Parent method to delete files and/or folders from a directory based on
     * the shared preference "recursive_delete"
     */
    private void performDelete()
    {
        root = new File(folder);

        if (root.exists())
        {
            if (root.canWrite())
            {
                Log.d(TAG, "Can write to: " + root.getAbsolutePath());
            }
            else
            {
                Log.e(TAG, "Cannot perform delete: " + root.getAbsolutePath());
                if (notifyOnDelete)
                {
                    Toast.makeText(this,
                            "ClearMeOut failed to clear:\n"
                                    + folder
                                    + "\nDid not have required access.\nDoes the directory exist?",
                            Toast.LENGTH_LONG).show();
                }
                stopSelf();
            }
        }
        else
        {
            Log.w(TAG, "Folder not exists: " + folder);
            disableService();
            stopSelf();
        }


        // apply delete to sub-folders also?
        if (recursiveDelete)
        {
            Log.d(TAG, "Performing recursive delete on " + root.getAbsolutePath());
            performRecursiveDelete(root);
        }
        else
        {
            Log.d(TAG, "Performing non-recursive delete on " + root.getAbsolutePath());
            performNonRecursiveDelete();
        }

        Log.d(TAG, "Finished delete");

        if (notifyOnDelete)
        {
            Toast.makeText(this, "ClearMeOut emptied:\n" + folder, Toast.LENGTH_LONG)
                    .show();		//TODO strings.xml
        }
    }


    /**
     * Delete all files AND folders from a directory
     * 
     * @param file
     */
    private void performRecursiveDelete(File file)
    {
        if (file.isFile())
        {
            Log.d(TAG, "Del (F): " + file.getAbsolutePath());
            file.delete();
        }
        else
        {
            String[] filesAndFolders = file.list();

            for (String fname : filesAndFolders)
            {
                performRecursiveDelete(new File(file, fname));
            }

            if (file.isDirectory() && deleteFolders)
            {
                if (file != root)
                {
                    Log.d(TAG, "Del (D): " + file.getAbsolutePath());
                    file.delete();
                }
                else
                {
                    Log.d(TAG, "Did not delete root dir: " + file.getAbsolutePath());
                }
            }
            else
            {
                Log.d(TAG, "Skipping delete of folder: " + file.getAbsolutePath());
            }
        }
    }


    /**
     * Delete ONLY the files from a folder leaving all sub-folders untouched
     */
    private void performNonRecursiveDelete()
    {
        FilenameFilter fileOnlyFilter = new FilenameFilter()
            {

                @Override
                public boolean accept(File dir, String filename)
                {
                    if ((new File(dir, filename).isFile()))
                        return true;
                    else
                        return false;
                }
            };

        String[] files = root.list(fileOnlyFilter);
        File delFile;
        if (files != null)
        {
            for (String file : files)
            {
                delFile = new File(root, file);
                Log.d(TAG, "Del: " + delFile.getAbsolutePath());
                delFile.delete();
            }
        }
    }

    /**
     * Set service to inactive if we hit an error
     */
    private void disableService()
    {
        Log.w(TAG, "Disabling service");

        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext());
        if (prefs.getBoolean(getString(R.string.pref_service_notifyOnDelete_key), false))   //FIXME need separate bool pref for error disp
        {
            Toast.makeText(getApplicationContext(),
                    "Disabling ClearMeOut service due to an error. Is the target folder set and exist?",		//TODO strings.xml
                    Toast.LENGTH_LONG).show();
        }

        Editor editPrefs = prefs.edit();
        editPrefs.putBoolean(getString(R.string.pref_service_isActive_key), false);
        editPrefs.commit();

        Intent updateIntent = new Intent(getApplicationContext(),
                UpdateAlarmsService.class);
        startService(updateIntent);

        stopSelf();
    }

    //// unused binder

    @Override
    public IBinder onBind(Intent arg0)
    {
        return null;
    }


}
