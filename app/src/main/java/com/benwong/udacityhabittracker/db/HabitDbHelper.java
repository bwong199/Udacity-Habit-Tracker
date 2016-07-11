package com.benwong.udacityhabittracker.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.benwong.udacityhabittracker.MainActivity;

/**
 * Created by benwong on 2016-07-10.
 */
public class HabitDbHelper extends SQLiteOpenHelper {

    public HabitDbHelper(Context context) {
        super(context, HabitContract.DB_NAME, null, HabitContract.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //myDatabase.execSQL("CREATE TABLE IF NOT EXISTS habits (habit VARCHAR, frequency INT(3))");

//        String createTable = "CREATE TABLE " + HabitContract.HabitEntry.TABLE + " ( " +
//                HabitContract.HabitEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
//                HabitContract.HabitEntry.COL_TASK_HABIT_NAME + " TEXT NOT NULL);";

        String createTable = "CREATE TABLE IF NOT EXISTS " + HabitContract.HabitEntry.TABLE + " (" + HabitContract.HabitEntry.COL_TASK_HABIT_NAME + " VARCHAR, " +                  HabitContract.HabitEntry.COL_TASK_HABIT_FREQ + " INT(3))";
        System.out.println("Create table " + createTable);
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + HabitContract.HabitEntry.TABLE);
        onCreate(db);
    }

    public void deleteDatabase(){
        this.deleteHabitsDB();
    }



    public void deleteHabitsDB() {
        String deleteScript = "delete from " + HabitContract.HabitEntry.TABLE;
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(deleteScript);


    }


    public void insert(String habitName){

        int defaultFreq = 0;

        SQLiteDatabase db = this.getWritableDatabase();
        System.out.println("Habit name in DB Helper " + habitName);

        ContentValues values = new ContentValues();
        values.put(HabitContract.HabitEntry.COL_TASK_HABIT_NAME, habitName); // Contact Name
        values.put(HabitContract.HabitEntry.COL_TASK_HABIT_FREQ, defaultFreq); // Contact Phone Number

        // Inserting Row
//        db.insert(HabitContract.HabitEntry.TABLE, null, values);

        db.insertWithOnConflict(HabitContract.HabitEntry.TABLE,
                null,
                values,
                SQLiteDatabase.CONFLICT_REPLACE);

        db.close(); // Closing database connection

//        myDatabase.execSQL("INSERT INTO habits (habit, frequency) VALUES (" + "'" + habitName + "'" + " , " + defaultFreq + " )");
    }

    public void update(int position){

        SQLiteDatabase db = this.getWritableDatabase();

        System.out.println("Update method habit name " + MainActivity.habitListName.get(position).toString());
        Cursor c = db.rawQuery("SELECT * FROM habits WHERE habit = " + "'" + MainActivity.habitListName.get(position).toString() + "'" , null);

        try {
            int habitIndex = c.getColumnIndex(HabitContract.HabitEntry.COL_TASK_HABIT_NAME);
            int frequencyIndex = c.getColumnIndex(HabitContract.HabitEntry.COL_TASK_HABIT_FREQ);


            if(c != null && c.moveToFirst()){
                do{
                    System.out.println("Update method " + c.getString(habitIndex));
                    System.out.println("Update method " + Integer.toString(c.getInt(frequencyIndex)));
                    int updatedFreq = c.getInt(frequencyIndex) + 1;
                    System.out.println("Updated freq " +  updatedFreq);
//                    String updateScript = "UPDATE " + HabitContract.HabitEntry.TABLE + " SET " + HabitContract.HabitEntry.COL_TASK_HABIT_FREQ  +" = " + updatedFreq + " WHERE " + HabitContract.HabitEntry.COL_TASK_HABIT_NAME +  " = "  + "'" +  c.getString(habitIndex)  + "'";
//                    System.out.println(updateScript);
//                    db.execSQL(updateScript);

                    ContentValues values = new ContentValues();
                    values.put(HabitContract.HabitEntry.COL_TASK_HABIT_NAME, c.getString(habitIndex));
                    values.put(HabitContract.HabitEntry.COL_TASK_HABIT_FREQ, updatedFreq);


                    db.update(HabitContract.HabitEntry.TABLE, values, HabitContract.HabitEntry.COL_TASK_HABIT_NAME + " = ?",
                            new String[] { String.valueOf(c.getString(habitIndex)) });

//                    System.out.println("UPDATE habits SET" + " frequency = " + updatedFreq + " WHERE habit = "  + "'" +  c.getString(habitIndex)  + "'");

                } while(c.moveToNext());
            }

            c.close();
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public void read(){
        try {

            SQLiteDatabase db = this.getWritableDatabase();

            String queryString = "SELECT * FROM habits";

//            myDatabase.execSQL("INSERT INTO habits (habit, frequency) VALUES ('Do homework', 1)");
//
//            myDatabase.execSQL("INSERT INTO habits (habit, frequency) VALUES ('Clean car', 1)");

            Cursor c = db.rawQuery(queryString, null);

            int habitIndex = c.getColumnIndex(HabitContract.HabitEntry.COL_TASK_HABIT_NAME);
            int frequencyIndex = c.getColumnIndex(HabitContract.HabitEntry.COL_TASK_HABIT_FREQ);

            if (c != null && c.moveToFirst()){
                do {
                    System.out.println("READ habit " + c.getString(habitIndex));
                    System.out.println("READ freq " + Integer.toString(c.getInt(frequencyIndex)));
                    String habit = c.getString(habitIndex) + " : " + Integer.toString(c.getInt(frequencyIndex));
                    MainActivity.habitListName.add(c.getString(habitIndex));
                    MainActivity.habitList.add(habit);

                } while(c.moveToNext());
            }

            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

