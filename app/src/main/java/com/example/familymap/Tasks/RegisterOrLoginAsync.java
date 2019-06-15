package com.example.familymap.Tasks;

import android.os.AsyncTask;

import com.example.familymap.Fragments.LoginFragment;
import com.example.familymap.Models.DataModel;
import com.example.familymap.Models.User;
import com.example.familymap.RequestResponse.ErrorResponse;
import com.example.familymap.RequestResponse.RegisterResponse;
import com.example.familymap.Server.FMSProxy;

import java.io.IOException;

public class RegisterOrLoginAsync extends AsyncTask<User, Integer, RegisterResponse> {

    public interface RegisterOrLoginContext {
        LoginFragment.LoginOrRegisterResponseListener getLoginOrRegisterResponseListener();
    }

    private RegisterOrLoginContext context;
    private String action;

    public RegisterOrLoginAsync(RegisterOrLoginContext context, String action) {
        this.action = action;
        this.context = context;
    }
    @Override
    protected RegisterResponse doInBackground(User... users)  {
        FMSProxy proxy = new FMSProxy(DataModel.getInstance().getBaseUrl());
        try {
            if (action.equals("register")) {
                return proxy.RegisterUser(users[0]);
            } else if (action.equals("login"))
            {
                return proxy.LoginUser(users[0]);
            } else {
                RegisterResponse response =  new RegisterResponse();
                response.setError(new ErrorResponse("Please pass in a valid action: [register] [login]"));
                return response;
            }
        } catch (IOException e) {
            RegisterResponse response =  new RegisterResponse();
            response.setError(new ErrorResponse(e.getMessage()));
            return response;
        }
    }

    @Override
    protected void onPostExecute(RegisterResponse registerResponse) {
        context.getLoginOrRegisterResponseListener().onLoginOrRegisterResponseRecieved(registerResponse);
    }
}
