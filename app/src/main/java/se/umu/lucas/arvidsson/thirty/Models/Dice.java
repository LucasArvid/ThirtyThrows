package se.umu.lucas.arvidsson.thirty.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Random;

/***
 * Class containing attributes and functions for handling dice related data.
 * Implements parcelable for saving state.
 */
public class Dice implements Parcelable{

    // Boolean to keep track of if the dice should be saved when re-rolling
    private  boolean keepDice;
    // Boolean to keep track of if the dice has been used to pair for score
    private  boolean diceUsed;

    // The dices value (1-6)
    private int dice;

    private Random random = new Random();

    /***
     * Default constructor
     */
    public  Dice() {
        keepDice = false;
        diceUsed = false;
        randomizeDice();
    }

    /***
     * Parcelable constructor
     * @param in Parcel in, for reconstructing state
     */
    protected Dice(Parcel in) {
        keepDice = in.readByte() != 0;
        diceUsed = in.readByte() != 0;
        dice = in.readInt();
    }

    /***
     * Parcelable function that generates instances of the Parcelable class from a Parcel.
     */
    public static final Creator<Dice> CREATOR = new Creator<Dice>() {
        /***
         * Constructing of single object
         * @param in Parcel object
         * @return Constructed class object
         */
        @Override
        public Dice createFromParcel(Parcel in) {
            return new Dice(in);
        }

        /***
         * Constructing of array of object
         * @param size Size of array
         * @return Constructed array of class object
         */
        @Override
        public Dice[] newArray(int size) {
            return new Dice[size];
        }
    };

    /***
     * Getter for tracking if the dice should be kept when rolling or not
     * @return Returns true if the dice should NOT be rolled
     */
    public boolean isKeepDice() {
        return keepDice;
    }

    /***
     * Set whether or not to keep the selected dice when rolling.
     * @param keepDice Whether to keep the dice or not
     */
    public void setKeepDice(boolean keepDice) {
        this.keepDice = keepDice;
    }

    /***
     * Getter for tracking if the dice has already been used for scoring / pairing this round.
     * @return Whether the dice has already been used or not.
     */
    public boolean isDiceUsed() {
        return diceUsed;
    }

    /***
     * Set whether the dice has been used this round or not.
     * @param diceUsed Whether the dice is used or not.
     */
    public void setDiceUsed(boolean diceUsed) {
        this.diceUsed = diceUsed;
    }

    /***
     * Randomizes the dice value to a value between 1 and 6.
     */
    public void randomizeDice() {
        dice = random.nextInt(6) + 1;
    }

    /***
     * Getter for the dices value
     * @return Returns the dice value.
     */
    public int getDiceValue() {
        return dice;
    }

    /***
     * Resets the dice states and randomizes the dice value.
     */
    public void resetDice() {
        keepDice = false;
        diceUsed = false;
        randomizeDice();
    }

    /***
     * Getter for the dice object
     * @return Returns the entire Dice object
     */
    public Dice getDice() {
        return this;
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
        dest.writeByte((byte) (keepDice ? 1 : 0));
        dest.writeByte((byte) (diceUsed ? 1 : 0));
        dest.writeInt(dice);

    }
}
