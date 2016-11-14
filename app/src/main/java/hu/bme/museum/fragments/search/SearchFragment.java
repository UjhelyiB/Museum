package hu.bme.museum.fragments.search;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import hu.bme.museum.R;
import hu.bme.museum.fragments.artwork.ArtworkListFragment;

public class SearchFragment extends Fragment {

    private ArtworkListFragment searchResultsArtworkList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search, null, false);

        final EditText searchEditText = (EditText) rootView.findViewById(R.id.searchEditText);
        searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    // Hide keyboard
                    InputMethodManager imm = (InputMethodManager) getActivity()
                            .getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(
                            getActivity().getWindow().getDecorView().getWindowToken(), 0);

                    searchResultsArtworkList = new ArtworkListFragment();
                    searchResultsArtworkList.setLinearLayoutContainerId(
                            R.id.searchContainerLinearLayout);

                    searchResultsArtworkList.setArtworkListTitle(
                            "Search Results: " + "\"" + searchEditText.getText().toString() + "\"" );

                    getFragmentManager().beginTransaction()
                            .replace(R.id.searchContainerLinearLayout, searchResultsArtworkList)
                            .addToBackStack(null).commit();

                    handled = true;
                }
                return handled;
            }
        });

        return rootView;
    }
}
