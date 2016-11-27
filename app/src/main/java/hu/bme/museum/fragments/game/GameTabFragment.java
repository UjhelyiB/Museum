package hu.bme.museum.fragments.game;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import hu.bme.museum.R;
import hu.bme.museum.fragments.TabFragment;
import hu.bme.museum.fragments.map.MapTabFragment;

public class GameTabFragment extends TabFragment {

    private MapTabFragment mapFragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_game, null, false);

        ChallengesFragment challengesFragment = new ChallengesFragment();
        challengesFragment.setMapFragment(mapFragment);

        getFragmentManager().beginTransaction().add(R.id.gameFragmentLinearLayout, challengesFragment).commit();

        return rootView;
    }



    @Override
    public String getTabTitle() {
        return "Game";
    }

    public void setMapFragment(MapTabFragment mapFragment) {
        this.mapFragment = mapFragment;
    }
}


