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
public class TargetSource
{

    ///////////////////////////////////////////////////////
    ////    data
    ///////////////////////////////////////////////////////

    private SQLiteDatabase db;
    private DbOpenHelper dbHelper;

    private static final int INDEX_ID = 0;
    private static final int INDEX_FK = 1;
    private static final int INDEX_ROOTDIR = 2;
    private static final int INDEX_RECURSIVE = 3;
    private static final int INDEX_DELETEDIRS = 4;

    private static final String TAG = "TargetSrc";


    ///////////////////////////////////////////////////////
    ////    init
    ///////////////////////////////////////////////////////

    public TargetSource(Context context)
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

    public Target createOrUpdateTarget(Target target)
    {
        if (target.getId() < 0)
        {
            return createTarget(target);
        }
        else
        {
            updateTarget(target);
            return target;
        }
    }

    public Target createTarget(Target target)
    {
        return createTarget(target.getParentProfileId(),
                target.getRootDirectory(),
                target.isRecursive(),
                target.doDeleteDirectories());
    }

    public Target createTarget(long parentId,
                               String rootDir,
                               boolean isRecursive,
                               boolean doDeleteDirs)
    {
        ContentValues values = fieldsToValues(parentId, rootDir, isRecursive, doDeleteDirs);
        long id = db.insert(DbOpenHelper.TABLE_TARGETS, null, values);
        return getTarget(id);
    }

    public Target getTarget(Target target)
    {
        return getTarget(target.getId());
    }

    public Target getTarget(long id)
    {
        Cursor cursor = db.query(DbOpenHelper.TABLE_TARGETS,
                DbOpenHelper.ALL_COLUMNS_TARGETS,
                DbOpenHelper.COLUMN_GENERIC_ID + " = " + id,
                null,
                null,
                null,
                null);
        if (cursor.getCount() != 1)
        {
            Log.e(TAG, "(Get) " + cursor.getCount() + " results found for id: " + id);
            cursor.close();
            return new Target();
        }

        Target target;
        if (cursor.moveToFirst())
        {
            target = cursorToTarget(cursor);
        }
        else
        {
            target = new Target();
        }
        cursor.close();
        return target;
    }

    public Target getProfileTarget(long parentProfileId)
    {
        Cursor cursor = db.query(DbOpenHelper.TABLE_TARGETS,
                DbOpenHelper.ALL_COLUMNS_TARGETS,
                DbOpenHelper.COLUMN_GENERIC_PARENTID + " = " + parentProfileId,
                null,
                null,
                null,
                null);
        if (cursor.getCount() != 1)
        {
            Log.e(TAG, "(getProfileTarget) " + cursor.getCount()
                    + " results found for profile id: " + parentProfileId);
            cursor.close();
            return new Target();
        }

        Target target;
        if (cursor.moveToFirst())
        {
            target = cursorToTarget(cursor);
        }
        else
        {
            target = new Target();
        }
        cursor.close();
        return target;
    }

    public boolean updateTarget(Target target)
    {
        long id = target.getId();
        if (id < 0)
        {
            Log.e(TAG, "(Update) Invalid id: " + id + " / " + target.getRootDirectory());
            return false;
        }
        else
        {
            ContentValues values = targetToValues(target);
            int rowsAffected = db.update(DbOpenHelper.TABLE_TARGETS,
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
                                    + target.getRootDirectory());
                    return false;
                default:
                    Log.e(TAG,
                            "(Update) Too many rows affected: " + id + " / "
                                    + target.getRootDirectory());
                    return false;
            }
        }
    }

    public boolean deleteTarget(Target target)
    {
        return deleteTarget(target.getId());
    }

    public boolean deleteTarget(long id)
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

    public int deleteAllProfileTargets(Profile profile)
    {
        return deleteAllProfileTargets(profile.getId());
    }

    public int deleteAllProfileTargets(long parentProfileId)
    {
        if (parentProfileId < 0)
        {
            Log.e(TAG, "(deleteAll) Invalid id: " + parentProfileId);
            return -1;
        }
        int rowsDeleted = db.delete(DbOpenHelper.TABLE_TARGETS,
                DbOpenHelper.COLUMN_GENERIC_PARENTID + " = " + parentProfileId,
                null);
        return rowsDeleted;
    }

    ///////////////////////////////////////////////////////
    ////    util
    ///////////////////////////////////////////////////////

    private Target cursorToTarget(Cursor cursor)
    {
        Target target = new Target();
        target.setId(cursor.getLong(INDEX_ID));
        target.setParentProfileId(cursor.getLong(INDEX_FK));
        target.setRootDirectory(cursor.getString(INDEX_ROOTDIR));
        switch (cursor.getInt(INDEX_RECURSIVE))
        {
            case 0:
                target.setRecursive(false);
                break;
            case 1:
                target.setRecursive(true);
                break;
            default:
                Log.e(TAG,
                        "(recursive) Error getting parsing int for boolean: "
                                + cursor.getInt(INDEX_RECURSIVE) + " / "
                                + target.getRootDirectory());
                break;
        }
        switch (cursor.getInt(INDEX_DELETEDIRS))
        {
            case 0:
                target.setDeleteDirectories(false);
                break;
            case 1:
                target.setDeleteDirectories(true);
                break;
            default:
                Log.e(TAG,
                        "(deleteDirs) Error getting parsing int for boolean: "
                                + cursor.getInt(INDEX_DELETEDIRS) + " / "
                                + target.getRootDirectory());
                break;
        }
        return target;
    }

    private ContentValues targetToValues(Target target)
    {
        if (target.getId() < 0)
        {
            // invalid id, not from db
            return fieldsToValues(target.getParentProfileId(),
                    target.getRootDirectory(),
                    target.isRecursive(),
                    target.doDeleteDirectories());
        }
        else
        {
            return fieldsToValues(target.getId(),
                    target.getParentProfileId(),
                    target.getRootDirectory(),
                    target.isRecursive(),
                    target.doDeleteDirectories());
        }
    }

    private ContentValues fieldsToValues(long parentId,
                                         String rootDir,
                                         boolean isRecursive,
                                         boolean doDeleteDirs)
    {
        ContentValues values = new ContentValues();
        values.put(DbOpenHelper.COLUMN_GENERIC_PARENTID, parentId);
        values.put(DbOpenHelper.COLUMN_TARGET_DIRECTORY, rootDir);
        values.put(DbOpenHelper.COLUMN_TARGET_ISRECURSIVE, isRecursive);
        values.put(DbOpenHelper.COLUMN_TARGET_DELETEDIRS, doDeleteDirs);
        return values;
    }

    private ContentValues fieldsToValues(long id,
                                         long parentId,
                                         String rootDir,
                                         boolean isRecursive,
                                         boolean doDeleteDirs)
    {
        ContentValues values = fieldsToValues(parentId, rootDir, isRecursive, doDeleteDirs);
        values.put(DbOpenHelper.COLUMN_GENERIC_ID, id);
        return values;
    }
}
