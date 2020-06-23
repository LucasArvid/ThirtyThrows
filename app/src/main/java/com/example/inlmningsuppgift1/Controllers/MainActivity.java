package com.example.inlmningsuppgift1.Controllers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.inlmningsuppgift1.Models.GameLogic;
import com.example.inlmningsuppgift1.R;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    // - Defines - (Constants) //
    private final static int ROLLED = 0;
    private final static int CLICKED = 1;
    private final static int SCORE = 2;
    private final static int PAIRED = 3;
    private final static int DICES_AMOUNT = 6;

    // Roll button, used to re-roll or score dices
    private Button button;

    // Used to track time between back button clicks, used to confirm intended action
    long prevTime;

    // Rolls and rounds tracker
    private int playerRolls = 0;
    private int playedRounds = 0;
    private int roundScore = 0;

    // Dice image index array, used to track selected dices
    private ArrayList<Integer> diceImageIndexArray = new ArrayList<>();;

    // Dice ImageView
    private ImageView[] diceViews = new ImageView[6];
    private int[] imageId = new int[6];

    // Targeted sum of paired dices when grading != LOW
    private int target = 0;

    // Drop down menu
    private Spinner spinner;

    // Score views
    private TextView tv_roundsPlayed;
    private TextView tv_rolls;

    // Class containing gamelogic and relevant custom classes (Dice and Score)
    private GameLogic gameLogic;

    // Adapter used for Spinner
    ArrayAdapter<String> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Result View Intent
        final Intent intent = new Intent(this, ResultActivity.class);

        // GameLogic class containing classes for handling dices and score
        gameLogic = new GameLogic(DICES_AMOUNT);

        // Initial view setup
        setupView();

        // Button click listener, used for rolling dices, scoring and ending the game
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick (View v) {
                handleButtonClicked(intent);
            }
        });

        // OnClick listener for each dice ImageView
        for (int i = 0; i < diceViews.length; i++) {
            final int index = i;
            diceViews[i].setOnClickListener(new View.OnClickListener() {
                public void onClick (View v) {
                    handleDiceClicked(index);
                }
            });
        }
    }

    // Set correct image depending on user action for each dice ImageView
    private void setDiceImage(int action, int dice) {
        Drawable drawable;

        switch (gameLogic.getDice(dice).getDiceValue()) {
            case 1:
                if (action == CLICKED) { // User wants to keep dice
                    gameLogic.setKeepDice(dice, true);
                    imageId[dice] = R.drawable.grey1; // Used to redraw the correct image when recreating activity
                    drawable = getResources().getDrawable(R.drawable.grey1);
                } else if (action == SCORE) { // User wants to score with dice
                    imageId[dice] = R.drawable.red1;
                    drawable = getResources().getDrawable(R.drawable.red1);
                    diceImageIndexArray.add(dice); // Used to track which dices have been paired for scoring
                    gameLogic.updateScoreTracker(1);
                } else if (action == PAIRED) { // Used to color code which dices have been used as a pair to get score
                    imageId[dice] = R.drawable.green1;
                    drawable = getResources().getDrawable(imageId[dice]);
                } else { // User rolled dice
                    imageId[dice] = R.drawable.white1;
                    drawable = getResources().getDrawable(R.drawable.white1);
                }
                break;
            case 2:
                if (action == CLICKED) {
                    gameLogic.setKeepDice(dice, true);
                    imageId[dice] = R.drawable.grey2;
                    drawable = getResources().getDrawable(R.drawable.grey2);
                } else if (action == SCORE) {
                    imageId[dice] = R.drawable.red2;
                    drawable = getResources().getDrawable(R.drawable.red2);
                    diceImageIndexArray.add(dice);
                    gameLogic.updateScoreTracker(2);
                } else if (action == PAIRED) {
                    imageId[dice] = R.drawable.green2;
                    drawable = getResources().getDrawable(imageId[dice]);
                } else {
                    imageId[dice] = R.drawable.white2;
                    drawable = getResources().getDrawable(R.drawable.white2);;
                }
                break;
            case 3:
                if (action == CLICKED) {
                    gameLogic.setKeepDice(dice, true);
                    imageId[dice] = R.drawable.grey3;
                    drawable = getResources().getDrawable(R.drawable.grey3);
                } else if (action == SCORE) {
                    imageId[dice] = R.drawable.red3;
                    drawable = getResources().getDrawable(R.drawable.red3);
                    diceImageIndexArray.add(dice);
                    gameLogic.updateScoreTracker(3);

                } else if (action == PAIRED) {
                    imageId[dice] = R.drawable.green3;
                    drawable = getResources().getDrawable(imageId[dice]);
                } else {
                    imageId[dice] = R.drawable.white3;
                    drawable = getResources().getDrawable(R.drawable.white3);
                }
                break;
            case 4:
                if (action == CLICKED) {
                    gameLogic.setKeepDice(dice, true);
                    imageId[dice] = R.drawable.grey4;
                    drawable = getResources().getDrawable(R.drawable.grey4);
                } else if (action == SCORE) {
                    imageId[dice] = R.drawable.red4;
                    drawable = getResources().getDrawable(R.drawable.red4);
                    diceImageIndexArray.add(dice);
                    gameLogic.updateScoreTracker(4);

                } else if (action == PAIRED) {
                    imageId[dice] = R.drawable.green4;
                    drawable = getResources().getDrawable(imageId[dice]);
                } else {
                    imageId[dice] = R.drawable.white4;
                    drawable = getResources().getDrawable(R.drawable.white4);
                }
                break;
            case 5:
                if (action == CLICKED) {
                    gameLogic.setKeepDice(dice, true);
                    imageId[dice] = R.drawable.grey5;
                    drawable = getResources().getDrawable(R.drawable.grey5);
                } else if (action == SCORE) {
                    imageId[dice] = R.drawable.red5;
                    drawable = getResources().getDrawable(R.drawable.red5);
                    diceImageIndexArray.add(dice);
                    gameLogic.updateScoreTracker(5);

                } else if (action == PAIRED) {
                    imageId[dice] = R.drawable.green5;
                    drawable = getResources().getDrawable(imageId[dice]);
                } else {
                    imageId[dice] = R.drawable.white5;
                    drawable = getResources().getDrawable(R.drawable.white5);
                }
                break;
            case 6:
                if (action == CLICKED) {
                    gameLogic.setKeepDice(dice, true);
                    imageId[dice] = R.drawable.grey6;
                    drawable = getResources().getDrawable(imageId[dice]);
                } else if (action == SCORE) {
                    imageId[dice] = R.drawable.red6;
                    drawable = getResources().getDrawable(imageId[dice]);
                    diceImageIndexArray.add(dice);
                    gameLogic.updateScoreTracker(6);

                } else if (action == PAIRED) {
                    imageId[dice] = R.drawable.green6;
                    drawable = getResources().getDrawable(imageId[dice]);
                } else {
                    imageId[dice] = R.drawable.white6;
                    drawable = getResources().getDrawable(imageId[dice]);
                }
                break;
            default:
                imageId[dice] = R.drawable.red1;
                drawable = getResources().getDrawable(imageId[dice]);
                break;
        }
        // Set the correct image for the dice
        diceViews[dice].setImageDrawable(drawable);
    }

    // Initial setup function for menu containing gradings
    private void fillSpinner() {
        ArrayList<String> items = new ArrayList<>();
        items.add("LOW");
        items.add("4");
        items.add("5");
        items.add("6");
        items.add("7");
        items.add("8");
        items.add("9");
        items.add("10");
        items.add("11");
        items.add("12");

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateTarget(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    // Updates the target score for the grading and redraws the spinner without the used grading,
    // gets list of unused gradings from Score object contained in GameLogic
    private void updateSpinner() {

        adapter.clear();
        adapter.addAll(gameLogic.getUnusedGradings());
        updateTarget(0);
    }

    // Updates targeted score, gets unused gradings from array in the Score object contained in GameLogic
    private void updateTarget(int position) { // position = object in menu clicked
        if(playedRounds == 9)
            return;
        if (gameLogic.getUnusedGradings().get(position) == "LOW")
            target = 3;
        else
            target = Integer.parseInt(gameLogic.getUnusedGradings().get(position));
    }

    // Setup function for view objects
    private void setupView() {
        // Initiate Spinner and Adapter
        spinner = findViewById(R.id.spinner);
        tv_roundsPlayed = findViewById(R.id.roundsPlayed);
        tv_rolls = findViewById(R.id.rolls);
        fillSpinner();

        // Connect to the dices ImageViews
        setupDices();

        // Connect to the button
        button = findViewById(R.id.button);
    }

    // Function for connecting to view objects and setting initial dice images
    private void setupDices() {
        diceViews[0] = findViewById(R.id.imageView);
        diceViews[1] = findViewById(R.id.imageView2);
        diceViews[2] = findViewById(R.id.imageView3);
        diceViews[3] = findViewById(R.id.imageView4);
        diceViews[4] = findViewById(R.id.imageView5);
        diceViews[5] = findViewById(R.id.imageView6);

        for (int i = 0; i < DICES_AMOUNT; i++) {
            // Set appropriate image for each dice
            setDiceImage(ROLLED, i);
        }
    }

    // Function for resetting game state
    private void resetGameState() {
        gameLogic.resetDices();
        playerRolls = 0;
        roundScore = 0;
        diceImageIndexArray.clear();
        for(int i = 0; i < DICES_AMOUNT; i++)
            setDiceImage(ROLLED, i);
    }

    // Function for handling rotation
    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Handle rotation without recreating Activity
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setMargins(555);
        } else {
            setMargins(55);
        }

    }

    // Save states in case of insufficient resource termination
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Log.d("","Saving state");
        outState.putInt("playerRolls", playerRolls);
        outState.putInt("playedRounds", playedRounds);
        outState.putInt("roundScore", roundScore);
        outState.putInt("target", target);
        outState.putParcelable("gameLogic", gameLogic);
        outState.putIntegerArrayList("diceImageIndexArray", diceImageIndexArray);
        outState.putIntArray("imageId", imageId);


    }

    // Rebuild in case of insufficient resource termination
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d("","Loading state");
        playerRolls = savedInstanceState.getInt("playerRolls");
        playedRounds = savedInstanceState.getInt("playedRounds");
        roundScore = savedInstanceState.getInt("roundScore");
        target = savedInstanceState.getInt("target");

        gameLogic = savedInstanceState.getParcelable("gameLogic");

        diceImageIndexArray = savedInstanceState.getIntegerArrayList("diceImageIndexArray");

        imageId = savedInstanceState.getIntArray("imageId");


        // Update grading spinner
        updateSpinner();

        // Redraw Dice images
        for (int i = 0; i < imageId.length; i++) {
            diceViews[i].setImageDrawable(getResources().getDrawable(imageId[i]));
        }



    }

    // Function for adjusting margins
    private void setMargins(int margin) {

        // Most left views
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) diceViews[0].getLayoutParams();
        params.setMarginStart(margin);
        diceViews[0].setLayoutParams(params);
        params = (ConstraintLayout.LayoutParams) diceViews[3].getLayoutParams();
        params.setMarginStart(margin);
        diceViews[3].setLayoutParams(params);

        // Most right views
        params = (ConstraintLayout.LayoutParams) diceViews[2].getLayoutParams();
        params.setMarginEnd(margin);;
        diceViews[2].setLayoutParams(params);
        params = (ConstraintLayout.LayoutParams) diceViews[5].getLayoutParams();
        params.setMarginEnd(margin);
        diceViews[5].setLayoutParams(params);
    }

    private void handleDiceClicked(int index) {
        if (playerRolls > 1) {
            if (gameLogic.isDiceUsed(index)){
                Log.d("", "USED");
                return;
            }
            gameLogic.updateGroupedScore(index);

            if(target == 3) { // Grading LOW selected
                if (gameLogic.getGroupedScore() <= 3) { // Selected dice is 3 or less
                    setDiceImage(SCORE, index); // Set appropriate image for each dice
                    gameLogic.handleGradingLow(index);
                    setDiceImage(PAIRED, index); // Recolor as a dice used to gain score
                }
                else if (gameLogic.getGroupedScore() > 3){ //
                    gameLogic.setGroupedScore(0);
                }
            }

            else {
                setDiceImage(SCORE, index); // Set appropriate image for each dice

                if(gameLogic.getGroupedScore() == target) { // Paired dices matches target score for selected grading
                    gameLogic.handleGradingElse(index, target, playedRounds);
                    for (int i = 0; i < diceImageIndexArray.size(); i++) {
                        gameLogic.setDiceUsed(diceImageIndexArray.get(i), true); // set each dice used to gain score to used, cannot be used to gain score again this round
                        setDiceImage(PAIRED, diceImageIndexArray.get(i));
                    }

                    diceImageIndexArray.clear();
                }
                else if (gameLogic.getGroupedScore() > target) { // Paired dices exceeds the target score for selected grading
                    for (int i = 0; i < diceImageIndexArray.size(); i++) {
                        setDiceImage(ROLLED, diceImageIndexArray.get(i)); // Set appropriate image for each dice
                        gameLogic.setDiceUsed(diceImageIndexArray.get(i), false); // Allow the dices to be selected again (to potentially pair with different dices)
                        gameLogic.setGroupedScore(0);
                    }

                    diceImageIndexArray.clear();
                    gameLogic.setGroupedScore(0);
                }
            }
        } else
            setDiceImage(CLICKED, index); // Set appropriate image for each dice
    }

    // Handle button click. Handle scoring if rolls == 2, handles game end if all rounds played and handles rolling else
    private void handleButtonClicked(Intent intent) {

        if (playerRolls == 2) {

            // SCORE THIS ROUND
            if (target == 3) // Grading LOW
                gameLogic.calculateLowScore();
            else // Grading else
                gameLogic.calculateScore(target, playedRounds);

            // Update gradings menu
            updateSpinner();

            // Ends the game if all rounds played and scored
            if (playedRounds >= 9) {
                // Game ended
                // Extras used in next activity to display score
                intent.putExtra("TotalScore", gameLogic.getTotalScore());
                intent.putExtra("RoundScore", gameLogic.getRoundScore());
                intent.putIntegerArrayListExtra("RoundGrading", gameLogic.getRoundGrading());

                startActivity(intent); // Start next activity

                finish(); // terminate activity

            }

            // Restore default button text
            button.setText(getResources().getString(R.string.button_name));

            playedRounds ++;

            tv_roundsPlayed.setText("Rounds played: " + playedRounds + " / 10");
            tv_rolls.setText("Rolls: 0 / 2");

            // Reset the games state for next round
            resetGameState();
            return;
        }

        // Rerolls the dices that the user does not want to keep
        for (int i = 0; i < DICES_AMOUNT; i++) {
            if (!gameLogic.getKeepDice(i)) {
                // Randomize dice value
                gameLogic.randomizeDice(i);
            }

            gameLogic.setKeepDice(i, false);
            // Set appropriate image for each dice
            setDiceImage(ROLLED, i);
        }

        if (playerRolls == 1) {
            // Change button text for scoring
            button.setText("Score");
            if (playedRounds >= 9) // Change button text for ending the game
                button.setText("End Game");
        }

        playerRolls++;

        tv_rolls.setText("Rolls: " + playerRolls + " / 2");
    }

    // Function for avoiding miss-clicking back button (asks for confirmation before terminating app)
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
