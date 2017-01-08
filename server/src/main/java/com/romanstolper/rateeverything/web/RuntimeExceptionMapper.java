package com.romanstolper.rateeverything.web;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

/**
 * Created by roman on 1/2/17.
 */
public class RuntimeExceptionMapper implements ExceptionMapper<RuntimeException> {
    @Override
    public Response toResponse(RuntimeException ex) {
        return Response.status(Response.Status.BAD_REQUEST).
                entity(ex.getMessage()).
                type("text/plain").
                build();
    }
}
