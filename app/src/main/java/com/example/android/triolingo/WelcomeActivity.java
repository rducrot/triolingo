package com.example.android.triolingo;

import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

public class WelcomeActivity extends AppCompatActivity implements View.OnClickListener{

    //Graphic elements.
    private RelativeLayout mFrench, mSpanish, mGerman;

    private static String questions[][]= new String [5][5];
    private static int correctAnswers[]= new int [5];
    private static String category;
    private final int[] frenchCorrectAnswers = {
            R.id.optionB, R.id.optionB, R.id.optionA, R.id.optionC, R.id.optionD,
    };
    private final int[] spanishCorrectAnswers = {
            R.id.optionA, R.id.optionC, R.id.optionD, R.id.optionA, R.id.optionC,
    };
    private final int[] germanCorrectAnswers = {
            R.id.optionC, R.id.optionC, R.id.optionD, R.id.optionA, R.id.optionB,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        mFrench = findViewById(R.id.selectFrench);
        mSpanish = findViewById(R.id.selectSpanish);
        mGerman = findViewById(R.id.selectGerman);

        mFrench.setOnClickListener(this);
        mSpanish.setOnClickListener(this);
        mGerman.setOnClickListener(this);
    }

    /**
     * Assigns the methods to their corresponding buttons.
     * @param view
     */
    @Override
    public void onClick(View view){
        switch(view.getId()){
            case R.id.selectFrench: {
                startFrenchQuiz();
                break;
            }
            case R.id.selectSpanish: {
                startSpanishQuiz();
                break;
            }
            case R.id.selectGerman: {
                startGermanQuiz();
                break;
            }
        }
    }

    /**
     * Method called when the "French" button is clicked.
     */
    public void startFrenchQuiz(){
        extractTypedArray(R.array.french_questions);
        correctAnswers = frenchCorrectAnswers;
        category = getString(R.string.french);
        startActivity(new Intent(this, MainActivity.class));
    }

    /**
     * Method called when the "Spanish" button is clicked.
     */
    public void startSpanishQuiz(){
        extractTypedArray(R.array.spanish_questions);
        correctAnswers = spanishCorrectAnswers;
        category = getString(R.string.spanish);
        startActivity(new Intent(this, MainActivity.class));
    }

    /**
     * Method called when the "German" button is clicked.
     */
    public void startGermanQuiz(){
        extractTypedArray(R.array.german_questions);
        correctAnswers = germanCorrectAnswers;
        category = getString(R.string.german);
        startActivity(new Intent(this, MainActivity.class));
    }

    /**
     * Method used to extract the array corresponding to the quiz selected.
     * @param arrayId
     */
    private void extractTypedArray(int arrayId){
        Resources resources = getResources();
        TypedArray typedArray = resources.obtainTypedArray(arrayId);
        int length = typedArray.length();
        for (int i = 0; i < length; ++i) {
            int id = typedArray.getResourceId(i, 0);
            questions[i] = resources.getStringArray(id);
        }
        typedArray.recycle();
    }

    /**
     *Returns the questions of the quiz selected.
     * @return
     */
    public static String[][] getQuestions(){
        return questions;
    }

    /**
     * Returns the correct answers of the quiz selected.
     * @return
     */
    public static int[] getCorrectAnswers(){
        return correctAnswers;
    }

    /**
     * Returns the category of the quiz selected.
     * @return
     */
    public static String getCategory(){
        return category;
    }
}

