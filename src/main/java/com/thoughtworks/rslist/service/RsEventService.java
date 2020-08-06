package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.exception.CustomException;
import com.thoughtworks.rslist.model.dto.RsEventDto;
import com.thoughtworks.rslist.model.po.RsEventPo;
import com.thoughtworks.rslist.repository.RsEventRepo;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

@Service
public class RsEventService {

    private final RsEventRepo rsEventRepo;

    public RsEventService(RsEventRepo rsEventRepo) {
        this.rsEventRepo = rsEventRepo;
    }

    public List<RsEventDto> getList(Integer start, Integer end) {
        List<RsEventPo> rsEventPoList;
        if (start == null || end == null) {
            rsEventPoList = rsEventRepo.findAll();
        } else {
            rsEventPoList = rsEventRepo.findByIdBetween(start, end);
        }
        List<RsEventDto> rsEventDtoList = new ArrayList<>();
        for (RsEventPo rsEventPo : rsEventPoList) {
            RsEventDto rsEventDto = RsEventDto.builder().eventName(rsEventPo.getEventName()).keyWord(rsEventPo.getKeyWord()).build();
            rsEventDtoList.add(rsEventDto);
        }
        return rsEventDtoList;
    }

    public RsEventDto getEvent(Integer id) throws CustomException {
        Optional<RsEventPo> rsEventPoOptional = rsEventRepo.findById(id);
        if (!rsEventPoOptional.isPresent()) {
            throw new CustomException("user not exist!");
        }
        RsEventPo rsEventPo = rsEventPoOptional.get();
        return RsEventDto.builder().eventName(rsEventPo.getEventName()).keyWord(rsEventPo.getKeyWord()).build();
    }

    public Integer createRsEvent(RsEventDto rsEventDto) {
        RsEventPo rsEventPo = RsEventPo.builder().eventName(rsEventDto.getEventName()).keyWord(rsEventDto.getKeyWord()).build();
        rsEventRepo.save(rsEventPo);
        return rsEventPo.getId();
    }

    public void modifyEvent(int id, RsEventDto rsEventDto) throws CustomException {
        Optional<RsEventPo> rsEventPoOptional = rsEventRepo.findById(id);
        if (!rsEventPoOptional.isPresent()) {
            throw new CustomException("user not exist!");
        }
        RsEventPo rsEventPo = rsEventPoOptional.get();
        if (!rsEventDto.getEventName().isEmpty()) {
            rsEventPo.setEventName(rsEventDto.getEventName());
        }
        if (!rsEventDto.getKeyWord().isEmpty()) {
            rsEventPo.setKeyWord(rsEventDto.getKeyWord());
        }
        rsEventRepo.save(rsEventPo);
    }

    @Transactional
    public void deleteEvent(int id) {
        rsEventRepo.deleteById(id);
    }
}
