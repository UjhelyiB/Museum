package hu.bme.museum;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ExhibitionsFragment extends TabFragment {

    LinearLayout artworksLinearLayout;
    LayoutInflater inflater;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        this.inflater = inflater;
        View rootView = inflater.inflate(R.layout.fragment_exhibitions, null, false);

        artworksLinearLayout = (LinearLayout) rootView.findViewById(R.id.artworksLinearLayout);

        addArtworks();

        return rootView;
    }

    public void addArtworks() {
        for (int i = 0; i < 3; ++i) {
            View newArtwork = inflater.inflate(R.layout.artwork, null);
            TextView title = (TextView) newArtwork.findViewById(R.id.artworkTitleTextView);
            title.setText(title.getText() + " " + (i+1));
            artworksLinearLayout.addView(newArtwork, 0);
        }
    }

    @Override
    public String getTabTitle() {
        return "Browse";
    }

}