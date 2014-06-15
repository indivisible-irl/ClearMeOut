package com.mburman.fileexplore;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;

import com.indivisible.clearmeout.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;


@SuppressWarnings("deprecation")	//FIXME comment this to view where to update to newer functionality
public class FileExplore extends Activity 
{

	// Stores names of traversed directories
	ArrayList<String> str = new ArrayList<String>();

	// Check if the first level of the directory structure is the one showing
	private Boolean firstLvl = true;

	private static final String TAG = "CMO:FileExplore";

	private Item[] fileList;
	private File path = new File(Environment.getExternalStorageDirectory() + "");
	private String chosenFile;
	private static final int DIALOG_LOAD_FILE = 1000;

	ListAdapter adapter;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{

		super.onCreate(savedInstanceState);

		loadFileList();

		showDialog(DIALOG_LOAD_FILE);
		Log.d(TAG, path.getAbsolutePath());

	}

	
	@Override
	public void onBackPressed()
	{
		Log.d(TAG, "Back key press. finish()");
    	Intent cancelIntent = new Intent();
    	setResult(0, cancelIntent);
        finish();
	}
	
	
	private void loadFileList()
	{
		try
		{
			path.mkdirs();
		}
		catch (SecurityException e)
		{
			Log.e(TAG, "unable to write to the sd card ");
		}

		// Checks whether path exists
		if (path.exists())
		{
			FilenameFilter filter = new FilenameFilter()
			{
				@Override
				public boolean accept(File dir, String filename)
				{
					File sel = new File(dir, filename);
					// Filters based on whether the file is hidden or not
					return (sel.isFile() || sel.isDirectory())
							&& !sel.isHidden();
				}
			};

			String[] fList = path.list(filter);
			fileList = new Item[fList.length];
			for (int i = 0; i < fList.length; i++)
			{
				fileList[i] = new Item(fList[i], R.drawable.file_icon);

				// Convert into file path
				File sel = new File(path, fList[i]);

				// Set drawables
				if (sel.isDirectory())
				{
					fileList[i].icon = R.drawable.directory_icon;
					Log.d("DIRECTORY", fileList[i].file);
				}
				else
				{
					Log.d("FILE", fileList[i].file);
				}
			}

			if (!firstLvl)
			{
				Item temp[] = new Item[fileList.length + 1];
				for (int i = 0; i < fileList.length; i++)
				{
					temp[i + 1] = fileList[i];
				}
				temp[0] = new Item("Up", R.drawable.directory_up);
				fileList = temp;
			}
		}
		else
		{
			Log.e(TAG, "path does not exist");
		}

		adapter = new ArrayAdapter<Item>(this,
				android.R.layout.select_dialog_item, android.R.id.text1,
				fileList)
		{
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				// creates view
				View view = super.getView(position, convertView, parent);
				TextView textView = (TextView) view
						.findViewById(android.R.id.text1);

				// put the image on the text view
				textView.setCompoundDrawablesWithIntrinsicBounds(
						fileList[position].icon, 0, 0, 0);

				// add margin between image and text (support various screen
				// densities)
				int dp5 = (int) (5 * getResources().getDisplayMetrics().density + 0.5f);
				textView.setCompoundDrawablePadding(dp5);

				return view;
			}
		};

	}

	private class Item
	{
		public String file;
		public int icon;

		public Item(String file, Integer icon)
		{
			this.file = file;
			this.icon = icon;
		}

		@Override
		public String toString()
		{
			return file;
		}
	}

	@Override
	protected Dialog onCreateDialog(int id)
	{
		Dialog dialog = null;
		AlertDialog.Builder builder = new Builder(this);

		if (fileList == null)
		{
			Log.e(TAG, "No files loaded");
			dialog = builder.create();
			
			return dialog;
		}

		switch (id)
		{
		case DIALOG_LOAD_FILE:
			builder.setTitle("Choose your file");
			builder.setAdapter(adapter, new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					chosenFile = fileList[which].file;
					File sel = new File(path + "/" + chosenFile);
					if (sel.isDirectory())
					{
						firstLvl = false;

						// Adds chosen directory to list
						str.add(chosenFile);
						fileList = null;
						path = new File(sel + "");

						loadFileList();

						removeDialog(DIALOG_LOAD_FILE);
						showDialog(DIALOG_LOAD_FILE);
						Log.d(TAG, path.getAbsolutePath());

					}

					// Checks if 'up' was clicked
					else if (chosenFile.equalsIgnoreCase("up") && !sel.exists())
					{

						// present directory removed from list
						String s = str.remove(str.size() - 1);

						// path modified to exclude present directory
						path = new File(path.toString().substring(0,
								path.toString().lastIndexOf(s)));
						fileList = null;

						// if there are no more directories in the list, then
						// its the first level
						if (str.isEmpty())
						{
							firstLvl = true;
						}
						loadFileList();

						removeDialog(DIALOG_LOAD_FILE);
						showDialog(DIALOG_LOAD_FILE);
						Log.d(TAG, path.getAbsolutePath());

					}
					// File picked
					else
					{
						Log.d("FileExplore", "File selected: " +sel.getAbsolutePath());
//						toastPath(sel);
						
						// Perform action with file picked
						Intent resultIntent = new Intent();
						resultIntent.putExtra("filepath", sel.getAbsolutePath());
						setResult(1, resultIntent);
						finish();
					}

				}
			});
			break;
			
		default:
			setResult(0, new Intent());
			finish();
		}
		
		dialog = builder.show();
		
		// listen for dismiss of dialog and finish() activity too
		dialog.setOnCancelListener(new DialogInterface.OnCancelListener()
		{
		    @Override
			public void onCancel(DialogInterface dialog)
		    {
		    	Log.d(TAG, "Dialog dismissed. finish() activity - return 0");
		    	Intent cancelIntent = new Intent();
		    	setResult(0, cancelIntent);
		        FileExplore.this.finish();
		    }
		});
		
		return dialog;
	}
	
	
//	private void toastPath(File f)
//	{
//		Toast.makeText(this, f.getAbsolutePath(), Toast.LENGTH_SHORT).show();
//	}

}