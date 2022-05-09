package com.test.market.services.impl;

import com.test.market.model.ItemEntity;
import com.test.market.model.UserEntity;
import com.test.market.model.projections.ItemView;
import com.test.market.model.projections.UserEntityView;
import com.test.market.repository.ItemRepository;
import com.test.market.services.UserEntityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.Assertions;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.lenient;

//https://stackoverflow.com/questions/156503/how-do-you-assert-that-a-certain-exception-is-thrown-in-junit-4-tests
@ExtendWith(MockitoExtension.class)
class ItemServiceImplTest {
    @Mock
    private ItemRepository mockedItemRepository;
    @Mock
    private UserEntityService mockedUserService;

    private ItemServiceImpl itemServiceToTest;

    @BeforeEach
    void init() {
        itemServiceToTest = new ItemServiceImpl(mockedItemRepository, mockedUserService);
    }

    @Test
    void removeItemFromMarket() throws Exception {
        UserEntity user = new UserEntity().setUsername("TestUser");
        ItemEntity item = new ItemEntity();
        item.setOwner(user).setName("TestItem").setId(1L);
        List<ItemEntity> items = new ArrayList<>();
        items.add(item);
        Mockito.when(mockedItemRepository.getItemByIdFetchOwnerEagerly(1L)).thenReturn(item);
        Assertions.assertTrue(itemServiceToTest.removeItemFromMarket(1L, "TestUser"));

        boolean thrown = false;
        String message = "";
        try {
            itemServiceToTest.removeItemFromMarket(1L, "NotOwner");
        } catch (Exception e) {
            thrown = true;
            message = e.getMessage();
        }
        Assertions.assertTrue(thrown);
        Assertions.assertEquals("User is not owner.", message);
    }

    @Test
    void getAllItemsByOwnerId() throws Exception {
        UserEntity user = new UserEntity().setUsername("TestUser");
        ItemEntity item = new ItemEntity();
        item.setOwner(user).setName("TestItem").setId(1L);
        List<ItemEntity> items = new ArrayList<>();
        items.add(item);

        Mockito.when(mockedItemRepository.getAllItemsByOwnerIdEagerly(1L)).thenReturn(items);
        Assertions.assertEquals(1, itemServiceToTest.getAllItemsByOwnerId(1L).size());
        items.add(item);
        Assertions.assertEquals(2, itemServiceToTest.getAllItemsByOwnerId(1L).size());

        boolean thrown = false;
        String message = "Wrong Exception Message";

        try {
            List<ItemEntity> allItemsByOwnerId = itemServiceToTest.getAllItemsByOwnerId(2L);
        } catch (Exception e) {
            thrown = true;
            message = e.getMessage();
        }

        Assertions.assertTrue(thrown);
        Assertions.assertEquals("User does not own items", message);
    }

    @Test
    void getItemByIdFetchOwnerEagerly() throws Exception {
        UserEntity user = new UserEntity().setUsername("TestUser");
        ItemEntity item = new ItemEntity();
        item.setName("TestItem")
                .setOwner(user)
                .setName("TestItem")
                .setId(1L);

        Mockito.when(mockedItemRepository.getItemByIdFetchOwnerEagerly(1L)).thenReturn(item);
        ItemEntity itemByIdFetchOwnerEagerly = itemServiceToTest.getItemByIdFetchOwnerEagerly(1L);

        Assertions.assertEquals(item.getName(), itemByIdFetchOwnerEagerly.getName());
        Assertions.assertEquals(item.getOwner().getUsername(), itemByIdFetchOwnerEagerly.getOwner().getUsername());

        boolean thrown = false;
        String message = "Wrong Exception Message";

        try {
            itemServiceToTest.getItemByIdFetchOwnerEagerly(2L);
        } catch (Exception e) {
            thrown = true;
            message = e.getMessage();
        }

        Assertions.assertTrue(thrown);
        Assertions.assertEquals("Item does not exist.", message);
    }

