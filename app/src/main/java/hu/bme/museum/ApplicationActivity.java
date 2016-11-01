package hu.bme.museum;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

public class ApplicationActivity extends FragmentActivity {

    ApplicationFragmentPagerAdapter pagerAdapter;
    ViewPager viewPager;

    public static final String LOGIN_SUCCESSFUL = "Login successful!";
    public static final String PLEASE_LOG_IN_TO_PLAY_THE_GAME = "Please log in to play the game!";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application);

        // Get the ViewPager and set it's PagerAdapter so that it can display items
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(new ApplicationFragmentPagerAdapter(getSupportFragmentManager(),
                ApplicationActivity.this));

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.slidingTabs);
        tabLayout.setupWithViewPager(viewPager);

    }

}