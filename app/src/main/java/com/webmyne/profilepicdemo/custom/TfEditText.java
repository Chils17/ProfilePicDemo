package com.webmyne.profilepicdemo.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

import com.webmyne.profilepicdemo.helper.Functions;


/**
 * Created by sagartahelyani on 25-07-2017.
 */

public class TfEditText extends android.support.v7.widget.AppCompatEditText {

    public TfEditText(Context context) {
        super(context);
        if (!isInEditMode()) {
            init();
        }
    }

    public TfEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode()) {
            init();
        }
    }

    private void init() {
        try {
            setTypeface(Functions.getFontType(getContext(), FontType.Regular.getId()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
