package com.romanstolper.rateeverything.user.external;

import com.romanstolper.rateeverything.user.domain.GoogleDetails;
import com.romanstolper.rateeverything.user.domain.NativeAuthDetails;
import com.romanstolper.rateeverything.user.domain.User;
import com.romanstolper.rateeverything.user.external.dto.NativeAuthCreateDTO;
import com.romanstolper.rateeverything.user.service.GoogleAuthService;
import com.romanstolper.rateeverything.user.service.NativeAuthService;
import com.romanstolper.rateeverything.user.service.UserService;
import com.romanstolper.rateeverything.web.ErrorResponseDTO;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.IOException;
import java.security.GeneralSecurityException;

/**
 * APIs for interacting with a users collection
 */
@Produces(MediaType.APPLICATION_JSON)
public class UsersResource {

    private final UserService userService;
    private final GoogleAuthService googleAuthService;
    private final NativeAuthService nativeAuthService;

    @Context
    UriInfo uriInfo;
    @Context
    Request request;

    public UsersResource(UserService userService, GoogleAuthService googleAuthService, NativeAuthService nativeAuthService, UriInfo uriInfo, Request request) {
        this.userService = userService;
        this.googleAuthService = googleAuthService;
        this.nativeAuthService = nativeAuthService;
        this.uriInfo = uriInfo;
        this.request = request;
    }

    @POST
    @Path("viaGoogleAuthToken")
    public Response createUser(@HeaderParam("AuthToken") String googleIdToken)
            throws GeneralSecurityException, IOException
    {
        GoogleDetails googleDetails = googleAuthService.parse(googleIdToken);
        if (googleDetails != null) {
            User newUser = new User(null);
            newUser.setGoogleId(googleDetails.getGoogleId());
            User createdUser = userService.createUser(newUser);
            return Response.ok(createdUser).build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid Google ID token").build();
        }
    }

    @POST
    @Path("viaNativeAuth")
    public Response createUserViaNativeAuth(NativeAuthCreateDTO nativeAuthCredsDTO) {
        String username = nativeAuthCredsDTO.getUsername();
        String password = nativeAuthCredsDTO.getPassword();
        try {
            nativeAuthService.validateUsername(username);
            nativeAuthService.validatePassword(password);
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
        User newUser = new User(null);
        newUser.setNativeAuthUsername(username);

        NativeAuthDetails nativeAuthDetails = new NativeAuthDetails();
        nativeAuthDetails.setUsername(username);
        // TODO: hash and salt
        nativeAuthDetails.setPassword(password);
        newUser.setNativeAuthDetails(nativeAuthDetails);

        if (userService.getUserViaUsername(username) != null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponseDTO("User already exists: " + username))
                    .build();
        }
        User createdUser = userService.createUser(newUser);
        return Response.ok(createdUser).build();
    }
}
