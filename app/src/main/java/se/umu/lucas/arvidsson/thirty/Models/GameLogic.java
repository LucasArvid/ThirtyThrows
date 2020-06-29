package se.umu.lucas.arvidsson.thirty.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/***
 * Class containing the game logic and utilization of the classes Dice and Score.
 * Used by a controller to update game state depending on user action.
 * Implements parcelable for saving states.
 */
public class GameLogic implements Parcelable {

    // Array of dices
    private ArrayList<Dice> dice;

    // Rolls and rounds tracker
    private int playerRolls;
    private int playedRounds;

    private boolean gradingLocked = false;

    // target for sum of dices when grading
    private int target;

    // Score class containing all data relevant to the games score
    private Score score;

    /***
     * Default constructor
     * @param amountOfDices The amount of dices to be created.
     */
    public GameLogic(int amountOfDices) {
        dice = new ArrayList<>();
        fillDiceArray(amountOfDices);
        score = new Score();

        playerRolls = 0;
        playedRounds = 0;
        target = 0;

    }

    /***
     * Parcelable constructor
     * @param in Parcel in, for reconstructing state
     */
    protected GameLogic(Parcel in) {
        dice = in.createTypedArrayList(Dice.CREATOR);
        score = in.readParcelable(Score.class.getClassLoader());
        playerRolls = in.readInt();
        playedRounds = in.readInt();
        target = in.readInt();
    }

    /***
     * Parcelable function that generates instances of the Parcelable class from a Parcel.
     */
    public static final Creator<GameLogic> CREATOR = new Creator<GameLogic>() {
        /***
         * Constructing of single object
         * @param in Parcel object
         * @return Constructed class object
         */
        @Override
        public GameLogic createFromParcel(Parcel in) {
            return new GameLogic(in);
        }

        /***
         * Constructing of array of object
         * @param size Size of array
         * @return Constructed array of class object
         */
        @Override
        public GameLogic[] newArray(int size) {
            return new GameLogic[size];
        }
    };

    /***
     * Function for setting up the list containing the Dice objects.
     * @param size The amount of dices to be constructed.
     */
    private void fillDiceArray(int size) {
        for (int i = 0; i < size; i++)
            dice.add(new Dice());
    }

    /***
     * Function for updating the Score objects grouped score tracker with the selected dice's value.
     * @param index The selected dice.
     */
    public void updateGroupedScore(int index) {
        score.updateGroupedScore(dice.get(index).getDiceValue());
    }

    /***
     * Function for setting the Score objects groupd score tracker. Also used to reset the tracker to 0.
     * @param value The new score.
     */
    public void setGroupedScore(int value) {
        score.setGroupedScore(value);
    }

    /***
     * Getter for the tracker of score gained whit grading LOW.
     * @return The score gained with grading LOW.
     */
    public int getGroupedScore() {
        return score.getGroupedScore();
    }

    /***
     * Function for updating the Score objects score tracker.
     * @param value Score to be updated with.
     */
    public void updateScoreTracker(int value) {
        score.updateScoreTracker(value);
    }

    /***
     * Function used for resetting the Score objects score tracker when using grading LOW.
     */
    public void resetGradingLowScore() {
        score.resetGradingLowScore();
    }

    /***
     * Function for calling the Score objects grade handler when selecting dices for grading LOW.
     * @param index
     */
    public void handleGradingLow(int index) {
        dice.get(index).setDiceUsed(true);
        score.handleGradingLow();
    }

    /***
     *  Function for randomizing a selected dice's value.
     * @param index The selected dice.
     */
    public void randomizeDice(int index) {
        dice.get(index).randomizeDice();

    }

    /***
     * Function for calling the Score objects grade handler when pairing dices when grading is not LOW.
     * @param index The selected dice.
     */
    public void handleGradingElse(int index) {
        dice.get(index).setDiceUsed(true);
        score.handleGradingElse();
    }

    /***
     * Setter for whether the selected dice should be kept when rolling or not.
     * @param index The selected dice.
     * @param KEEP Whether the dice should be kept or not.
     */
    public void setKeepDice(int index, boolean KEEP) {
        dice.get(index).setKeepDice(KEEP);
    }

    /***
     * Getter for whether the selected dice should be kept when rolling or not.
     * @param index The selected dice.
     * @return Whether the dice should be kept while rolling.
     */
    public boolean getKeepDice(int index) {
        return dice.get(index).isKeepDice();
    }

    /***
     * Function for setting the Score objects amount of successfully paired dices.
     * @param pairs The amount of successful pairs.
     */
    public void setDicePairs(int pairs) {
        score.setDicePairs(pairs);
    }

