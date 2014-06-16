package com.indivisible.clearmeout.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by indiv on 15/06/14.
 */
public class DbOpenHelper
        extends SQLiteOpenHelper
{

    ///////////////////////////////////////////////////////
    ////    data
    ///////////////////////////////////////////////////////

    // keys & versions

    public static final String DATABASE_NAME = "ClearMeOut_profiles.db";
    public static final int DATABASE_VERSION = 11;

    public static final String TABLE_PROFILES = "profiles";
    public static final String TABLE_TARGETS = "targets";
    public static final String TABLE_INTERVALS = "intervals";
    public static final String TABLE_FILTERS = "filters";

    public static final String COLUMN_GENERIC_ID = "_id";
    public static final String COLUMN_GENERIC_PARENTID = "parent_id";
    public static final String COLUMN_GENERIC_ISACTIVE = "is_active";
    public static final String COLUMN_PROFILE_NAME = "name";
    public static final String COLUMN_TARGET_DIRECTORY = "root_directory";
    public static final String COLUMN_TARGET_ISRECURSIVE = "is_recursive";
    public static final String COLUMN_TARGET_DELETEDIRS = "delete_directories";
    public static final String COLUMN_FILTER_TYPE = "filter_type";
    public static final String COLUMN_FILTER_ISWHITELIST = "is_whitelist";
    public static final String COLUMN_FILTER_DATA = "data";
    public static final String COLUMN_INTERVAL_TYPE = "interval_type";
    public static final String COLUMN_INTERVAL_ISSTRICT = "is_strict_alarm";
    public static final String COLUMN_INTERVAL_LASTRUN = "last_run_millis";
    public static final String COLUMN_INTERVAL_DATA1 = "data_1";
    public static final String COLUMN_INTERVAL_DATA2 = "data_2";
    public static final String COLUMN_INTERVAL_DATA3 = "data_3";
    public static final String COLUMN_INTERVAL_DATA4 = "data_4";

    private static final String TAG = "DbOpenHelper";


    // column arrays

    /**
     * <ul>
     * <li><b>0:</b> (int) id</li>
     * <li><b>1:</b> (int) isActive</li>
     * <li><b>2:</b> (text) name</li>
     * </ul>
     */
    public static final String[] ALL_COLUMNS_PROFILES = {
            COLUMN_GENERIC_ID, COLUMN_GENERIC_ISACTIVE, COLUMN_PROFILE_NAME
    };
    /**
     * <ul>
     * <li><b>0:</b> (int ) id</li>
     * <li><b>1:</b> (int ) parentId</li>
     * <li><b>2:</b> (text) targetDirectory</li>
     * <li><b>3:</b> (int ) isRecursive</li>
     * <li><b>4:</b> (int ) doDeleteDirectories</li>
     * </ul>
     */
    public static final String[] ALL_COLUMNS_TARGETS = {
            COLUMN_GENERIC_ID, COLUMN_GENERIC_PARENTID, COLUMN_TARGET_DIRECTORY,
            COLUMN_TARGET_ISRECURSIVE, COLUMN_TARGET_DELETEDIRS
    };
    /**
     * <ul>
     * <li><b>0:</b> (int ) id</li>
     * <li><b>1:</b> (int ) parentId</li>
     * <li><b>2:</b> (text) filterType</li>
     * <li><b>3:</b> (int ) isWhitelist</li>
     * <li><b>4:</b> (text) data</li>
     * </ul>
     */
    public static final String[] ALL_COLUMNS_FILTERS = {
            COLUMN_GENERIC_ID, COLUMN_GENERIC_PARENTID, COLUMN_FILTER_TYPE,
            COLUMN_GENERIC_ISACTIVE, COLUMN_FILTER_ISWHITELIST, COLUMN_FILTER_DATA
    };
    /**
     * <ul>
     * <li><b>0:</b> (int ) id</li>
     * <li><b>1:</b> (int ) parentId</li>
     * <li><b>2:</b> (text) intervalType</li>
     * <li><b>3:</b> (int ) isStrictAlarm</li>
     * <li><b>4:</b> (int ) lastRun</li>
     * <li><b>5:</b> (text) data1</li>
     * <li><b>6:</b> (text) data2</li>
     * <li><b>7:</b> (text) data3</li>
     * <li><b>8:</b> (text) data4</li>
     * </ul>
     */
    public static final String[] ALL_COLUMNS_INTERVALS = {
            COLUMN_GENERIC_ID, COLUMN_GENERIC_PARENTID, COLUMN_INTERVAL_TYPE,
            COLUMN_GENERIC_ISACTIVE, COLUMN_INTERVAL_ISSTRICT, COLUMN_INTERVAL_LASTRUN,
            COLUMN_INTERVAL_DATA1, COLUMN_INTERVAL_DATA2, COLUMN_INTERVAL_DATA3,
            COLUMN_INTERVAL_DATA4
    };


    // create tables

    private static final String CREATE_TABLE_PROFILES = "create table "     //  excessive
            + TABLE_PROFILES + " ( "                                        //  comments
            + COLUMN_GENERIC_ID + " integer primary key autoincrement, "    //  to
            + COLUMN_GENERIC_ISACTIVE + " integer not null, "               //  keep
            + COLUMN_PROFILE_NAME + " text not null );";                    //  the

    private static final String CREATE_TABLE_TARGETS = "create table "      //  auto
            + TABLE_TARGETS + " ( "                                         //  formatter
            + COLUMN_GENERIC_ID + " integer primary key autoincrement, "    //  from
            + COLUMN_GENERIC_PARENTID + " integer not null, "               //  completely
            + COLUMN_TARGET_DIRECTORY + " text not null, "                  //  destroying
            + COLUMN_TARGET_ISRECURSIVE + " integer not null, "             //  the
            + COLUMN_TARGET_DELETEDIRS + " integer not null );";            //  carefully

    private static final String CREATE_TABLE_FILTERS = "create table "      //  laid
            + TABLE_FILTERS + " ( "                                         //  out
            + COLUMN_GENERIC_ID + " integer primary key autoincrement, "    //  tidy
            + COLUMN_GENERIC_PARENTID + " integer not null, "               //  code
            + COLUMN_FILTER_TYPE + " text not null, "                       //  that
            + COLUMN_GENERIC_ISACTIVE + " integer not null, "               //  was
            + COLUMN_FILTER_ISWHITELIST + " integer not null, "             //  very
            + COLUMN_FILTER_DATA + " text not null );";                     //  painstakingly

    private static final String CREATE_TABLE_INTERVALS = "create table "    //  positioned
            + TABLE_INTERVALS + " ( "                                       //  to
            + COLUMN_GENERIC_ID + " integer primary key autoincrement, "    //  make
            + COLUMN_GENERIC_PARENTID + " integer not null, "               //  it
            + COLUMN_INTERVAL_TYPE + " text not null, "                     //  very
            + COLUMN_GENERIC_ISACTIVE + " integer not null, "               //  easy
            + COLUMN_INTERVAL_ISSTRICT + " integer not null, "              //  to
            + COLUMN_INTERVAL_LASTRUN + " integer not null, "               //  follow
            + COLUMN_INTERVAL_DATA1 + " text, "                             //  the
            + COLUMN_INTERVAL_DATA2 + " text, "                             //  table
            + COLUMN_INTERVAL_DATA3 + " text, "                             //  create
            + COLUMN_INTERVAL_DATA4 + " text );";                           //  statements


    ///////////////////////////////////////////////////////
    ////    init
    ///////////////////////////////////////////////////////

    public DbOpenHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        Log.i(TAG, "Creating new database...");
        createAllTables(db);
        Log.i(TAG, "...database created.");
    }

    private void createAllTables(SQLiteDatabase db)
    {
        db.execSQL(CREATE_TABLE_PROFILES);
        db.execSQL(CREATE_TABLE_TARGETS);
        db.execSQL(CREATE_TABLE_FILTERS);
        db.execSQL(CREATE_TABLE_INTERVALS);
    }

    private void dropAllTables(SQLiteDatabase db)
    {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROFILES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TARGETS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FILTERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INTERVALS);
    }


    ///////////////////////////////////////////////////////
    ////    upgrade
    ///////////////////////////////////////////////////////

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        Log.i(TAG, "Upgrading database from v" + oldVersion + " to v" + newVersion);
        Log.w(TAG, "ALL DATA WILL BE LOST");
        dropAllTables(db);
        onCreate(db);
    }
}
