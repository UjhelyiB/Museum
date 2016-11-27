package hu.bme.museum.fragments.game;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import hu.bme.museum.R;
import hu.bme.museum.db.FirebaseAdapter;
import hu.bme.museum.fragments.map.MapTabFragment;
import hu.bme.museum.model.game.Challenge;

public class ChallengesFragment extends Fragment {
    private static final String QUIZ = "quiz";
    private static final String CHALLENGES = "challenges";

    private boolean challengeAlreadyGenerated = false;

    private LayoutInflater inflater;
    private LinearLayout gameLayout;
    private Button highScoreButton;
    private Button newGameButton;

    private List<Challenge> challengeList = new ArrayList<>();
    private List<String> alreadyAnsweredQuizKeysList = new ArrayList<>();

    private MapTabFragment mapFragment;
    private Challenge challenge;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        container.removeAllViews();

        this.inflater = inflater;
        View rootView = inflater.inflate(R.layout.fragment_challenges, null, false);
        gameLayout = (LinearLayout) rootView.findViewById(R.id.gameLinearLayout);
        highScoreButton = (Button) rootView.findViewById(R.id.gameHighScoreButton);
        newGameButton = (Button) rootView.findViewById(R.id.gameNewChallengeButton);

        setListeners();

        alreadyAnsweredQuizKeysList = FirebaseAdapter.getInstance().getAlreadyAnsweredQuizKeysList(this);

        if(!challengeAlreadyGenerated){
            challengeList = FirebaseAdapter.getInstance().getChallenge(this);
            challengeAlreadyGenerated = true;
        }

        return rootView;
    }

    public List<String> getAlreadyAnsweredQuizKeysList() {
        return alreadyAnsweredQuizKeysList;
    }

    public void setListeners(){
        highScoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment highScoreFragment = new HighScoreFragment();
                getFragmentManager().beginTransaction()
                        .replace(R.id.gameFragmentLinearLayout, highScoreFragment).addToBackStack(null).commit();

            }
        });
        newGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChallengesFragment challengesFragment = new ChallengesFragment();
                challengesFragment.setMapFragment(mapFragment);
                getFragmentManager().beginTransaction()
                        .replace(R.id.gameFragmentLinearLayout, challengesFragment).addToBackStack(null).commit();
            }
        });
    }

    public void populateQuizes(){
        if(challenge != null){
            challenge= null;
        }

        Random random = new Random();
        challenge = challengeList.get(random.nextInt(challengeList.size()));
        challenge.addQuestion(gameLayout, inflater, getActivity());
        challenge.setMapFragment(mapFragment);
    }

    public void setMapFragment(MapTabFragment mapFragment) {
        this.mapFragment = mapFragment;
    }
}
