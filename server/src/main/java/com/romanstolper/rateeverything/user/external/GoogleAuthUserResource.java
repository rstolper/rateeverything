package com.romanstolper.rateeverything.user.external;

import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.romanstolper.rateeverything.user.domain.GoogleId;
import com.romanstolper.rateeverything.user.domain.User;
import com.romanstolper.rateeverything.user.service.UserService;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

/**
 * APIs for google authentication
 */
@Produces(MediaType.APPLICATION_JSON)
public class GoogleAuthUserResource {

    private final String googleIdToken;
    private final UserService userService;

    private static final String CLIENT_ID = "206994273750-sv9aap9cqet8h0ofo983kopo0vsclgbe.apps.googleusercontent.com";

    private static final GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new JacksonFactory())
            .setAudience(Collections.singletonList(CLIENT_ID))
            .build();

    @Context
    UriInfo uriInfo;
    @Context
    Request request;

    public GoogleAuthUserResource(String googleIdToken, UserService userService, UriInfo uriInfo, Request request) {
        this.googleIdToken = googleIdToken;
        this.userService = userService;
        this.uriInfo = uriInfo;
        this.request = request;
    }

    @GET
    public Response getUser() throws GeneralSecurityException, IOException {
        GoogleIdToken idToken = verifier.verify(googleIdToken);
        if (idToken != null) {
            Payload payload = idToken.getPayload();
            String googleId = payload.getSubject();
            System.out.println("Getting user with GoogleId: " + googleId);
            return Response.ok(userService.getUserViaGoogle(new GoogleId(googleId))).build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid Google ID token").build();
        }
    }

    @POST
    public Response createUser() throws GeneralSecurityException, IOException {
        GoogleIdToken idToken = verifier.verify(googleIdToken);
        if (idToken != null) {
            Payload payload = idToken.getPayload();
            String googleId = payload.getSubject();
            System.out.println("Create user with GoogleID: " + googleId);

//            // Get profile information from payload
//            String email = payload.getEmail();
//            boolean emailVerified = Boolean.valueOf(payload.getEmailVerified());
//            String name = (String) payload.get("name");
//            String pictureUrl = (String) payload.get("picture");
//            String locale = (String) payload.get("locale");
//            String familyName = (String) payload.get("family_name");
//            String givenName = (String) payload.get("given_name");

            User newUser = new User(null);
            newUser.setGoogleId(new GoogleId(googleId));
            User createdUser = userService.createUser(newUser);
            return Response.ok(createdUser).build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid Google ID token").build();
        }
    }
}
