package se.umu.lucas.arvidsson.thirty.Controllers;

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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import se.umu.lucas.arvidsson.thirty.R;

import java.util.ArrayList;

import se.umu.lucas.arvidsson.thirty.Models.GameLogic;

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

    private int prevPosition = 0;

    // Dice image index array, used to track selected dices
    private ArrayList<Integer> diceImageIndexArray = new ArrayList<>();;

    // Dice ImageView
    private ImageView[] diceViews = new ImageView[6];
    private int[] imageId = new int[6];

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
        int diceValue = gameLogic.getDice(dice).getDiceValue();
        // Set the correct image for the dice
        diceViews[dice].setImageDrawable(getDrawable(action, dice, diceValue));
    }

    // Gets the correct drawable for each action and dice value
    private Drawable getDrawable(int action, int dice, int diceValue) {
        Drawable drawable;
        if (action == CLICKED) {
            String uri = "drawable/grey" + diceValue;
            int imageResource = getResources().getIdentifier(uri, "drawable", getPackageName());
            gameLogic.setKeepDice(dice, true);
            imageId[dice] = imageResource;
            drawable = getResources().getDrawable(imageResource);
        } else if (action == SCORE) {
            String uri = "drawable/red" + diceValue;
            int imageResource = getResources().getIdentifier(uri, "drawable", getPackageName());
            imageId[dice] = imageResource;
            drawable = getResources().getDrawable(imageResource);
            diceImageIndexArray.add(dice);
            gameLogic.updateScoreTracker(diceValue);

        } else if (action == PAIRED) {
            String uri = "drawable/green" + diceValue;
            int imageResource = getResources().getIdentifier(uri, "drawable", getPackageName());
            imageId[dice] = imageResource;
            drawable = getResources().getDrawable(imageResource);
        } else {
            String uri = "drawable/white" + diceValue;
            int imageResource = getResources().getIdentifier(uri, "drawable", getPackageName());
            imageId[dice] = imageResource;
            drawable = getResources().getDrawable(imageResource);
        }
        return drawable;
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
                if (gameLogic.isGradingLocked()) {
                    spinner.setSelection(prevPosition);
                    Toast.makeText(getApplicationContext(), "Cannot change grading with dices paired", Toast.LENGTH_SHORT).show();
                    return;
                }
                prevPosition = position;
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
        if(gameLogic.getPlayedRounds() == 9)
            return;
        if (gameLogic.getUnusedGradings().get(position).equals("LOW"))
            gameLogic.setTarget(3);
        else
            gameLogic.setTarget(Integer.parseInt(gameLogic.getUnusedGradings().get(position)));
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
        gameLogic.setPlayerRolls(0);
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

    // Save states
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable("gameLogic", gameLogic);
        outState.putIntegerArrayList("diceImageIndexArray", diceImageIndexArray);
        outState.putIntArray("imageId", imageId);


    }

    // Rebuild state
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        gameLogic = savedInstanceState.getParcelable("gameLogic");

        diceImageIndexArray = savedInstanceState.getIntegerArrayList("diceImageIndexArray");

        imageId = savedInstanceState.getIntArray("imageId");


        // Update grading spinner
        updateSpinner();

        // Setup View objects texts
        setupTexts();

        // Redraw Dice images
        for (int i = 0; i < imageId.length; i++) {
            diceViews[i].setImageDrawable(getResources().getDrawable(imageId[i]));
        }



    }

    // Setup texts after state recreation
    private void setupTexts() {
        // Button texts
        if (gameLogic.getPlayerRolls() == 2) {
            button.setText("Score");
            if (gameLogic.getPlayedRounds() >= 9) // Change button text for ending the game
                button.setText("End Game");
        }

        tv_roundsPlayed.setText("Rounds played: " + gameLogic.getPlayedRounds() + " / 10");
        tv_rolls.setText("Rolls: " + gameLogic.getPlayerRolls() + " / 2");

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
        if (gameLogic.getPlayerRolls() > 1) {
            if (gameLogic.isDiceUsed(index)){
                Log.d("", "USED");
                return;
            }
            gameLogic.updateGroupedScore(index);

            if(gameLogic.getTarget() == 3) { // Grading LOW selected
                if (gameLogic.getGroupedScore() <= 3) { // Selected dice is 3 or less
                    gameLogic.setGradingLocked(true);
                    setDiceImage(SCORE, index); // Set appropriate image for each dice (action SCORE adds the dice value to a tracker used for scoring)
                    gameLogic.handleGradingLow(index);
                    setDiceImage(PAIRED, index); // Recolor as a dice used to gain score
                }
                else if (gameLogic.getGroupedScore() > 3){ //
                    gameLogic.setGroupedScore(0);
                }
            }

            else {
                setDiceImage(SCORE, index); // Set appropriate image for each dice

                if(gameLogic.getGroupedScore() == gameLogic.getTarget()) { // Paired dices matches target score for selected grading
                    gameLogic.setGradingLocked(true);
                    gameLogic.handleGradingElse(index);
                    for (int i = 0; i < diceImageIndexArray.size(); i++) {
                        gameLogic.setDiceUsed(diceImageIndexArray.get(i), true); // set each dice used to gain score to used, cannot be used to gain score again this round
                        setDiceImage(PAIRED, diceImageIndexArray.get(i));
                    }

                    diceImageIndexArray.clear();
                }
                else if (gameLogic.getGroupedScore() > gameLogic.getTarget()) { // Paired dices exceeds the target score for selected grading
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

        if (gameLogic.getPlayerRolls() == 2) {

            // SCORE THIS ROUND
            gameLogic.scoreRound();
            gameLogic.setGradingLocked(false);
            // Update gradings menu
            updateSpinner();

            // Ends the game if all rounds played and scored
            if (gameLogic.getPlayedRounds() >= 9) {
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

            gameLogic.increasePlayedRounds();

            tv_roundsPlayed.setText("Rounds played: " + gameLogic.getPlayedRounds() + " / 10");
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

        if (gameLogic.getPlayerRolls() == 1) {
            // Change button text for scoring
            button.setText("Score");
            if (gameLogic.getPlayedRounds() >= 9) // Change button text for ending the game
                button.setText("End Game");
        }

        gameLogic.increasePlayerRolls();

        tv_rolls.setText("Rolls: " + gameLogic.getPlayerRolls() + " / 2");
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
