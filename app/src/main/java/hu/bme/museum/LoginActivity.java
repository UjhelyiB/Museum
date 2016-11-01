package hu.bme.museum;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import hu.bme.museum.loginfragments.LoginFragment;
import hu.bme.museum.loginfragments.RegistrationFragment;

public class LoginActivity extends AppCompatActivity {
    Button btLoginPage;
    Button btRegistrationPage;
    Button btContinue;

    @Override
    protected void  onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initUI();

    }

    private void initUI() {
        btLoginPage = (Button) findViewById(R.id.btLoginPage);
        btRegistrationPage = (Button) findViewById(R.id.btRegistrationPage);

        btLoginPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragment(LoginFragment.TAG);
            }
        });

        btRegistrationPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragment(RegistrationFragment.TAG);
            }
        });

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
