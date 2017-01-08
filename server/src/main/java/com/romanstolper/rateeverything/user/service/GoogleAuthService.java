package com.romanstolper.rateeverything.user.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.romanstolper.rateeverything.user.domain.GoogleDetails;
import com.romanstolper.rateeverything.user.domain.GoogleId;
import com.romanstolper.rateeverything.user.domain.GoogleProfile;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

/**
 * Service for helping with google auth
 */
public class GoogleAuthService {
    private static final String CLIENT_ID = "206994273750-sv9aap9cqet8h0ofo983kopo0vsclgbe.apps.googleusercontent.com";
    private static final GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new JacksonFactory())
            .setAudience(Collections.singletonList(CLIENT_ID))
            .build();

    public GoogleDetails parse(String googleIdToken) throws GeneralSecurityException, IOException {
        GoogleIdToken idToken = verifier.verify(googleIdToken);
        if (idToken != null) {
            GoogleIdToken.Payload payload = idToken.getPayload();
            String googleId = payload.getSubject();
            System.out.println("Google auth token contains GoogleID: " + googleId);

            // Get profile information from payload
            String email = payload.getEmail();
            boolean emailVerified = Boolean.valueOf(payload.getEmailVerified());
            String name = (String) payload.get("name");
            String pictureUrl = (String) payload.get("picture");
            String locale = (String) payload.get("locale");
            String familyName = (String) payload.get("family_name");
            String givenName = (String) payload.get("given_name");

            GoogleDetails googleDetails = new GoogleDetails(new GoogleId(googleId), new GoogleProfile(
                    name, givenName, familyName, email, emailVerified, pictureUrl, locale));

            return googleDetails;
        } else {
//            throw new RuntimeException("Failed to parse Google ID Token.");
            return null;
        }
    }
}
