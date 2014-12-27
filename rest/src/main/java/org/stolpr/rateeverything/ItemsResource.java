package org.stolpr.rateeverything;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.Map;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("items")
public class ItemsResource {

    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getIt() {
        return "Got it!";
    }

    @GET
    @Path("json")
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Object> getItJson() {
        Map<String, Object> result = new HashMap<>();
        result.put("name", "x");
        return result;
    }
}
