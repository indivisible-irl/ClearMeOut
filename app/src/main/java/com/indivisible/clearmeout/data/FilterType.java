package com.indivisible.clearmeout.data;

/**
 * Created by indiv on 15/06/14.
 */
public enum FilterType
{
    EXTENSION,          // file extensions
    FILE_NAME,          // file name
    DIR_NAME,           // directory name
    DATE_CREATED,       // file creation times
    DATE_MODIFIED,      // file modify times
    DATE_ACCESSED,      // file access times
    INVALID;            // error or unset
}
