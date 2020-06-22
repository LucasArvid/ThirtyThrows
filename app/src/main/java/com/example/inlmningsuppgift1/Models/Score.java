package com.example.inlmningsuppgift1.Models;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.ArrayList;

public class Score implements Parcelable {

    private static int dicePairs;
    private static int groupedScore;
    private static int[] gradingLowScore;
    private static ArrayList<Integer> scoreTracker;
    private int[] roundScore;
    private ArrayList<Integer> roundGrading;
    private static int totalScore;

    private ArrayList<String> unUsedGradings;



    public Score() {
        dicePairs = 0;
        groupedScore = 0;
        gradingLowScore = new int[6];
        scoreTracker = new ArrayList<>();
        roundScore = new int[10];
        roundGrading = new ArrayList<>();

        setupUnusedGradings();
    }

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

    public int getDicePairs() {
        return dicePairs;
    }

    public void setDicePairs(int dicePairs) {
        Score.dicePairs = dicePairs;
    }

    public int getGroupedScore() {
        return groupedScore;
    }

    public void setGroupedScore(int groupedScore) {
        Score.groupedScore = groupedScore;
    }

    public void updateGroupedScore(int groupedScore) {
        Score.groupedScore += groupedScore;
    }

    public int[] getGradingLowScore() {
        return gradingLowScore;
    }

    public void setGradingLowScore(int[] gradingLowScore) {
        Score.gradingLowScore = gradingLowScore;
    }

    public ArrayList<Integer> getScoreTracker() {
        return scoreTracker;
    }

    public static void updateScoreTracker(int value) {
        scoreTracker.add(value);
    }

    public void setScoreTracker(ArrayList<Integer> scoreTracker) {
        Score.scoreTracker = scoreTracker;
    }

    public int[] getRoundScore() {
        return roundScore;
    }

    public void handleGradingLow(Dice dice){
        Log.d("", "SET USED");
        dice.setDiceUsed(true);
        for (int i = 0; i < scoreTracker.size(); i++) {
            gradingLowScore[i] = scoreTracker.get(i);
            Log.d("Debug", "" + gradingLowScore[i]);
        }
        groupedScore = 0;
    }

    public void handleGradingElse(Dice dice, int target, int round) {
        Log.d("", "SET USED");
        dice.setDiceUsed(true);

        dicePairs ++;

        // Clear score tracker and score counter
        groupedScore = 0;
    }

    public int calculateScore(int grading, int round) {
        // Summan av alla för valet ingående tärningars värde ger poängen
        int score = 0;
        score = dicePairs * grading;
        Log.d("GameLogic", "GameLogic Else Score: " + score);
        roundScore[round] = score;
        roundGrading.add(grading);
        dicePairs = 0;
        totalScore += score;

        while(unUsedGradings.contains(Integer.toString(grading)))
            unUsedGradings.remove(Integer.toString(grading));

        return score;
    }

    public int calculateLowScore() {
        int score = 0;
        for (int i = 0; i < gradingLowScore.length; i++) {
            score += gradingLowScore[i];
        }

        roundScore[roundGrading.size()] = score;
        roundGrading.add(3);
        totalScore += score;
        Log.d("GameLogic", "GameLogic Low Score: " + score);

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

    public void setupUnusedGradings() {
        unUsedGradings = new ArrayList<>();
        unUsedGradings.add("LOW");
        for (int i = 4; i <= 12; i++)
            unUsedGradings.add(Integer.toString(i));
    }

    public ArrayList<String> getUnUsedGradings() {
        return unUsedGradings;
    }

    public void resetUnusedGradings() {
        setupUnusedGradings();
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
