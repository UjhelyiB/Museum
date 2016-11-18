package hu.bme.museum.fragments.browse;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import hu.bme.museum.R;
import hu.bme.museum.fragments.artwork.ArtworkDetailsFragment;
import hu.bme.museum.fragments.artwork.ArtworkListFragment;
import hu.bme.museum.model.Artwork;
import hu.bme.museum.model.Exhibition;
import hu.bme.museum.model.User;

public class ExhibitionsFragment extends Fragment {
    private static final String EXHIBTION_CHILD = "exhibitions";

    //Firebase
    private DatabaseReference databaseReference;

    private LayoutInflater inflater;
    private LinearLayout exhibitionsLinearLayout;
//    private TextView exhibitionsTitleTextView;

    private List<Exhibition> exhibitionsList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        this.inflater = inflater;
        container.removeAllViews();
        View rootView = inflater.inflate(R.layout.fragment_exhibitions, null, false);

        exhibitionsLinearLayout = (LinearLayout) rootView.findViewById(R.id.exhibitionsLinearLayout);
//        exhibitionsTitleTextView = (TextView) rootView.findViewById(R.id.exhibitionsTitleTextView);

        setUpFirebase();

        return rootView;
    }

    public void setUpFirebase(){
        // New child entries
        databaseReference = FirebaseDatabase.getInstance().getReference(EXHIBTION_CHILD);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                exhibitionsLinearLayout.removeAllViews();
                exhibitionsList.clear();

                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Exhibition exhibition = snapshot.getValue(Exhibition.class);
                    exhibition.key = snapshot.getKey();
                    if(!exhibitionsList.contains(exhibition)){
                        exhibitionsList.add(exhibition);
                    }
                }

                for (final Exhibition exhibition : exhibitionsList) {
                    View row = inflater.inflate(R.layout.exhibition_row, null);

                    TextView exhibitionNameTextView
                            = (TextView) row.findViewById(R.id.exhibitionNameTextView);

                    row.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ArtworkListFragment artworkListFragment = new ArtworkListFragment();
                            artworkListFragment.setLinearLayoutContainerId(R.id.browseLinearLayout);
                            artworkListFragment.setExhibition(exhibition);

                            getFragmentManager().beginTransaction()
                                    .replace(R.id.browseLinearLayout, artworkListFragment)
                                    .addToBackStack(null).commit();
                        }
                    });

                    exhibitionNameTextView.setText(exhibition.name);

                    exhibitionsLinearLayout.addView(row);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}

        });
    }
}