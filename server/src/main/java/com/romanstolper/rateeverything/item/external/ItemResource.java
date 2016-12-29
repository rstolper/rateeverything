package com.romanstolper.rateeverything.item.external;

import com.romanstolper.rateeverything.item.domain.Item;
import com.romanstolper.rateeverything.item.domain.ItemId;
import com.romanstolper.rateeverything.item.domain.Rating;
import com.romanstolper.rateeverything.item.external.dto.ItemDTO;
import com.romanstolper.rateeverything.item.service.ItemService;
import com.romanstolper.rateeverything.user.domain.UserId;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

/**
 * APIs for a single item
 */
@Produces(MediaType.APPLICATION_JSON)
public class ItemResource {

    private UserId userId;
    private ItemId itemId;
    private ItemService itemService;

    @Context UriInfo uriInfo;
    @Context Request request;

    public ItemResource(
            UserId userId,
            ItemId itemId,
            ItemService itemService,
            UriInfo uriInfo,
            Request request
    ) {
        this.userId = userId;
        this.itemId = itemId;
        this.itemService = itemService;
        this.uriInfo = uriInfo;
        this.request = request;
    }

    @GET
    public ItemDTO getItem(){
        System.out.println("Getting item for userId: " + userId + ", itemId: " + itemId);
        Item item = itemService.getItem(userId, itemId);
        ItemDTO itemDTO = new ItemDTO(item);
        itemDTO.setUrl(uriInfo.getAbsolutePath());
        return itemDTO;
    }

    @DELETE
    public Response deleteItem() {
        System.out.println("Deleting item for userId: " + userId + ", itemId: " + itemId);
        if (!itemService.existsItem(userId, itemId)) {
            return Response.status(Response.Status.BAD_REQUEST).entity("No item exists with id: " + itemId).build();
        }
        Item deletedItem = itemService.deleteItem(userId, itemId);
        ItemDTO itemDTO = new ItemDTO(deletedItem);
        return Response.ok().entity(itemDTO).build();
    }

    @PUT
    public Response updateItem(ItemDTO updateItemDTO) {
        System.out.println("Updating item for userId: " + userId + ", itemId: " + itemId);
        if (!itemService.existsItem(userId, itemId)) {
            return Response.status(Response.Status.BAD_REQUEST).entity("No item exists with id: " + itemId).build();
        }
        Rating itemRating = Rating.valueOfDisplayText(updateItemDTO.getRating());
        if (itemRating == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid rating: " + updateItemDTO.getRating()).build();
        }
        Item updatedItem = itemService.updateItem(new Item(itemId, userId, updateItemDTO.getName(), updateItemDTO.getCategory(), itemRating));
        ItemDTO updatedItemDTO = new ItemDTO(updatedItem);
        updatedItemDTO.setUrl(uriInfo.getAbsolutePath());
        return Response.ok().entity(updatedItemDTO).build();
    }
}
