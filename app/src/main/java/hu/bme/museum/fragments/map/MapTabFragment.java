package hu.bme.museum.fragments.map;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import hu.bme.museum.R;
import hu.bme.museum.fragments.TabFragment;

public class MapTabFragment extends TabFragment {

    private MapFragment mapFragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        this.setTabChildFragmentContainerId(R.id.mapTabLinearLayout);

        View rootView = inflater.inflate(R.layout.fragment_map_tab, null, false);


            mapFragment = new MapFragment();

        mapFragment.setParentTabFragment(this);

        this.initializeTabChildFragment(mapFragment);

        return rootView;
    }

    @Override
    public String getTabTitle() {
        return "Map";
    }

    public MapFragment getMapFragment() {

        return mapFragment;
    }
}
