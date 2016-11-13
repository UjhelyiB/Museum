package hu.bme.museum.activities;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import hu.bme.museum.fragments.ApplicationFragmentPagerAdapter;
import hu.bme.museum.R;
import hu.bme.museum.model.User;

public class ApplicationActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "ApplicationActivity";
    public static final String USERS = "users";
    ApplicationFragmentPagerAdapter pagerAdapter;
    ViewPager viewPager;

    public static final String LOGIN_SUCCESSFUL = "Login successful!";

    private FirebaseDatabase firebase;
    private DatabaseReference dbReference;
    GoogleApiClient googleApiClient;
    ApplicationFragmentPagerAdapter fragmentPagerAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application);

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        // Get the ViewPager and set it's PagerAdapter so that it can display items
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        fragmentPagerAdapter = new ApplicationFragmentPagerAdapter(getSupportFragmentManager(),
                ApplicationActivity.this);
        viewPager.setAdapter(fragmentPagerAdapter);

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.slidingTabs);
        tabLayout.setupWithViewPager(viewPager);

        //Set topmost toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.topmost_toolbar);
        setSupportActionBar(toolbar);

        addUserToDBIfItIsANewUser();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API)
                .build();
    }

    private void addUserToDBIfItIsANewUser(){
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        user.getToken(true)
                .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                    public void onComplete(@NonNull Task<GetTokenResult> task) {
                        if (task.isSuccessful()) {
                            firebase = FirebaseDatabase.getInstance();
                            dbReference = firebase.getReference(USERS);

                            dbReference.addListenerForSingleValueEvent(new ValueEventListener() {

                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    User currentUser = new User();
                                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
//                                        //for some reason, at some time, Firebase could not convert properly its data into the class
//                                        //the error was: com.google.firebase.database.DatabaseException: Failed to convert a value of type java.lang.String to int
//                                        //so I've put in this code to fix it but somehow it works now, so I just commented it out
//
//                                        currentUser.email = (String) snapshot.child("email").getValue();
//                                        currentUser.name = (String) snapshot.child("name").getValue();
//                                        currentUser.imageLink = (String) snapshot.child("imageLink").getValue();
//                                        currentUser.lastActive = (long) snapshot.child("lastActive").getValue();
//                                        currentUser.score = Integer.parseInt(String.valueOf(snapshot.child("score").getValue()));

                                        currentUser = snapshot.getValue(User.class);
                                        if(currentUser.email.equals(user.getEmail())){
                                            return;
                                        }
                                    }
                                    currentUser.email = user.getEmail();
                                    currentUser.name = user.getDisplayName();
                                    currentUser.score = 0;
                                    currentUser.lastActive = System.currentTimeMillis()/1000;
                                    dbReference.child(user.getUid()).setValue(currentUser);
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {}
                            });
                        } else {
                            Log.d("Error "," in LoginCompleteListener");
                            Toast.makeText(getBaseContext(), "Error in LoginCompleteListener", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case R.id.logout_option:
                FirebaseAuth.getInstance().signOut();

                Auth.GoogleSignInApi.signOut(googleApiClient);

                startActivity(new Intent(this, LoginActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }
}