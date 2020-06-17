package com.example.inlmningsuppgift1.Models;

import java.util.Random;

public class Dice {

    private  boolean keepDice;
    private  boolean diceUsed;

    private int dice;

    private Random random = new Random();

    public void Dice() {
        keepDice = true;
        diceUsed = true;
        randomizeDice();
    }

    public boolean isKeepDice() {
        return keepDice;
    }

    public void setKeepDice(boolean keepDice) {
        this.keepDice = keepDice;
    }

    public boolean isDiceUsed() {
        return diceUsed;
    }

    public void setDiceUsed(boolean diceUsed) {
        this.diceUsed = diceUsed;
    }



    public void randomizeDice() {
        dice = random.nextInt(6) + 1;

    }

    public void resetDice() {
        keepDice = false;
        diceUsed = false;
        randomizeDice();
    }
}
