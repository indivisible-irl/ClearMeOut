package com.indivisible.clearmeout.data;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.indivisible.clearmeout.database.DbOpenHelper;

/**
 * Created by indiv on 15/06/14.
 */
public class ProfileSource
{

    ///////////////////////////////////////////////////////
    ////    data
    ///////////////////////////////////////////////////////

    private SQLiteDatabase db;
    private DbOpenHelper dbHelper;

    private static final int INDEX_ID = 0;
    private static final int INDEX_NAME = 1;
    private static final int INDEX_ISACTIVE = 2;

    private static final String TAG = "ProfileSrc";


    ///////////////////////////////////////////////////////
    ////    init
    ///////////////////////////////////////////////////////

    public ProfileSource(Context context)
    {
        this.dbHelper = new DbOpenHelper(context);
    }


    ///////////////////////////////////////////////////////
    ////    open & close
    ///////////////////////////////////////////////////////

    public void openReadable()
        throws SQLException
    {
        db = dbHelper.getReadableDatabase();
    }

    public void openWriteable()
        throws SQLException
    {
        db = dbHelper.getWritableDatabase();
    }

    public void close()
    {
        db.close();
    }


    ///////////////////////////////////////////////////////
    ////    CRUD
    ///////////////////////////////////////////////////////

    public Profile createOrUpdateProfile(Profile profile)
    {
        if (profile.getId() < 0)
        {
            return createProfile(profile);
        }
        else
        {
            updateProfile(profile);
            return profile;
        }
    }

    public Profile createProfile(Profile profile)
    {
        return createProfile(profile.getName(), profile.isActive());
    }

    public Profile createProfile(String profileName, boolean isActive)
    {
        ContentValues values = fieldsToValues(profileName, isActive);
        long id = db.insert(DbOpenHelper.TABLE_PROFILES, null, values);
        return getProfile(id);
    }

    public Profile getProfile(long id)
    {
        Cursor cursor = db.query(DbOpenHelper.TABLE_PROFILES,
                DbOpenHelper.ALL_COLUMNS_PROFILES,
                DbOpenHelper.COLUMN_GENERIC_ID + " = " + id,
                null,
                null,
                null,
                null);
        if (cursor.getCount() != 1)
        {
            Log.e(TAG, "(Get) " + cursor.getCount() + " results found for id: " + id);
            cursor.close();
            return new Profile();
        }

        Profile profile;
        if (cursor.moveToFirst())
        {
            profile = cursorToProfile(cursor);
        }
        else
        {
            profile = new Profile();
        }
        cursor.close();
        return profile;
    }

    public List<Profile> getAllProfiles()
    {
        Cursor cursor = db.query(DbOpenHelper.TABLE_PROFILES,
                DbOpenHelper.ALL_COLUMNS_PROFILES,
                null,
                null,
                null,
                null,
                null);
        Log.i(TAG, "(getAll) Number of results: " + cursor.getCount());

        List<Profile> allProfiles = new ArrayList<Profile>();
        if (cursor.moveToFirst())
        {
            while (!cursor.isAfterLast())
            {
                allProfiles.add(cursorToProfile(cursor));
                cursor.moveToNext();
            }
        }
        return allProfiles;
    }

    public boolean updateProfile(Profile profile)
    {
        long id = profile.getId();
        if (id < 0)
        {
            Log.e(TAG, "(Update) Invalid id: " + id + " / " + profile.getName());
            return false;
        }
        else
        {
            ContentValues values = profileToValues(profile);
            int rowsAffected = db.update(DbOpenHelper.TABLE_PROFILES,
                    values,
                    DbOpenHelper.COLUMN_GENERIC_ID + " = " + id,
                    null);
            switch (rowsAffected)
            {
                case 1:
                    return true;
                case 0:
                    Log.e(TAG,
                            "(Update) Didn't update, no id match: " + id + " / "
                                    + profile.getName());
                    return false;
                default:
                    Log.e(TAG,
                            "(Update) Too many rows affected: " + id + " / "
                                    + profile.getName());
                    return false;
            }
        }
    }

    public boolean deleteProfile(Profile profile)
    {
        return deleteProfile(profile.getId());
    }

    public boolean deleteProfile(long id)
    {
        int rowsDeleted = db.delete(DbOpenHelper.TABLE_PROFILES,
                DbOpenHelper.COLUMN_GENERIC_ID + " = " + id,
                null);
        switch (rowsDeleted)
        {
            case 1:
                return true;
            case 0:
                Log.e(TAG, "(Delete) Didn't delete, no id match: " + id);
                return false;
            default:
                Log.e(TAG, "(Delete) Too many rows affected: " + id);
                return false;
        }
    }

    public int deleteAllProfiles()
    {
        int rowsDeleted = db.delete(DbOpenHelper.TABLE_PROFILES, null, null);
        return rowsDeleted;
    }


    ///////////////////////////////////////////////////////
    ////    util
    ///////////////////////////////////////////////////////

    private Profile cursorToProfile(Cursor cursor)
    {
        Profile profile = new Profile();
        profile.setId(cursor.getLong(INDEX_ID));
        profile.setName(cursor.getString(INDEX_NAME));
        switch (cursor.getInt(INDEX_ISACTIVE))
        {
            case 0:
                profile.setActive(false);
                break;
            case 1:
                profile.setActive(true);
                break;
            default:
                Log.e(TAG,
                        "(isActive) Error parsing int for boolean: "
                                + cursor.getInt(INDEX_ISACTIVE) + " / " + profile.getName());
                break;
        }
        return profile;
    }

    private ContentValues profileToValues(Profile profile)
    {
        if (profile.getId() < 0)
        {
            // invalid id, not from db
            return fieldsToValues(profile.getName(), profile.isActive());
        }
        else
        {
            return fieldsToValues(profile.getId(), profile.getName(), profile.isActive());
        }
    }

    private ContentValues fieldsToValues(String profileName, boolean isActive)
    {
        ContentValues values = new ContentValues();
        values.put(DbOpenHelper.COLUMN_PROFILE_NAME, profileName);
        values.put(DbOpenHelper.COLUMN_GENERIC_ISACTIVE, isActive);
        return values;
    }

    private ContentValues fieldsToValues(long id, String profileName, boolean isActive)
    {
        ContentValues values = fieldsToValues(profileName, isActive);
        values.put(DbOpenHelper.COLUMN_GENERIC_ID, id);
        return values;
    }


}