    @Test
    void getAllItems() {
        UserEntity user = new UserEntity().setUsername("TestUser");
        ItemEntity item = new ItemEntity();
        item.setOwner(user).setName("TestItem").setId(1L);
        List<ItemView> items = new ArrayList<>();
        items.add(new ItemView() {
            @Override
            public String getName() {
                return "TestItem";
            }

            @Override
            public UserEntityView getOwner() {
                return () -> "TestUser";
            }
        });
        items.add(new ItemView() {
            @Override
            public String getName() {
                return "TestItem2";
            }

            @Override
            public UserEntityView getOwner() {
                return null;
            }
        });

        Mockito.when(mockedItemRepository.getAllItems()).thenReturn(items);

        Assertions.assertEquals("TestItem", itemServiceToTest.getAllItems().get(0).getName());
        Assertions.assertEquals("TestUser", itemServiceToTest.getAllItems().get(0).getOwner().getUsername());
        Assertions.assertEquals(2, itemServiceToTest.getAllItems().size());
    }

    @Test
    void getItemsByUserId() throws Exception {

        UserEntity owner1 = new UserEntity();
        owner1.setId(1L);
        owner1.setUsername("TestUser1");

        ItemView item1 = new ItemView() {
            @Override
            public String getName() {
                return "TestItem1";
            }

            @Override
            public UserEntityView getOwner() {
                return () -> owner1.getUsername();
            }
        };
        ItemView item2 = new ItemView() {
            @Override
            public String getName() {
                return "TestItem2";
            }

            @Override
            public UserEntityView getOwner() {
                return () -> owner1.getUsername();
            }
        };

        Mockito.when(mockedItemRepository.getItemViewByUserId(1L)).thenReturn(List.of(item1, item2));
        List<ItemView> itemsByUserId = itemServiceToTest.getItemsByUserId(1L);

        Assertions.assertEquals(2, itemsByUserId.size());
        Assertions.assertEquals(owner1.getUsername(), itemsByUserId.get(0).getOwner().getUsername());
        Assertions.assertEquals(owner1.getUsername(), itemsByUserId.get(1).getOwner().getUsername());
        Assertions.assertEquals(item1.getName(), itemsByUserId.get(0).getName());
        Assertions.assertEquals(item2.getName(), itemsByUserId.get(1).getName());

        boolean thrown = false;
        String message = "Wrong Exception Message";

        try {
            itemServiceToTest.getItemsByUserId(3L);
        } catch (Exception e) {
            thrown = true;
            message = e.getMessage();
        }

        Assertions.assertTrue(thrown);
        Assertions.assertEquals("User does not own items.", message);
    }


    @Test
    void editItemNameById() throws Exception {
        UserEntity user = new UserEntity();
        user.setUsername("TestUser");
        UserEntity user2 = new UserEntity();
        user2.setUsername("TestUser2");
        ItemEntity item = new ItemEntity();
        item.setName("TestItem").setOwner(user).setId(1L);

        lenient().when(mockedUserService.getUserByUsername("TestUser")).thenReturn(user);
        Mockito.when(mockedUserService.getUserByUsername("TestUser2")).thenReturn(user2);
        Mockito.when(mockedItemRepository.getItemById(1L)).thenReturn(item);

        boolean thrown = false;
        String message = "WrongMessage";

        try {
            itemServiceToTest.editItemNameById(1L, "TestUser2", "NewItemName");
        } catch (Exception e) {
            thrown = true;
            message = e.getMessage();
        }

        Assertions.assertTrue(thrown);
        Assertions.assertEquals("User does not own item.", message);

        ItemView itemView = itemServiceToTest.editItemNameById(1L, "TestUser", "NewItemName");
        Assertions.assertEquals("NewItemName", itemView.getName());
        Assertions.assertEquals("TestUser", itemView.getOwner().getUsername());
    }
}