package com.indivisible.clearmeout.activity;

import java.io.File;
import java.io.IOException;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import com.indivisible.clearmeout.R;
import com.indivisible.clearmeout.service.DeleteService;

public class StartActivity
        extends Activity
        implements OnClickListener
{

    //TODO runs on boot (and app close?)
    //TODO only need one alarm id (repeating)


    //// data
    private Button bPref, bDelete, bHelp, bRefill;
    private TextView tvFolderHint;

    private String folder;

    private static final String TAG = "CMO:StartActivity";
    private boolean enableRefill = true;					// will show a button to populate target folder for testing


    //// default Activity methods

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        initViews();
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        folder = prefs.getString(getString(R.string.pref_delete_targetFolder_key),
                getString(R.string.pref_delete_targetFolder_default));

        tvFolderHint.setText(String.format(getString(R.string.start_tv_target_folder),
                folder));
    }


    //// onCLick

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.start_bPref:
                Log.d(TAG, "Opening PreferencesActivity...");
                Intent openPrefIntent = new Intent(this, MainPrefsActivity.class);
                startActivity(openPrefIntent);
                break;

            case R.id.start_bDelete:
                performDelete();
                break;

            case R.id.start_bHelp:
                showHelp();
                break;

            case R.id.start_bFillDir:
                try
                {
                    refillFolder();
                    Log.d(TAG, "Repopulated target folder with spam for testing");
                }
                catch (IOException e)
                {
                    Log.e(TAG, "Error while populating target folder");
                    e.printStackTrace();
                    finish();
                }

                break;
        }

    }


    //// private methods

    /**
     * Initialise the layout's Views
     */
    private void initViews()
    {
        tvFolderHint = (TextView) findViewById(R.id.start_tvFolderHint);
        bPref = (Button) findViewById(R.id.start_bPref);
        bDelete = (Button) findViewById(R.id.start_bDelete);
        bHelp = (Button) findViewById(R.id.start_bHelp);
        bRefill = (Button) findViewById(R.id.start_bFillDir);

        bPref.setOnClickListener(this);
        bDelete.setOnClickListener(this);
        bHelp.setOnClickListener(this);
        bRefill.setOnClickListener(this);

        // remove the refill button from release versions
        if (!enableRefill) bRefill.setVisibility(View.GONE);
    }

    /**
     * Start the DeleteService to perform the folder clear (recursive
     * depending on SharedPreference)
     */
    private void performDelete()				//TODO move to new class
    {
        Log.d(TAG, "Starting DeleteService...");
        Intent deleteIntent = new Intent(this, DeleteService.class);
        startService(deleteIntent);
    }

    /**
     * Display the help screen
     */
    private void showHelp()
    {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle(getString(R.string.help_title));
        dialogBuilder.setMessage(getString(R.string.help_text));
        dialogBuilder.setIcon(R.drawable.ic_launcher);

        Dialog helpDialog = dialogBuilder.create();
        helpDialog.show();

    }

    /**
     * Method to populate the target folder with folders and files for testing
     * purposes
     * 
     * @throws IOException
     */
    private void refillFolder()
        throws IOException
    {
        File root = new File(folder);

        String[] newFolders = {
                "0", "1", "2"
        };
        String[] newFiles = {
                "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l"
        };

        File useFolder;
        File newFile;
        for (int i = 0; i < newFiles.length; i++)
        {
            if ((i / 3) < 1)
            {
                useFolder = new File(root, newFolders[0]);
                useFolder.mkdir();
                newFile = new File(useFolder, newFiles[i] + ".txt");
                newFile.createNewFile();
            }
            else if ((i / 3) < 2)
            {
                useFolder = new File(root, newFolders[1]);
                useFolder.mkdir();
                newFile = new File(useFolder, newFiles[i] + ".txt");
                newFile.createNewFile();
            }
            else if ((i / 3) < 3)
            {
                useFolder = new File(root, newFolders[2]);
                useFolder.mkdir();
                newFile = new File(useFolder, newFiles[i] + ".txt");
                newFile.createNewFile();
            }
            else
            {
                newFile = new File(root, newFiles[i] + ".txt");
                newFile.createNewFile();
            }
        }
    }

}
