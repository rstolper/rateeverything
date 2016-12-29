package com.romanstolper.rateeverything.web;

import com.romanstolper.rateeverything.item.persistence.DynamoDbItemPersistence;
import com.romanstolper.rateeverything.item.service.ItemService;
import com.romanstolper.rateeverything.item.service.ItemServiceImpl;
import com.romanstolper.rateeverything.user.external.UsersResource;
import com.romanstolper.rateeverything.user.persistence.H2UserPersistence;
import com.romanstolper.rateeverything.user.service.UserService;
import com.romanstolper.rateeverything.user.service.UserServiceImpl;
import com.romanstolper.rateeverything.util.SetupH2Database;

import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.*;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@Path("api/v1")
@Produces(MediaType.APPLICATION_JSON)
@Singleton
public class RootResource {

    private ItemService itemService = new ItemServiceImpl(
//            new H2ItemPersistence(SetupH2Database.dbUrl)
            new DynamoDbItemPersistence()
    );
    private UserService userService = new UserServiceImpl(
            new H2UserPersistence(SetupH2Database.dbUrl)
    );

    @Context UriInfo uriInfo;
    @Context Request request;

    public RootResource() {}

    @GET
    public Response getResources() {
        Map<String, URI> resources = new HashMap<>();
        resources.put("Users", uriInfo.getAbsolutePathBuilder().path("users").build());
        return Response.ok().entity(resources).build();
    }

    @Path("users")
    public UsersResource getUsersResource() {
        return new UsersResource(userService, itemService, uriInfo, request);
    }
}
