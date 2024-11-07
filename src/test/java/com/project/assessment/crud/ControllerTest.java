package com.project.assessment.crud;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.assessment.crud.model.request.AuthRequest;
import com.project.assessment.crud.model.request.UserRequest;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static String id;
    private static String token;

    @Order(1)
    @Test
    public void testRegisterSuccess() throws Exception {
        AuthRequest request = new AuthRequest("user@example.com", "password123");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.email").value("user@example.com"))
                .andDo(result -> {
                    String content = result.getResponse().getContentAsString();
                    id = objectMapper.readTree(content).get("data").get("id").toString();
                });
    }

    @Order(2)
    @Test
    public void testRegisterFailInvalidEmail() throws Exception {
        AuthRequest request = new AuthRequest("invalidEmail", "password123");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Bad Request"));
    }

    @Order(3)
    @Test
    public void testRegisterFailDuplicateEmail() throws Exception {
        AuthRequest request = new AuthRequest("user@example.com", "password123");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Conflict"));
    }

    @Order(4)
    @Test
    public void testLoginFailIncorrectPassword() throws Exception {
        AuthRequest request = new AuthRequest("user@example.com", "wrongPassword");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Unauthorized"));
    }

    @Order(5)
    @Test
    public void testLoginSuccess() throws Exception {
        AuthRequest request = new AuthRequest("user@example.com", "password123");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.token").isNotEmpty())
                .andDo(result -> {
                    String content = result.getResponse().getContentAsString();
                    token = objectMapper.readTree(content).get("data").get("token").asText();
                })
        ;
    }

    @Order(6)
    @Test
    public void testAddUserSuccess() throws Exception {
        UserRequest request = new UserRequest("John Doe", 30, true);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.name").value("John Doe"))
                .andExpect(jsonPath("$.data.age").value(30))
                .andExpect(jsonPath("$.data.membership").value(true))
                .andDo(result -> {
                    String content = result.getResponse().getContentAsString();
                    id = objectMapper.readTree(content).get("data").get("id").asText();
                });
    }

    @Order(7)
    @Test
    public void testGetAllUsersSuccess() throws Exception {
        mockMvc.perform(get("/api/users")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].id").isString())
                .andExpect(jsonPath("$.data[0].name").isString())
                .andExpect(jsonPath("$.data[0].age").isNumber())
                .andExpect(jsonPath("$.data[0].membership").isBoolean())
                .andExpect(jsonPath("$.paging").isNotEmpty());
    }

    @Order(8)
    @Test
    public void testGetUserByIdSuccess() throws Exception {
        mockMvc.perform(get("/api/users/" + id)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(id));
    }

    @Order(9)
    @Test
    public void testGetUserByIdNotFound() throws Exception {
        mockMvc.perform(get("/api/users/xxx")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Not Found"));
    }

    @Order(10)
    @Test
    public void testUpdateUserSuccess() throws Exception {
        UserRequest updateRequest = new UserRequest("Updated Name", 35, false);

        mockMvc.perform(put("/api/users/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest))
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("Updated Name"))
                .andExpect(jsonPath("$.data.age").value(35))
                .andExpect(jsonPath("$.data.membership").value(false));
    }

    @Order(11)
    @Test
    public void testDeleteUserSuccess() throws Exception {

        mockMvc.perform(delete("/api/users/" + id)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value("User with id " + id + " has been deleted"));
    }

    @Order(12)
    @Test
    public void testDeleteUserNotFound() throws Exception {
        mockMvc.perform(delete("/api/users/" + id)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Not Found"));
    }
}
