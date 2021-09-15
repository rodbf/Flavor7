package com.flavorplus.util;

import androidx.databinding.BindingAdapter;

import com.google.android.material.textfield.TextInputLayout;

public class DataBindingAdapters {
    @BindingAdapter("errorText")
    public static void setErrorMessage(TextInputLayout view, String errorMessage) {
        view.setError(errorMessage);
    }
}