    /***
     * Setter for whether the selected dice is used or not.
     * @param index Selected dice.
     * @param USED Whether the dice should be set to used or not.
     */
    public void setDiceUsed(int index, boolean USED) {
        dice.get(index).setDiceUsed(USED);
    }

    /***
     * Function for calling the Score objects scoring function when not using grading LOW.
     */
    public void calculateScore() {
        score.calculateScore(target, playedRounds);
    }

    /***
     * Function for calling the Score objects scoring function when using grading LOW.
     */
    public void calculateLowScore() {
        score.calculateLowScore();
    }

    /***
     * Function for resetting the dice state and the Score objects round score tracker.
     */
    public void resetDices() {
        score.resetRoundScoreTracker();
        for (Dice d : dice) {
            d.resetDice();
        }
    }

    /***
     * Function for reseting the Score objects tracker of the selected dice's value.
     */
    public void resetRoundScoreTracker() {
        score.resetRoundScoreTracker();
    }

    /***
     * Getter for whether the grading menu is locked.
     * @return Returns whether the grading menu is locked or not.
     */
    public boolean isGradingLocked() {
        return gradingLocked;
    }

    /***
     * Function for locking the grading menu.
     * The grading menu cannot be utilized when dices already have been paired.
     * @param gradingLocked Whether the grading menu should be locked or not.
     */
    public void setGradingLocked(boolean gradingLocked) {
        this.gradingLocked = gradingLocked;
    }

    /***
     *  Getter for tracking if the selected dice is already used or not.
     * @param index Position of the selected dice
     * @return Returns whether the dice is used or not.
     */
    public boolean isDiceUsed(int index) {
        return dice.get(index).isDiceUsed();
    }

    /***
     * Getter for a selected Dice in the list of dices.
     * @param index Position of the selected dice.
     * @return Returns the selected Dice object
     */
    public Dice getDice(int index) {
        return dice.get(index);
    }

    /**
     * Getter for the Score objects total score.
     * @return Returns the total score.
     */
    public int getTotalScore() {
        return score.getTotalScore();
    }

    /***
     * Getter for the Score objects array of round scores.
     * The array tracks which score was received which round.
     * @return Returns the entire array of round scores.
     */
    public int[] getRoundScore() {
        return score.getRoundScore();
    }

    /***
     * Getter for the Score objects list of round gradings.
     * The list tracks which grading was used which round.
     * @return The entire list of round gradings.
     */
    public ArrayList<Integer> getRoundGrading() {
        return score.getRoundGrading();
    }

    /***
     * Getter for the Score objects list of unused gradings.
     * @return Returns the entire list of unused gradings.
     */
    public ArrayList<String> getUnusedGradings() {
        return score.getUnUsedGradings();
    }

    /***
     * Getter for the player rolls counter
     * @return The amount of player rolls.
     */
    public int getPlayerRolls() {
        return playerRolls;
    }

    /***
     * Function for setting the amount of player rolls. Also utilized to reset the counter by setting to 0.
     * @param playerRolls The amount of player rolls.
     */
    public void setPlayerRolls(int playerRolls) {
        this.playerRolls = playerRolls;
    }

    /***
     * Function for incrementing the amount of player rolls.
     */
    public void increasePlayerRolls() {
        playerRolls ++;
    }

    /***
     * Getter for the counter of rounds played.
     * @return Returns the amount of rounds played.
     */
    public int getPlayedRounds() {
        return playedRounds;
    }

    /***
     * Function for incrementing the counter for rounds played.
     */
    public void increasePlayedRounds() {
        playedRounds ++;
    }

    /***
     * Getter for the target score for the current grading.
     * @return The target score for the current grading.
     */
    public int getTarget() {
        return target;
    }

    /***
     * Setter for the target score for the current grading.
     * @param target The target score for the current grading.
     */
    public void setTarget(int target) {
        this.target = target;
    }

    /***
     * Function for scoring the round depending on the grading.
     */
    public void scoreRound() {
        if (target == 3) // Grading LOW
            calculateLowScore();
        else // Grading else
            calculateScore();
    }

    /***
     * Parcelable function for creating bitmask. Returns 0 if there is no need for FileDescriptor, CONTENTS_FILE_DESCRIPTOR otherwise.
     * @return Returns 0
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /***
     * Parcel function for saving the object state
     * @param dest Parcel to be saved to.
     * @param flags Flags describing how the object should be written.
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(dice);
        dest.writeParcelable(score, flags);
        dest.writeInt(playerRolls);
        dest.writeInt(playedRounds);
        dest.writeInt(target);
    }
}
