package hu.bme.museum;


import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

public class ApplicationActivity extends FragmentActivity {

    ApplicationFragmentPagerAdapter pagerAdapter;
    ViewPager viewPager;

    public static final String LOGIN_SUCCESSFUL = "Login successful!";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application);

        // Get the ViewPager and set it's PagerAdapter so that it can display items
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(new ApplicationFragmentPagerAdapter(getSupportFragmentManager(),
                ApplicationActivity.this));
    }
}