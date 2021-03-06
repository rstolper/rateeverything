package com.romanstolper.rateeverything.user.external;

import com.romanstolper.rateeverything.item.external.ItemsResource;
import com.romanstolper.rateeverything.item.service.ItemService;
import com.romanstolper.rateeverything.user.domain.UserId;
import com.romanstolper.rateeverything.user.service.UserService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

/**
 * APIs for a single user
 * TODO
 */
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {

    private final UserId userId;
    private final UserService userService;
    private final ItemService itemService;

    @Context
    UriInfo uriInfo;
    @Context
    Request request;

    public UserResource(UserId userId,
                        UserService userService,
                        ItemService itemService,
                        UriInfo uriInfo,
                        Request request) {
        this.userId = userId;
        this.userService = userService;
        this.itemService = itemService;
        this.uriInfo = uriInfo;
        this.request = request;
    }

    @Path("items")
    public ItemsResource getItems() {
        return new ItemsResource(userId, itemService, uriInfo, request);
    }

    @GET
    public Response getUser() {
        return Response.ok(userService.getUser(userId)).build();
    }
}
