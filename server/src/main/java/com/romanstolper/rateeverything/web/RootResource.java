package com.romanstolper.rateeverything.web;

import com.romanstolper.rateeverything.item.persistence.DynamoDbItemPersistence;
import com.romanstolper.rateeverything.item.service.ItemService;
import com.romanstolper.rateeverything.item.service.ItemServiceImpl;
import com.romanstolper.rateeverything.user.domain.GoogleDetails;
import com.romanstolper.rateeverything.user.domain.User;
import com.romanstolper.rateeverything.user.external.UserResource;
import com.romanstolper.rateeverything.user.external.UsersResource;
import com.romanstolper.rateeverything.user.persistence.DynamoDbUserPersistence;
import com.romanstolper.rateeverything.user.service.GoogleAuthService;
import com.romanstolper.rateeverything.user.service.UserService;
import com.romanstolper.rateeverything.user.service.UserServiceImpl;

import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.*;
import java.io.IOException;
import java.net.URI;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Map;

@Path("api/v1")
@Produces(MediaType.APPLICATION_JSON)
@Singleton
public class RootResource {

    private static ItemService itemService = new ItemServiceImpl(
//            new H2ItemPersistence(SetupH2Database.dbUrl)
            new DynamoDbItemPersistence()
    );
    private static UserService userService = new UserServiceImpl(
//            new H2UserPersistence(SetupH2Database.dbUrl)
            new DynamoDbUserPersistence()
    );

    private GoogleAuthService googleAuthService = new GoogleAuthService();

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
        // TODO : switch on auth provider
        // authenticate user
        GoogleDetails googleDetails = googleAuthService.getGoogleDetails(authToken);
        // determine user id
        User user = userService.getUserViaGoogle(googleDetails.getGoogleId());
        // return the user resource
        return new UserResource(user.getUserId(), userService, itemService, uriInfo, request);
    }

    @Path("users")
    public UsersResource getUsersResource() {
        return new UsersResource(userService, itemService, uriInfo, request);
    }
}
