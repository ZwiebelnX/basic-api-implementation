package com.thoughtworks.rslist.repository;

import com.thoughtworks.rslist.model.po.UserPo;

import org.springframework.data.repository.CrudRepository;

public interface UserRepo extends CrudRepository<UserPo, Integer> {

    UserPo findFirstByName(String name);
}
