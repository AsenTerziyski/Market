package com.test.market.services.impl;

import com.test.market.model.ItemEntity;
import com.test.market.model.UserEntity;
import com.test.market.repository.UserRepository;
import com.test.market.services.UserEntityService;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
class UserEntityServiceImplTest {
    @Mock
    private UserRepository mockedUserRepository;
    private UserEntityService userEntityServiceToTest;

    @BeforeEach
    void init() {
        userEntityServiceToTest = new UserEntityServiceImpl(mockedUserRepository);
    }

    @Test
    void registerUser() throws Exception {
        UserEntity user = new UserEntity();
        user.setUsername("TestUser");

        UserEntity notExistingUser = new UserEntity();
        notExistingUser.setUsername("NotExistingUser");
        Mockito
                .when(mockedUserRepository.findExistingUserByUsername("TestUser"))
                .thenReturn(Optional.of(user));

        boolean thrown = false;
        String message = "Wrong message";

        try {
            userEntityServiceToTest.registerUser(user);
        } catch (Exception e) {
            thrown = true;
            message = e.getMessage();
        }

        Assertions.assertTrue(thrown);
        Assertions.assertEquals("Username occupied.", message);
    }

    @Test
    void removeUser() throws Exception {
        UserEntity user = new UserEntity();
        user.setId(1L);
        lenient()
                .when(mockedUserRepository.findUserById(1L))
                .thenReturn(Optional.of(user));

        String message = "WrongMessage";
        boolean thrown = false;

        try {
            userEntityServiceToTest.removeUser(2L);
        } catch (Exception e) {
            thrown = true;
            message = e.getMessage();
        }

        Assertions.assertEquals("User does not exist.", message);
        Assertions.assertTrue(thrown);
        Assertions.assertTrue(userEntityServiceToTest.removeUser(1L));
    }

    @Test
    void getUserByUsername() throws Exception {
        UserEntity user = new UserEntity();
        user.setUsername("TestUser").setId(1L);

        lenient()
                .when(mockedUserRepository.findExistingUserByUsername("TestUser"))
                .thenReturn(Optional.of(user));

        String message = "WrongMessage";
        boolean thrown = false;

        try {
            userEntityServiceToTest.getUserByUsername("NotExistingUser");
        } catch (Exception e) {
            thrown = true;
            message = e.getMessage();
        }

        Assertions.assertEquals("User does not exist.", message);
        Assertions.assertTrue(thrown);

        UserEntity testUser = userEntityServiceToTest.getUserByUsername("TestUser");
        assertEquals(1L, testUser.getId());
    }

    @Test
    void getUserByIdFetchItemsEagerly() throws Exception {
        UserEntity user = new UserEntity();
        user.setId(1L);
        ItemEntity item1 = new ItemEntity().setOwner(user).setName("TestItem1");
        ItemEntity item2 = new ItemEntity().setOwner(user).setName("TestItem2");
        List<ItemEntity> items = List.of(item1, item2);
        user.setItems(items);

        lenient()
                .when(mockedUserRepository.findByIdAndFetchItemsEagerly(1L))
                .thenReturn(user);

        String message = "WrongMessage";
        boolean thrown = false;
        try {
            userEntityServiceToTest.getUserByIdFetchItemsEagerly(100L);
        } catch (Exception e) {
            thrown = true;
            message = e.getMessage();
        }

        Assertions.assertTrue(thrown);
        Assertions.assertEquals("User does not exist.", message);

        UserEntity foundUser = userEntityServiceToTest.getUserByIdFetchItemsEagerly(1L);
        List<ItemEntity> foundUserItems = foundUser.getItems();
        Assertions.assertEquals(2, foundUserItems.size());
        Assertions.assertEquals(1L, foundUser.getId());
        for (int i = 0; i < items.size(); i++) {
            Assertions.assertEquals(items.get(i).getName(), foundUserItems.get(i).getName());
        }
    }

}