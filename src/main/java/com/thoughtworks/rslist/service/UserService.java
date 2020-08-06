package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.exception.CustomException;
import com.thoughtworks.rslist.model.dto.UserDto;
import com.thoughtworks.rslist.model.po.UserPo;
import com.thoughtworks.rslist.repository.UserRepo;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepo userRepo;

    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public Integer regUserInDatabase(UserDto userDto) {
        UserPo userPO;
        userPO = userRepo.findFirstByName(userDto.getName());
        if (userPO == null) {
            userPO = UserPo.builder()
                .name(userDto.getName())
                .age(userDto.getAge())
                .gender(userDto.getGender())
                .email(userDto.getEmail())
                .phone(userDto.getPhone())
                .build();
            userRepo.save(userPO);
        }

        return userPO.getId();
    }

    public UserDto getUserFromDatabase(Integer id) throws CustomException {
        Optional<UserPo> userPOOptional = userRepo.findById(id);
        if (!userPOOptional.isPresent()) {
            throw new CustomException("invalid user's id");
        }
        UserPo userPO = userPOOptional.get();
        return UserDto.builder()
            .name(userPO.getName())
            .age(userPO.getAge())
            .gender(userPO.getGender())
            .email(userPO.getEmail())
            .phone(userPO.getPhone())
            .build();
    }
}
