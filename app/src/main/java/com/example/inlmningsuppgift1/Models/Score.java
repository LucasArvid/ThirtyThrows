package com.example.inlmningsuppgift1.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
/*
Custom class containing attributes and functions for handling score related data,
implements parcelable for saving state
*/
public class Score implements Parcelable {

    // Used to calculate the score when not using grading LOW
    private int dicePairs;

    // Track score gained when using grading LOW
    private int groupedScore;

    // List of dices used for grading LOW score
    private int[] gradingLowScore;

    // Tracks the selected dices value
    private ArrayList<Integer> scoreTracker;

    // List to track which score was gained which round
    private int[] roundScore;
    // List to track which grading was used which round
    private ArrayList<Integer> roundGrading;

    // Players total score
    private int totalScore;

    // List to track which gradings that have not been used to gain score
    private ArrayList<String> unUsedGradings;



    public Score() {
        dicePairs = 0;
        groupedScore = 0;
        gradingLowScore = new int[6];
        scoreTracker = new ArrayList<>();
        roundScore = new int[10];
        roundGrading = new ArrayList<>();

        // Setup gradings list
        setupUnusedGradings();
    }

    // Parcelable constructor
    protected Score(Parcel in) {
        dicePairs = in.readInt();
        groupedScore = in.readInt();
        gradingLowScore = in.createIntArray();
        scoreTracker = in.readArrayList(String.class.getClassLoader());
        roundScore = in.createIntArray();
        roundGrading = in.readArrayList(String.class.getClassLoader());
    }

    public static final Creator<Score> CREATOR = new Creator<Score>() {
        @Override
        public Score createFromParcel(Parcel in) {
            return new Score(in);
        }

        @Override
        public Score[] newArray(int size) {
            return new Score[size];
        }
    };


    public int getGroupedScore() {
        return groupedScore;
    }

    public void setGroupedScore(int newScore) {
        groupedScore = newScore;
    }

    public void updateGroupedScore(int increaseScoreBy) {
        groupedScore += increaseScoreBy;
    }

    public void updateScoreTracker(int value) {
        scoreTracker.add(value);
    }

    public int[] getRoundScore() {
        return roundScore;
    }

    // Tracks the dice value of each used dice below the value 3 when using grading LOW.
    // variables used to calculate score when finishing round.
    public void handleGradingLow(Dice dice){
        dice.setDiceUsed(true);
        for (int i = 0; i < scoreTracker.size(); i++)
            gradingLowScore[i] = scoreTracker.get(i);
        groupedScore = 0;
    }

    // Sets the dices paired to used, and increases the amount of pairs that will give the-
    // user score when finishing the round when not using grading LOW
    public void handleGradingElse(Dice dice, int target, int round) {
        dice.setDiceUsed(true);
        dicePairs ++;
        // Clear score tracker and score counter
        groupedScore = 0;
    }

    // Calculate the total score when not using grading LOW
    public int calculateScore(int grading, int round) {

        int score = 0;
        score = dicePairs * grading;
        roundScore[round] = score;
        roundGrading.add(grading);
        dicePairs = 0;
        totalScore += score;

        // Remove the used grading from the list, also used by controller to update the spinner menu
        while(unUsedGradings.contains(Integer.toString(grading)))
            unUsedGradings.remove(Integer.toString(grading));

        return score;
    }

    // Calculate the total score gained from grading LOW
    public int calculateLowScore() {
        int score = 0;
        for (int i = 0; i < gradingLowScore.length; i++) {
            score += gradingLowScore[i];
        }

        roundScore[roundGrading.size()] = score;
        roundGrading.add(3);
        totalScore += score;

        // Remove the grading LOW from the list, also used by controller to update the spinner menu
        while(unUsedGradings.contains("LOW"))
            unUsedGradings.remove("LOW");

        return score;
    }

    public void resetRoundScoreTracker() {
        scoreTracker.clear();
        groupedScore = 0;
    }

    public ArrayList<Integer> getRoundGrading() {
        return roundGrading;
    }

    public int getTotalScore() {
        return totalScore;
    }

    // Setup list containing gradings used for scoring
    public void setupUnusedGradings() {
        unUsedGradings = new ArrayList<>();
        unUsedGradings.add("LOW");
        for (int i = 4; i <= 12; i++)
            unUsedGradings.add(Integer.toString(i));
    }

    public ArrayList<String> getUnUsedGradings() {
        return unUsedGradings;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(dicePairs);
        dest.writeInt(groupedScore);
        dest.writeIntArray(gradingLowScore);
        dest.writeList(scoreTracker);
        dest.writeIntArray(roundScore);
        dest.writeList(roundGrading);

    }
}
