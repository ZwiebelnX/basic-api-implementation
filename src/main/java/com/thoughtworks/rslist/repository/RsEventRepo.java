package com.thoughtworks.rslist.repository;

import com.thoughtworks.rslist.model.po.RsEventPo;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RsEventRepo extends CrudRepository<RsEventPo, Integer> {

    List<RsEventPo> findAll();

    List<RsEventPo> findByIdBetween(Integer start, Integer end);
}
