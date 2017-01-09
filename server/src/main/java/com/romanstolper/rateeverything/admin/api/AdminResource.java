package com.romanstolper.rateeverything.admin.api;

import com.romanstolper.rateeverything.item.service.ItemService;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * API for admin functionality
 */
@Produces(MediaType.APPLICATION_JSON)
public class AdminResource {

    private ItemService itemService;

    public AdminResource(ItemService itemService) {
        this.itemService = itemService;
    }

//    @GET
//    @Path("copyItemsFrom/{fromUserId}/to/{toUserId}")
//    public Response copyItemsFrom(
//            @PathParam("fromUserId") String fromUserId,
//            @PathParam("toUserId") String toUserId
//    ) {
//        Collection<Item> otherUserItems = itemService.getAllItems(new UserId(fromUserId));
//        for (Item otherUserItem : otherUserItems) {
//            otherUserItem.setUserId(new UserId(toUserId));
//            // TODO: batch insert, and delete from fromUser if it's opted in
//            itemService.createItem(otherUserItem);
//        }
//        return Response.ok("copied over " + otherUserItems.size() + " items from " + fromUserId).build();
//    }
}
