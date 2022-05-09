package com.test.market.web;

import com.test.market.model.ItemEntity;
import com.test.market.model.UserEntity;
import com.test.market.model.dto.UserDto;
import com.test.market.model.projections.UserEntityView;
import com.test.market.services.UserEntityService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/market/users")
public class UserController {
    private final UserEntityService userEntityService;

    public UserController(UserEntityService userEntityService) {
        this.userEntityService = userEntityService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<UserEntityView> getAllRegisteredUsers() {
        return userEntityService.getAllRegisteredUsers();
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public UserEntityView registerUser(@RequestBody UserDto user) throws Exception {
        return userEntityService.registerUserAndReturnUserView(user);
    }

    @RequestMapping(value = "/delete/{userId}", method = RequestMethod.DELETE)
    public String deleteUser(@PathVariable Long userId) throws Exception {
        userEntityService.removeUser(userId);
        return String.format("Deleted user with id: %d", userId);
    }

}
