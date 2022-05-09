package com.test.market.services;

import com.test.market.model.ItemEntity;
import com.test.market.model.UserEntity;
import com.test.market.model.projections.ItemView;

import java.util.List;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

public interface ItemService {

    ItemEntity addItemToMarket(String item, String username) throws Exception;


    boolean removeItemFromMarket(Long id, String owner) throws Exception;

    List<ItemEntity> getAllItemsByOwnerId(Long id) throws Exception;

    ItemEntity getItemByIdFetchOwnerEagerly(long itemId) throws Exception;

    List<ItemView> getAllItems();

    List<ItemView> getItemsByUserId(Long userId) throws Exception;

    ItemView addItemViewToMarket(String name, String username) throws Exception;

    ItemView editItemNameById(Long itemId, String username, String newItemName) throws Exception;

    Long getContractIdByItemID(Long itemId) throws Exception;
}
