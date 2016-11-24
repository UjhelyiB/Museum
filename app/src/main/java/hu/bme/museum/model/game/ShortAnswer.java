package hu.bme.museum.model.game;

import android.app.Activity;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import hu.bme.museum.model.game.Challenge;

public class ShortAnswer extends Challenge {
    public String correctAnswer;
    public String question;

    @Override
    public void addQuestion(LinearLayout layout, LayoutInflater inflater, Activity activity) {

    }
}
