package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.exception.CustomException;
import com.thoughtworks.rslist.model.dto.UserDto;
import com.thoughtworks.rslist.model.po.UserPo;
import com.thoughtworks.rslist.repository.UserRepo;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

@Service
public class UserService {

    private final UserRepo userRepo;

    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public List<UserDto> getAllUsers() {
        List<UserDto> userDtoList = new ArrayList<>();
        for (UserPo userPo : userRepo.findAll()) {
            UserDto userDto = UserDto.builder()
                .name(userPo.getName())
                .age(userPo.getAge())
                .gender(userPo.getGender())
                .email(userPo.getEmail())
                .phone(userPo.getPhone())
                .build();
            userDtoList.add(userDto);
        }
        return userDtoList;
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
        UserPo userPo = userPOOptional.get();
        return UserDto.builder()
            .name(userPo.getName())
            .age(userPo.getAge())
            .gender(userPo.getGender())
            .email(userPo.getEmail())
            .phone(userPo.getPhone())
            .build();
    }

    @Transactional
    public void deleteUser(Integer id) {
        userRepo.deleteById(id);
    }
}
