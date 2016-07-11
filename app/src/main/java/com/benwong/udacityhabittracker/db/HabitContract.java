package com.benwong.udacityhabittracker.db;

import android.provider.BaseColumns;

/**
 * Created by benwong on 2016-07-10.
 */
public class HabitContract {
    public static final String DB_NAME = "com.benwong.udacityhabittracker.db";
    public static final int DB_VERSION = 1;

    public class HabitEntry implements BaseColumns {
        public static final String TABLE = "habits";

        public static final String COL_TASK_HABIT_NAME = "habit";
        public static final String COL_TASK_HABIT_FREQ = "freq";
    }
}
