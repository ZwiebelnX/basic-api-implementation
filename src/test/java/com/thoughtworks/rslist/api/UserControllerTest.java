package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.model.dto.UserDto;
import com.thoughtworks.rslist.repository.UserRepo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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

    @Autowired
    private UserRepo userRepo;

    UserControllerTest() {
        objectMapper = new ObjectMapper();
        objectMapper.configure(MapperFeature.USE_ANNOTATIONS, false);
    }

    @BeforeEach
    public void setUp() {
        RsController.initAll();

        userRepo.deleteAll();
    }

    @Test
    public void should_add_user_once() throws Exception {
        UserDto userDto = new UserDto("onion", "male", 19, "onion@163.com", "15800000000");
        String requestBody = objectMapper.writeValueAsString(userDto);
        mockMvc.perform(post("/user").content(requestBody).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andExpect(header().stringValues("index", "1"));
        mockMvc.perform(post("/user").content(requestBody).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andExpect(header().stringValues("index", "1"));
    }

    @Test
    void should_throw_error_when_post_user_given_wrong_param() throws Exception {
        UserDto userDto = new UserDto("Xiao Zhang", "female", 22, "xiaozhang@t.com", "18100000000");
        String requestBody = objectMapper.writeValueAsString(userDto);
        mockMvc.perform(post("/user").content(requestBody).contentType(MediaType.APPLICATION_JSON))
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

    @Test
    public void should_register_user_in_database_when_post_reg_given_user_info() throws Exception {
        UserDto userDto = new UserDto("onion", "male", 22, "onion@thoughtworks.com", "18100000000");
        String requestBody = objectMapper.writeValueAsString(userDto);
        String index = mockMvc.perform(post("/db/user").contentType(MediaType.APPLICATION_JSON).content(requestBody))
            .andExpect(status().isCreated())
            .andReturn()
            .getResponse()
            .getHeader("index");
        mockMvc.perform(post("/db/user").contentType(MediaType.APPLICATION_JSON).content(requestBody))
            .andExpect(status().isCreated())
            .andExpect(header().stringValues("index", String.valueOf(index)));
    }

    @Test
    public void should_get_user_from_database_when_get_user() throws Exception {
        UserDto userDto = new UserDto("onion", "male", 22, "onion@thoughtworks.com", "18100000000");
        String requestBody = objectMapper.writeValueAsString(userDto);
        String id = mockMvc.perform(post("/db/user").contentType(MediaType.APPLICATION_JSON).content(requestBody))
            .andExpect(status().isCreated())
            .andReturn()
            .getResponse()
            .getHeader("index");
        mockMvc.perform(get("/db/user/" + id))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.user_name").value("onion"))
            .andExpect(jsonPath("$.user_age").value(22))
            .andExpect(jsonPath("$.user_gender").value("male"))
            .andExpect(jsonPath("$.user_email").value("onion@thoughtworks.com"))
            .andExpect(jsonPath("$.user_phone").value("18100000000"));
    }

    @Test
    public void should_delete_user_when_delete_user_given_id() throws Exception {
        UserDto userDto = new UserDto("onion", "male", 22, "onion@thoughtworks.com", "18100000000");
        String requestBody = objectMapper.writeValueAsString(userDto);
        String id = mockMvc.perform(post("/db/user").contentType(MediaType.APPLICATION_JSON).content(requestBody))
            .andExpect(status().isCreated())
            .andReturn()
            .getResponse()
            .getHeader("index");
        mockMvc.perform(delete("/db/user/" + id)).andExpect(status().isOk());
        mockMvc.perform(get("/db/user/" + id)).andExpect(status().isBadRequest()).andExpect(jsonPath("$.error").value("invalid user's id"));
    }

}
