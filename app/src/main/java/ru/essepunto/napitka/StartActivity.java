package ru.essepunto.napitka;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class StartActivity extends AppCompatActivity {

    DatabaseHelper myDb;



    Button scannerButton;
    Button printButton;
    Button instructionButton;
    Button aboutProduct;
    TextView counterStart;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_main);
        myDb = new DatabaseHelper(this);

        scannerButton = (Button) findViewById(R.id.makeButton);
        aboutProduct = (Button) findViewById(R.id.check_price_button);
        printButton = (Button) findViewById(R.id.printButton);
        instructionButton = (Button) findViewById(R.id.instructionButton);
        counterStart = findViewById(R.id.counter);
        setCountToTextView();

        scannerButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        printButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartActivity.this, PrintActivity.class);
                startActivity(intent);
            }
        });
        instructionButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartActivity.this, instruction.class);
                startActivity(intent);
            }
        });
        aboutProduct.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartActivity.this, CheckPrice.class);
                startActivity(intent);
            }
        });

    }
    @Override
    protected void onResume() {
        super.onResume();
        setCountToTextView();

    }

    private void setCountToTextView()
    {
        String co = myDb.getRecordCount();
        counterStart.setText("Количество записей в БД:"+co);
    }
}
