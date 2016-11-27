package hu.bme.museum.fragments.game;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import hu.bme.museum.R;
import hu.bme.museum.fragments.TabFragment;
import hu.bme.museum.fragments.map.MapFragment;
import hu.bme.museum.fragments.map.MapTabFragment;

public class GameTabFragment extends TabFragment {

    private MapTabFragment mapTabFragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        this.setTabChildFragmentContainerId(R.id.gameFragmentLinearLayout);

        View rootView = inflater.inflate(R.layout.fragment_game, null, false);

        ChallengesFragment challengesFragment = new ChallengesFragment();
        challengesFragment.setParentTabFragment(this);
        challengesFragment.setMapFragment(mapTabFragment.getMapFragment());

        this.initializeTabChildFragment(challengesFragment);

        return rootView;
    }



    @Override
    public String getTabTitle() {
        return "Game";
    }

    public void setMapTabFragment(MapTabFragment mapTabFragment) {
        this.mapTabFragment = mapTabFragment;
    }
}


