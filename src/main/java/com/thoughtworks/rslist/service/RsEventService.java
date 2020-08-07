package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.exception.CustomException;
import com.thoughtworks.rslist.model.dto.RsEventDto;
import com.thoughtworks.rslist.model.dto.VoteDto;
import com.thoughtworks.rslist.model.po.RsEventPo;
import com.thoughtworks.rslist.model.po.UserPo;
import com.thoughtworks.rslist.model.po.VotePo;
import com.thoughtworks.rslist.repository.RsEventRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

@Service
public class RsEventService {

    private final RsEventRepo rsEventRepo;

    private UserService userService;

    private VoteService voteService;

    public RsEventService(RsEventRepo rsEventRepo) {
        this.rsEventRepo = rsEventRepo;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setVoteService(VoteService voteService) {
        this.voteService = voteService;
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
            throw new CustomException("invalid index");
        }
        RsEventPo rsEventPo = rsEventPoOptional.get();
        int voteNum = 0;
        for (VotePo votePo : rsEventPo.getVotePoList()) {
            voteNum += votePo.getVoteNum();
        }
        return RsEventDto.builder()
            .eventName(rsEventPo.getEventName())
            .keyWord(rsEventPo.getKeyWord())
            .id(rsEventPo.getId())
            .voteNum(voteNum)
            .build();
    }

    public Integer createRsEvent(RsEventDto rsEventDto) throws CustomException {
        UserPo userPo = userService.getUserPo(rsEventDto.getUserId());
        RsEventPo rsEventPo = RsEventPo.builder().eventName(rsEventDto.getEventName()).keyWord(rsEventDto.getKeyWord()).userPo(userPo).build();
        rsEventRepo.save(rsEventPo);
        return rsEventPo.getId();
    }

    public void modifyEvent(int id, RsEventDto rsEventDto) throws CustomException {
        RsEventPo rsEventPo = getRsEventPo(id);
        UserPo userPo = userService.getUserPo(rsEventDto.getUserId());
        if (userPo != rsEventPo.getUserPo()) {
            throw new CustomException("user not match");
        }
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

    public void voteForRsEvent(Integer rsEventId, VoteDto voteDto) throws CustomException {
        userService.deductVoteNum(voteDto.getUserId(), voteDto.getVoteNum());
        voteService.createVote(rsEventId, voteDto.getUserId(), voteDto.getVoteNum(), voteDto.getVoteTime());
    }

    public RsEventPo getRsEventPo(Integer id) throws CustomException {
        Optional<RsEventPo> rsEventPoOptional = rsEventRepo.findById(id);
        if (!rsEventPoOptional.isPresent()) {
            throw new CustomException("rs event not exist");
        }
        return rsEventPoOptional.get();
    }

    public List<RsEventDto> getPageList(int page, Integer pageSize) {
        List<RsEventDto> rsEventDtoList = new ArrayList<>();
        Page<RsEventPo> rsEventPoList = rsEventRepo.findAll(PageRequest.of(page - 1, pageSize));
        for (RsEventPo rsEventPo : rsEventPoList) {
            RsEventDto rsEventDto = RsEventDto.builder()
                .id(rsEventPo.getId())
                .keyWord(rsEventPo.getKeyWord())
                .eventName(rsEventPo.getEventName())
                .voteNum(0)
                .build();
            for (VotePo votePo : rsEventPo.getVotePoList()) {
                rsEventDto.setVoteNum(rsEventDto.getVoteNum() + votePo.getVoteNum());
            }
            rsEventDtoList.add(rsEventDto);
        }
        return rsEventDtoList;
    }
}
