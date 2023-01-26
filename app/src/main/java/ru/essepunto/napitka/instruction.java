package ru.essepunto.napitka;

import android.os.Bundle;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;


public class instruction extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instruction);

        // Find the TextView and ScrollView in the layout
        TextView instructionsTextView = findViewById(R.id.instructionsTextView);
        ScrollView scrollView = findViewById(R.id.scrollView);

        // Set the text for the TextView
        instructionsTextView.setText("Insert instructions here.");
        instructionsTextView.setTextSize(16);
        instructionsTextView.setPadding(16, 16, 16, 16);
        instructionsTextView.setLineSpacing(1.5f, 1.8f);
        // Add the TextView to the ScrollView
    }
}
