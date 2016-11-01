package hu.bme.museum;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by dominic on 2016.11.01.
 */
public class SearchFragment extends TabFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search, null, false);
        return rootView;
    }

    @Override
    public String getTabTitle() {
        return "Search";
    }
}
