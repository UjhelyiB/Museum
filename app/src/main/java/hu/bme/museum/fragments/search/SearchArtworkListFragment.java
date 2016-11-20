package hu.bme.museum.fragments.search;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import hu.bme.museum.R;
import hu.bme.museum.db.FirebaseAdapter;
import hu.bme.museum.fragments.artwork.ArtworkListFragment;

public class SearchArtworkListFragment extends ArtworkListFragment {

    private String searchQuery = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        setArtworks(FirebaseAdapter.getInstance().getArtworksForSearchQuery(searchQuery, this));

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public void setSearchQuery(String query) {
        this.searchQuery = query;

        setArtworkListTitle("Search Results: " + "\"" + query + "\"");
    }
}
