package ru.essepunto.napitka;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class PrintActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print);
        ArrayList<String> names;
        ListView listView;
        ArrayAdapter<String> adapter;

// inside onCreate or onViewCreated method
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        names = databaseHelper.getNames();
        listView = (ListView) findViewById(R.id.listView);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, names);
        listView.setAdapter(adapter);
    }
}