package com.indivisible.clearmeout.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

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


    ///////////////////////////////////////////////////////
    ////    init
    ///////////////////////////////////////////////////////

    public ProfileSource(Context context)
    {
        this.dbHelper = new DbOpenHelper()
    }
}
