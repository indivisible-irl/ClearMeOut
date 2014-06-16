package com.indivisible.clearmeout.data;

import java.sql.SQLException;
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

    public Profile createProfile(String profileName, boolean isActive)
    {
        ContentValues values = valuesToValues(profileName, isActive);
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
            Log.e(TAG, "Get: " + cursor.getCount() + " results found for id: " + id);
            cursor.close();
            return new Profile();
        }
        cursor.moveToFirst();
        Profile profile = cursorToProfile(cursor);
        cursor.close();
        return profile;
    }

    public boolean updateProfile(Profile profile)
    {
        long id = profile.getId();
        if (id < 0)
        {
            Log.e(TAG, "Update: Invalid id: " + id + " / " + profile.getName());
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
                            "Update: Didn't update, no id match: " + id + " / "
                                    + profile.getName());
                    return false;
                default:
                    Log.e(TAG,
                            "Update: Too many rows affected: " + id + " / "
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
                Log.e(TAG, "Delete: Didn't delete, no id match: " + id);
                return false;
            default:
                Log.e(TAG, "Delete: Too many rows affected: " + id);
                return false;
        }
    }


    ///////////////////////////////////////////////////////
    ////    util
    ///////////////////////////////////////////////////////

    private Profile cursorToProfile(Cursor cursor)
    {
        Profile profile = new Profile();
        profile.setId(cursor.getLong(0));
        profile.setName(cursor.getString(1));
        switch (cursor.getInt(2))
        {
            case 0:
                profile.setActive(false);
                break;
            case 1:
                profile.setActive(true);
                break;
            default:
                Log.e(TAG, "Error getting parsing int for boolean: " + cursor.getInt(2)
                        + " / " + profile.getName());
                break;
        }
        return profile;
    }

    private ContentValues profileToValues(Profile profile)
    {
        if (profile.getId() < 0)
        {
            // invalid id, not from db
            return valuesToValues(profile.getName(), profile.isActive());
        }
        else
        {
            return valuesToValues(profile.getId(), profile.getName(), profile.isActive());
        }
    }

    private ContentValues valuesToValues(String profileName, boolean isActive)
    {
        ContentValues values = new ContentValues();
        values.put(DbOpenHelper.COLUMN_PROFILE_NAME, profileName);
        values.put(DbOpenHelper.COLUMN_GENERIC_ISACTIVE, isActive);
        return values;
    }

    private ContentValues valuesToValues(long id, String profileName, boolean isActive)
    {
        ContentValues values = new ContentValues();
        values.put(DbOpenHelper.COLUMN_GENERIC_ID, id);
        values.put(DbOpenHelper.COLUMN_PROFILE_NAME, profileName);
        values.put(DbOpenHelper.COLUMN_GENERIC_ISACTIVE, isActive);
        return values;
    }


}
