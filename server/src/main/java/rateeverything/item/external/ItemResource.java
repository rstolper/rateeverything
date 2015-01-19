package rateeverything.item.external;

import rateeverything.item.domain.Item;
import rateeverything.item.domain.Rating;
import rateeverything.item.external.dto.ItemDTO;
import rateeverything.item.service.ItemService;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

/**
 * Created by roman on 1/18/2015.
 */
@Produces(MediaType.APPLICATION_JSON)
public class ItemResource {

    private long itemId;
    private ItemService itemService;

    @Context UriInfo uriInfo;
    @Context Request request;

    public ItemResource(
            long itemId,
            ItemService itemService,
            UriInfo uriInfo,
            Request request
    ) {
        this.itemId = itemId;
        this.itemService = itemService;
        this.uriInfo = uriInfo;
        this.request = request;
    }

    @GET
    public ItemDTO getItem(){
        System.out.println("Getting item for id: " + itemId);
        Item item = itemService.getItem(itemId);
        ItemDTO itemDTO = new ItemDTO(item);
        itemDTO.setUrl(uriInfo.getAbsolutePath());
        return itemDTO;
    }

    @DELETE
    public Response deleteItem() {
        System.out.println("Deleting item: " + itemId);
        if (!itemService.existsItem(itemId)) {
            return Response.status(Response.Status.BAD_REQUEST).entity("No item exists with id: " + itemId).build();
        }
        Item deletedItem = itemService.deleteItem(itemId);
        ItemDTO itemDTO = new ItemDTO(deletedItem);
        return Response.ok().entity(itemDTO).build();
    }

    @PUT
    public Response updateItem(ItemDTO updateItemDTO) {
        System.out.println("Updating item: " + itemId);
        if (!itemService.existsItem(itemId)) {
            return Response.status(Response.Status.BAD_REQUEST).entity("No item exists with id: " + itemId).build();
        }
        // ensure id gets set
        updateItemDTO.setId(itemId);
        Rating itemRating = Rating.valueOfDisplayText(updateItemDTO.getRating());
        if (itemRating == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid rating: " + updateItemDTO.getRating()).build();
        }
        Item updatedItem = itemService.updateItem(new Item(updateItemDTO.getId(), updateItemDTO.getName(), updateItemDTO.getCategory(), updateItemDTO.getOwner(), itemRating));
        ItemDTO updatedItemDTO = new ItemDTO(updatedItem);
        updatedItemDTO.setUrl(uriInfo.getAbsolutePath());
        return Response.ok().entity(updatedItemDTO).build();
    }
}
