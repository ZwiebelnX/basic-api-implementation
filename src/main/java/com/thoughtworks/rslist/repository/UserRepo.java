package com.thoughtworks.rslist.repository;

import com.thoughtworks.rslist.models.po.UserPO;

import org.springframework.data.repository.CrudRepository;

public interface UserRepo extends CrudRepository<UserPO, Integer> {

    UserPO findFirstByName(String name);
}
