package com.romanstolper.rateeverything.web;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.romanstolper.rateeverything.item.persistence.DynamoDbItemPersistence;
import com.romanstolper.rateeverything.item.service.ItemService;
import com.romanstolper.rateeverything.item.service.ItemServiceImpl;
import com.romanstolper.rateeverything.user.domain.GoogleDetails;
import com.romanstolper.rateeverything.user.domain.User;
import com.romanstolper.rateeverything.user.domain.UserId;
import com.romanstolper.rateeverything.user.external.dto.NativeAuthCredsDTO;
import com.romanstolper.rateeverything.user.external.UserResource;
import com.romanstolper.rateeverything.user.external.UsersResource;
import com.romanstolper.rateeverything.user.external.dto.NativeAuthToken;
import com.romanstolper.rateeverything.user.persistence.DynamoDbUserPersistence;
import com.romanstolper.rateeverything.user.service.GoogleAuthService;
import com.romanstolper.rateeverything.user.service.NativeAuthService;
import com.romanstolper.rateeverything.user.service.UserService;
import com.romanstolper.rateeverything.user.service.UserServiceImpl;

import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.*;
import java.io.IOException;
import java.net.URI;
import java.security.GeneralSecurityException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import static java.time.temporal.ChronoUnit.HOURS;

@Path("api/v1")
@Produces(MediaType.APPLICATION_JSON)
@Singleton
public class RootResource {

    private static ItemService itemService = new ItemServiceImpl(
            new DynamoDbItemPersistence(new AmazonDynamoDBClient())
    );
    private static UserService userService = new UserServiceImpl(
            new DynamoDbUserPersistence(new AmazonDynamoDBClient())
    );

    private GoogleAuthService googleAuthService = new GoogleAuthService();
    private NativeAuthService nativeAuthService = new NativeAuthService();

    @Context UriInfo uriInfo;
    @Context Request request;

    public RootResource() {}

    @GET
    public Response getResources() {
        Map<String, URI> resources = new HashMap<>();
        resources.put("Users", uriInfo.getAbsolutePathBuilder().path("users").build());
        resources.put("Items", uriInfo.getAbsolutePathBuilder().path("user/items").build());
        return Response.ok().entity(resources).build();
    }

    @Path("user")
    public UserResource authenticatedUserResource(@HeaderParam("AuthToken") String authToken,
                                                  @HeaderParam("AuthProvider") String authProvider)
            throws GeneralSecurityException, IOException
    {
        // TODO: only need the userid, not the whole user
        // may need to change the GSIs for this.
        User user = null;
        switch(authProvider) {
            case "Google":
                GoogleDetails googleDetails = googleAuthService.parse(authToken);
                user = userService.getUserViaGoogle(googleDetails.getGoogleId());
                break;
            case "Native":
                UserId userId = nativeAuthService.parse(authToken);
                user = new User(userId);
                break;
        }

        if (user != null) {
            return new UserResource(user.getUserId(), userService, itemService, uriInfo, request);
        } else {
            throw new RuntimeException("Could not find user.");
        }
    }

    @Path("users")
    public UsersResource getUsersResource() {
        return new UsersResource(userService, googleAuthService, nativeAuthService, uriInfo, request);
    }

    /**
     * This creates, saves, and returns a new auth token
     * @return
     */
    @Path("auth")
    @POST
    public Response getNativeAuthToken(NativeAuthCredsDTO nativeAuthCredsDTO) {
        // find the user by username
        User user = userService.getUserViaUsername(nativeAuthCredsDTO.getUsername());
        // verify password

        // create an auth token
        Instant expiry = Instant.now().plus(24, HOURS);
        String authToken = nativeAuthService.generateAuthToken(user.getUserId(), expiry);
        user.getNativeAuthDetails().setAuthToken(authToken);
        user.getNativeAuthDetails().setAuthTokenExpiry(expiry.toString());
        // store it
        userService.updateUser(user);
        // return it
        return Response.ok(new NativeAuthToken(authToken)).build();
    }
}
