package hu.bme.museum.model.game;

import android.app.Activity;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import hu.bme.museum.fragments.map.MapFragment;
import hu.bme.museum.fragments.map.MapTabFragment;

public abstract class Challenge {
    public String key;

    public abstract void addQuestion(LinearLayout layout, LayoutInflater inflater, final Activity activity);

    public abstract void sendAnswer(boolean answerIsCorrect, Activity activity);

    public abstract void setMapTabFragment(MapTabFragment mapTabFragment);
}
