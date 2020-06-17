package com.example.inlmningsuppgift1.Controllers;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import com.example.inlmningsuppgift1.Models.GameLogic;
import com.example.inlmningsuppgift1.R;

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
    private int totalScore = 0;

    // Dice image index array, used to track selected dices
    private ArrayList<Integer> diceImageIndexArray = new ArrayList<>();;

    // Dice Images
    private ImageView[] diceViews = new ImageView[6];

    // Target sum of paired dices when grading != LOW
    private int target = 0;

    // Drop down menu
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupView();


        button.setOnClickListener(new View.OnClickListener() {
            public void onClick (View v) {

                if (playerRolls == 2) {
                    if (playedRounds >= 9) {
                        // Game ended
                    }
                    // Score this round
                    if (target == 3)
                        roundScore = GameLogic.calculateLowScore();
                    else
                        roundScore = GameLogic.calculateScore(target);
                    totalScore += roundScore;

                    // Score this round
                    Log.d("", "roundscore: " + roundScore);
                    // Restore default button text (ändra till @string)
                    button.setText(getResources().getString(R.string.button_name));
                    playedRounds ++;
                    resetGameState();
                    GameLogic.resetDices();
                    return;
                }

                for (int i = 0; i < DICES_AMOUNT; i++) {
                    if (!GameLogic.getKeepDice(i)) {
                        // Randomize dice value
                        GameLogic.randomizeDice(i);
                    }

                    GameLogic.setKeepDice(i, false);
                    // Set appropriate image for each dice
                    setDiceImage(ROLLED, i);
                }

                if (playerRolls == 1) {
                    // change to @string
                    button.setText("Score");
                }

                resetDices();
                playerRolls++;
            }
        });

        // OnClick listener for each dice ImageView
        for (int i = 0; i < diceViews.length; i++) {
            final int index = i;
            diceViews[i].setOnClickListener(new View.OnClickListener() {
                public void onClick (View v) {
                    if (playerRolls > 1) {
                        if (GameLogic.isDiceUsed(index)){
                            Log.d("", "USED");
                            return;
                        }
                        GameLogic.updateGroupedScore(index);

                        if(target == 3) { // Grading LOW selected
                            if (GameLogic.getGroupedScore() <= 3) { // Selected dice is 3 or less
                                setDiceImage(SCORE, index); // Set appropriate image for each dice
                                GameLogic.handleGradingLow(index);
                            }
                            else if (GameLogic.getGroupedScore() > 3){ //
                                GameLogic.setGroupedScore(0);
                            }
                        } else {

                            setDiceImage(SCORE, index); // Set appropriate image for each dice

                            if(GameLogic.getGroupedScore() == target) {
                                GameLogic.handleGradingElse(index, target);
                                for (int i = 0; i < diceImageIndexArray.size(); i++)
                                    GameLogic.setDiceUsed(i, true);
                                diceImageIndexArray.clear();
                            }
                            else if (GameLogic.getGroupedScore() > target) {
                                for (int i = 0; i < diceImageIndexArray.size(); i++) {
                                    setDiceImage(ROLLED, diceImageIndexArray.get(i)); // Set appropriate image for each dice
                                    GameLogic.setDiceUsed(i, false);
                                    GameLogic.setGroupedScore(0);
                                }
                                diceImageIndexArray.clear();
                                GameLogic.setGroupedScore(0);
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
        switch (GameLogic.getDice(dice)) {
            case 1:
                if (action == CLICKED) {
                    GameLogic.setKeepDice(dice, true);
                    drawable = getResources().getDrawable(R.drawable.grey1);
                } else if (action == SCORE) {
                    drawable = getResources().getDrawable(R.drawable.red1);
                    diceImageIndexArray.add(dice);
                    GameLogic.updateScoreTracker(1);
                } else {
                    drawable = getResources().getDrawable(R.drawable.white1);
                }
                break;
            case 2:
                if (action == CLICKED) {
                    GameLogic.setKeepDice(dice, true);
                    drawable = getResources().getDrawable(R.drawable.grey2);
                } else if (action == SCORE) {
                    drawable = getResources().getDrawable(R.drawable.red2);
                    diceImageIndexArray.add(dice);
                    GameLogic.updateScoreTracker(2);


                } else {
                    drawable = getResources().getDrawable(R.drawable.white2);;
                }
                break;
            case 3:
                if (action == CLICKED) {
                    GameLogic.setKeepDice(dice, true);
                    drawable = getResources().getDrawable(R.drawable.grey3);
                } else if (action == SCORE) {
                    drawable = getResources().getDrawable(R.drawable.red3);
                    diceImageIndexArray.add(dice);
                    GameLogic.updateScoreTracker(3);

                } else {
                    drawable = getResources().getDrawable(R.drawable.white3);
                }
                break;
            case 4:
                if (action == CLICKED) {
                    GameLogic.setKeepDice(dice, true);
                    drawable = getResources().getDrawable(R.drawable.grey4);
                } else if (action == SCORE) {
                    drawable = getResources().getDrawable(R.drawable.red4);
                    diceImageIndexArray.add(dice);
                    GameLogic.updateScoreTracker(4);

                } else {
                    drawable = getResources().getDrawable(R.drawable.white4);
                }
                break;
            case 5:
                if (action == CLICKED) {
                    GameLogic.setKeepDice(dice, true);
                    drawable = getResources().getDrawable(R.drawable.grey5);
                } else if (action == SCORE) {
                    drawable = getResources().getDrawable(R.drawable.red5);
                    diceImageIndexArray.add(dice);
                    GameLogic.updateScoreTracker(5);

                } else {
                    drawable = getResources().getDrawable(R.drawable.white5);
                }
                break;
            case 6:
                if (action == CLICKED) {
                    GameLogic.setKeepDice(dice, true);
                    drawable = getResources().getDrawable(R.drawable.grey6);
                } else if (action == SCORE) {
                    drawable = getResources().getDrawable(R.drawable.red6);
                    diceImageIndexArray.add(dice);
                    GameLogic.updateScoreTracker(6);

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
        String[] items = new String[]{
                "LOW",
                "4",
                "5",
                "6",
                "7",
                "8",
                "9",
                "10",
                "11",
                "12"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                target = position + 3;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
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
            // Randomize dice value
            GameLogic.randomizeDice(i);
            // Set appropriate image for each dice
            setDiceImage(ROLLED, i);
        }
    }

    private void resetGameState() {
        resetDices();
        playerRolls = 0;
        roundScore = 0;
        diceImageIndexArray.clear();
    }

    private void resetDices() {
        for (int i = 0; i < DICES_AMOUNT; i++)
            GameLogic.setDiceUsed(i, false);
    }

}
