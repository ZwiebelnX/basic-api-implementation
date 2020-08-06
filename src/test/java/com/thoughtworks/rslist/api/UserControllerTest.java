package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domain.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper;

    UserControllerTest() {
        objectMapper = new ObjectMapper();
        objectMapper.configure(MapperFeature.USE_ANNOTATIONS, false);
    }

    @BeforeEach
    public void setUp() {
        RsController.initAll();
    }

    @Test
    public void should_add_user_once() throws Exception {
        User user = new User("onion", "male", 19, "onion@163.com", "15800000000");
        String requestBody = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user")
            .content(requestBody).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andExpect(header().stringValues("index", "1"));
        mockMvc.perform(post("/user")
            .content(requestBody).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andExpect(header().stringValues("index", "1"));
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
