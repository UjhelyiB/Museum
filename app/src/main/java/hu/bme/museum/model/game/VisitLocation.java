package hu.bme.museum.model.game;

import android.app.Activity;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import hu.bme.museum.R;
import hu.bme.museum.db.FirebaseAdapter;
import hu.bme.museum.fragments.map.MapTabFragment;

public class VisitLocation extends Challenge {
    public String question;
    public double lat;
    public double lng;
    public Location goalLocation;

    private MapTabFragment mapFragment;
    private View visitLocationView;

    @Override
    public void addQuestion(LinearLayout layout, LayoutInflater inflater, Activity activity) {
        layout.removeAllViews();

        visitLocationView = inflater.inflate(R.layout.challenge_visit_location, null);

        TextView question = (TextView) visitLocationView.findViewById(R.id.visitLocationQuestion);

        question.setText(this.question);

        layout.addView(visitLocationView);
    }

    @Override
    public void sendAnswer(boolean answerIsCorrect, Activity activity) {
        FirebaseAdapter.getInstance().givePointToCurrentUser(key);

        TextView locationVisited = (TextView) visitLocationView.findViewById(R.id.locationVisited);
        locationVisited.setText(R.string.good_job_location_visited);
        locationVisited.setVisibility(View.VISIBLE);

        FirebaseAdapter.getInstance().addQuizToUserCompletedQuizes(key);
    }

    @Override
    public void setMapFragment(MapTabFragment mapFragment) {
        this.mapFragment = mapFragment;
        mapFragment.setvisitLocationChallenge(this);
    }

    public Location getGoalLocation() {
        return goalLocation;
    }
}
