package com.thoughtworks.rslist;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.api.RsController;
import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;

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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class RsListApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper;

    RsListApplicationTests() {
        objectMapper = new ObjectMapper();
        objectMapper.configure(MapperFeature.USE_ANNOTATIONS, false);
    }

    @BeforeEach
    public void setUp() {
        RsController.initRsList();
    }

    @Test
    public void contextLoads() throws Exception {
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
    public void get_one_rs_event() throws Exception {
        mockMvc.perform(get("/rs/1"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.eventName").value("第一条事件"))
            .andExpect(jsonPath("$.keyWord").value("无"));
    }

    @Test
    public void get_some_rs_events() throws Exception {
        mockMvc.perform(get("/rs/list?start=1&end=2"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$[0].eventName").value("第一条事件"))
            .andExpect(jsonPath("$[0].keyWord").value("无"))
            .andExpect(jsonPath("$[1].eventName").value("第二条事件"))
            .andExpect(jsonPath("$[1].keyWord").value("无"));
    }

    @Test
    public void add_one_event() throws Exception {
        User user = new User("zhang", "female", 22, "xiaozhang@t.com", "18100000000");
        RsEvent rsEvent = new RsEvent("猪肉涨价了", "经济", user);
        String requestBody = objectMapper.writeValueAsString(rsEvent);
        mockMvc.perform(post("/rs/event").content(requestBody).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andExpect(header().stringValues("index", "3"));
        mockMvc.perform(get("/rs/4"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.eventName").value("猪肉涨价了"))
            .andExpect(jsonPath("$.keyWord").value("经济"));
    }

    @Test
    public void delete_one_event() throws Exception {
        mockMvc.perform(delete("/rs/1")).andExpect(status().isOk());
        mockMvc.perform(get("/rs/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.eventName").value("第二条事件"))
            .andExpect(jsonPath("$.keyWord").value("无"));
    }

    @Test
    public void modify_one_event() throws Exception {
        RsEvent rsEvent = new RsEvent("猪肉涨价了-改", "经济", null);
        String requestBody = objectMapper.writeValueAsString(rsEvent);
        mockMvc.perform(put("/rs/1").content(requestBody).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
        mockMvc.perform(get("/rs/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.eventName").value("猪肉涨价了-改"))
            .andExpect(jsonPath("$.keyWord").value("经济"));
    }

    @Test
    public void should_add_user_once() throws Exception {
        User user = new User("onion", "male", 19, "onion@163.com", "15800000000");
        String requestBody = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user")
            .content(requestBody).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andExpect(header().stringValues("index", "2"));
        mockMvc.perform(post("/user")
            .content(requestBody).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andExpect(header().stringValues("index", "2"));
    }

    @Test
    public void should_get_event_info_but_not_include_user_info() throws Exception {
        mockMvc.perform(get("/rs/1"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$", not(hasKey("user"))))
            .andExpect(jsonPath("$.eventName").value("第一条事件"))
            .andExpect(jsonPath("$.keyWord").value("无"));
    }

    @Test
    void should_throw_error_when_get_list_given_wrong_index() throws Exception {
        mockMvc.perform(get("/rs/list?start=-1&end=2"))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.error").value("invalid request param"));
    }

    @Test
    void should_throw_error_when_get_single_event_given_wrong_index() throws Exception {
        mockMvc.perform(get("/rs/-1"))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.error").value("invalid index"));
    }

    @Test
    void should_throw_error_when_post_rs_event_given_wrong_param() throws Exception {
        User user = new User("zhang", "female", 22, "xiaozhang@t.com", "18100000000");
        RsEvent rsEvent = new RsEvent("猪肉涨价了", null, user);
        String requestBody = objectMapper.writeValueAsString(rsEvent);
        mockMvc.perform(post("/rs/event")
            .content(requestBody)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.error").value("invalid param"));
    }

    @Test
    void should_throw_error_when_post_user_given_wrong_param() throws Exception {
        User user = new User("Xiao Zhang", "female", 22, "xiaozhang@t.com", "18100000000");
        String requestBody = objectMapper.writeValueAsString(user);
        mockMvc.perform(
            post("/user").content(requestBody)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.error").value("invalid user"));
    }
    @Test
    public void should_give_correct_user_json_when_get_user_info() throws Exception {
        mockMvc.perform(get("/users"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$[0].user_name").value("Sicong"))
            .andExpect(jsonPath("$[0].user_age").value(22))
            .andExpect(jsonPath("$[0].user_gender").value("male"))
            .andExpect(jsonPath("$[0].user_email").value("sicong.chen@163.com"))
            .andExpect(jsonPath("$[0].user_phone").value("15800000000"));

    }

}
