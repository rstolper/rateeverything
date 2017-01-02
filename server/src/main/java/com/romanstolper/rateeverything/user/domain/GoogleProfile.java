package com.romanstolper.rateeverything.user.domain;

/**
 * Google profile for a user
 */
public class GoogleProfile {
    private final String name;
    private final String givenName;
    private final String familyName;
    private final String email;
    private final boolean emailVerified;
    private final String imageUrl;
    private final String locale;

    public GoogleProfile(String name,
                         String givenName,
                         String familyName,
                         String email,
                         boolean emailVerified,
                         String imageUrl,
                         String locale) {
        this.name = name;
        this.givenName = givenName;
        this.familyName = familyName;
        this.email = email;
        this.emailVerified = emailVerified;
        this.imageUrl = imageUrl;
        this.locale = locale;
    }

    public String getName() {
        return name;
    }

    public String getGivenName() {
        return givenName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public String getEmail() {
        return email;
    }

    public boolean isEmailVerified() {
        return emailVerified;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getLocale() {
        return locale;
    }
}
