package hu.bme.museum.fragments.artwork;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import hu.bme.museum.R;
import hu.bme.museum.db.FirebaseAdapter;

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
