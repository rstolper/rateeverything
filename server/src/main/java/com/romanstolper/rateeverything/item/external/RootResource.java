package com.romanstolper.rateeverything.item.external;

import com.romanstolper.rateeverything.item.service.ItemService;
import com.romanstolper.rateeverything.item.service.ItemServiceImpl;
import com.romanstolper.rateeverything.users.external.UsersResource;

import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by roman on 1/18/2015.
 */
@Path("api/v1")
@Produces(MediaType.APPLICATION_JSON)
@Singleton
public class RootResource {
    @Context UriInfo uriInfo;
    @Context Request request;

    private ItemService itemService = new ItemServiceImpl();

    public RootResource() {}

    @GET
    public Response getResources() {
        Map<String, URI> resources = new HashMap<>();
        resources.put("Items", uriInfo.getAbsolutePathBuilder().path("items").build());
        //resources.put("Item", uriInfo.getAbsolutePathBuilder().path("items").path("{itemId}").build("-itemId-"));
        resources.put("Users", uriInfo.getAbsolutePathBuilder().path("users").build());
        //resources.put("User", uriInfo.getAbsolutePathBuilder().path("users").path("{userName}").build("-userName-"));
        return Response.ok().entity(resources).build();
    }

    @Path("items")
    public ItemsResource getItemsResource() {
        return new ItemsResource(itemService, uriInfo, request);
    }

    @Path("users")
    public UsersResource getUsersResource() {
        return new UsersResource(uriInfo, request);
    }
}
