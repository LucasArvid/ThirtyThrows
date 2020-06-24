package com.example.inlmningsuppgift1.Controllers;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.inlmningsuppgift1.R;

import java.util.ArrayList;

public class ResultActivity extends AppCompatActivity {

    // Variables to store extras from previous activity containing scores
    private int totalScore;
    private int[] roundScore;
    private ArrayList<Integer> roundGrading;

    // Intent for unpacking extras
    private Intent intent;

    // View objects
    private TextView resultsView;
    private ListView resultsListView;
    private Button replayButton;

    // Used to confirm applciation termination
    long prevTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        intent = getIntent();

        getExtras();
        setupViews();
    }

    // Retrieve extras from previous activity
    private void getExtras() {
        totalScore = intent.getIntExtra("TotalScore",0);
        roundScore = intent.getIntArrayExtra("RoundScore");
        roundGrading = intent.getIntegerArrayListExtra("RoundGrading");
    }

    // Setup views
    private void setupViews() {
        resultsView = findViewById(R.id.textView);
        resultsListView = findViewById(R.id.listView);
        replayButton = findViewById(R.id.replay_button);

        // Populate the views
        populateViews();

        replayButton.setText("Replay");
        replayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    // Populate the views, connected adapter to listview that is filled with score from previous activity
    private void populateViews() {
        resultsView.setText(getResources().getString(R.string.results) + " " + totalScore);

        ArrayList<String> stringArray = new ArrayList<>();

        // Populate ListView with grading and corresponding score
        for (int i = 0; i < roundGrading.size(); i++) {
            if (roundGrading.get(i) == 3)
                stringArray.add("Grading: LOW Generated " + Integer.toString(roundScore[i]) + " Points");
            else
                stringArray.add("Grading: " + Integer.toString(roundGrading.get(i)) + " Generated " + Integer.toString(roundScore[i]) + " Points");
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                stringArray );

        resultsListView.setAdapter(arrayAdapter);
    }

    // Application termination confirmation on back button
    @Override
    public void onBackPressed() {
        long currentTime = System.currentTimeMillis();

        if ((prevTime - currentTime) < 1 && prevTime != 0) {
            finish();
        } else {
            Toast.makeText(this, "Sure you want to quit?", Toast.LENGTH_LONG).show();
            prevTime = System.currentTimeMillis();
        }

    }


}
