package com.romanstolper.rateeverything.user.external;

import com.romanstolper.rateeverything.item.domain.ItemId;
import com.romanstolper.rateeverything.item.external.ItemResource;
import com.romanstolper.rateeverything.item.service.ItemService;
import com.romanstolper.rateeverything.user.domain.UserId;
import com.romanstolper.rateeverything.user.service.UserService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.*;

/**
 * APIs for interacting with a users collection
 */
@Produces(MediaType.APPLICATION_JSON)
public class UsersResource {

    private final UserService userService;
    private final ItemService itemService;

    @Context
    UriInfo uriInfo;
    @Context
    Request request;

    public UsersResource(UserService userService, ItemService itemService, UriInfo uriInfo, Request request) {
        this.userService = userService;
        this.itemService = itemService;
        this.uriInfo = uriInfo;
        this.request = request;
    }

    @GET
    public Response getAllUsers() {
        // TODO: get all users maybe
        return Response.ok().build();
    }

    @Path("{userId}")
    public UserResource getUser(@PathParam("userId") String userId){
        return new UserResource(new UserId(userId), userService, itemService, uriInfo, request);
    }
}
