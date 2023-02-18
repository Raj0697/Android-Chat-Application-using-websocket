package com.example.chatapp.Presenter;

import com.example.chatapp.Model.User;
import com.example.chatapp.View.ILoginView;

public class LoginPresenter implements ILoginPresenter{
    ILoginView loginView;

    public LoginPresenter(ILoginView loginView) {
        this.loginView = loginView;
    }
    @Override
    public void onLogin(String name, String mobile, int quantity, boolean isconnection) {
        User user = new User(name, mobile, quantity, isconnection);
        int logincode = user.isValidData();

        if(logincode == 0)
            loginView.onLoginError("name field is required");
        else if(logincode == 1)
            loginView.onLoginError("Enter valid mobile number");
        else if(logincode == 2)
            loginView.onLoginError("Minimum order quantity should be");
        else if(logincode == 3)
            loginView.onLoginError("Please check your internet connection");
        else
            loginView.onLoginSuccess("Login success");
    }
}
