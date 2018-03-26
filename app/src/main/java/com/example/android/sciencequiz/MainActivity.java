package com.example.android.sciencequiz;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    //set currentPoints value to 0 for new scoring session
    public int currentPoints = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //create an onClickListener for the reset button, and runs function resetAnswers onCilck
        Button reset = findViewById(R.id.reset);
        reset.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                resetAnswers();
            }
        });
    }

    /**
     * This function checks the answers provided by the user upon pressing the "Submit" button.
     * It then grades the quiz and presents a total line along with a total Toast message
     *
     * @param view Total possible points: 1 point for each correct EditText/Radio button entry +
     *             1 point for each correct checkbox entry = total 8 points
     */

    public void checkAnswers(View view) {

        //create variables

        int totalPoints = 8;
        String answer;

        //get string response to answer1, then compare answer1 to correct answer, log points

        String userEntry = ((EditText) findViewById(R.id.quiz_answer_1)).getText().toString();
        checkEditText(userEntry, getString(R.string.question1_answer), getString(R.string.question1_answer_2), R.id.answer1);

        // compare answer2 to correct answer
        int question2 = 0;  //there are 3 right answers in this question. This keeps total point value to ensure question is marked wrong if <3 points are received.
        int question2_wrong = 0;

        CheckBox checkbox = findViewById(R.id.quiz_answer_2a);

        //a,b,c is true
        if (checkbox.isChecked()) {
            question2 = question2 + 1;
            setCorrect(R.id.quiz_answer_2a, true);
        }

        checkbox = findViewById(R.id.quiz_answer_2b);
        if (checkbox.isChecked()) {
            question2 = question2 + 1;
            setCorrect(R.id.quiz_answer_2b, true);
        }

        checkbox = findViewById(R.id.quiz_answer_2c);
        if (checkbox.isChecked()) {
            question2 = question2 + 1;
            setCorrect(R.id.quiz_answer_2c, true);
        }

        checkbox = findViewById(R.id.quiz_answer_2d);
        if (checkbox.isChecked()) {
            setCorrect(R.id.quiz_answer_2d, false);
            question2_wrong = question2_wrong+1;
        }

        checkbox = findViewById(R.id.quiz_answer_2e);
        if (checkbox.isChecked()) {
            setCorrect(R.id.quiz_answer_2e, false);
            question2_wrong = question2_wrong+1;
        }

        if (question2 < 3) {
            answer = getString(R.string.question2_answer);
            updateAnswer(R.id.answer2, answer, false);
        } else if (question2 == 3) {
            //check if there are wrong answers included as well
            if (question2_wrong>0) {
                answer = getString(R.string.question2_answer_someWrong);
                ((TextView) findViewById(R.id.answer2)).setText(answer);
                setCorrect(R.id.answer2, true);
            } else {
                answer = getString(R.string.correct_answer);
                updateAnswer(R.id.answer2, answer, true);
            }
        } else {
            Toast toast = Toast.makeText(getApplicationContext(), R.string.question2_wrongPoints, Toast.LENGTH_SHORT);
            toast.show();
            return;
        }

        currentPoints = currentPoints + question2;

        // compare answer3 to correct answer,then log points

        userEntry = ((EditText) findViewById(R.id.quiz_answer_3)).getText().toString();
        checkEditText(userEntry, getString(R.string.question3_answer), getString(R.string.question3_answer), R.id.answer3);

        //compare answer 4 to correct answer
        RadioGroup question4 = findViewById(R.id.question_4_group);
        int selectedValueId = question4.getCheckedRadioButtonId();
        checkRadio(selectedValueId, R.id.quiz_answer_4d, R.id.answer4, getString(R.string.question4_answer));

        //compare answer 5 to correct answer, then compare answer1 to correct answer, log points

        userEntry = ((EditText) findViewById(R.id.quiz_answer_5)).getText().toString();
        checkEditText(userEntry, getString(R.string.question5_answer), getString(R.string.question5_answer_2), R.id.answer5);

        //compare answer 6 to correct answer
        RadioGroup question6 = findViewById(R.id.question_6_group);
        selectedValueId = question6.getCheckedRadioButtonId();
        checkRadio(selectedValueId, R.id.quiz_answer_6a, R.id.answer6, getString(R.string.question6_answer));

        //Calculate score
        String message = getString(R.string.total, currentPoints);
        double scorePercent = (((double) currentPoints / (double) totalPoints) * 100);

        //if score > 60, light bulb lights!
        if (scorePercent >= 60) {
            ((ImageView) findViewById(R.id.lightbulb)).setImageResource(R.drawable.lighton);
        }

        //make toast
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

        //create & display total message in Total TextViwe
        message = message + " This is a score of " + scorePercent + "%!";
        ((TextView) findViewById(R.id.Total)).setText(message);

        //scroll to top of quiz to see answers
        ScrollView sv = findViewById(R.id.myScroll);
        sv.scrollTo(0, 0);
    }

    /**
     * function checkEditText compares the user input against correct answers in strings.xml
     * It then updates the answer line and appropriate correct/incorrect colors as required.
     *
     * @param answer          - user answer
     * @param possibleAnswer1 - possible answer 1 (full name)
     * @param possibleAnswer2 - possible answer 2 (last name only)
     * @param answerID        - textView ID where the answer should be inputted after.
     */
    private void checkEditText(String answer, String possibleAnswer1, String possibleAnswer2, int answerID) {

        if (answer.trim().equalsIgnoreCase(possibleAnswer1.trim())) {
            currentPoints = currentPoints + 1;
            //set answer to correct and display green
            updateAnswer(answerID, possibleAnswer1, true);
        } else if (answer.trim().equalsIgnoreCase(possibleAnswer2.trim())) {
            currentPoints = currentPoints + 1;
            //set answer to correct and display green
            updateAnswer(answerID, possibleAnswer1, true);
        } else {
            //set answer to incorrect, display correct answer, and display red
            answer = possibleAnswer1 + " is correct.";
            updateAnswer(answerID, answer, false);
        }
    }

    /**
     * the checkRadio function compares the user provided answer against the known answer, updates the
     * color of the radio selection, and applies the right answer in the answer TextView
     *
     * @param selectedValueId - the answer the user selected
     * @param correctValue    - the correct answer id
     * @param answerID        - TextView id where the answer is applied
     * @param answer          - String of correct answer text.
     */
    private void checkRadio(int selectedValueId, int correctValue, int answerID, String answer) {

        if (selectedValueId == correctValue) {
            //correct
            setCorrect(selectedValueId, true);
            updateAnswer(answerID, answer, true);
            currentPoints = currentPoints + 1;
        } else {
            //incorrect
            setCorrect(selectedValueId, false);
            updateAnswer(answerID, answer, false);

        }
    }

    /**
     * The updateAnswer function updates the answer line below each question after the quiz is submitted
     * and gives it the appropriate correct/incorrect color
     *
     * @param viewID is the number value of the view
     * @param answer is the answer for the question referenced
     */
    public void updateAnswer(int viewID, String answer, boolean correct) {

        String finalAnswer = correct ? getString(R.string.correct_answer) : getString(R.string.incorrect_answer) + " " + answer;
        ((TextView) findViewById(viewID)).setText(finalAnswer);
        setCorrect(viewID, correct);
    }

    /**
     * The setCorrect function updates the textColor values of answers after submitting.
     * Correct is green; incorrect is red
     *
     * @param viewId  the int view id of the text object being changed
     * @param correct a boolean value corresponding with right/wrong answers
     */
    public void setCorrect(int viewId, boolean correct) {
        //the ? operator says that if the colorId is TRUE, that it uses the correct color (green),
        //otherwise it will use the incorrect color (red) - both of which are in colors.xml
        int colorId = correct ? R.color.correct : R.color.incorrect;
        int color = ContextCompat.getColor(getApplicationContext(), colorId);
        ((TextView) findViewById(viewId)).setTextColor(color);
    }

    /**
     * function resetAnswers resets all user entries to blank, resets the colors
     * and turns off the light so the quiz can be taken again
     */
    public void resetAnswers() {
        //get integer of color number for resetting RadioButtons & CheckBoxes
        int standardColor = getResources().getColor(R.color.standardText);
        // Reset EditText values & colors
        ((EditText) findViewById(R.id.quiz_answer_1)).setText("");
        (findViewById(R.id.quiz_answer_1)).getResources().getColor(R.color.standardText);
        ((EditText) findViewById(R.id.quiz_answer_3)).setText("");
        (findViewById(R.id.quiz_answer_3)).getResources().getColor(R.color.standardText);
        ((EditText) findViewById(R.id.quiz_answer_5)).setText("");
        (findViewById(R.id.quiz_answer_5)).getResources().getColor(R.color.standardText);
        // Reset RadioGroup selections and colors
        RadioGroup rg = findViewById(R.id.question_4_group);
        rg.clearCheck();
        //reset question 4 group text & color
        ((RadioButton) findViewById(R.id.quiz_answer_4a)).setTextColor(standardColor);
        ((RadioButton) findViewById(R.id.quiz_answer_4b)).setTextColor(standardColor);
        ((RadioButton) findViewById(R.id.quiz_answer_4c)).setTextColor(standardColor);
        ((RadioButton) findViewById(R.id.quiz_answer_4d)).setTextColor(standardColor);
        //reset question 6 group text & color
        rg = findViewById(R.id.question_6_group);
        rg.clearCheck();
        ((RadioButton) findViewById(R.id.quiz_answer_6a)).setTextColor(standardColor);
        ((RadioButton) findViewById(R.id.quiz_answer_6b)).setTextColor(standardColor);
        //reset value and color of all CheckBox views
        ((CheckBox) findViewById(R.id.quiz_answer_2a)).setChecked(false);
        ((CheckBox) findViewById(R.id.quiz_answer_2a)).setTextColor(standardColor);
        ((CheckBox) findViewById(R.id.quiz_answer_2b)).setChecked(false);
        ((CheckBox) findViewById(R.id.quiz_answer_2b)).setTextColor(standardColor);
        ((CheckBox) findViewById(R.id.quiz_answer_2c)).setChecked(false);
        ((CheckBox) findViewById(R.id.quiz_answer_2c)).setTextColor(standardColor);
        ((CheckBox) findViewById(R.id.quiz_answer_2d)).setChecked(false);
        ((CheckBox) findViewById(R.id.quiz_answer_2d)).setTextColor(standardColor);
        ((CheckBox) findViewById(R.id.quiz_answer_2e)).setChecked(false);
        ((CheckBox) findViewById(R.id.quiz_answer_2e)).setTextColor(standardColor);
        //set value of all TextViews to empty strings ("")
        ((TextView) findViewById(R.id.answer1)).setText("");
        ((TextView) findViewById(R.id.answer2)).setText("");
        ((TextView) findViewById(R.id.answer3)).setText("");
        ((TextView) findViewById(R.id.answer4)).setText("");
        ((TextView) findViewById(R.id.answer5)).setText("");
        ((TextView) findViewById(R.id.answer6)).setText("");
        ((TextView) findViewById(R.id.Total)).setText("");
        //Turn off the light!
        ((ImageView) findViewById(R.id.lightbulb)).setImageResource(R.drawable.lightoff);
        //scroll to top of quiz
        ScrollView sv = findViewById(R.id.myScroll);
        sv.scrollTo(0, 0);
        //reset points
        currentPoints = 0;
    }


}


