package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.exception.CustomException;
import com.thoughtworks.rslist.model.dto.UserDto;
import com.thoughtworks.rslist.service.UserService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

@RestController
public class UserController {

    private static final List<UserDto> USER_DTO_LIST = new ArrayList<>();

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> getUser() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PostMapping("/user")
    public ResponseEntity<String> regUserInDatabase(@RequestBody @Valid UserDto userDto) {
        Integer index = userService.createUser(userDto);
        return ResponseEntity.created(URI.create("")).header("index", String.valueOf(index)).build();
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<UserDto> getUserFromDatabase(@PathVariable Integer id) throws CustomException {
        UserDto userDto = userService.getUser(id);
        return ResponseEntity.ok(userDto);
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<String> deleteUserFromDatabase(@PathVariable Integer id) {
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }
}
