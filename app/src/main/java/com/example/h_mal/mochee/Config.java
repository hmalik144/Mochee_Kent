package com.example.h_mal.mochee;

import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

/**
 * Created by h_mal on 14/01/2018.
 */

public class Config {

    private Config() {}

    public static final String PAYPAL_CLIENT_ID = "QVhwOUFrM2dpaHdZbGxfVzFueS1qZWU0RlFmQ1o0ZGtuUjdhU0NLUFNwUVdpNEhSWWcyQ0hDY0o3enZVTDJkQkxFdWxUa2U2Ny1tc1FVd0w=";

    public static final void clearForm(ViewGroup group) {
        for (int i = 0, count = group.getChildCount(); i < count; ++i) {
            View view = group.getChildAt(i);
            if (view instanceof EditText) {
                ((EditText)view).setText("");
            }

            if(view instanceof ViewGroup && (((ViewGroup)view).getChildCount() > 0))
                clearForm((ViewGroup)view);
        }
    }
}
