package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.User;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Set;

import javax.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {

    private static final Set<User> userSet = new HashSet<>();

    @PostMapping("")
    public void addUser(@RequestBody @Valid User user) {
        userSet.add(user);
    }

    public static void registerUser(User user) {
        userSet.add(user);
    }
}
