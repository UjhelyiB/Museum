package hu.bme.museum.fragments.browse;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import hu.bme.museum.R;
import hu.bme.museum.fragments.artwork.ArtworkListFragment;
import hu.bme.museum.fragments.TabFragment;

public class BrowseFragment extends TabFragment {

    LayoutInflater inflater;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        this.inflater = inflater;
        View rootView = inflater.inflate(R.layout.fragment_browse, null, false);

        ArtworkListFragment artworkListFragment = new ArtworkListFragment();
        getFragmentManager().beginTransaction().add(R.id.browseLinearLayout, artworkListFragment).commit();

        return rootView;
    }

    @Override
    public String getTabTitle() {
        return "Browse";
    }
}