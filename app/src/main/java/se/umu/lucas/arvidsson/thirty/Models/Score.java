package se.umu.lucas.arvidsson.thirty.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/***
 * Class containing the attributes and functions for handling the game score related data.
 * Implements parcelable for saving the state.
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

    /***
     * Default constructor
     */
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

    /***
     * Parcelable constructor
     * @param in Parcel in, for reconstructing state
     */
    protected Score(Parcel in) {
        dicePairs = in.readInt();
        groupedScore = in.readInt();
        gradingLowScore = in.createIntArray();
        scoreTracker = in.readArrayList(String.class.getClassLoader());
        roundScore = in.createIntArray();
        roundGrading = in.readArrayList(String.class.getClassLoader());
        unUsedGradings = in.readArrayList(String.class.getClassLoader());
    }

    /***
     * Parcelable function that generates instances of the Parcelable class from a Parcel.
     */
    public static final Creator<Score> CREATOR = new Creator<Score>() {
        /***
         * Constructing of single object
         * @param in Parcel object
         * @return Constructed class object
         */
        @Override
        public Score createFromParcel(Parcel in) {
            return new Score(in);
        }

        /***
         * Constructing of array of object
         * @param size Size of array
         * @return Constructed array of class object
         */
        @Override
        public Score[] newArray(int size) {
            return new Score[size];
        }
    };

    /***
     * Getter for the grouped score tracker which tracks the amount of score gained with grading LOW.
     * @return The grouped score.
     */
    public int getGroupedScore() {
        return groupedScore;
    }

    /***
     * Setter for the grouped score tracker.
     * @param newScore The new grouped score.
     */
    public void setGroupedScore(int newScore) {
        groupedScore = newScore;
    }

    /***
     * Function for reseting the score tracker when using grading LOW.
     */
    public void resetGradingLowScore() {
        gradingLowScore = null;
        gradingLowScore = new int[6];
    }

    /***
     * Function for updating the grouped score tracker.
     * @param increaseScoreBy The amount to increase the grouped score tracker with.
     */
    public void updateGroupedScore(int increaseScoreBy) {
        groupedScore += increaseScoreBy;
    }

    /***
     * Function for updating the list containing the selected dices score value.
     * @param value The new score to be added.
     */
    public void updateScoreTracker(int value) {
        scoreTracker.add(value);
    }

    /***
     * Function for getting the array tracking which score was gained which round.
     * @return Returns the entire array.
     */
    public int[] getRoundScore() {
        return roundScore;
    }

    /***
     * Function for setting the amount of dice successfully paired. Also used to reset the tracker.
     * @param pairs The amount of pairs.
     */
    public void setDicePairs(int pairs) {
        dicePairs = pairs;
    }

    /***
     * Tracks the dice value of each used dice below the value 3 when using grading LOW.
     * Variables are used to calculate score when finishing round.
     */
    public void handleGradingLow(){
        for (int i = 0; i < scoreTracker.size(); i++)
            gradingLowScore[i] = scoreTracker.get(i);
        groupedScore = 0;
    }

    /***
     * Sets the dices paired to used, and increases the amount of pairs that will give the-
     * user score when finishing the round when not using grading LOW
     */
    public void handleGradingElse() {
        dicePairs ++;
        // Clear score tracker and score counter
        groupedScore = 0;
    }

    /***
     * Function for calculating the total round score when not using grading LOW.
     * @param grading The selected grading.
     * @param round The current round.
     * @return The calculated score.
     */
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

    /***
     * Function for calculating the total round score when using grading LOW.
     * @return The calculated score.
     */
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

    /***
     * Function for resetting the round score tracker. Also resets the tracker for the dice values used for grading LOW
     */
    public void resetRoundScoreTracker() {
        scoreTracker.clear();
        groupedScore = 0;
    }

    /***
     * Function for getting the list tracking which grading was used which round.
     * @return The entire List.
     */
    public ArrayList<Integer> getRoundGrading() {
        return roundGrading;
    }

    /***
     * Function for getting the total score.
     * @return The total score.
     */
    public int getTotalScore() {
        return totalScore;
    }

    /***
     * Function for setting up the list containing the unused gradings.
     */
    public void setupUnusedGradings() {
        unUsedGradings = new ArrayList<>();
        unUsedGradings.add("LOW");
        for (int i = 4; i <= 12; i++)
            unUsedGradings.add(Integer.toString(i));
    }

    /***
     * Function for getting the list containing the currently unused gradings.
     * @return
     */
    public ArrayList<String> getUnUsedGradings() {
        return unUsedGradings;
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

        dest.writeInt(dicePairs);
        dest.writeInt(groupedScore);
        dest.writeIntArray(gradingLowScore);
        dest.writeList(scoreTracker);
        dest.writeIntArray(roundScore);
        dest.writeList(roundGrading);
        dest.writeList(unUsedGradings);

    }
}
