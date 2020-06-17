package com.example.inlmningsuppgift1.Models;

import android.util.Log;

import java.util.ArrayList;

public class Score {

    private static int dicePairs;
    private static int groupedScore;
    private static int[] gradingLowScore;
    private static ArrayList<Integer> scoreTracker;
    private static int[] roundScore;

    public void Score() {
        dicePairs = 0;
        groupedScore = 0;
        gradingLowScore = new int[6];
        scoreTracker = new ArrayList<>();
        roundScore = new int[10];
    }

    public static int getDicePairs() {
        return dicePairs;
    }

    public static void setDicePairs(int dicePairs) {
        Score.dicePairs = dicePairs;
    }

    public static int getGroupedScore() {
        return groupedScore;
    }

    public static void setGroupedScore(int groupedScore) {
        Score.groupedScore = groupedScore;
    }

    public static int[] getGradingLowScore() {
        return gradingLowScore;
    }

    public static void setGradingLowScore(int[] gradingLowScore) {
        Score.gradingLowScore = gradingLowScore;
    }

    public static ArrayList<Integer> getScoreTracker() {
        return scoreTracker;
    }

    public static void setScoreTracker(ArrayList<Integer> scoreTracker) {
        Score.scoreTracker = scoreTracker;
    }

    public static int[] getRoundScore() {
        return roundScore;
    }

    public static void setRoundScore(int[] roundScore) {
        Score.roundScore = roundScore;
    }

    public static void handleGradingLow(int index){
        Log.d("", "SET USED");
        diceUsed[index] = true;
        for (int i = 0; i < scoreTracker.size(); i++) {
            gradingLowScore[i] = scoreTracker.get(i);
            Log.d("Debug", "" + gradingLowScore[i]);
        }
        groupedScore = 0;
    }


}
