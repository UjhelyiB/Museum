package hu.bme.museum.fragments;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import hu.bme.museum.fragments.browse.BrowseTabFragment;
import hu.bme.museum.fragments.game.GameTabFragment;
import hu.bme.museum.fragments.map.MapTabFragment;
import hu.bme.museum.fragments.search.SearchTabFragment;

public class ApplicationFragmentPagerAdapter extends FragmentPagerAdapter {

    private final Context context;

    private List<TabFragment> fragments = new ArrayList<>();

    public ApplicationFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;

        BrowseTabFragment browseTabFragment = new BrowseTabFragment();
        GameTabFragment gameTabFragment = new GameTabFragment();
        MapTabFragment mapFragment = new MapTabFragment();
        SearchTabFragment searchTabFragment = new SearchTabFragment();

        gameTabFragment.setMapFragment(mapFragment);

        fragments.add(browseTabFragment);
        fragments.add(gameTabFragment);
        fragments.add(mapFragment);
        fragments.add(searchTabFragment);
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
