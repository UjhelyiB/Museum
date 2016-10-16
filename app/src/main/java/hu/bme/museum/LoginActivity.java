package hu.bme.museum;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import hu.bme.museum.hu.bme.museum.loginfragments.LoginFragment;
import hu.bme.museum.hu.bme.museum.loginfragments.RegistrationFragment;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void  onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);



    }

    private void showFragment(String tag){
        Fragment fragment = getFragmentManager().findFragmentByTag(tag);

        if(tag.equals(LoginFragment.TAG)){
            fragment = new LoginFragment();
        }else if(tag.equals(RegistrationFragment.TAG)){
            fragment = new RegistrationFragment();
        }

        if(fragment != null){
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.loginFragmentContainer, fragment, tag);
            ft.addToBackStack(null);
            ft.commit();
        }
    }
}
