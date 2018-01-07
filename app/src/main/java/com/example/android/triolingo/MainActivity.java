package com.example.android.triolingo;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    //Graphic elements.
    private TextView mQuestion;
    private Button mNext, mSubmit;
    private RadioGroup mOptions;
    private RadioButton mOptionA, mOptionB, mOptionC, mOptionD;

    private String[][] questions;
    private int[] correctAnswers;
    private int questionNumber;
    private RadioButton correctOption, wrongOption;
    private boolean isNextEnabled, isSubmitDisabled, correctOptionIsShown, wrongOptionIsShown;
    private int score;

    static final String KEY_QUESTIONNUMBER = "SavedStateOfQuestionNumber";
    static final String KEY_CORRECT_OPTION_IS_SHOWN = "SavedStateOfCorrectOptionIsShown";
    static final String KEY_WRONG_OPTION_IS_SHOWN = "SavedStateOfWrongOptionIsShown";
    static final String KEY_IS_NEXT_ENABLED = "SavedStateOfIsNextEnabled";
    static final String KEY_IS_SUBMIT_DISABLED = "SavedStateOfIsSubmitDisabled";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Gets the quiz selected on the Welcome Activity.
        questions = WelcomeActivity.getQuestions();
        correctAnswers = WelcomeActivity.getCorrectAnswers();
        String category = WelcomeActivity.getCategory();

        //Gets the theme colors corresponding to the selected quiz before the ContentView is created.
        if (category.equals(getString(R.string.french))) {
            setTheme(R.style.FrenchTheme);
        } else if (category.equals(getString(R.string.spanish))) {
            setTheme(R.style.SpanishTheme);
        } else if (category.equals(getString(R.string.german))) {
            setTheme(R.style.GermanTheme);
        }
        setContentView(R.layout.activity_main);

        mQuestion = findViewById(R.id.question);
        mOptions = findViewById(R.id.options);
        mOptionA = findViewById(R.id.optionA);
        mOptionB =  findViewById(R.id.optionB);
        mOptionC =  findViewById(R.id.optionC);
        mOptionD = findViewById(R.id.optionD);
        mNext = findViewById(R.id.next);
        mSubmit = findViewById(R.id.submit);
        score = 0;

        mSubmit.setOnClickListener(this);
        mNext.setOnClickListener(this);
        mOptionA.setOnClickListener(this);
        mOptionB.setOnClickListener(this);
        mOptionC.setOnClickListener(this);
        mOptionD.setOnClickListener(this);

        //Sets the first question.
        mQuestion.setText(questions[questionNumber][0]);
        mOptionA.setText(questions[questionNumber][1]);
        mOptionB.setText(questions[questionNumber][2]);
        mOptionC.setText(questions[questionNumber][3]);
        mOptionD.setText(questions[questionNumber][4]);

        //Sets the background color.
        LinearLayout root = findViewById(R.id.root);
        if (category.equals(getString(R.string.french))) {
            root.setBackgroundColor(getResources().getColor(R.color.frenchLight));
        } else if (category.equals(getString(R.string.spanish))) {
            root.setBackgroundColor(getResources().getColor(R.color.spanishLight));
        } else if (category.equals(getString(R.string.german))) {
            root.setBackgroundColor(getResources().getColor(R.color.germanLight));
        }
    }

    /**
     * Assigns the methods to their corresponding buttons.
     * @param view
     */
    @Override
    public void onClick(View view){
        switch(view.getId()){
            case R.id.submit: {
                checkTheAnswer();
                break;
            }
            case R.id.next: {
                setNextQuestion();
                break;
            }
        }
    }

    /**
     * Saves the state of the layout when the orientation has changed.
     * @param outState
     */
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_QUESTIONNUMBER, questionNumber);
        outState.putBoolean(KEY_CORRECT_OPTION_IS_SHOWN, correctOptionIsShown);
        outState.putBoolean(KEY_WRONG_OPTION_IS_SHOWN, wrongOptionIsShown);
        outState.putBoolean(KEY_IS_NEXT_ENABLED, isNextEnabled);
        outState.putBoolean(KEY_IS_SUBMIT_DISABLED, isSubmitDisabled);
    }

    /**
     * Restores the state of the layout when the orientation has changed.
     * @param savedInstanceState
     */
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        questionNumber = savedInstanceState.getInt(KEY_QUESTIONNUMBER);
        mQuestion.setText(questions[questionNumber][0]);
        mOptionA.setText(questions[questionNumber][1]);
        mOptionB.setText(questions[questionNumber][2]);
        mOptionC.setText(questions[questionNumber][3]);
        mOptionD.setText(questions[questionNumber][4]);
        isNextEnabled = savedInstanceState.getBoolean(KEY_IS_NEXT_ENABLED);
        if (isNextEnabled) mNext.setEnabled(true);
        isSubmitDisabled = savedInstanceState.getBoolean(KEY_IS_SUBMIT_DISABLED);
        if (isSubmitDisabled) mSubmit.setEnabled(false);
        correctOptionIsShown = savedInstanceState.getBoolean(KEY_CORRECT_OPTION_IS_SHOWN);
        if (correctOptionIsShown) {
            correctOption = findViewById(correctAnswers[questionNumber]);
            correctOption.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.correct_highlighted));
        }
        wrongOptionIsShown = savedInstanceState.getBoolean(KEY_WRONG_OPTION_IS_SHOWN);
        if (wrongOptionIsShown) {
            wrongOption = findViewById(mOptions.getCheckedRadioButtonId());
            wrongOption.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.wrong_highlighted));
        }
    }

    /**
     * Method called when the "Submit" button is clicked.
     */
    public void checkTheAnswer() {
        //Warns if no answer has been selected.
        if (!(mOptionA.isChecked()) && !(mOptionB.isChecked()) && !(mOptionC.isChecked()) && !(mOptionD.isChecked())) {
            Toast.makeText(this, R.string.noOptionSelected, Toast.LENGTH_SHORT).show();
            return;
        }
        //Shows the correct option in green.
        correctOption = findViewById(correctAnswers[questionNumber]);
        correctOption.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.correct_highlighted));
        correctOptionIsShown = true;

        //Actions to do if the answer is correct.
        if (mOptions.getCheckedRadioButtonId() == correctAnswers[questionNumber]) {
            score += 1;
            Toast.makeText(this, R.string.correctToastMessage, Toast.LENGTH_SHORT).show();
            //Actions to do if the answer is wrong.
        } else {
            //Shows the wrong option in red.
            wrongOption = findViewById(mOptions.getCheckedRadioButtonId());
            wrongOption.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.wrong_highlighted));
            wrongOptionIsShown = true;
            Toast.makeText(this, R.string.wrongToastMessage, Toast.LENGTH_SHORT).show();
        }
        //"Next" button is enabled and "Submit" button is disabled to avoid to answer multiple times.
        mNext.setEnabled(true);
        isNextEnabled = true;
        mSubmit.setEnabled(false);
        isSubmitDisabled = true;
        //An alert dialog pops up after the last question.
        if (questionNumber == 4) {
            if (score == 5) {
                String message = getString(R.string.congratulations) + score + " " + getString(R.string.points);
                createAlertDialog(message);
            } else if (score > 0 && score < 5) {
                String message = getString(R.string.goodJob) + score + " " + getString(R.string.points);
                createAlertDialog(message);
            } else {
                String message = getString(R.string.tryAgain) + score +  " " + getString(R.string.points);
                createAlertDialog(message);
            }
        }
    }

    /**
     * Method called to show an alert dialog.
     * @param message
     */
    public void createAlertDialog(String message) {
        //setCancelable(false) prevents from closing the alert dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.Theme_AppCompat_DayNight_Dialog).setCancelable(false);
        builder.setMessage(message);
        //Button "New Game".
        builder.setPositiveButton(R.string.newGame, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
                Intent categoriesIntent = new Intent(MainActivity.this, WelcomeActivity.class);
                // Goes back to the Welcome Activity.
                startActivity(categoriesIntent);
            }
        });
        //Button "Quit".
        builder.setNegativeButton(R.string.quit, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent goToHome = new Intent(Intent.ACTION_MAIN);
                goToHome.addCategory(Intent.CATEGORY_HOME);
                //Leaves the application.
                startActivity(goToHome);
            }
        });
        builder.create();
        builder.show();
    }

    /**
     * Method called when the "Next" button is clicked.
     */
    public void setNextQuestion() {
        //Question number is increased.
        questionNumber++;
        //Next question is set and the color of the options go back to transparent.
        mQuestion.setText(questions[questionNumber][0]);
        mOptionA.setText(questions[questionNumber][1]);
        mOptionA.setBackgroundColor(this.getResources().getColor(R.color.transparent));
        mOptionA.setVisibility(View.VISIBLE);
        mOptionB.setText(questions[questionNumber][2]);
        mOptionB.setBackgroundColor(this.getResources().getColor(R.color.transparent));
        mOptionB.setVisibility(View.VISIBLE);
        mOptionC.setText(questions[questionNumber][3]);
        mOptionC.setBackgroundColor(this.getResources().getColor(R.color.transparent));
        mOptionC.setVisibility(View.VISIBLE);
        mOptionD.setText(questions[questionNumber][4]);
        mOptionD.setBackgroundColor(this.getResources().getColor(R.color.transparent));
        mOptionD.setVisibility(View.VISIBLE);
        mOptions.clearCheck();
        //Next button is disabled and Submit button is enabled.
        mNext.setEnabled(false);
        isNextEnabled = false;
        mSubmit.setEnabled(true);
        isSubmitDisabled = false;
        correctOptionIsShown = false;
        wrongOptionIsShown = false;
    }
}
