package hu.bme.museum.model.game;

import android.app.Activity;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

public abstract class Challenge {
    public abstract void addQuestion(LinearLayout layout, LayoutInflater inflater, final Activity activity);
}
