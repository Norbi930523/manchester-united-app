package com.udacity.norbi930523.manutdapp.util;

import android.os.Build;
import android.text.Html;
import android.widget.TextView;

public class TextUtils {

    private TextUtils(){}

    public static void setHtmlText(TextView textView, String htmlContent){
        /* https://stackoverflow.com/a/2116191 */
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            textView.setText(Html.fromHtml(htmlContent, Html.FROM_HTML_MODE_COMPACT));
        } else {
            textView.setText(Html.fromHtml(htmlContent));
        }
    }

}
