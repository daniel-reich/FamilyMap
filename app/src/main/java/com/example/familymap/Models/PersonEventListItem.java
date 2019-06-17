package com.example.familymap.Models;

import android.graphics.drawable.Drawable;

public class PersonEventListItem {
    private String type;
    private String topText;
    private String bottomText;
    private String id;
    private Drawable icon;

    public PersonEventListItem(String type, String topText, String bottomText, String id, Drawable icon) {
        this.type = type;
        this.topText = topText;
        this.bottomText = bottomText;
        this.id = id;
        this.icon = icon;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTopText() {
        return topText;
    }

    public void setTopText(String topText) {
        this.topText = topText;
    }

    public String getBottomText() {
        return bottomText;
    }

    public void setBottomText(String bottomText) {
        this.bottomText = bottomText;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }
}
