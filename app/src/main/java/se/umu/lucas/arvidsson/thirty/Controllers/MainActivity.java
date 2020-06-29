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

/**
 * Initial activity graphically displaying the game state to the player.
 * Controller for the model and view.
 */
public class MainActivity extends AppCompatActivity {

    // - Defines - (Constants) //
    private final static int ROLLED = 0;
    private final static int CLICKED = 1;
    private final static int SCORE = 2;
    private final static int PAIRED = 3;
    private final static int DICES_AMOUNT = 6;

    // Roll button, used to re-roll or score dices
    private Button rollButton;
    // Reset button, used to reset the pariing of dices.
    private Button resetButton;

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


    /***
     * Initial create function for the activity
     * @param savedInstanceState Reference to Bundle object, null if first time activity is started.
     */
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
        rollButton.setOnClickListener(new View.OnClickListener() {
            public void onClick (View v) {
                handleButtonClicked(intent);
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            public void onClick (View v) {
                resetDicePairing();
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

    /***
     * Set correct drawable depending on user action for each dice ImageView
     * @param action Int describing action such as rolled, clicked etc.
     * @param dice Int describing which dice was clicked.
     */
    private void setDiceImage(int action, int dice) {
        int diceValue = gameLogic.getDice(dice).getDiceValue();

        // Set the correct image for the dice
        diceViews[dice].setImageDrawable(getDrawable(action, dice, diceValue));
    }

    /***
     * Gets the correct drawable for each action and dice value
     * @param action Int describing action such as rolled, clicked etc.
     * @param dice Int describing which dice was clicked.
     * @param diceValue The value of the selected dice.
     * @return Returns a drawable of the correct dice.
     */
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

    /***
     *  Initial setup for the drop down menu containing gradings
     */
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
                    if(position == prevPosition)
                        return;
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

    /***
     * Updates the target score for the grading and redraws the spinner without the used grading,
     * gets list of unused gradings from Score object contained in GameLogic
     */
    private void updateSpinner(boolean rebuild) {

        adapter.clear();
        adapter.addAll(gameLogic.getUnusedGradings());
        if (!rebuild) {
            updateTarget(0);
            spinner.setSelection(0);
        }

    }

    /***
     * Updates targeted score, gets unused gradings from array in the Score object contained in GameLogic
     * @param position the selected item in the spinner
     */
    private void updateTarget(int position) { // position = object in menu clicked
        if(gameLogic.getPlayedRounds() == 9)
            return;
        if (gameLogic.getUnusedGradings().get(position).equals("LOW"))
            gameLogic.setTarget(3);
        else {
            gameLogic.setTarget(Integer.parseInt(gameLogic.getUnusedGradings().get(position)));
        }
    }

    /***
     * Setup function for view objects
     */
    private void setupView() {
        // Initiate Spinner and Adapter
        spinner = findViewById(R.id.spinner);
        tv_roundsPlayed = findViewById(R.id.roundsPlayed);
        tv_rolls = findViewById(R.id.rolls);
        fillSpinner();

        // Connect to the dices ImageViews
        setupDices();

        // Connect to the buttons
        rollButton = findViewById(R.id.button);
        resetButton = findViewById(R.id.resetButton);
    }

    /***
     * Function for connecting to view objects and setting initial dice images
     */
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

    /***
     * Function for resetting game state and dices
     */
    private void resetGameState() {
        gameLogic.resetDices();
        gameLogic.setPlayerRolls(0);
        diceImageIndexArray.clear();
        for(int i = 0; i < DICES_AMOUNT; i++)
            setDiceImage(ROLLED, i);
    }

    /***
     *  Function for handling screen rotation
     * @param newConfig new configuration object
     */
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

    /***
     * Function called on saving of states
     * @param outState Bundle for saving state
     */
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable("gameLogic", gameLogic);
        outState.putIntegerArrayList("diceImageIndexArray", diceImageIndexArray);
        outState.putIntArray("imageId", imageId);
        outState.putInt("prevPosition", prevPosition);
    }

    /***
     * Function for rebuild state
     * @param savedInstanceState Bundle containing saved states
     */
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        gameLogic = savedInstanceState.getParcelable("gameLogic");

        diceImageIndexArray = savedInstanceState.getIntegerArrayList("diceImageIndexArray");

        imageId = savedInstanceState.getIntArray("imageId");

        prevPosition = savedInstanceState.getInt("prevPosition");


        // Update grading spinner
        updateSpinner(true);

        // Setup View objects texts
        setupTexts();

        // Redraw Dice images
        for (int i = 0; i < imageId.length; i++) {
            diceViews[i].setImageDrawable(getResources().getDrawable(imageId[i]));
        }
    }

    /***
     * Setup texts after state recreation
     */
    private void setupTexts() {
        // Button texts
        if (gameLogic.getPlayerRolls() == 2) {
            rollButton.setText("Score");
            if (gameLogic.getPlayedRounds() >= 9) // Change button text for ending the game
                rollButton.setText("End Game");
        }

        tv_roundsPlayed.setText("Rounds played: " + gameLogic.getPlayedRounds() + " / 10");
        tv_rolls.setText("Rolls: " + gameLogic.getPlayerRolls() + " / 2");

    }

    /***
     * Function for adjusting View objects margins
     * @param margin
     */
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

    /***
     * Function for resetting the dices used for scoring.
     * Can be used if the players wants to change grading after already pairing dices.
     */
    private void resetDicePairing() {
        gameLogic.setGradingLocked(false);
        // reset color and used tracker on all dices.
        for (int i = 0; i < DICES_AMOUNT; i++) {
            setDiceImage(ROLLED, i);
            gameLogic.setDiceUsed(i, false);
        }
        // Reset the amount of dices successfully paired.
        gameLogic.setDicePairs(0);
        // Reset the amount of dices used for scoring with grading LOW.
        gameLogic.resetGradingLowScore();
        // Reset the round score tracker.
        gameLogic.resetRoundScoreTracker();
        // Reset the tracker for which dice is in processes of being paired.
        diceImageIndexArray.clear();
    }

    /***
     * Function for handling dice images being clicked.
     * Functionality dependent on selected grading (target).
     * @param index Dice image that was clicked
     */
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
                gameLogic.setDiceUsed(index, true);
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

    /***
     * Handle button click. Handle scoring if rolls == 2, handles game end if all rounds played and handles rolling else
     * @param intent Intent for starting new activity
     */
    private void handleButtonClicked(Intent intent) {

        if (gameLogic.getPlayerRolls() == 2) {
            // SCORE THIS ROUND
            gameLogic.scoreRound();
            gameLogic.setGradingLocked(false);
            // Update gradings menu
            updateSpinner(false);

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
            rollButton.setText(getResources().getString(R.string.button_name));
            gameLogic.increasePlayedRounds();
            // Set appropriate text.
            tv_roundsPlayed.setText("Rounds played: " + gameLogic.getPlayedRounds() + " / 10");
            tv_rolls.setText("Rolls: 0 / 2");

            // Reset the games state for next round
            resetGameState();
            return;
        }

        // Re-rolls the dices that the user does not want to keep
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
            rollButton.setText("Score");
            if (gameLogic.getPlayedRounds() >= 9) // Change button text for ending the game
                rollButton.setText("End Game");
        }
        gameLogic.increasePlayerRolls();
        tv_rolls.setText("Rolls: " + gameLogic.getPlayerRolls() + " / 2");
    }

    /***
     * Function for avoiding miss-clicking back button (asks for confirmation before terminating app)
     */
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
