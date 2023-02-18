package com.example.chatapp.HelperClasses;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;

public class CardHelper {

    int image, count;
    String title;
    GradientDrawable color;

    public CardHelper(GradientDrawable color, int image, String title, int count) {
        this.image = image;
        this.title = title;
        this.color = color;
        this.count = count;
    }

    public int getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }

    public Drawable getgradient() {
        return color;
    }

    public int getCount() {
        return CardAdapter.increasecount;
    }
}
