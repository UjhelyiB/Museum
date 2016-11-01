package hu.bme.museum;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
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

<<<<<<< HEAD
        // Get the ViewPager and set it's PagerAdapter so that it can display items
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(new ApplicationFragmentPagerAdapter(getSupportFragmentManager(),
                ApplicationActivity.this));
=======
        Toast.makeText(ApplicationActivity.this, LOGIN_SUCCESSFUL, Toast.LENGTH_SHORT).show();

        initUI();
    }

    private void initUI() {
        btGame = (Button) findViewById(R.id.btGame);
        btExhibitions = (Button) findViewById(R.id.btExhibitions);
        btMap = (Button) findViewById(R.id.btMap);

        btGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    startGame();
            }
        });
>>>>>>> 0e75d6e010db81c8fc3b0a832e12b781c17e6c5b

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.slidingTabs);
        tabLayout.setupWithViewPager(viewPager);

    }

}