package com.example.inlmningsuppgift1.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Random;

/*
Custom class containing attributes and functions for handling dice related data,
implements parcelable for saving state
*/
public class Dice implements Parcelable{

    // Boolean to keep track of if the dice should be saved when re-rolling
    private  boolean keepDice;
    // Boolean to keep track of if the dice has been used to pair for score
    private  boolean diceUsed;

    // The dices value (1-6)
    private int dice;

    private Random random = new Random();

    public  Dice() {
        keepDice = false;
        diceUsed = false;
        randomizeDice();
    }

    // Parcelable constructor
    protected Dice(Parcel in) {
        keepDice = in.readByte() != 0;
        diceUsed = in.readByte() != 0;
        dice = in.readInt();
    }

    public static final Parcelable.Creator<Dice> CREATOR = new Parcelable.Creator<Dice>() {
        @Override
        public Dice createFromParcel(Parcel in) {
            return new Dice(in);
        }

        @Override
        public Dice[] newArray(int size) {
            return new Dice[size];
        }
    };

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


    // Randomize the dice value to a value between 1 and 6
    public void randomizeDice() {
        dice = random.nextInt(6) + 1;
    }

    public int getDiceValue() {
        return dice;
    }

    public void resetDice() {
        keepDice = false;
        diceUsed = false;
        randomizeDice();
    }

    public Dice getDice() {
        return this;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (keepDice ? 1 : 0));
        dest.writeByte((byte) (diceUsed ? 1 : 0));
        dest.writeInt(dice);

    }
}
