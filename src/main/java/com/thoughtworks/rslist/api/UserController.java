package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.service.UserService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

@RestController
public class UserController {

    private static final List<User> userList = new ArrayList<>();

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/user")
    public ResponseEntity<String> addUser(@RequestBody @Valid User user) {
        int index = registerUser(user);
        return ResponseEntity.created(URI.create("")).header("index", String.valueOf(index)).build();
    }

    @PostMapping("/user/reg")
    public ResponseEntity<String> regUserInDatabase(@RequestBody @Valid User user) {
        Integer index = userService.regUserInDatabase(user);
        return ResponseEntity.created(URI.create("")).header("index", String.valueOf(index)).build();
    }

    public static int registerUser(User user) {
        if (!userList.contains(user)) {
            userList.add(user);
        }
        return userList.indexOf(user);
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getUser() {
        return ResponseEntity.ok(userList);
    }
}
