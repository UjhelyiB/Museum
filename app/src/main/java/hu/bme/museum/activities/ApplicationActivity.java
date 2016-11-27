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
import com.google.firebase.auth.FirebaseAuth;

import java.util.Stack;

import hu.bme.museum.db.FirebaseAdapter;
import hu.bme.museum.fragments.ApplicationFragmentPagerAdapter;
import hu.bme.museum.R;
import hu.bme.museum.fragments.TabFragment;

public class ApplicationActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "ApplicationActivity";

    private GoogleApiClient googleApiClient;

    private ViewPager viewPager;
    private ApplicationFragmentPagerAdapter fragmentPagerAdapter;
    private TabLayout tabLayout;

    private Stack<Integer> tabBackStack = new Stack<>();
    private int previousTab = 0;
    private boolean enableSaveToHistory = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application);

        // Get the ViewPager and set it's PagerAdapter so that it can display items
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        fragmentPagerAdapter = new ApplicationFragmentPagerAdapter(getSupportFragmentManager(),
                ApplicationActivity.this);
        viewPager.setAdapter(fragmentPagerAdapter);

        // Give the TabLayout the ViewPager
        tabLayout = (TabLayout) findViewById(R.id.slidingTabs);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (enableSaveToHistory) {
                    tabBackStack.push(previousTab);
                }
                previousTab = tab.getPosition();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) { }

            @Override
            public void onTabReselected(TabLayout.Tab tab) { }
        });

        //Set topmost toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.topmost_toolbar);
        setSupportActionBar(toolbar);

        FirebaseAdapter.getInstance().addUserToDBIfItIsANewUser(this);

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API)
                .build();
    }

    @Override
    public void onBackPressed() {
        TabFragment currentTabFragment =
                (TabFragment) fragmentPagerAdapter.getItem(tabLayout.getSelectedTabPosition());

        if (currentTabFragment.isBackStackEmpty()) {
            if (tabBackStack.isEmpty()) {
                super.onBackPressed();
            } else {
                enableSaveToHistory = false;
                viewPager.setCurrentItem(tabBackStack.pop());
                enableSaveToHistory = true;
            }
        } else {
            currentTabFragment.onBackPressed();
        }
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