package com.thoughtworks.rslist.repository;

import com.thoughtworks.rslist.model.po.RsEventPo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface RsEventRepo extends PagingAndSortingRepository<RsEventPo, Integer> {

    List<RsEventPo> findAll();

    List<RsEventPo> findByIdBetween(Integer start, Integer end);

    Page<RsEventPo> findAll(Pageable pageable);
}
