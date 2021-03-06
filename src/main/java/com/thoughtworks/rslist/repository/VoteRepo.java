package com.thoughtworks.rslist.repository;

import com.thoughtworks.rslist.model.po.VotePo;

import org.springframework.data.repository.CrudRepository;

import java.sql.Timestamp;
import java.util.List;

public interface VoteRepo extends CrudRepository<VotePo, Integer> {

    List<VotePo> findAll();

    List<VotePo> findByVoteTimeBetween(Timestamp start, Timestamp end);
}
