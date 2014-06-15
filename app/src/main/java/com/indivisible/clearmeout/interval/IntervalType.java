package com.indivisible.clearmeout.interval;


public enum IntervalType
{
    EveryXMinutes,      // int:minutes
    EveryXHours,        // int:hours
    EveryXDays,         // int:days, Time:time
    Daily,              // Time:time
    Weekly,             // int:weekday, Time:time
    OnTheseWeekdays,    // String:7binary, Time:time
    OnTheseDates,       // String:dates, Time:time
    INVALID;            // didn't catch or handle something...

}
