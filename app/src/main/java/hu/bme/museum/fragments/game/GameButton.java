package hu.bme.museum.fragments.game;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ToggleButton;

import hu.bme.museum.R;

public class GameButton extends ToggleButton{
    boolean isCorrect = false;
    String text;

    public GameButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public GameButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GameButton(Context context) {
        super(context);
    }

    public void setCorrect(boolean isCorrect){
        this.isCorrect = isCorrect;
    }

    public void answerSent(){
        if (isCorrect) {
            this.setBackgroundResource(R.drawable.button_correct_answer);
        } else {
            this.setBackgroundResource(R.drawable.button_wrong_answer);
        }
    }
}
