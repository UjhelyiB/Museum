package hu.bme.museum.fragments.search;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import hu.bme.museum.R;
import hu.bme.museum.fragments.artwork.ArtworkListFragment;
import hu.bme.museum.fragments.TabFragment;

public class SearchContainerFragment extends TabFragment {

    private SearchFragment searchFragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_search_container, null, false);

        searchFragment = new SearchFragment();
        getFragmentManager().beginTransaction().add(R.id.searchContainerLinearLayout,
                searchFragment).commit();

        return rootView;
    }

    @Override
    public String getTabTitle() {
        return "Search";
    }
}
