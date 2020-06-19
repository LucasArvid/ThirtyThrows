package com.example.inlmningsuppgift1.Controllers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.PrecomputedText;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import com.example.inlmningsuppgift1.Models.GameLogic;
import com.example.inlmningsuppgift1.R;
import com.google.gson.Gson;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //Defines
    private final static int ROLLED = 0;
    private final static int CLICKED = 1;
    private final static int SCORE = 2;
    private final static int DICES_AMOUNT = 6;

    // -- Class Variables -- //

    private Button button;

    // Rolls and rounds tracker
    private int playerRolls = 0;
    private int playedRounds = 0;
    private int roundScore = 0;

    // Dice image index array, used to track selected dices
    private ArrayList<Integer> diceImageIndexArray = new ArrayList<>();;

    // Dice Images
    private ImageView[] diceViews = new ImageView[6];

    // Target sum of paired dices when grading != LOW
    private int target = 0;

    // Drop down menu
    private Spinner spinner;

    private GameLogic gameLogic;

    ArrayAdapter<String> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Result View Intent
        final Intent intent = new Intent(this, ResultActivity.class);

        gameLogic = new GameLogic(DICES_AMOUNT);

        setupView();

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick (View v) {

                if (playerRolls == 2) {

                    // SCORE THIS ROUND
                    if (target == 3)
                        gameLogic.calculateLowScore();
                    else
                        gameLogic.calculateScore(target, playedRounds);

                    updateSpinner();

                    if (playedRounds == 9)
                        button.setText("End Game");
                    if (playedRounds >= 1) {
                        // Game ended
                        intent.putExtra("TotalScore", gameLogic.getTotalScore());
                        intent.putExtra("RoundScore", gameLogic.getRoundScore());
                        intent.putIntegerArrayListExtra("RoundGrading", gameLogic.getRoundGrading());
                        startActivity(intent);
                        finish();

                    }

                    Log.d("", "roundscore: " + roundScore);
                    // Restore default button text (ändra till @string)
                    button.setText(getResources().getString(R.string.button_name));
                    playedRounds ++;
                    resetGameState();
                    return;
                }

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
                    // change to @string
                    button.setText("Score");
                }

                playerRolls++;
            }
        });

        // OnClick listener for each dice ImageView
        for (int i = 0; i < diceViews.length; i++) {
            final int index = i;
            diceViews[i].setOnClickListener(new View.OnClickListener() {
                public void onClick (View v) {
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
                            }
                            else if (gameLogic.getGroupedScore() > 3){ //
                                gameLogic.setGroupedScore(0);
                            }
                        } else {

                            Log.d("","dice: " + gameLogic.getDice(index).getDiceValue() + " Was clicked");
                            setDiceImage(SCORE, index); // Set appropriate image for each dice

                            if(gameLogic.getGroupedScore() == target) {
                                gameLogic.handleGradingElse(index, target, playedRounds);
                                for (int i = 0; i < diceImageIndexArray.size(); i++) {
                                    gameLogic.setDiceUsed(diceImageIndexArray.get(i), true);
                                    Log.d("","dice: " + gameLogic.getDice(i).getDiceValue() + " Was set to used");
                                }

                                diceImageIndexArray.clear();
                            }
                            else if (gameLogic.getGroupedScore() > target) {
                                for (int i = 0; i < diceImageIndexArray.size(); i++) {
                                    setDiceImage(ROLLED, diceImageIndexArray.get(i)); // Set appropriate image for each dice
                                    gameLogic.setDiceUsed(diceImageIndexArray.get(i), false);
                                    Log.d("","dice: " + gameLogic.getDice(i).getDiceValue() + " Was set to not used");
                                    gameLogic.setGroupedScore(0);
                                }
                                diceImageIndexArray.clear();
                                gameLogic.setGroupedScore(0);
                            }
                        }
                    } else
                        setDiceImage(CLICKED, index); // Set appropriate image for each dice
                }
            });
        }
    }

    // Set correct image for each dice ImageView
    private void setDiceImage(int action, int dice) {
        Drawable drawable;



        switch (gameLogic.getDice(dice).getDiceValue()) {
            case 1:
                if (action == CLICKED) {
                    gameLogic.setKeepDice(dice, true);
                    drawable = getResources().getDrawable(R.drawable.grey1);
                } else if (action == SCORE) {
                    drawable = getResources().getDrawable(R.drawable.red1);
                    diceImageIndexArray.add(dice);
                    gameLogic.updateScoreTracker(1);
                } else {
                    drawable = getResources().getDrawable(R.drawable.white1);
                }
                break;
            case 2:
                if (action == CLICKED) {
                    gameLogic.setKeepDice(dice, true);
                    drawable = getResources().getDrawable(R.drawable.grey2);
                } else if (action == SCORE) {
                    drawable = getResources().getDrawable(R.drawable.red2);
                    diceImageIndexArray.add(dice);
                    gameLogic.updateScoreTracker(2);


                } else {
                    drawable = getResources().getDrawable(R.drawable.white2);;
                }
                break;
            case 3:
                if (action == CLICKED) {
                    gameLogic.setKeepDice(dice, true);
                    drawable = getResources().getDrawable(R.drawable.grey3);
                } else if (action == SCORE) {
                    drawable = getResources().getDrawable(R.drawable.red3);
                    diceImageIndexArray.add(dice);
                    gameLogic.updateScoreTracker(3);

                } else {
                    drawable = getResources().getDrawable(R.drawable.white3);
                }
                break;
            case 4:
                if (action == CLICKED) {
                    gameLogic.setKeepDice(dice, true);
                    drawable = getResources().getDrawable(R.drawable.grey4);
                } else if (action == SCORE) {
                    drawable = getResources().getDrawable(R.drawable.red4);
                    diceImageIndexArray.add(dice);
                    gameLogic.updateScoreTracker(4);

                } else {
                    drawable = getResources().getDrawable(R.drawable.white4);
                }
                break;
            case 5:
                if (action == CLICKED) {
                    gameLogic.setKeepDice(dice, true);
                    drawable = getResources().getDrawable(R.drawable.grey5);
                } else if (action == SCORE) {
                    drawable = getResources().getDrawable(R.drawable.red5);
                    diceImageIndexArray.add(dice);
                    gameLogic.updateScoreTracker(5);

                } else {
                    drawable = getResources().getDrawable(R.drawable.white5);
                }
                break;
            case 6:
                if (action == CLICKED) {
                    gameLogic.setKeepDice(dice, true);
                    drawable = getResources().getDrawable(R.drawable.grey6);
                } else if (action == SCORE) {
                    drawable = getResources().getDrawable(R.drawable.red6);
                    diceImageIndexArray.add(dice);
                    gameLogic.updateScoreTracker(6);

                } else {
                    drawable = getResources().getDrawable(R.drawable.white6);
                }
                break;
            default:
                drawable = getResources().getDrawable(R.drawable.red1);
                break;
        }
        // Set the correct image for the dice
        diceViews[dice].setImageDrawable(drawable);
    }

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
                //target = position + 3;
                updateTarget(position);
                Log.d("","target:" + target);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void updateSpinner() {

        adapter.clear();
        adapter.addAll(gameLogic.getUnusedGradings());
        updateTarget(0);
    }

    private void updateTarget(int position) {
        if (gameLogic.getUnusedGradings().get(position) == "LOW")
            target = 3;
        else
            target = Integer.parseInt(gameLogic.getUnusedGradings().get(position));
    }

    private void setupView() {
        // Initiate Spinner and Adapter
        spinner = findViewById(R.id.spinner);
        fillSpinner();

        // Connect to the dices ImageViews
        setupDices();

        // Connect to the button
        button = findViewById(R.id.button);
    }

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

    private void resetGameState() {
        gameLogic.resetDices();
        playerRolls = 0;
        roundScore = 0;
        diceImageIndexArray.clear();
        for(int i = 0; i < DICES_AMOUNT; i++)
            setDiceImage(ROLLED, i);
    }


    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.d("","Hello rotation");

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setMargins(555);
        } else {
            setMargins(55);
        }

    }

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

}
