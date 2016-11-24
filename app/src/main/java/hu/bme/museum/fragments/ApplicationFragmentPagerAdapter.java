package hu.bme.museum.fragments;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import hu.bme.museum.fragments.browse.BrowseFragment;
import hu.bme.museum.fragments.game.GameFragment;
import hu.bme.museum.fragments.map.MapFragment;
import hu.bme.museum.fragments.search.SearchContainerFragment;
import hu.bme.museum.fragments.search.SearchFragment;

public class ApplicationFragmentPagerAdapter extends FragmentPagerAdapter {

    private final Context context;

    private List<TabFragment> fragments = new ArrayList<>();

    public ApplicationFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;

        BrowseFragment browseFragment = new BrowseFragment();
        GameFragment gameFragment = new GameFragment();
        MapFragment mapFragment = new MapFragment();
        SearchContainerFragment searchContainerFragment = new SearchContainerFragment();

        gameFragment.setMapFragment(mapFragment);

        fragments.add(browseFragment);
        fragments.add(gameFragment);
        fragments.add(mapFragment);
        fragments.add(searchContainerFragment);
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
