package com.test.market.services.impl;

import com.test.market.model.UserEntity;

import com.test.market.model.dto.UserDto;
import com.test.market.model.projections.UserEntityView;
import com.test.market.repository.UserRepository;
import com.test.market.services.UserEntityService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
public class UserEntityServiceImpl implements UserEntityService {
    private final UserRepository userRepository;

    public UserEntityServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserEntity registerUser(UserEntity user) throws Exception {
        Optional<UserEntity> userByUsername = userRepository.findExistingUserByUsername(user.getUsername());

        if (!userByUsername.isEmpty()) {
            throw new Exception("Username occupied.");
        }

        return userRepository.save(user);
    }

    @Transactional
    @Override
    public boolean removeUser(Long id) throws Exception {
        Optional<UserEntity> userById = userRepository.findUserById(id);
        if (userById.isEmpty()) {
            throw new Exception("User does not exist.");
        }

        userRepository.removeUserFromDB(id);
        return true;
    }

    @Override
    public UserEntity getUserByUsername(String username) throws Exception {
        Optional<UserEntity> userByUsername = userRepository.findExistingUserByUsername(username);

        if (userByUsername.isEmpty()) {
            throw new Exception("User does not exist.");
        }

        return userByUsername.get();
    }

    @Override
    public UserEntity getUserByIdFetchItemsEagerly(Long userId) throws Exception {
        UserEntity user = userRepository.findByIdAndFetchItemsEagerly(userId);
        if (user == null) {
            throw new Exception("User does not exist.");
        }

        return user;
    }

    @Override
    public List<UserEntityView> getAllRegisteredUsers() {
        return userRepository.getAllRegisteredUsers();
    }

    @Override
    public UserEntityView registerUserAndReturnUserView(UserDto userDto) throws Exception {
        System.out.println();
        Optional<UserEntity> userByUsername = userRepository.findExistingUserByUsername(userDto.getUsername());
        if (userByUsername.isPresent()) {
            throw new Exception("Username occupied.");
        }

        UserEntity saved = userRepository.save(new UserEntity().setUsername(userDto.getUsername()));
        return () -> saved.getUsername();
    }

    @Override
    public UserDto getUserById(Long sellerId) throws Exception {
        Optional<UserEntity> userById = userRepository.findUserById(sellerId);
        if (userById.isEmpty()) {
            throw new Exception("User does not exist.");
        }
        return null;
    }

}
