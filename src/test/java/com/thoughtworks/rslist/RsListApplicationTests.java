package com.thoughtworks.rslist;

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

    @Test
    void contextLoads() throws Exception {
        mockMvc.perform(get("/rs/list"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$[0].name").value("第一条事件"))
            .andExpect(jsonPath("$[0].key").value("无"))
            .andExpect(jsonPath("$[1].name").value("第二条事件"))
            .andExpect(jsonPath("$[1].key").value("无"))
            .andExpect(jsonPath("$[2].name").value("第三条事件"))
            .andExpect(jsonPath("$[2].key").value("无"));
    }

    @Test
    void get_one_rs_event() throws Exception {
        mockMvc.perform(get("/rs/list/1"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.name").value("第一条事件"))
            .andExpect(jsonPath("$.key").value("无"));
    }

    @Test
    void get_some_rs_events() throws Exception {
        mockMvc.perform(get("/rs/list?start=1&end=2"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$[0].name").value("第一条事件"))
            .andExpect(jsonPath("$[0].key").value("无"))
            .andExpect(jsonPath("$[1].name").value("第二条事件"))
            .andExpect(jsonPath("$[1].key").value("无"));
    }

    @Test
    void add_one_event() throws Exception {
        mockMvc.perform(post("/rs/list").content("{\n" + "\t\"name\" : \"猪肉涨价了\",\n" + "\t\"key\" : \"社会\"\n" + "}").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
        mockMvc.perform(get("/rs/list/4")).andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("猪肉涨价了"))
            .andExpect(jsonPath("$.key").value("社会"));
    }

}
