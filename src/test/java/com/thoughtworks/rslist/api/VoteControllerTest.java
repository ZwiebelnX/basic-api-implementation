package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.model.dto.RsEventDto;
import com.thoughtworks.rslist.model.dto.UserDto;
import com.thoughtworks.rslist.model.dto.VoteDto;
import com.thoughtworks.rslist.repository.RsEventRepo;
import com.thoughtworks.rslist.repository.UserRepo;
import com.thoughtworks.rslist.repository.VoteRepo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Timestamp;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class VoteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RsEventRepo rsEventRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private VoteRepo voteRepo;

    private final ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        rsEventRepo.deleteAll();
        userRepo.deleteAll();
        voteRepo.deleteAll();
    }

    public VoteControllerTest() {
        objectMapper = new ObjectMapper();
        objectMapper.configure(MapperFeature.USE_ANNOTATIONS, false);
    }

    @Test
    public void should_get_all_vote_when_get_vote_given_start_and_end_time() throws Exception {
        Timestamp testStartTime = new Timestamp(System.currentTimeMillis());
        String userId = post_one_user();
        RsEventDto rsEventDto = new RsEventDto("事件一", "无", Integer.parseInt(userId));
        String rsEventIdOne = post_one_event(rsEventDto);
        rsEventDto = new RsEventDto("事件二", "无", Integer.parseInt(userId));
        post_one_event(rsEventDto);
        rsEventDto = new RsEventDto("事件三", "无", Integer.parseInt(userId));
        String rsEventIdTwo = post_one_event(rsEventDto);
        rsEventDto = new RsEventDto("事件四", "无", Integer.parseInt(userId));
        post_one_event(rsEventDto);

        VoteDto voteDto = VoteDto.builder().userId(Integer.parseInt(userId)).voteNum(2).voteTime(new Timestamp(System.currentTimeMillis())).build();
        String requestBody = objectMapper.writeValueAsString(voteDto);
        mockMvc.perform(post("/rs/vote/" + rsEventIdOne).contentType(MediaType.APPLICATION_JSON).content(requestBody)).andExpect(status().isOk());
        voteDto = VoteDto.builder().userId(Integer.parseInt(userId)).voteNum(4).voteTime(new Timestamp(System.currentTimeMillis())).build();
        requestBody = objectMapper.writeValueAsString(voteDto);
        mockMvc.perform(post("/rs/vote/" + rsEventIdTwo).contentType(MediaType.APPLICATION_JSON).content(requestBody)).andExpect(status().isOk());

        Timestamp testEndTime = new Timestamp(System.currentTimeMillis());

        mockMvc.perform(get("/vote?start=" + testStartTime + "&end=" + testEndTime)).andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(2)));
    }

    private String post_one_user() throws Exception {
        UserDto userDto = new UserDto("onion", "male", 22, "onion@thoughtworks.com", "18100000000");
        String requestBody = objectMapper.writeValueAsString(userDto);
        return mockMvc.perform(post("/user").contentType(MediaType.APPLICATION_JSON).content(requestBody))
            .andExpect(status().isCreated())
            .andReturn()
            .getResponse()
            .getHeader("index");
    }

    private String post_one_event(RsEventDto rsEventDto) throws Exception {
        String requestBody = objectMapper.writeValueAsString(rsEventDto);
        return mockMvc.perform(post("/rs/event").contentType(MediaType.APPLICATION_JSON).content(requestBody))
            .andExpect(status().isCreated())
            .andReturn()
            .getResponse()
            .getHeader("index");
    }
}
