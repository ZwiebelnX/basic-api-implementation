package com.thoughtworks.rslist;

import com.thoughtworks.rslist.api.RsController;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.hasKey;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class RsListApplicationTests {

    @Autowired
    MockMvc mockMvc;

    String requestBody;

    RsListApplicationTests() {
        requestBody = "{\"eventName\":\"猪肉涨价了\",\"keyWord\":\"经济\",\"user\": {\"name\":\"xyxia\",\"age\": 19,\"gender\": \"male\",\"email\": \"a@b.com\",\"phone\": \"18888888888\"}}";
    }

    @BeforeEach
    public void setUp() {
        RsController.initRsList();
    }

    @Test
    void contextLoads() throws Exception {
        mockMvc.perform(get("/rs/list"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$[0].eventName").value("第一条事件"))
            .andExpect(jsonPath("$[0].keyWord").value("无"))
            .andExpect(jsonPath("$[1].eventName").value("第二条事件"))
            .andExpect(jsonPath("$[1].keyWord").value("无"))
            .andExpect(jsonPath("$[2].eventName").value("第三条事件"))
            .andExpect(jsonPath("$[2].keyWord").value("无"));
    }

    @Test
    void get_one_rs_event() throws Exception {
        mockMvc.perform(get("/rs/list/1"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.eventName").value("第一条事件"))
            .andExpect(jsonPath("$.keyWord").value("无"));
    }

    @Test
    void get_some_rs_events() throws Exception {
        mockMvc.perform(get("/rs/list?start=1&end=2"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$[0].eventName").value("第一条事件"))
            .andExpect(jsonPath("$[0].keyWord").value("无"))
            .andExpect(jsonPath("$[1].eventName").value("第二条事件"))
            .andExpect(jsonPath("$[1].keyWord").value("无"));
    }

    @Test
    void add_one_event() throws Exception {
        mockMvc.perform(post("/rs/list").content(requestBody).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());
        mockMvc.perform(get("/rs/list/4"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.eventName").value("猪肉涨价了"))
            .andExpect(jsonPath("$.keyWord").value("经济"));
    }

    @Test
    void delete_one_event() throws Exception {
        mockMvc.perform(delete("/rs/list/1")).andExpect(status().isOk());
        mockMvc.perform(get("/rs/list/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.eventName").value("第二条事件"))
            .andExpect(jsonPath("$.keyWord").value("无"));
    }

    @Test
    void modify_one_event() throws Exception {
        mockMvc.perform(put("/rs/list/1").content(requestBody).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
        mockMvc.perform(get("/rs/list/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.eventName").value("猪肉涨价了"))
            .andExpect(jsonPath("$.keyWord").value("经济"));
    }

    @Test
    void should_add_user() throws Exception {
        mockMvc.perform(post("/user").content("{\"name\":\"xyxia\",\"age\": 19,\"gender\": \"male\",\"email\": \"a@b.com\",\"phone\": \"18888888888\"}").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }

    @Test
    void should_get_event_info_but_not_include_user_info() throws Exception {
        mockMvc.perform(get("/rs/list/1"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$", not(hasKey("user"))))
            .andExpect(jsonPath("$.eventName").value("第一条事件"))
            .andExpect(jsonPath("$.keyWord").value("无"));
    }

}
