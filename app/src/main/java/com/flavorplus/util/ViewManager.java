package com.flavorplus.util;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.view.inputmethod.InputMethodManager;

import com.flavorplus.R;

public class ViewManager {

    public static void expand(final View v) {
        int widthSpec = View.MeasureSpec.makeMeasureSpec(((ViewGroup)v.getParent()).getWidth(), View.MeasureSpec.EXACTLY);
        int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        v.measure(widthSpec, heightSpec);
        final int targetHeight = v.getMeasuredHeight();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.getLayoutParams().height = 1;
        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {

                if(v.getVisibility() == View.GONE && interpolatedTime > 0.01)
                    v.setVisibility(View.VISIBLE);
                if(interpolatedTime == 1)
                    v.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
                else
                    v.getLayoutParams().height = (int)(targetHeight*interpolatedTime);
                v.setElevation(v.getContext().getResources().getDimension(R.dimen.cardview_default_elevation)*interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };
        a.setDuration((int)(2*targetHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    public static void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if(interpolatedTime == 1){
                    v.setVisibility(View.GONE);
                }else{
                    v.getLayoutParams().height = initialHeight - (int)(initialHeight * interpolatedTime);
                    v.setElevation(v.getContext().getResources().getDimension(R.dimen.cardview_default_elevation) - v.getContext().getResources().getDimension(R.dimen.cardview_default_elevation)*interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // Collapse speed of 1dp/ms
        a.setDuration((int)(2*initialHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);

    }

    public static void closeKeyboard(View v){
        InputMethodManager imm = (InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public static void showKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if(imm != null){
            imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
        }
        v.requestFocus();
    }


}
