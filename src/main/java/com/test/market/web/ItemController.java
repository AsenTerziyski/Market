package com.test.market.web;

import com.test.market.model.dto.ItemDto;
import com.test.market.model.dto.UserDto;
import com.test.market.model.projections.ItemView;
import com.test.market.services.ContractService;
import com.test.market.services.ItemService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/market/items")
public class ItemController {
    private final ItemService itemService;
    private final ContractService contractService;

    public ItemController(ItemService itemService, ContractService contractService) {
        this.itemService = itemService;
        this.contractService = contractService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<ItemView> getAllItems() {
        return itemService.getAllItems();
    }

    @RequestMapping(value = "/user/{userId}", method = RequestMethod.GET)
    public List<ItemView> getAllItemsByUserId(@PathVariable(value = "userId") Long userId) throws Exception {
        return itemService.getItemsByUserId(userId);
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ItemView addNewItem(@RequestBody ItemDto itemDto) throws Exception {
        return itemService.addItemViewToMarket(itemDto.getName(), itemDto.getUsername());
    }

    @RequestMapping(value = "/edit/{itemId}", method = RequestMethod.PUT)
    public ItemView editItemName(@PathVariable Long itemId,
                                 @RequestBody ItemDto itemDto) throws Exception {
        return itemService.editItemNameById(itemId, itemDto.getUsername(), itemDto.getName());
    }

    @RequestMapping(value = "/remove/{itemId}", method = RequestMethod.DELETE)
    public String removeItem(@PathVariable Long itemId,
                             @RequestBody UserDto user) throws Exception {

        Long contractIdByItemID = itemService.getContractIdByItemID(itemId);
        itemService.removeItemFromMarket(itemId, user.getUsername());
        if (contractIdByItemID >= 1) {
            contractService.removeContractById(contractIdByItemID);
        } else {
            return "Contract does not exist.";
        }
        return String.format("User: %s removed item with id = %d", user.getUsername(), itemId);
    }

    @RequestMapping(value = "/buy/{itemId}", method = RequestMethod.POST)
    public ItemView buyItem(@PathVariable Long itemId, @RequestBody UserDto user) throws Exception {
        return contractService.buyItem(user.getUsername(), itemId);
    }

}
