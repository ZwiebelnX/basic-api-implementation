package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.model.dto.VoteDto;
import com.thoughtworks.rslist.service.VoteService;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Timestamp;
import java.util.List;

@Controller
public class VoteController {

    private final VoteService voteService;

    public VoteController(VoteService voteService) {
        this.voteService = voteService;
    }

    @GetMapping("/vote")
    public ResponseEntity<List<VoteDto>> getVoteInfo(@RequestParam Timestamp start, @RequestParam Timestamp end) {
        return ResponseEntity.ok(voteService.getVoteInfo(start, end));
    }

}
