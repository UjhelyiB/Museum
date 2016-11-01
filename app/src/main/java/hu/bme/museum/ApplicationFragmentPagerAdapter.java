package hu.bme.museum;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ApplicationFragmentPagerAdapter extends FragmentPagerAdapter {

    private final Context context;

    private List<TabFragment> fragments = new ArrayList<>();

    public ApplicationFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;

        fragments.add(new ExhibitionsFragment());
        fragments.add(new GameFragment());
        fragments.add(new MapFragment());
        fragments.add(new SearchFragment());
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return fragments.get(position).getTabTitle();
    }
}
