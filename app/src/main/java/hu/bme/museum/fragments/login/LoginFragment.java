package hu.bme.museum.fragments.login;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import hu.bme.museum.activities.ApplicationActivity;
import hu.bme.museum.R;


//Not used right now
public class LoginFragment extends Fragment {
    public static final String TAG = "LoginFragment";
    public static final String LOGIN_UNSUCCESSFUL = "Login unsuccessful!";

    private EditText etEmail;
    private EditText etPassword;
    private Button btLogin;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_login, null);
        etEmail = (EditText) rootView.findViewById(R.id.etLoginEmail);
        etPassword = (EditText) rootView.findViewById(R.id.etLoginPassword);
        btLogin = (Button) rootView.findViewById(R.id.btLogin);

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tryToLogin()){
                    continueToApplicationWithAuth();
                }else{
                    Toast.makeText(getActivity(), LOGIN_UNSUCCESSFUL, Toast.LENGTH_SHORT).show();
                }
            }
        });

        return rootView;

    }

    //TODO
    //read from DB
    private boolean tryToLogin(){
        return true;
    }

    public void continueToApplicationWithAuth(){
        Intent intent = new Intent();
        intent.setClass(getActivity(), ApplicationActivity.class);
        startActivity(intent);
    }
}
