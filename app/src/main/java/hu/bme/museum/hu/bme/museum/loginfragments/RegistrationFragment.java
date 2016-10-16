package hu.bme.museum.hu.bme.museum.loginfragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import hu.bme.museum.R;

public class RegistrationFragment extends Fragment {
    public static final String TAG = "RegistrationFragment";
    public static final String REGISTRATION_UNSUCCESSFUL = "Registration unsuccessful!";
    public static final String REGISTRATION_SUCCESSFUL = "Registration successful!";

    EditText etEmail;
    EditText etPassword;
    EditText etPasswordAgain;
    Button btRegister;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_registration, null);

        etEmail = (EditText) rootView.findViewById(R.id.etRegistratonEmail);
        etPassword = (EditText) rootView.findViewById(R.id.etRegistrationPassword);
        etPasswordAgain = (EditText) rootView.findViewById(R.id.etRegistrationPasswordAgain);

        btRegister = (Button) rootView.findViewById(R.id.btRegister);

        btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tryRegister()){
                    Toast.makeText(getActivity(), REGISTRATION_SUCCESSFUL, Toast.LENGTH_SHORT);
                }else{
                    Toast.makeText(getActivity(), REGISTRATION_UNSUCCESSFUL, Toast.LENGTH_SHORT);
                }
            }
        });

        return rootView;
    }

    //TODO
    private boolean tryRegister(){
        return true;
    }
}
