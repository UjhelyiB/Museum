package hu.bme.museum.fragments.browse;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import hu.bme.museum.R;
import hu.bme.museum.db.FirebaseAdapter;
import hu.bme.museum.fragments.artwork.ArtworkListFragment;

import hu.bme.museum.fragments.artwork.ExhibitionArtworkListFragment;
import hu.bme.museum.model.Exhibition;

public class ExhibitionsFragment extends Fragment {

    private LayoutInflater inflater;
    private LinearLayout exhibitionsLinearLayout;
//    private TextView exhibitionsTitleTextView;

    private List<Exhibition> exhibitions;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        this.inflater = inflater;
        container.removeAllViews();
        View rootView = inflater.inflate(R.layout.fragment_exhibitions, null, false);

        exhibitionsLinearLayout = (LinearLayout) rootView.findViewById(R.id.exhibitionsLinearLayout);
        exhibitionsLinearLayout.removeAllViews();
//        exhibitionsTitleTextView = (TextView) rootView.findViewById(R.id.exhibitionsTitleTextView);

        exhibitions = FirebaseAdapter.getInstance().getExhibitions(this);

        return rootView;
    }

    public void populateExhibitions() {
        exhibitionsLinearLayout.removeAllViews();

        for (final Exhibition exhibition : exhibitions) {

            View row = inflater.inflate(R.layout.exhibition_row, null);

            TextView exhibitionNameTextView
                    = (TextView) row.findViewById(R.id.exhibitionNameTextView);
            exhibitionNameTextView.setText(exhibition.name);

            row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ExhibitionArtworkListFragment artworkListFragment = new ExhibitionArtworkListFragment();
                    artworkListFragment.setLinearLayoutContainerId(R.id.browseLinearLayout);
                    artworkListFragment.setExhibition(exhibition);

                    getFragmentManager().beginTransaction()
                            .replace(R.id.browseLinearLayout, artworkListFragment)
                            .addToBackStack(null).commit();
                }
            });

            exhibitionsLinearLayout.addView(row);
        }
    }
}