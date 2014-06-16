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
public class FilterSource
{

    ///////////////////////////////////////////////////////
    ////    data
    ///////////////////////////////////////////////////////

    private SQLiteDatabase db;
    private DbOpenHelper dbHelper;

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

    public Filter createFilter()
    {
        return null;
    }

    public Filter getFilter(Target target)
    {
        return getFilter(target.getId());
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
        cursor.moveToFirst();
        Filter filter = cursorToFilter(cursor);
        cursor.close();
        return filter;
    }


    ///////////////////////////////////////////////////////
    ////    util
    ///////////////////////////////////////////////////////

    private Filter cursorToFilter(Cursor cursor)
    {
        Filter filter = new Filter();
        filter.setId(cursor.getLong(0));
        filter.setParentProfileId(cursor.getLong(1));
        filter.setFilterType(FilterType.valueOf(cursor.getString(2)));
        switch (cursor.getInt(3))
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
                                + cursor.getInt(3) + " / " + filter.getData());
                break;
        }
        filter.setData(cursor.getString(4));
        return filter;
    }

    private ContentValues filterToValues(Filter filter)
    {
        if (filter.getId() < 0)
        {
            // invalid id, not from db
            return valuesToValues(filter.getParentProfileId(),
                    filter.getFilterType(),
                    filter.isWhitelist(),
                    filter.getData());
        }
        else
        {
            return valuesToValues(filter.getId(),
                    filter.getParentProfileId(),
                    filter.getFilterType(),
                    filter.isWhitelist(),
                    filter.getData());
        }
    }

    private ContentValues valuesToValues(long parentId,
                                         FilterType filterType,
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

    private ContentValues valuesToValues(long id,
                                         long parentId,
                                         FilterType filterType,
                                         boolean isWhitelist,
                                         String data)
    {
        ContentValues values = new ContentValues();
        values.put(DbOpenHelper.COLUMN_GENERIC_ID, id);
        values.put(DbOpenHelper.COLUMN_GENERIC_PARENTID, parentId);
        values.put(DbOpenHelper.COLUMN_FILTER_TYPE, filterType.name());
        values.put(DbOpenHelper.COLUMN_FILTER_ISWHITELIST, isWhitelist);
        values.put(DbOpenHelper.COLUMN_FILTER_DATA, data);
        return values;
    }
}
