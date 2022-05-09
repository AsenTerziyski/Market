package com.test.market.services.impl;

import com.test.market.model.ItemEntity;
import com.test.market.model.UserEntity;
import com.test.market.model.projections.ItemView;
import com.test.market.model.projections.UserEntityView;
import com.test.market.repository.ItemRepository;
import com.test.market.services.ItemService;
import com.test.market.services.UserEntityService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.test.market.services.impl.ContractServiceImpl.getItemView;

@Service
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserEntityService userEntityService;


    public ItemServiceImpl(ItemRepository itemRepository, UserEntityService userEntityService) {
        this.itemRepository = itemRepository;
        this.userEntityService = userEntityService;
    }

    @Override
    public ItemEntity addItemToMarket(String item, String username) throws Exception {
        UserEntity userByUsername = userEntityService.getUserByUsername(username);
        ItemEntity itm = new ItemEntity();
        itm.setName(item).setOwner(userByUsername);
        return itemRepository.save(itm);
    }

    @Override
    @Transactional
    public boolean removeItemFromMarket(Long id, String owner) throws Exception {
        ItemEntity itemByIdFetchOwnerEagerly = itemRepository.getItemByIdFetchOwnerEagerly(id);

        if (itemByIdFetchOwnerEagerly == null) {
            throw new Exception("Item does not exist.");
        }

        if (!itemByIdFetchOwnerEagerly.getOwner().getUsername().equals(owner)) {
            throw new Exception("User is not owner.");
        }

        this.itemRepository.removeItemById(id);
        return true;
    }

    @Override
    public List<ItemEntity> getAllItemsByOwnerId(Long id) throws Exception {
        List<ItemEntity> allItemsByOwnerId = itemRepository.getAllItemsByOwnerIdEagerly(id);

        if (allItemsByOwnerId.isEmpty()) {
            throw new Exception("User does not own items");
        }

        return allItemsByOwnerId;
    }

    @Override
    public ItemEntity getItemByIdFetchOwnerEagerly(long itemId) throws Exception {
        ItemEntity item = itemRepository.getItemByIdFetchOwnerEagerly(itemId);
        if (item == null) {
            throw new Exception("Item does not exist.");
        }

        return item;
    }

    @Override
    public List<ItemView> getAllItems() {
        return itemRepository.getAllItems();
    }

    @Override
    public List<ItemView> getItemsByUserId(Long userId) throws Exception {
        List<ItemView> items = itemRepository.getItemViewByUserId(userId);
        if (items.isEmpty()) {
            throw new Exception("User does not own items.");
        }
        return items;
    }

    @Override
    public ItemView addItemViewToMarket(String name, String username) throws Exception {
        UserEntity userByUsername = userEntityService.getUserByUsername(username);
        ItemEntity item = new ItemEntity().setName(name).setOwner(userByUsername);
        ItemEntity saved = itemRepository.save(item);
        return getItemView(username, saved);
    }

    @Override
    public ItemView editItemNameById(Long itemId, String username, String newItemName) throws Exception {
        UserEntity userByUsername = userEntityService.getUserByUsername(username);
        ItemEntity itemById = itemRepository.getItemById(itemId);
        if (!userByUsername.getUsername().equals(itemById.getOwner().getUsername())) {
            throw new Exception("User does not own item.");
        }
        itemById.setName(newItemName);
        ItemEntity edited = itemRepository.save(itemById);

        return new ItemView() {
            @Override
            public String getName() {
                return itemById.getName();
            }

            @Override
            public UserEntityView getOwner() {
                return () -> userByUsername.getUsername();
            }
        };
    }

    @Override
    public Long getContractIdByItemID(Long itemId) throws Exception {

        Long contractId = -1L;

        try {
        ItemEntity contractIdByItemId = itemRepository.getContractIdByItemId(itemId);
        contractId = contractIdByItemId.getContractEntity().getId();
        } catch (Exception e) {
        }

        return contractId;
    }


}
