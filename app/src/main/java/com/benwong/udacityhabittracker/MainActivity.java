package com.benwong.udacityhabittracker;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<String> habitList = new ArrayList<String>();
    private ArrayList<String> habitListName = new ArrayList<String>();
    private EditText habitNameTF;

    private Button submitBtn;
    private SQLiteDatabase myDatabase;
    private ListView habitListView;
    private ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        habitListView = (ListView) findViewById(R.id.habitListView);
        habitNameTF = (EditText) findViewById(R.id.habitNameTF);

        submitBtn = (Button) findViewById(R.id.submitBtn);

        myDatabase = this.openOrCreateDatabase("Habits", MODE_PRIVATE, null);
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, habitList);
        habitListView.setAdapter(arrayAdapter);

        getFromDB();


        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String habitName = habitNameTF.getText().toString();
                int defaultFreq = 0;
                System.out.println("Habit " + habitName);
                System.out.println("DefaultFreq " + defaultFreq);
                myDatabase.execSQL("INSERT INTO habits (habit, frequency) VALUES (" + "'" + habitName + "'" + " , " + defaultFreq + " )");
                getFromDB();
                arrayAdapter.notifyDataSetChanged();
            }
        });

        habitListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor c = myDatabase.rawQuery("SELECT * FROM habits WHERE habit = " + "'" + habitListName.get(position).toString() + "'" , null);

                try {
                    int habitIndex = c.getColumnIndex("habit");
                    int frequencyIndex = c.getColumnIndex("frequency");


                    if(c != null && c.moveToFirst()){
                        do{
                            System.out.println(c.getString(habitIndex));
                            System.out.println(Integer.toString(c.getInt(frequencyIndex)));
                            int updatedFreq = c.getInt(frequencyIndex) + 1;
                            System.out.println(updatedFreq);
                            myDatabase.execSQL("UPDATE habits SET" + " frequency = " + updatedFreq + " WHERE habit = "  + "'" +  c.getString(habitIndex)  + "'");
                            System.out.println("UPDATE habits SET" + " frequency = " + updatedFreq + " WHERE habit = "  + "'" +  c.getString(habitIndex)  + "'");

                        } while(c.moveToNext());
                    }

                    c.close();
                } catch(Exception e){
                    e.printStackTrace();
                }

                getFromDB();

            }


        });



    }

    private void getFromDB() {
        habitList.clear();
        try {


            myDatabase.execSQL("CREATE TABLE IF NOT EXISTS habits (habit VARCHAR, frequency INT(3))");

//            myDatabase.execSQL("INSERT INTO habits (habit, frequency) VALUES ('Do homework', 1)");
//
//            myDatabase.execSQL("INSERT INTO habits (habit, frequency) VALUES ('Clean car', 1)");

            Cursor c = myDatabase.rawQuery("SELECT * FROM habits", null);

            int habitIndex = c.getColumnIndex("habit");
            int frequencyIndex = c.getColumnIndex("frequency");

            if (c != null && c.moveToFirst()){
                do {
                    System.out.println("habit " + c.getString(habitIndex));
                    System.out.println("freq " + Integer.toString(c.getInt(frequencyIndex)));
                    String habit = c.getString(habitIndex) + " : " + Integer.toString(c.getInt(frequencyIndex));
                    habitListName.add(c.getString(habitIndex));
                    habitList.add(habit);

                } while(c.moveToNext());
            }



            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        arrayAdapter.notifyDataSetChanged();
    }
}
