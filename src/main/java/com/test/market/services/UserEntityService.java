package com.test.market.services;

import com.test.market.model.ItemEntity;
import com.test.market.model.UserEntity;
import com.test.market.model.dto.UserDto;
import com.test.market.model.projections.UserEntityView;


import java.util.List;

public interface UserEntityService {

    UserEntity registerUser(UserEntity user) throws Exception;

    boolean removeUser(Long id) throws Exception;

    UserEntity getUserByUsername(String username) throws Exception;

    UserEntity getUserByIdFetchItemsEagerly(Long userId) throws Exception;

    List<UserEntityView> getAllRegisteredUsers();

    UserEntityView registerUserAndReturnUserView(UserDto user) throws Exception;

    UserDto getUserById(Long sellerId) throws Exception;
}
