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
public class FilterSource
{

    ///////////////////////////////////////////////////////
    ////    data
    ///////////////////////////////////////////////////////

    private SQLiteDatabase db;
    private DbOpenHelper dbHelper;

    private static final int INDEX_ID = 0;
    private static final int INDEX_FK = 1;
    private static final int INDEX_TYPE = 2;
    private static final int INDEX_ACTIVE = 3;
    private static final int INDEX_WHITELIST = 4;
    private static final int INDEX_DATA = 5;

    private static final String TAG = "FilterSrc";


    ///////////////////////////////////////////////////////
    ////    init
    ///////////////////////////////////////////////////////

    public FilterSource(Context context)
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

    public Filter createOrUpdateFilter(Filter filter)
    {
        if (filter.getId() < 0)
        {
            return createFilter(filter);
        }
        else
        {
            updateFilter(filter);
            return filter;
        }
    }

    public Filter createFilter(Filter filter)
    {
        return createFilter(filter.getParentProfileId(),
                filter.getFilterType(),
                filter.isActive(),
                filter.isWhitelist(),
                filter.getData());
    }

    public Filter createFilter(long parentProfileID,
                               FilterType filterType,
                               boolean isActive,
                               boolean isWhitelist,
                               String data)
    {
        ContentValues values = fieldsToValues(parentProfileID,
                filterType,
                isActive,
                isWhitelist,
                data);
        long id = db.insert(DbOpenHelper.TABLE_FILTERS, null, values);
        return getFilter(id);
    }

    public Filter getFilter(Filter filter)
    {
        return getFilter(filter.getId());
    }

    public Filter getFilter(long id)
    {
        Cursor cursor = db.query(DbOpenHelper.TABLE_FILTERS,
                DbOpenHelper.ALL_COLUMNS_FILTERS,
                DbOpenHelper.COLUMN_GENERIC_ID + " = " + id,
                null,
                null,
                null,
                null);
        if (cursor.getCount() != 1)
        {
            Log.e(TAG, "Get: " + cursor.getCount() + " results found for id: " + id);
            cursor.close();
            return new Filter();
        }

        Filter filter;
        if (cursor.moveToFirst())
        {
            filter = cursorToFilter(cursor);
        }
        else
        {
            filter = new Filter();
        }
        cursor.close();
        return filter;
    }

    public List<Filter> getProfileFilters(long parentProfileId)
    {
        Cursor cursor = db.query(DbOpenHelper.TABLE_FILTERS,
                DbOpenHelper.ALL_COLUMNS_FILTERS,
                DbOpenHelper.COLUMN_GENERIC_PARENTID + " = " + parentProfileId,
                null,
                null,
                null,
                null);
        Log.i(TAG, "(getAll) Number of results: " + cursor.getCount());

        List<Filter> allProfileFilters = new ArrayList<Filter>();
        if (cursor.moveToFirst())
        {
            while (!cursor.isAfterLast())
            {
                allProfileFilters.add(cursorToFilter(cursor));
                cursor.moveToNext();
            }
        }
        return allProfileFilters;
    }

    public boolean updateFilter(Filter filter)
    {
        long id = filter.getId();
        if (id < 0)
        {
            Log.e(TAG, "(Update) Invalid id: " + id + " / " + filter.getData());
            return false;
        }
        else
        {
            ContentValues values = filterToValues(filter);
            int rowsAffected = db.update(DbOpenHelper.TABLE_FILTERS,
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
                                    + filter.getData());
                    return false;
                default:
                    Log.e(TAG,
                            "(Update) Too many rows affected: " + id + " / "
                                    + filter.getData());
                    return false;
            }
        }
    }

    public boolean deleteFilter(Filter filter)
    {
        return deleteFilter(filter.getId());
    }

    public boolean deleteFilter(long id)
    {
        int rowsDeleted = db.delete(DbOpenHelper.TABLE_FILTERS, DbOpenHelper.COLUMN_GENERIC_ID
                + " = " + id, null);
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

    public int deleteAllProfileFilters(Filter filter)
    {
        return deleteAllProfileFilters(filter.getId());
    }

    public int deleteAllProfileFilters(long parentProfileId)
    {
        if (parentProfileId < 0)
        {
            Log.e(TAG, "(deleteAll) Invalid id: " + parentProfileId);
            return -1;
        }
        int rowsDeleted = db.delete(DbOpenHelper.TABLE_FILTERS,
                DbOpenHelper.COLUMN_GENERIC_PARENTID + " = " + parentProfileId,
                null);
        return rowsDeleted;
    }


    ///////////////////////////////////////////////////////
    ////    util
    ///////////////////////////////////////////////////////

    private Filter cursorToFilter(Cursor cursor)
    {
        Filter filter = new Filter();
        filter.setId(cursor.getLong(INDEX_ID));
        filter.setParentProfileId(cursor.getLong(INDEX_FK));
        filter.setFilterType(FilterType.valueOf(cursor.getString(INDEX_TYPE)));
        filter.setData(cursor.getString(INDEX_DATA));
        switch (cursor.getInt(INDEX_ACTIVE))
        {
            case 0:
                filter.setActive(false);
                break;
            case 1:
                filter.setActive(true);
                break;
            default:
                Log.e(TAG,
                        "(isActive) Error getting parsing int for boolean: "
                                + cursor.getInt(INDEX_ACTIVE) + " / " + filter.getData());
                break;
        }
        switch (cursor.getInt(INDEX_WHITELIST))
        {
            case 0:
                filter.setWhitelist(false);
                break;
            case 1:
                filter.setWhitelist(true);
                break;
            default:
                Log.e(TAG,
                        "(whitelist) Error getting parsing int for boolean: "
                                + cursor.getInt(INDEX_WHITELIST) + " / " + filter.getData());
                break;
        }
        return filter;
    }

    private ContentValues filterToValues(Filter filter)
    {
        if (filter.getId() < 0)
        {
            // invalid id, not from db
            return fieldsToValues(filter.getParentProfileId(),
                    filter.getFilterType(),
                    filter.isActive(),
                    filter.isWhitelist(),
                    filter.getData());
        }
        else
        {
            return fieldsToValues(filter.getId(),
                    filter.getParentProfileId(),
                    filter.getFilterType(),
                    filter.isActive(),
                    filter.isWhitelist(),
                    filter.getData());
        }
    }

    private ContentValues fieldsToValues(long parentId,
                                         FilterType filterType,
                                         boolean isActive,
                                         boolean isWhitelist,
                                         String data)
    {
        ContentValues values = new ContentValues();
        values.put(DbOpenHelper.COLUMN_GENERIC_PARENTID, parentId);
        values.put(DbOpenHelper.COLUMN_FILTER_TYPE, filterType.name());
        values.put(DbOpenHelper.COLUMN_FILTER_ISWHITELIST, isWhitelist);
        values.put(DbOpenHelper.COLUMN_FILTER_DATA, data);
        return values;
    }

    private ContentValues fieldsToValues(long id,
                                         long parentId,
                                         FilterType filterType,
                                         boolean isActive,
                                         boolean isWhitelist,
                                         String data)
    {
        ContentValues values = fieldsToValues(parentId,
                filterType,
                isActive,
                isWhitelist,
                data);
        values.put(DbOpenHelper.COLUMN_GENERIC_ID, id);
        return values;
    }
}
