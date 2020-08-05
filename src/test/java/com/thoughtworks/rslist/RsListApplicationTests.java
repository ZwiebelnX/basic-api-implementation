package com.thoughtworks.rslist;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class RsListApplicationTests {

    @Autowired
    MockMvc mockMvc;

    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void contextLoads() throws Exception {
        mockMvc.perform(get("/rs/list"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$[0].eventName").value("第一条事件"))
            .andExpect(jsonPath("$[0].key").value("无"))
            .andExpect(jsonPath("$[1].eventName").value("第二条事件"))
            .andExpect(jsonPath("$[1].key").value("无"))
            .andExpect(jsonPath("$[2].eventName").value("第三条事件"))
            .andExpect(jsonPath("$[2].key").value("无"));
    }

    @Test
    void get_one_rs_event() throws Exception {
        mockMvc.perform(get("/rs/list/1"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.eventName").value("第一条事件"))
            .andExpect(jsonPath("$.key").value("无"));
    }

    @Test
    void get_some_rs_events() throws Exception {
        mockMvc.perform(get("/rs/list?start=1&end=2"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$[0].eventName").value("第一条事件"))
            .andExpect(jsonPath("$[0].key").value("无"))
            .andExpect(jsonPath("$[1].eventName").value("第二条事件"))
            .andExpect(jsonPath("$[1].key").value("无"));
    }

    @Test
    void add_one_event() throws Exception {
        RsEvent rsEvent = new RsEvent("猪肉涨价了", "社会");
        ObjectMapper objectMapper = new ObjectMapper();
        String body = objectMapper.writeValueAsString(rsEvent);
        mockMvc.perform(post("/rs/list").content(body).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
        mockMvc.perform(get("/rs/list/4")).andExpect(status().isOk())
            .andExpect(jsonPath("$.eventName").value("猪肉涨价了"))
            .andExpect(jsonPath("$.key").value("社会"));
    }

    @Test
    void delete_one_event() throws Exception {
        mockMvc.perform(delete("/rs/list/1")).andExpect(status().isOk());
        mockMvc.perform(get("/rs/list/1")).andExpect(status().isOk())
            .andExpect(jsonPath("$.eventName").value("第二条事件"))
            .andExpect(jsonPath("$.key").value("无"));
    }

    @Test
    void modify_one_event() throws Exception {
        RsEvent rsEvent = new RsEvent("猪肉涨价了", "社会");
        ObjectMapper objectMapper = new ObjectMapper();
        String body = objectMapper.writeValueAsString(rsEvent);
        mockMvc.perform(put("/rs/list/1").content(body).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
        mockMvc.perform(get("/rs/list/1")).andExpect(status().isOk())
            .andExpect(jsonPath("$.eventName").value("猪肉涨价了"))
            .andExpect(jsonPath("$.key").value("社会"));
    }

    @Test
    void should_add_user() throws Exception {
        User user = new User("Sicong", "male", 22, "sicong.chen@163.com", "15800000000");
        String requestBody = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user").content(requestBody).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }

}
