package com.example.pictorutinas.model;

public class Step {
    private String textKey;
    private String imageResName;

    public Step(String textKey, String imageResName) {
        this.textKey = textKey;
        this.imageResName = imageResName;
    }
    public String getTextKey() { return textKey; }
    public String getImageResName() { return imageResName; }
}