package hu.bme.museum.fragments;

import android.support.v4.app.Fragment;

public class TabChildFragment extends Fragment {
    private TabFragment tabFragment;

    public void setParentTabFragment(TabFragment tabFragment) {
        this.tabFragment = tabFragment;
    }

    public TabFragment getParentTabFragment() {
        return tabFragment;
    }
}
