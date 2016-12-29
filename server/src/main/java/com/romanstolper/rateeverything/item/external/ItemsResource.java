package com.romanstolper.rateeverything.item.external;

import com.romanstolper.rateeverything.item.domain.ItemId;
import com.romanstolper.rateeverything.item.domain.Rating;
import com.romanstolper.rateeverything.item.external.dto.ItemCreateDTO;
import com.romanstolper.rateeverything.item.external.dto.ItemDTO;
import com.romanstolper.rateeverything.item.service.ItemService;
import com.romanstolper.rateeverything.item.domain.Item;
import com.romanstolper.rateeverything.user.domain.UserId;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;

/**
 * APIs for interacting with an items collection
 */
@Produces(MediaType.APPLICATION_JSON)
//@Singleton
public class ItemsResource {

    private ItemService itemService;
    private final UserId userId;

    @Context UriInfo uriInfo;
    @Context Request request;

    public ItemsResource(
            UserId userId,
            ItemService itemService,
            UriInfo uriInfo,
            Request request
    ) {
        this.userId = userId;
        this.itemService = itemService;
        this.uriInfo = uriInfo;
        this.request = request;
    }

    /**
     * Get all items for user
     */
    @GET
    public Response getItemsForUser() {
        Collection<Item> items = null;
        if (userId.getValue() != null && !userId.getValue().trim().equals("")) {
            System.out.println("Getting all items for: " + userId);
            items = itemService.getAllItems(userId);
            Collection<ItemDTO> itemDTOs = new ArrayList<>();
            for (Item item : items) {
                ItemDTO itemDTO = new ItemDTO(item);
                itemDTO.setUrl(uriInfo.getAbsolutePathBuilder().path("{itemId}").build(item.getItemId()));
                itemDTOs.add(itemDTO);
            }
            return Response.ok().entity(itemDTOs).build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid userId: " + userId.getValue()).build();
        }
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
        Item createdItem = itemService.createItem(new Item(null, userId, newItem.getName(), newItem.getCategory(), itemRating, Instant.now()));
        ItemDTO itemDTO = new ItemDTO(createdItem);
        itemDTO.setUrl(uriInfo.getAbsolutePathBuilder().path("{itemId}").build(createdItem.getItemId()));
        return Response.created(itemDTO.getUrl()).entity(itemDTO).build();
    }

    @Path("{itemId}")
    public ItemResource getItem(@PathParam("itemId") String itemId){
        return new ItemResource(userId, new ItemId(itemId), itemService, uriInfo, request);
    }
}
