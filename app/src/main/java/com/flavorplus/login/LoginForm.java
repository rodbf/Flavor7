package com.flavorplus.login;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.flavorplus.BR;

public class LoginForm extends BaseObservable {
    private String email, emailError, password, passwordError, loginError;
    private boolean submitEnabled;

    public LoginForm() {
        this.submitEnabled = true;
        this.emailError = "";
        this.passwordError = "";
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Bindable
    public String getEmailError() {
        return emailError;
    }

    @Bindable
    public String getPasswordError() {
        return passwordError;
    }

    private void validateEmail(){
        emailError = "";
        notifyPropertyChanged(BR.emailError);
        if(email == null || email.length() == 0)
            addEmailError("e-mail não pode estar vazio");
        else if(!email.matches("^[^@\\s]+@[^@\\s\\.]+\\.[^@\\.\\s]+$"))
            addEmailError("e-mail inválido");
        else if(email.contains("@mailinator.com"))
            addEmailError("e-mail inválido");

    }

    private void validatePassword(){
        passwordError = "";
        notifyPropertyChanged(BR.passwordError);
        if(password == null || password.length() == 0)
            addPasswordError("senha não pode estar vazia");
    }

    private void enableSubmit(){
        submitEnabled = emailError.isEmpty() && passwordError.isEmpty();
    }

    public boolean validateForm(){
        validateEmail();
        validatePassword();
        enableSubmit();
        return submitEnabled;
    }

    public void addEmailError(String error) {
        if(!this.emailError.isEmpty())
            this.emailError += "\n";
        this.emailError += error;
        notifyPropertyChanged(BR.emailError);
    }

    public void addPasswordError(String error){
        if(!this.passwordError.isEmpty())
            this.passwordError += "\n";
        this.passwordError += error;
        notifyPropertyChanged(BR.passwordError);
    }

    public void setLoginError(String error){
        this.loginError = error;
    }

    public void clearLoginError(){
        this.loginError = "";
    }
}