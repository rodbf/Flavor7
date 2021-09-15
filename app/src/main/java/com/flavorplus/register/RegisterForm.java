package com.flavorplus.register;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.flavorplus.BR;

public class RegisterForm extends BaseObservable {
    private String displayName, displayNameError, email, emailError, password, passwordError, passwordConfirm, passwordConfirmError;
    private boolean submitEnabled;

    public RegisterForm() {
        this.submitEnabled = true;
        this.displayNameError = "";
        this.emailError = "";
        this.passwordError = "";
    }



    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
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

    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }

    @Bindable
    public String getDisplayNameError() {
        return displayNameError;
    }

    @Bindable
    public String getEmailError() {
        return emailError;
    }

    @Bindable
    public String getPasswordError() {
        return passwordError;
    }

    @Bindable
    public String getPasswordConfirmError() {
        return passwordConfirmError;
    }

    private void validateDisplayName(){
        displayNameError = "";
        notifyPropertyChanged(BR.displayNameError);
        if(displayName == null || displayName.length() == 0)
            addDisplayNameError("nome não pode estar vazio");
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
        else {
            if(!password.matches(".*[a-z].*"))
                addPasswordError("senha deve conter um caractere minúsculo");
            if(!password.matches(".*[A-Z].*"))
                addPasswordError("senha deve conter um caractere maiúsculo");
            if(!password.matches(".*[0-9].*"))
                addPasswordError("senha deve conter um número");
            if(password.length() < 8)
                addPasswordError(("senha deve conter no mínimo 8 caracteres"));
        }
    }

    private void validatePasswordConfirm(){
        passwordConfirmError = "";
        notifyPropertyChanged(BR.passwordConfirmError);
        if(!password.equals(passwordConfirm))
            addPasswordConfirmError("senhas não batem");
    }

    public boolean validateForm(){
        validateDisplayName();
        validateEmail();
        validatePassword();
        validatePasswordConfirm();
        enableSubmit();
        return submitEnabled;
    }

    private void enableSubmit(){
        submitEnabled = displayNameError.isEmpty() && emailError.isEmpty() && passwordError.isEmpty() && passwordConfirmError.isEmpty();
    }

    public void addDisplayNameError(String error){
        if(!this.displayNameError.isEmpty())
            this.displayNameError += "\n";
        this.displayNameError += error;
        notifyPropertyChanged(BR.displayNameError);
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

    public void addPasswordConfirmError(String error){
        if(!this.passwordConfirmError.isEmpty())
            this.passwordConfirmError += "\n";
        this.passwordConfirmError += error;
        notifyPropertyChanged(BR.passwordConfirmError);
    }
}