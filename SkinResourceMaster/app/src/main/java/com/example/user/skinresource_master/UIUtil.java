package com.example.user.skinresource_master;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;

public class UIUtil {

    public static Drawable getImageDrawable(Context ctx){
        return ctx.getResources().getDrawable(R.drawable.abc);
    }

    public static int getImageDrawableId(){
        return R.drawable.abc;
    }


}