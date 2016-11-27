package hu.bme.museum.fragments;

import android.support.v4.app.Fragment;

import java.util.Stack;

public abstract class TabFragment extends Fragment {
    private Stack<Integer> backStack;

    abstract public String getTabTitle();

    public void changeChildFragment(Fragment newFragment) {

    }

    public boolean isBackStackEmpty() {
        return true;
    }

    public void onBackPressed() {
        getFragmentManager().popBackStack();
    }
}
