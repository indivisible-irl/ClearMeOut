package com.indivisible.clearmeout.data;

import java.sql.SQLException;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
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
    private static final int INDEX_STRICT = 3;
    private static final int INDEX_LASTRUN = 4;
    private static final int INDEX_DATA1 = 5;
    private static final int INDEX_DATA2 = 6;
    private static final int INDEX_DATA3 = 7;
    private static final int INDEX_DATA4 = 8;
    private static final String[] DATA_KEYS = {
            DbOpenHelper.COLUMN_INTERVAL_DATA1, DbOpenHelper.COLUMN_INTERVAL_DATA2,
            DbOpenHelper.COLUMN_INTERVAL_DATA3, DbOpenHelper.COLUMN_INTERVAL_DATA4
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


    ///////////////////////////////////////////////////////
    ////    util
    ///////////////////////////////////////////////////////

    private Interval cursorToInterval(Cursor cursor)
    {
        Interval interval = new Interval();
        interval.setId(cursor.getLong(INDEX_ID));
        interval.setParentProfileId(cursor.getLong(INDEX_FK));
        interval.setIntervalType(IntervalType.valueOf(cursor.getString(INDEX_TYPE)));
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
                        "(strictAlarm) Error getting parsing int for boolean: "
                                + cursor.getInt(3) + " / " + interval.getIntervalType().name());
                break;
        }
        interval.setLastRun(cursor.getLong(INDEX_LASTRUN));
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
                    interval.isStrictAlarm(),
                    interval.getLastRun(),
                    interval.getData());
        }
        else
        {
            return fieldsToValues(interval.getId(),
                    interval.getParentProfileId(),
                    interval.getIntervalType(),
                    interval.isStrictAlarm(),
                    interval.getLastRun(),
                    interval.getData());
        }
    }

    private ContentValues fieldsToValues(long parentId,
                                         IntervalType intervalType,
                                         boolean isStrict,
                                         long lastRunMillis,
                                         String[] data)
    {
        ContentValues values = new ContentValues();
        values.put(DbOpenHelper.COLUMN_GENERIC_PARENTID, parentId);
        values.put(DbOpenHelper.COLUMN_INTERVAL_TYPE, intervalType.name());
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
                                         boolean isStrict,
                                         long lastRunMillis,
                                         String[] data)
    {
        ContentValues values = fieldsToValues(parentId,
                intervalType,
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
