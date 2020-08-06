package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.models.po.UserPO;
import com.thoughtworks.rslist.repository.UserRepo;

import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepo userRepo;

    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public Integer regUserInDatabase(User user) {
        UserPO userPO;
        userPO = userRepo.findFirstByName(user.getName());
        if (userPO == null) {
            userPO = UserPO.builder()
                .name(user.getName())
                .age(user.getAge())
                .gender(user.getGender())
                .email(user.getEmail())
                .phone(user.getPhone())
                .build();
            userRepo.save(userPO);
        }

        return userPO.getId();
    }
}
