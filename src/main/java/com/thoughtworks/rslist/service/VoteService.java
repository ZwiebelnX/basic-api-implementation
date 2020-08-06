package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.exception.CustomException;
import com.thoughtworks.rslist.model.dto.VoteDto;
import com.thoughtworks.rslist.model.po.RsEventPo;
import com.thoughtworks.rslist.model.po.UserPo;
import com.thoughtworks.rslist.model.po.VotePo;
import com.thoughtworks.rslist.repository.VoteRepo;

import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
public class VoteService {

    private final VoteRepo voteRepo;

    private final UserService userService;

    private final RsEventService rsEventService;

    public VoteService(VoteRepo voteRepo, UserService userService, RsEventService rsEventService) {
        this.voteRepo = voteRepo;
        this.rsEventService = rsEventService;
        this.userService = userService;
    }

    public void createVote(Integer RsEventId, Integer userId, Integer voteNum, Timestamp voteTime) throws CustomException {
        RsEventPo rsEventPo = rsEventService.getRsEventPo(RsEventId);
        UserPo userPo = userService.getUserPo(userId);
        VotePo votePo = VotePo.builder().voteNum(voteNum).rsEventPo(rsEventPo).userPo(userPo).voteTime(voteTime).build();
        voteRepo.save(votePo);
    }

    public List<VoteDto> getVoteInfo(Timestamp start, Timestamp end) {
        List<VoteDto> result = new ArrayList<>();
        List<VotePo> votePoList = voteRepo.findByVoteTimeBetween(start, end);
        for (VotePo votePo : votePoList) {
            VoteDto voteDto = VoteDto.builder().userId(votePo.getUserPo().getId()).rsEventId(votePo.getRsEventPo().getId()).build();
            result.add(voteDto);
        }
        return result;
    }
}
