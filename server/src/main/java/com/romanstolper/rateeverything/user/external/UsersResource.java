package com.romanstolper.rateeverything.user.external;

import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;

/**
 * Created by roman on 1/18/2015.
 */
@Produces(MediaType.APPLICATION_JSON)
public class UsersResource {

    @Context
    UriInfo uriInfo;
    @Context
    Request request;

    public UsersResource(UriInfo uriInfo, Request request) {
        this.uriInfo = uriInfo;
        this.request = request;
    }
}
