package rateeverything.item.external;

import rateeverything.item.domain.Rating;
import rateeverything.item.external.dto.ItemCreateDTO;
import rateeverything.item.external.dto.ItemDTO;
import rateeverything.item.service.ItemService;
import rateeverything.item.service.ItemServiceImpl;
import rateeverything.item.domain.Item;

import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Root resource (exposed at "myresource" path)
 */
@Produces(MediaType.APPLICATION_JSON)
//@Singleton
public class ItemsResource {

    private ItemService itemService;

    @Context UriInfo uriInfo;
    @Context Request request;

    public ItemsResource(
            ItemService itemService,
            UriInfo uriInfo,
            Request request
    ) {
        this.itemService = itemService;
        this.uriInfo = uriInfo;
        this.request = request;
    }

    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */
    @GET
    public Response getItems(
            @DefaultValue("") @QueryParam("owner") String owner
    ) {
        Collection<Item> items = null;
        if (owner != null && !owner.trim().equals("")) {
            System.out.println("Getting all items for: " + owner);
            items = itemService.getAllItemsByOwner(owner);
        } else {
            System.out.println("Getting all items");
            items = itemService.getAllItems();
        }
        Collection<ItemDTO> itemDTOs = new ArrayList<>();
        for (Item item : items) {
            ItemDTO itemDTO = new ItemDTO(item);
            itemDTO.setUrl(uriInfo.getAbsolutePathBuilder().path("{itemId}").build(item.getId()));
            itemDTOs.add(itemDTO);
        }
        return Response.ok().entity(itemDTOs).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createItem(ItemCreateDTO newItem) {
        if (newItem == null || newItem.getName() == null || newItem.getName().trim().equals("")) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Name cannot be blank").build();
        }
        System.out.println("Creating a new item: " + newItem.getName());
        Rating itemRating = Rating.valueOfDisplayText(newItem.getRating());
        if (itemRating == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid rating: " + newItem.getRating()).build();
        }
        Item createdItem = itemService.createItem(new Item(-1, newItem.getName(), newItem.getCategory(), newItem.getOwner(), itemRating));
        ItemDTO itemDTO = new ItemDTO(createdItem);
        itemDTO.setUrl(uriInfo.getAbsolutePathBuilder().path("{itemId}").build(createdItem.getId()));
        return Response.created(itemDTO.getUrl()).entity(itemDTO).build();
    }

    @Path("{itemId}")
    public ItemResource getItem(@PathParam("itemId") long itemId){
        return new ItemResource(itemId, itemService, uriInfo, request);
    }
}
