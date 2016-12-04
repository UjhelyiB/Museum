package hu.bme.museum.fragments;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import java.util.Stack;

import hu.bme.museum.R;

public abstract class TabFragment extends Fragment {

    private Stack<TabChildFragment> backStack = new Stack<>();
    protected TabChildFragment currentTabChildFragment = null;

    private int tabChildFragmentContainerId;


    abstract public String getTabTitle();

    protected void setTabChildFragmentContainerId(int tabChildFragmentContainerId) {
        this.tabChildFragmentContainerId = tabChildFragmentContainerId;
    }

    protected void initializeTabChildFragment(TabChildFragment tabChildFragment) {
        backStack.clear();

        currentTabChildFragment = tabChildFragment;

        getFragmentManager().beginTransaction()
                .add(tabChildFragmentContainerId, tabChildFragment)
                .commit();
    }

    public void changeTabChildFragment(TabChildFragment newTabChildFragment) {
        backStack.push(currentTabChildFragment);
        currentTabChildFragment = newTabChildFragment;

        setTabChildFragment(currentTabChildFragment);
    }

    // Does not modify the backStack. Used internally to avoid a loop when calling
    // changeTabChildFragment while popping the backStack
    private void setTabChildFragment(TabChildFragment tabChildFragment) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();

        ft.setCustomAnimations(R.anim.slide_in_upwards, R.anim.slide_out_downwards);

        ft.replace(tabChildFragmentContainerId, tabChildFragment);

        ft.commit();
    }

    public void changeTabChildFragmentNoBackStack(TabChildFragment tabChildFragment) {
        currentTabChildFragment = tabChildFragment;
        setTabChildFragment(tabChildFragment);
    }

    public boolean isBackStackEmpty() {
        if (backStack.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    public void onBackPressed() {
        if (!backStack.isEmpty()) {
            changeTabChildFragmentNoBackStack(backStack.pop());
        }
    }
}
