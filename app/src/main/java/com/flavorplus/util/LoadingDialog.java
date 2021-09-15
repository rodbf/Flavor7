package com.flavorplus.util;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;

import androidx.annotation.NonNull;

import com.flavorplus.R;

import java.util.Objects;

public class LoadingDialog extends Dialog {
    public LoadingDialog(@NonNull Context context) {
        super(context);
        this.setCancelable(false);
        this.setCanceledOnTouchOutside(false);
        Objects.requireNonNull(this.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        this.setContentView(R.layout.loading_dialog);
    }
}
