package com.example.inlmningsuppgift1.Models;

import android.util.Log;

import java.util.ArrayList;
import java.util.*;

/*
    Class for game logic utility functions such as calculating player score.
 */


public class GameLogic {

    private static int dicePairs = 0;
    private static int groupedScore = 0;
    private static int[] gradingLowScore = new int[6];

    private static boolean[] keepDice = new boolean[6];
    private static boolean[] diceUsed = new boolean[6];

    private static ArrayList<Integer> scoreTracker = new ArrayList<>();

    private static int[] dices = new int[6];

    private static Random random = new Random();

    private static int roundScore = 0;


    public static void updateGroupedScore(int index) {
        groupedScore += dices[index];
    }

    public static void setGroupedScore(int value) {
        groupedScore = 0;
    }

    public static int getGroupedScore() {
        return groupedScore;
    }

    public static void updateScoreTracker(int value) {
        scoreTracker.add(value);
        Log.d("", "size" +scoreTracker.size());
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

    public static void randomizeDice(int index) {
        dices[index] = random.nextInt(6) + 1;

    }

    public static void handleGradingElse(int index, int target) {
        Log.d("", "SET USED");
        diceUsed[index] = true;

        dicePairs ++;
        // Send for score calculation
        roundScore = GameLogic.calculateScore(target);

        // Clear score tracker and score counter
        groupedScore = 0;
    }

    public static void setKeepDice(int index, boolean KEEP) {
        keepDice[index] = KEEP;
    }

    public static boolean getKeepDice(int index) {
        return keepDice[index];
    }

    public static void setDiceUsed(int index, boolean USED) {
        diceUsed[index] = USED;
    }


    public static int calculateScore(int grading) {
        // Summan av alla för valet ingående tärningars värde ger poängen
        int score = 0;
        score = dicePairs * grading;
        Log.d("GameLogic", "GameLogic Else Score: " + score);
        return score;
    }

    public static int calculateLowScore() {
        int score = 0;
        for (int i = 0; i < gradingLowScore.length; i++) {
            score += gradingLowScore[i];
        }
        Log.d("GameLogic", "GameLogic Low Score: " + score);
        return score;
    }


    public static void resetDices() {
        scoreTracker.clear();
        groupedScore = 0;
    }

    public static boolean isDiceUsed(int index) {
        return diceUsed[index];
    }

    public static int getDice(int index) {
        return dices[index];
    }
}
