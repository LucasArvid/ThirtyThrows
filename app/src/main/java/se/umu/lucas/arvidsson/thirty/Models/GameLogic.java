package se.umu.lucas.arvidsson.thirty.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/*
    Wrapper class containing gamelogic utilization of the classes Dice and Score.
    Used by controller to update gamestate depending on user action.
    Implements parcelable for saving states
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


    public GameLogic(int amountOfDices) {
        dice = new ArrayList<>();
        fillDiceArray(amountOfDices);
        score = new Score();

        playerRolls = 0;
        playedRounds = 0;
        target = 0;

    }

    // Parcelable constructor
    protected GameLogic(Parcel in) {
        dice = in.createTypedArrayList(Dice.CREATOR);
        score = in.readParcelable(Score.class.getClassLoader());
        playerRolls = in.readInt();
        playedRounds = in.readInt();
        target = in.readInt();
    }

    public static final Creator<GameLogic> CREATOR = new Creator<GameLogic>() {
        @Override
        public GameLogic createFromParcel(Parcel in) {
            return new GameLogic(in);
        }

        @Override
        public GameLogic[] newArray(int size) {
            return new GameLogic[size];
        }
    };

    // Setup for the array containing Dices
    private void fillDiceArray(int size) {
        for (int i = 0; i < size; i++)
            dice.add(new Dice());
    }

    public void updateGroupedScore(int index) {
        score.updateGroupedScore(dice.get(index).getDiceValue());
    }

    public void setGroupedScore(int value) {
        score.setGroupedScore(value);
    }

    public int getGroupedScore() {
        return score.getGroupedScore();
    }

    public void updateScoreTracker(int value) {
        score.updateScoreTracker(value);
    }

    public void handleGradingLow(int index) {
        score.handleGradingLow(dice.get(index));
    }

    public int getDicePairs() {
        return score.getDicePairs();
    }

    public void randomizeDice(int index) {
        dice.get(index).randomizeDice();

    }

    public int[] getGradingLowScore() {
        return score.getGradingLowScore();
    }

    public void handleGradingElse(int index) {
        score.handleGradingElse(dice.get(index));
    }

    public void setKeepDice(int index, boolean KEEP) {
        dice.get(index).setKeepDice(KEEP);
    }

    public boolean getKeepDice(int index) {
        return dice.get(index).isKeepDice();
    }

    public void setDiceUsed(int index, boolean USED) {
        dice.get(index).setDiceUsed(USED);
    }


    public void calculateScore() {
        score.calculateScore(target, playedRounds);
    }

    public void calculateLowScore() {
        score.calculateLowScore();
    }


    public void resetDices() {
        score.resetRoundScoreTracker();
        for (Dice d : dice) {
            d.resetDice();
        }
    }

    public boolean isGradingLocked() {
        return gradingLocked;
    }

    public void setGradingLocked(boolean gradingLocked) {
        this.gradingLocked = gradingLocked;
    }

    public boolean isDiceUsed(int index) {
        return dice.get(index).isDiceUsed();
    }

    public Dice getDice(int index) {
        return dice.get(index);
    }

    public int getTotalScore() {
        return score.getTotalScore();
    }

    public int[] getRoundScore() {
        return score.getRoundScore();
    }

    public ArrayList<Integer> getRoundGrading() {
        return score.getRoundGrading();
    }

    public ArrayList<String> getUnusedGradings() {
        return score.getUnUsedGradings();
    }

    public int getPlayerRolls() {
        return playerRolls;
    }

    public void setPlayerRolls(int playerRolls) {
        this.playerRolls = playerRolls;
    }

    public void increasePlayerRolls() {
        playerRolls ++;
    }

    public int getPlayedRounds() {
        return playedRounds;
    }

    public void setPlayedRounds(int playedRounds) {
        this.playedRounds = playedRounds;
    }

    public void increasePlayedRounds() {
        playedRounds ++;
    }

    public int getTarget() {
        return target;
    }

    public void setTarget(int target) {
        this.target = target;
    }

    public void scoreRound() {
        if (target == 3) // Grading LOW
            calculateLowScore();
        else // Grading else
            calculateScore();
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(dice);
        dest.writeParcelable(score, flags);
        dest.writeInt(playerRolls);
        dest.writeInt(playedRounds);
        dest.writeInt(target);
    }
}
