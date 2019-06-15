package com.example.familymap.Fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.familymap.Models.DataModel;
import com.example.familymap.Models.Gender;
import com.example.familymap.Models.User;
import com.example.familymap.R;
import com.example.familymap.RequestResponse.RegisterResponse;
import com.example.familymap.Tasks.RegisterOrLoginAsync;

import java.net.MalformedURLException;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment implements RegisterOrLoginAsync.RegisterOrLoginContext {

    Button loginButton;
    Button registerButton;
    EditText editServerHost;
    EditText editServerPort;
    EditText editUserName;
    EditText editPassword;
    EditText editFirstName;
    EditText editLastName;
    EditText editEmail;
    RadioButton radioMale;
    RadioButton radioFemale;


    public LoginFragment() {
        // Required empty public constructor
    }

    public interface LoginOrRegisterResponseListener
    {
        void onLoginOrRegisterResponseRecieved(RegisterResponse response);
    }

    public LoginOrRegisterResponseListener getLoginOrRegisterResponseListener() {
        return loginOrRegisterResponseListener;
    }

    public void setLoginOrRegisterResponseListener(LoginOrRegisterResponseListener loginOrRegisterResponseListener) {
        this.loginOrRegisterResponseListener = loginOrRegisterResponseListener;
    }

    LoginOrRegisterResponseListener loginOrRegisterResponseListener;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_login, container, false);

        editServerHost = view.findViewById(R.id.editServerHost);
        editServerPort = view.findViewById(R.id.editServerPort);
        editUserName = view.findViewById(R.id.editUserName);
        editPassword = view.findViewById(R.id.editPassword);
        editFirstName = view.findViewById(R.id.editFirstName);
        editLastName = view.findViewById(R.id.editLastName);
        radioMale = view.findViewById(R.id.radioMale);
        radioFemale = view.findViewById(R.id.radioFemale);
        editEmail = view.findViewById(R.id.editEmail);
        loginButton = view.findViewById(R.id.loginButton);
        registerButton = view.findViewById(R.id.registerButton);

        loginButton.setEnabled(false);
        registerButton.setEnabled(false);

        editServerHost.addTextChangedListener(new LoginTextWatcher());
        editServerPort.addTextChangedListener(new LoginTextWatcher());
        editUserName.addTextChangedListener(new LoginTextWatcher());
        editPassword.addTextChangedListener(new LoginTextWatcher());
        editFirstName.addTextChangedListener(new RegisterTextWatcher());
        editLastName.addTextChangedListener(new RegisterTextWatcher());
        editEmail.addTextChangedListener(new RegisterTextWatcher());
        editFirstName.addTextChangedListener(new RegisterTextWatcher());
        radioMale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                canEnableRegisterButton();
            }
        });
        radioFemale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                canEnableRegisterButton();
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    DataModel.getInstance().setBaseUrl("http://"+editServerHost.getText().toString()+":"+editServerPort.getText().toString());
                } catch (MalformedURLException e) {
                    Toast.makeText(getActivity(), "Bad URL: "+e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
                Gender gender = radioFemale.isChecked()? Gender.f: Gender.m;
                User newUser = new User(
                       editUserName.getText().toString(),
                       editPassword.getText().toString(),
                       editEmail.getText().toString(),
                       editFirstName.getText().toString(),
                       editLastName.getText().toString(),
                       gender);

                RegisterOrLoginAsync registerTask = new RegisterOrLoginAsync(LoginFragment.this, "register");
                registerTask.execute(newUser);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    DataModel.getInstance().setBaseUrl("http://"+editServerHost.getText().toString()+":"+editServerPort.getText().toString());
                } catch (MalformedURLException e) {
                    Toast.makeText(getActivity(), "Bad URL: "+e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
                User newUser = new User(
                        editUserName.getText().toString(),
                        editPassword.getText().toString());

                RegisterOrLoginAsync loginTask = new RegisterOrLoginAsync(LoginFragment.this, "login");
                loginTask.execute(newUser);

            }
        });



        return view;
    }

    private class LoginTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            canEnableLoginButton();
            canEnableRegisterButton();
        }
    }

    private class RegisterTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            canEnableRegisterButton();
        }
    }

    private void canEnableRegisterButton() {
        boolean enable = true;
        if (editServerHost.getText().toString().equals("")) enable = false;
        if (editServerPort.getText().toString().equals("")) enable = false;
        if (editUserName.getText().toString().equals("")) enable = false;
        if (editPassword.getText().toString().equals("")) enable = false;
        if (editFirstName.getText().toString().equals("")) enable = false;
        if (editLastName.getText().toString().equals("")) enable = false;
        if (editEmail.getText().toString().equals("")) enable = false;
        System.out.println(editEmail.getText().toString());
        if (!radioMale.isChecked() && !radioFemale.isChecked()) enable = false;
        registerButton.setEnabled(enable);
    }

    private void canEnableLoginButton() {
        boolean enable = true;
        if (editServerHost.getText().toString().equals("")) enable = false;
        if (editServerPort.getText().toString().equals("")) enable = false;
        if (editUserName.getText().toString().equals("")) enable = false;
        if (editPassword.getText().toString().equals("")) enable = false;
        loginButton.setEnabled(enable);
    }

}
