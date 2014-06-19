package com.indivisible.clearmeout.data;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.indivisible.clearmeout.database.DbOpenHelper;

/**
 * Created by indiv on 16/06/14.
 */
public class IntervalSource
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
    private static final int INDEX_STRICT = 4;
    private static final int INDEX_LASTRUN = 5;
    private static final int INDEX_DATA1 = 6;
    private static final int INDEX_DATA2 = 7;
    private static final int INDEX_DATA3 = 8;
    private static final int INDEX_DATA4 = 9;

    private static final String[] DATA_KEYS = {
            DbOpenHelper.COLUMN_INTERVAL_DATA1, DbOpenHelper.COLUMN_INTERVAL_DATA2,
            DbOpenHelper.COLUMN_INTERVAL_DATA3, DbOpenHelper.COLUMN_INTERVAL_DATA4
    };
    private static final int[] DATA_INDEXES = {
            INDEX_DATA1, INDEX_DATA2, INDEX_DATA3, INDEX_DATA4
    };

    private static final String TAG = "IntervalSrc";


    ///////////////////////////////////////////////////////
    ////    init
    ///////////////////////////////////////////////////////

    public IntervalSource(Context context)
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

    public Interval createOrUpdateInterval(Interval interval)
    {
        if (interval.getId() < 0)
        {
            return createInterval(interval);
        }
        else
        {
            updateInterval(interval);
            return interval;
        }
    }

    public Interval createInterval(Interval interval)
    {
        return createInterval(interval.getParentProfileId(),
                interval.getIntervalType(),
                interval.isActive(),
                interval.isStrictAlarm(),
                interval.getLastRunMillis(),
                interval.getData());
    }

    public Interval createInterval(long parentProfileId,
                                   IntervalType intervalType,
                                   boolean isActive,
                                   boolean isStrictAlarm,
                                   long lastRunMillis,
                                   String[] data)
    {
        ContentValues values = fieldsToValues(parentProfileId,
                intervalType,
                isActive,
                isStrictAlarm,
                lastRunMillis,
                data);
        long id = db.insert(DbOpenHelper.TABLE_INTERVALS, null, values);
        return getInterval(id);
    }

    public Interval getInterval(Interval interval)
    {
        return getInterval(interval.getId());
    }

    public Interval getInterval(long id)
    {
        Cursor cursor = db.query(DbOpenHelper.TABLE_INTERVALS,
                DbOpenHelper.ALL_COLUMNS_INTERVALS,
                DbOpenHelper.COLUMN_GENERIC_ID + " = " + id,
                null,
                null,
                null,
                null);
        if (cursor.getCount() != 1)
        {
            Log.e(TAG, "(Get) " + cursor.getCount() + " results found for id: " + id);
            cursor.close();
            return new Interval();
        }

        Interval interval;
        if (cursor.moveToFirst())
        {
            interval = cursorToInterval(cursor);
        }
        else
        {
            interval = new Interval();
        }
        cursor.close();
        return interval;
    }

    public List<Interval> getProfileIntervals(long parentProfileId)
    {
        Cursor cursor = db.query(DbOpenHelper.TABLE_INTERVALS,
                DbOpenHelper.ALL_COLUMNS_INTERVALS,
                DbOpenHelper.COLUMN_GENERIC_PARENTID + " = " + parentProfileId,
                null,
                null,
                null,
                null);
        Log.i(TAG, "(getAll) Number of results: " + cursor.getCount());

        List<Interval> allProfileIntervals = new ArrayList<Interval>();
        if (cursor.moveToFirst())
        {
            while (!cursor.isAfterLast())
            {
                allProfileIntervals.add(cursorToInterval(cursor));
                cursor.moveToNext();
            }
        }
        return allProfileIntervals;
    }

    public long[] getProfileIntervalCounts(long parentProfileId)
    {
        long[] intervalCounts = new long[2];
        intervalCounts[0] = getProfileIntervalCountTotal(parentProfileId);
        intervalCounts[1] = getProfileIntervalCountActive(parentProfileId);
        return intervalCounts;
    }

    public long getProfileIntervalCountTotal(long parentProfileId)
    {
        long count = DatabaseUtils.queryNumEntries(db,
                DbOpenHelper.TABLE_INTERVALS,
                DbOpenHelper.COLUMN_GENERIC_PARENTID + " = " + parentProfileId);
        return count;
    }

    public long getProfileIntervalCountActive(long parentProfileId)
    {
        long count = DatabaseUtils.queryNumEntries(db,
                DbOpenHelper.TABLE_INTERVALS,
                DbOpenHelper.COLUMN_GENERIC_PARENTID + " = " + parentProfileId + " and "
                        + DbOpenHelper.COLUMN_GENERIC_ISACTIVE + " = 1");
        return count;
    }

    public boolean updateInterval(Interval interval)
    {
        long id = interval.getId();
        if (id < 0)
        {
            Log.e(TAG, "(Update) Invalid id: " + id + " / "
                    + interval.getIntervalType().name());
            return false;
        }
        else
        {
            ContentValues values = intervalToValues(interval);
            int rowsAffected = db.update(DbOpenHelper.TABLE_INTERVALS,
                    values,
                    DbOpenHelper.COLUMN_GENERIC_ID + " = " + id,
                    null);
            switch (rowsAffected)
            {
                case 1:
                    return true;
                case 0:
                    Log.e(TAG, "(Update) Didn't update, no id match: " + id + " / "
                            + interval.getIntervalType().name());
                    return false;
                default:
                    Log.e(TAG, "(Update) Too many rows affected: " + id + " / "
                            + interval.getIntervalType().name());
                    return false;
            }
        }
    }

    public boolean deleteInterval(Interval interval)
    {
        return deleteInterval(interval.getId());
    }

    public boolean deleteInterval(long id)
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

    public int deleteAllProfileIntervals(Interval interval)
    {
        return deleteAllProfileIntervals(interval.getId());
    }

    public int deleteAllProfileIntervals(long parentProfileId)
    {
        if (parentProfileId < 0)
        {
            Log.e(TAG, "(deleteAll) Invalid id: " + parentProfileId);
            return -1;
        }
        int rowsDeleted = db.delete(DbOpenHelper.TABLE_INTERVALS,
                DbOpenHelper.COLUMN_GENERIC_PARENTID + " = " + parentProfileId,
                null);
        return rowsDeleted;
    }

    ///////////////////////////////////////////////////////
    ////    util
    ///////////////////////////////////////////////////////

    private Interval cursorToInterval(Cursor cursor)
    {
        Interval interval = new Interval();
        interval.setId(cursor.getLong(INDEX_ID));
        interval.setParentProfileId(cursor.getLong(INDEX_FK));
        interval.setIntervalType(IntervalType.valueOf(cursor.getString(INDEX_TYPE)));
        switch (cursor.getInt(INDEX_ACTIVE))
        {
            case 0:
                interval.setActive(false);
                break;
            case 1:
                interval.setActive(true);
                break;
            default:
                Log.e(TAG,
                        "(isActive) Error getting parsing int for boolean: "
                                + cursor.getInt(3) + " / " + interval.getIntervalType().name());
                break;
        }
        switch (cursor.getInt(INDEX_STRICT))
        {
            case 0:
                interval.setStrictAlarm(false);
                break;
            case 1:
                interval.setStrictAlarm(true);
                break;
            default:
                Log.e(TAG,
                        "(strict) Error getting parsing int for boolean: " + cursor.getInt(3)
                                + " / " + interval.getIntervalType().name());
                break;
        }
        interval.setLastRunMillis(cursor.getLong(INDEX_LASTRUN));
        interval.setData(getDataFromCursor(cursor));
        return interval;
    }

    private ContentValues intervalToValues(Interval interval)
    {
        if (interval.getId() < 0)
        {
            // invalid id, not from db
            return fieldsToValues(interval.getParentProfileId(),
                    interval.getIntervalType(),
                    interval.isActive(),
                    interval.isStrictAlarm(),
                    interval.getLastRunMillis(),
                    interval.getData());
        }
        else
        {
            return fieldsToValues(interval.getId(),
                    interval.getParentProfileId(),
                    interval.getIntervalType(),
                    interval.isActive(),
                    interval.isStrictAlarm(),
                    interval.getLastRunMillis(),
                    interval.getData());
        }
    }

    private ContentValues fieldsToValues(long parentId,
                                         IntervalType intervalType,
                                         boolean isActive,
                                         boolean isStrict,
                                         long lastRunMillis,
                                         String[] data)
    {
        ContentValues values = new ContentValues();
        values.put(DbOpenHelper.COLUMN_GENERIC_PARENTID, parentId);
        values.put(DbOpenHelper.COLUMN_INTERVAL_TYPE, intervalType.name());
        values.put(DbOpenHelper.COLUMN_GENERIC_ISACTIVE, isActive);
        values.put(DbOpenHelper.COLUMN_INTERVAL_ISSTRICT, isStrict);
        values.put(DbOpenHelper.COLUMN_INTERVAL_LASTRUN, lastRunMillis);
        for (int i = 0; i < data.length; i++)
        {
            values.put(DATA_KEYS[i], data[i]);
        }
        return values;
    }

    private ContentValues fieldsToValues(long id,
                                         long parentId,
                                         IntervalType intervalType,
                                         boolean isActive,
                                         boolean isStrict,
                                         long lastRunMillis,
                                         String[] data)
    {
        ContentValues values = fieldsToValues(parentId,
                intervalType,
                isActive,
                isStrict,
                lastRunMillis,
                data);
        values.put(DbOpenHelper.COLUMN_GENERIC_ID, id);
        return values;
    }

    private String[] getDataFromCursor(Cursor cursor)
    {
        switch (IntervalType.valueOf(cursor.getString(INDEX_TYPE)))
        {
            case INVALID:
                Log.e(TAG, "data: Invalid type, setting empty array");
                return new String[0];
            case EveryXMinutes:
            case EveryXHours:
            case Daily:
                return new String[] {
                    cursor.getString(INDEX_DATA1)
                };
            case EveryXDays:
            case Weekly:
            case OnTheseWeekdays:
            case OnTheseDates:
                return new String[] {
                        cursor.getString(INDEX_DATA1), cursor.getString(INDEX_DATA2)
                };
            default:
                Log.e(TAG, "data: Unhandled IntervalType: " + cursor.getString(INDEX_TYPE));
                return new String[0];
        }
    }

}
