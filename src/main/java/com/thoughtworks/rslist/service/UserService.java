package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.exception.CustomException;
import com.thoughtworks.rslist.models.po.UserPO;
import com.thoughtworks.rslist.repository.UserRepo;

import org.springframework.stereotype.Service;

import java.util.Optional;

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

    public User getUserFromDatabase(Integer id) throws CustomException {
        Optional<UserPO> userPOOptional = userRepo.findById(id);
        if (!userPOOptional.isPresent()) {
            throw new CustomException("invalid user's id");
        }
        UserPO userPO = userPOOptional.get();
        return User.builder()
            .name(userPO.getName())
            .age(userPO.getAge())
            .gender(userPO.getGender())
            .email(userPO.getEmail())
            .phone(userPO.getPhone())
            .build();
    }
}
