package com.example.inlmningsuppgift1.Models;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.Random;

public class Dice implements Parcelable{

    private  boolean keepDice;
    private  boolean diceUsed;

    private int dice;

    private Random random = new Random();

    public  Dice() {
        keepDice = false;
        diceUsed = false;
        randomizeDice();
    }
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
