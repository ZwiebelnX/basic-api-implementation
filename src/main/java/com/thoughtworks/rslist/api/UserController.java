package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.User;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {

    private List<User> userList = new ArrayList<>();

    @PostMapping("")
    public void addUser(@RequestBody @Valid User user) {
        userList.add(user);
    }

}
