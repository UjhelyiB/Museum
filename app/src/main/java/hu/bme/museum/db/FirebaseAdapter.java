package hu.bme.museum.db;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import hu.bme.museum.fragments.artwork.ArtworkListFragment;
import hu.bme.museum.fragments.browse.ExhibitionsFragment;
import hu.bme.museum.model.Artwork;
import hu.bme.museum.model.Exhibition;

public class FirebaseAdapter {

    private final String LOG_TAG = "FirebaseAdapter";

    private static FirebaseAdapter instance = null;

    private DatabaseReference databaseReference;

    private static final String EXHIBTION_CHILD = "exhibitions";
    private static final String ARTWORK_CHILD = "artworks";

    private List<Artwork> artworks = new ArrayList<>();

    private FirebaseAdapter() {
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    public static FirebaseAdapter getFirebaseAdapter() {
        if (instance == null) {
            instance = new FirebaseAdapter();
        }
        return instance;
    }

    public List<Exhibition> getExhibitions(final ExhibitionsFragment exhibitionsFragment) {

        final List<Exhibition> exhibitions = new ArrayList<>();

        databaseReference.child(EXHIBTION_CHILD)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        exhibitions.clear();

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Exhibition exhibition = snapshot.getValue(Exhibition.class);

                            Log.w(LOG_TAG, "Found exhibition " + exhibition.name);

                            exhibition.key = snapshot.getKey();
                            if (!exhibitions.contains(exhibition)){
                                exhibitions.add(exhibition);
                            }
                        }

                        exhibitionsFragment.populateExhibitions();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        return exhibitions;
    }

    public List<Artwork> getArtworksForExhibtition(String exhibitionKey,
                                                   final ArtworkListFragment artworkListFragment) {

        databaseReference.child(EXHIBTION_CHILD).child(exhibitionKey).child(ARTWORK_CHILD)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        artworks.clear();

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Artwork artwork = snapshot.getValue(Artwork.class);

                            Log.w(LOG_TAG, "Found artwork " + artwork.name);

                            if (!artworks.contains(artwork)){
                                artworks.add(artwork);
                            }
                        }

                        artworkListFragment.populateArtworks();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        return artworks;
    }

    public List<Artwork> getArtworksForSearchQuery(String query) {
        List<Artwork> artworks = new ArrayList<>();
        return artworks;
    }
}
