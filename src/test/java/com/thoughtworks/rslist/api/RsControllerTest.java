package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.model.dto.RsEventDto;
import com.thoughtworks.rslist.model.dto.UserDto;
import com.thoughtworks.rslist.repository.RsEventRepo;
import com.thoughtworks.rslist.repository.UserRepo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class RsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper;

    @Autowired
    private RsEventRepo rsEventRepo;

    @Autowired
    private UserRepo userRepo;

    RsControllerTest() {
        objectMapper = new ObjectMapper();
        objectMapper.configure(MapperFeature.USE_ANNOTATIONS, false);
    }

    @BeforeEach
    public void setUp() {
        userRepo.deleteAll();
        rsEventRepo.deleteAll();
    }

    @Test
    public void should_add_event_when_post_event_given_correct_info() throws Exception {
        String userId = post_default_one_user();
        RsEventDto rsEventDto = new RsEventDto("猪肉涨价了", "经济", Integer.parseInt(userId));
        String requestBody = objectMapper.writeValueAsString(rsEventDto);
        mockMvc.perform(post("/rs/event").contentType(MediaType.APPLICATION_JSON).content(requestBody)).andExpect(status().isCreated());
        assertEquals(1, rsEventRepo.findAll().size());
    }

    @Test
    public void should_throw_error_when_post_event_given_dont_exist_user_id() throws Exception {
        RsEventDto rsEventDto = new RsEventDto("猪肉涨价了", "经济", -1);
        String requestBody = objectMapper.writeValueAsString(rsEventDto);
        mockMvc.perform(post("/rs/event").contentType(MediaType.APPLICATION_JSON).content(requestBody)).andExpect(status().isBadRequest());
    }

    @Test
    public void should_get_one_event_when_get_event_given_event_id() throws Exception {
        String userId = post_default_one_user();
        RsEventDto rsEventDto = new RsEventDto("猪肉涨价了", "经济", Integer.parseInt(userId));
        String eventId = post_one_event(rsEventDto);
        mockMvc.perform(get("/rs/" + eventId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.eventName").value("猪肉涨价了"))
            .andExpect(jsonPath("$.keyWord").value("经济"));
    }

    @Test
    public void should_get_all_events_when_get_event_list_given_non_args() throws Exception {
        String userId = post_default_one_user();
        RsEventDto rsEventDto = new RsEventDto("事件一", "无", Integer.parseInt(userId));
        post_one_event(rsEventDto);
        rsEventDto = new RsEventDto("事件二", "无", Integer.parseInt(userId));
        post_one_event(rsEventDto);
        rsEventDto = new RsEventDto("事件三", "无", Integer.parseInt(userId));
        post_one_event(rsEventDto);
        rsEventDto = new RsEventDto("事件四", "无", Integer.parseInt(userId));
        post_one_event(rsEventDto);
        mockMvc.perform(get("/rs/list")).andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(4)));

    }

    @Test
    public void should_get_some_events_when_get_event_list_given_start_and_end_index() throws Exception {
        String userId = post_default_one_user();
        RsEventDto rsEventDto = new RsEventDto("事件一", "无", Integer.parseInt(userId));
        String startId = post_one_event(rsEventDto);
        rsEventDto = new RsEventDto("事件二", "无", Integer.parseInt(userId));
        post_one_event(rsEventDto);
        rsEventDto = new RsEventDto("事件三", "无", Integer.parseInt(userId));
        String endId = post_one_event(rsEventDto);
        rsEventDto = new RsEventDto("事件四", "无", Integer.parseInt(userId));
        post_one_event(rsEventDto);
        mockMvc.perform(get("/rs/list?start=" + startId + "&end=" + endId)).andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(3)));
    }

    @Test
    public void should_delete_one_event_when_delete_event_given_event_id() throws Exception {
        String userId = post_default_one_user();
        RsEventDto rsEventDto = new RsEventDto("事件一", "无", Integer.parseInt(userId));
        String eventId = post_one_event(rsEventDto);
        mockMvc.perform(delete("/rs/" + eventId)).andExpect(status().isOk());
        assertFalse(rsEventRepo.findById(Integer.parseInt(eventId)).isPresent());
    }

    @Test
    public void should_modify_one_event_when_put_event_given_event_dto() throws Exception {
        String userId = post_default_one_user();
        RsEventDto rsEventDto = new RsEventDto("事件一", "无", Integer.parseInt(userId));
        String eventId = post_one_event(rsEventDto);
        rsEventDto = new RsEventDto("事件一改", "无", Integer.parseInt(userId));
        String requestBody = objectMapper.writeValueAsString(rsEventDto);

        mockMvc.perform(put("/rs/" + eventId).contentType(MediaType.APPLICATION_JSON).content(requestBody)).andExpect(status().isOk());
        assertEquals("事件一改", rsEventRepo.findById(Integer.parseInt(eventId)).get().getEventName());
    }

    private String post_default_one_user() throws Exception {
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

    @Test
    void should_throw_error_when_get_single_event_given_wrong_index() throws Exception {
        mockMvc.perform(get("/rs/-1"))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.error").value("invalid index"));
    }

}
