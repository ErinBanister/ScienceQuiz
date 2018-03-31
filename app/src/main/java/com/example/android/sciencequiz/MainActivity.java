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
    public int currentPoints = 0;
    public Button resetButton;
    public EditText quizAnswer1;
    public TextView answer1;
    public CheckBox quizAnswer2a;
    public CheckBox quizAnswer2b;
    public CheckBox quizAnswer2c;
    public CheckBox quizAnswer2d;
    public CheckBox quizAnswer2e;
    public TextView answer2;
    public EditText quizAnswer3;
    public TextView answer3;
    public RadioGroup answer4group;
    public RadioGroup answer6group;
    public RadioButton quizAnswer4a;
    public RadioButton quizAnswer4b;
    public RadioButton quizAnswer4c;
    public RadioButton quizAnswer4d;
    public TextView answer4;
    public EditText quizAnswer5;
    public TextView answer5;
    public RadioButton quizAnswer6a;
    public RadioButton quizAnswer6b;
    public TextView answer6;
    public TextView Total;
    public ImageView lightBulb;
    public Button reset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resetButton= findViewById(R.id.reset);
        quizAnswer1 = findViewById(R.id.quiz_answer_1);
        answer1 =  findViewById(R.id.answer1);
        quizAnswer2a= findViewById(R.id.quiz_answer_2a);
        quizAnswer2b= findViewById(R.id.quiz_answer_2b);
        quizAnswer2c= findViewById(R.id.quiz_answer_2c);
        quizAnswer2d= findViewById(R.id.quiz_answer_2d);
        quizAnswer2e= findViewById(R.id.quiz_answer_2e);
        answer2 = findViewById(R.id.answer2);
        quizAnswer3= findViewById(R.id.quiz_answer_3);
        answer3= findViewById(R.id.answer3);
        answer4group = findViewById(R.id.question_4_group);
        answer6group = findViewById(R.id.question_6_group);
        quizAnswer4a= findViewById(R.id.quiz_answer_4a);
        quizAnswer4b= findViewById(R.id.quiz_answer_4b);
        quizAnswer4c= findViewById(R.id.quiz_answer_4c);
        quizAnswer4d= findViewById(R.id.quiz_answer_4d);
        answer4= findViewById(R.id.answer4);
        quizAnswer5= findViewById(R.id.quiz_answer_5);
        answer5= findViewById(R.id.answer5);
        quizAnswer6a= findViewById(R.id.quiz_answer_6a);
        quizAnswer6b= findViewById(R.id.quiz_answer_6b);
        answer6= findViewById(R.id.answer6);
        Total = findViewById(R.id.Total);
        lightBulb = findViewById(R.id.lightbulb);

        //create an onClickListener for the reset button, and runs function resetAnswers onCilck
        resetButton.setOnClickListener(//set all id question/answer id values to public variables, to speed process times
        new View.OnClickListener() {
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
        String userEntry = quizAnswer1.getText().toString();
        checkEditText(userEntry, getString(R.string.question1_answer), getString(R.string.question1_answer_2), answer1);

        // compare answer2 to correct answer
        int question2 = 0;  //there are 3 right answers in this question. This keeps total point value to ensure question is marked wrong if <3 points are received.
        int question2_wrong = 0;

        //a,b,c is true
        if (quizAnswer2a.isChecked()) {
            question2 = question2 + 1;
            setCorrect(quizAnswer2a, true);
        }

        if (quizAnswer2b.isChecked()) {
            question2 = question2 + 1;
            setCorrect(quizAnswer2b, true);
        }

        if (quizAnswer2c.isChecked()) {
            question2 = question2 + 1;
            setCorrect(quizAnswer2c, true);
        }

        if (quizAnswer2d.isChecked()) {
            setCorrect(quizAnswer2d, false);
            question2_wrong = question2_wrong+1;
        }

        if (quizAnswer2e.isChecked()) {
            setCorrect(quizAnswer2e, false);
            question2_wrong = question2_wrong+1;
        }

        if (question2 < 3) {
            answer = getString(R.string.question2_answer);
            updateAnswer(answer2, answer, false);
        } else if (question2 == 3) {
            //check if there are wrong answers included as well
            if (question2_wrong>0) {
                answer = getString(R.string.question2_answer_someWrong);
                answer2.setText(answer);
                setCorrect(answer2, true);
            } else {
                answer = getString(R.string.correct_answer);
                updateAnswer(answer2, answer, true);
            }
        } else {
            Toast toast = Toast.makeText(getApplicationContext(), R.string.question2_wrongPoints, Toast.LENGTH_SHORT);
            toast.show();
            return;
        }

        currentPoints = currentPoints + question2;

        // compare answer3 to correct answer,then log points

        userEntry = quizAnswer3.getText().toString();
        checkEditText(userEntry, getString(R.string.question3_answer), getString(R.string.question3_answer), answer3);

        //compare answer 4 to correct answer
        RadioButton selectedValueId = findViewById(answer4group.getCheckedRadioButtonId());
        checkRadio(selectedValueId,quizAnswer4d, answer4, getString(R.string.question4_answer));

        //compare answer 5 to correct answer, then compare answer1 to correct answer, log points

        userEntry = ((EditText) findViewById(R.id.quiz_answer_5)).getText().toString();
        checkEditText(userEntry, getString(R.string.question5_answer), getString(R.string.question5_answer_2), answer5);

        //compare answer 6 to correct answer
        selectedValueId = findViewById(answer6group.getCheckedRadioButtonId());
        checkRadio(selectedValueId, quizAnswer6a, answer6, getString(R.string.question6_answer));

        //Calculate score
        String message = getString(R.string.total, currentPoints);
        double scorePercent = (((double) currentPoints / (double) totalPoints) * 100);

        //if score > 60, light bulb lights!
        if (scorePercent >= 60) {
            lightBulb.setImageResource(R.drawable.lighton);
        }

        //make toast
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

        //create & display total message in Total TextView
        message = message + " This is a score of " + scorePercent + "%!";
        Total.setText(message);

        //scroll to top of quiz to see answers
        ScrollView sv = findViewById(R.id.myScroll);
        sv.scrollTo(0, 0);

        //reset points
        currentPoints = 0;
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
    private void checkEditText(String answer, String possibleAnswer1, String possibleAnswer2, TextView answerID) {

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
    private void checkRadio(RadioButton selectedValueId, RadioButton correctValue, TextView answerID, String answer) {

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
    public void updateAnswer(TextView viewID, String answer, boolean correct) {

        String finalAnswer = correct ? getString(R.string.correct_answer) : getString(R.string.incorrect_answer) + " " + answer;
        viewID.setText(finalAnswer);
        setCorrect(viewID, correct);
    }

    /**
     * The setCorrect function updates the textColor values of answers after submitting.
     * Correct is green; incorrect is red
     *
     * TextView viewId is the view id of the text object being changed
     * @param correct a boolean value corresponding with right/wrong answers
     */
    public void setCorrect(TextView viewID, boolean correct) {
        //the ? operator says that if the colorId is TRUE, that it uses the correct color (green),
        //otherwise it will use the incorrect color (red) - both of which are in colors.xml
        int colorId = correct ? R.color.correct : R.color.incorrect;
        int color = ContextCompat.getColor(getApplicationContext(), colorId);
        viewID.setTextColor(color);
    }

    /**
     * function resetAnswers resets all user entries to blank, resets the colors
     * and turns off the light so the quiz can be taken again
     */
    public void resetAnswers() {
        //get integer of color number for resetting RadioButtons & CheckBoxes
        int standardColor = getResources().getColor(R.color.standardText);
        // Reset EditText values & colors
        quizAnswer1.setText("");
        quizAnswer1.setTextColor(standardColor);
        quizAnswer3.setText("");
        quizAnswer3.setTextColor(standardColor);
        quizAnswer5.setText("");
        quizAnswer5.setTextColor(standardColor);
        // Reset RadioGroup selections and colors
        answer4group.clearCheck();
        //reset question 4 group text & color
        quizAnswer4a.setTextColor(standardColor);
        quizAnswer4b.setTextColor(standardColor);
        quizAnswer4c.setTextColor(standardColor);
        quizAnswer4d.setTextColor(standardColor);
        //reset question 6 group text & color
        answer6group.clearCheck();
        quizAnswer6a.setTextColor(standardColor);
        quizAnswer6b.setTextColor(standardColor);
        //reset value and color of all CheckBox views
        quizAnswer2a.setChecked(false);
        quizAnswer2a.setTextColor(standardColor);
        quizAnswer2b.setChecked(false);
        quizAnswer2b.setTextColor(standardColor);
        quizAnswer2c.setChecked(false);
        quizAnswer2c.setTextColor(standardColor);
        quizAnswer2d.setChecked(false);
        quizAnswer2d.setTextColor(standardColor);
        quizAnswer2e.setChecked(false);
        quizAnswer2e.setTextColor(standardColor);
         //set value of all TextViews to empty strings ("")
        answer1.setText("");
        answer2.setText("");
        answer3.setText("");
        answer4.setText("");
        answer5.setText("");
        answer6.setText("");
        Total.setText("");

        ((TextView) findViewById(R.id.answer1)).setText("");
        ((TextView) findViewById(R.id.answer2)).setText("");
        ((TextView) findViewById(R.id.answer3)).setText("");
        ((TextView) findViewById(R.id.answer4)).setText("");
        ((TextView) findViewById(R.id.answer5)).setText("");
        ((TextView) findViewById(R.id.answer6)).setText("");
        ((TextView) findViewById(R.id.Total)).setText("");
        //Turn off the light!
        lightBulb.setImageResource(R.drawable.lightoff);
        //scroll to top of quiz
        ScrollView sv = findViewById(R.id.myScroll);
        sv.scrollTo(0, 0);
        //reset points
        currentPoints = 0;
    }


}


