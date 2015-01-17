package rateeverything.item;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("items")
public class ItemsResource {

    private ItemService itemService = new ItemService();

    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Collection<Item> getIt() {
        return itemService.GetAllItems();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Item createItem(Item item) {
        return itemService.CreateItem(item);
    }

    @GET
    @Path("{itemId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Item getItem(@PathParam("itemId") int itemId){
        System.out.println("Getting item for id: " + itemId);
        return Item.DEFAULT;
    }

//    @GET
//    @Path("json")
//    @Produces(MediaType.APPLICATION_JSON)
//    public Map<String, Object> getItJson() {
//        Map<String, Object> result = new HashMap<>();
//        result.put("name", "x");
//        return result;
//    }
}
