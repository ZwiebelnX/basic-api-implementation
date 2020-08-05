package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.User;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {

    private static final List<User> userList = new ArrayList<>();

    @PostMapping("")
    public ResponseEntity<String> addUser(@RequestBody @Valid User user) {
        int index = registerUser(user);
        return ResponseEntity.created(URI.create("")).header("index", String.valueOf(index)).build();
    }

    public static int registerUser(User user) {
        if (!userList.contains(user)) {
            userList.add(user);
        }
        return userList.indexOf(user);
    }
}
