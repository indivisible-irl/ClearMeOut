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

    private static final String TAG = "TargetSource";


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

    public Target createTarget(long parentId,
                               String rootDir,
                               boolean isRecursive,
                               boolean doDeleteDirs)
    {
        ContentValues values = valuesToValues(parentId, rootDir, isRecursive, doDeleteDirs);
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
            Log.v(TAG, "Get: " + cursor.getCount() + " results found for id: " + id);
            cursor.close();
            return new Target();
        }
        cursor.moveToFirst();
        Target target = cursorToTarget(cursor);
        cursor.close();
        return target;
    }

    public boolean updateTarget(Target target)
    {
        long id = target.getId();
        if (id < 0)
        {
            Log.e(TAG, "Update: Invalid id: " + id + " / " + target.getRootDirectory());
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
                            "Update: Didn't update, no id match: " + id + " / "
                                    + target.getRootDirectory());
                    return false;
                default:
                    Log.e(TAG,
                            "Update: Too many rows affected: " + id + " / "
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

    private Target cursorToTarget(Cursor cursor)
    {
        Target target = new Target();
        target.setId(cursor.getLong(0));
        target.setParentProfileId(cursor.getLong(1));
        target.setRootDirectory(cursor.getString(2));
        switch (cursor.getInt(3))
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
                                + cursor.getInt(3) + " / " + target.getRootDirectory());
                break;
        }
        switch (cursor.getInt(4))
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
                                + cursor.getInt(4) + " / " + target.getRootDirectory());
                break;
        }
        return target;
    }

    private ContentValues targetToValues(Target target)
    {
        if (target.getId() < 0)
        {
            // invalid id, not from db
            return valuesToValues(target.getParentProfileId(),
                    target.getRootDirectory(),
                    target.isRecursive(),
                    target.doDeleteDirectories());
        }
        else
        {
            return valuesToValues(target.getId(),
                    target.getParentProfileId(),
                    target.getRootDirectory(),
                    target.isRecursive(),
                    target.doDeleteDirectories());
        }
    }

    private ContentValues valuesToValues(long parentId,
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

    private ContentValues valuesToValues(long id,
                                         long parentId,
                                         String rootDir,
                                         boolean isRecursive,
                                         boolean doDeleteDirs)
    {
        ContentValues values = new ContentValues();
        values.put(DbOpenHelper.COLUMN_GENERIC_ID, id);
        values.put(DbOpenHelper.COLUMN_GENERIC_PARENTID, parentId);
        values.put(DbOpenHelper.COLUMN_TARGET_DIRECTORY, rootDir);
        values.put(DbOpenHelper.COLUMN_TARGET_ISRECURSIVE, isRecursive);
        values.put(DbOpenHelper.COLUMN_TARGET_DELETEDIRS, doDeleteDirs);
        return values;
    }
}
