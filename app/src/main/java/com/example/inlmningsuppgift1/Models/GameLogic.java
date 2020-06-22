package com.example.inlmningsuppgift1.Models;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.ArrayList;
import java.util.*;

/*
    Class for game logic utility functions such as calculating player score.
 */


public class GameLogic implements Parcelable {

    // Array of dices
    private ArrayList<Dice> dice;
    private Score score;

    public GameLogic(int amountOfDices) {
        dice = new ArrayList<>();
        fillDiceArray(amountOfDices);
        score = new Score();

    }

    protected GameLogic(Parcel in) {
        dice = in.createTypedArrayList(Dice.CREATOR);
        score = in.readParcelable(Score.class.getClassLoader());
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

    public void randomizeDice(int index) {
        dice.get(index).randomizeDice();

    }

    public void handleGradingElse(int index, int target, int round) {
        score.handleGradingElse(dice.get(index), target, round);
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


    public void calculateScore(int grading, int round) {
        score.calculateScore(grading, round);
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(dice);
        dest.writeParcelable(score, flags);
    }
}
