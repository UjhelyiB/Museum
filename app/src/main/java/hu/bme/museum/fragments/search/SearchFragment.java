package hu.bme.museum.fragments.search;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import hu.bme.museum.R;
import hu.bme.museum.fragments.artwork.ArtworkListFragment;
import hu.bme.museum.fragments.TabFragment;

public class SearchFragment extends TabFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_browse, null, false);

        return rootView;
    }

    @Override
    public String getTabTitle() {
        return "Search";
    }
}
