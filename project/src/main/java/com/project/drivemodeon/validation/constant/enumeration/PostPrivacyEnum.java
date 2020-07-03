package com.project.drivemodeon.validation.constant.enumeration;

public enum PostPrivacyEnum {
    
    PUBLIC("Public"),
    FOR_FRIENDS_ONLY("For friends"),
    ONLY_ME("Only me");

    private final String text;

    PostPrivacyEnum(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
