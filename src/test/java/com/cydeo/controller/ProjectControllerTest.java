package com.cydeo.controller;

import com.cydeo.dto.ProjectDTO;
import com.cydeo.dto.ResponseDTO;
import com.cydeo.dto.RoleDTO;
import com.cydeo.dto.UserDTO;
import com.cydeo.enums.Gender;
import com.cydeo.enums.Status;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ProjectControllerTest {

    @Autowired
    private MockMvc mvc;

    static UserDTO userDTO;
    static ProjectDTO projectDTO;

    static String token ;

    @BeforeAll
    static void setUp(){

        token = "Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJhWktWRmRyTUtOMzhZSm1OVnZWZ3dBcjFpUUZPZ05vLXo3MEY4T0p0VHVBIn0.eyJleHAiOjE2NzMwMTI4MTksImlhdCI6MTY3MzAxMTAxOSwianRpIjoiNjZjYTQ1ODItNGJkYi00Mzg1LTk4NzgtMTczOTM0M2NmNDljIiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo4MDgwL2F1dGgvcmVhbG1zL3NwcmluZy1kZXYiLCJhdWQiOiJhY2NvdW50Iiwic3ViIjoiNDE0NzQ0NjktZTk3Ny00ZmM2LWJjMDktM2Y5NDhjYWY1NGM4IiwidHlwIjoiQmVhcmVyIiwiYXpwIjoidGlja2V0aW5nLWFwcCIsInNlc3Npb25fc3RhdGUiOiI5ODViZGI2Zi01ZjdhLTRiMTYtYTc0Ni1lNDg1NTYxMDc5MTgiLCJhY3IiOiIxIiwiYWxsb3dlZC1vcmlnaW5zIjpbImh0dHA6Ly9sb2NhbGhvc3Q6ODA4MSJdLCJyZWFsbV9hY2Nlc3MiOnsicm9sZXMiOlsib2ZmbGluZV9hY2Nlc3MiLCJ1bWFfYXV0aG9yaXphdGlvbiIsImRlZmF1bHQtcm9sZXMtc3ByaW5nLWRldiJdfSwicmVzb3VyY2VfYWNjZXNzIjp7InRpY2tldGluZy1hcHAiOnsicm9sZXMiOlsiQWRtaW4iXX0sImFjY291bnQiOnsicm9sZXMiOlsibWFuYWdlLWFjY291bnQiLCJtYW5hZ2UtYWNjb3VudC1saW5rcyIsInZpZXctcHJvZmlsZSJdfX0sInNjb3BlIjoib3BlbmlkIHByb2ZpbGUgZW1haWwiLCJzaWQiOiI5ODViZGI2Zi01ZjdhLTRiMTYtYTc0Ni1lNDg1NTYxMDc5MTgiLCJlbWFpbF92ZXJpZmllZCI6dHJ1ZSwicHJlZmVycmVkX3VzZXJuYW1lIjoib3p6eSJ9.e-TISY9KZLMTvQ6JpFP5PnwZk_CnQu3LMS6cWRGHxpwctu1Z57xwVq0OVE9hIW5pr3W4U8pj9eTJS5ODuOFqK2RFWPLzkoEng19g_6GN47KSHhvZn4v6lXtuz0CJ1lXH6kyNnEqLSDT-1QuiXkm2bYwICJ_uKZOZ79X_-4LTg7SrqYC4xL-K1RGugU0EV2-YPNnMApS5Pbme-n4EVlavA6tR1PMFGeYtlnmN3bU4Q91t40QOTauo4gT3lMsv0eYkYE4twof_3B0ypNm9svu6X9qkYADwIz8pcSzcDEpnyvWtLmg4va2JwV7Z3L3tI8EpJCVejcxz2JlpAesmjXpqog";

        userDTO = UserDTO.builder()
                .id(2L)
                .firstName("ozzy")
                .lastName("ozzy")
                .userName("ozzy")
                .passWord("Abc1")
                .confirmPassWord("Abc1")
                .role(new RoleDTO(2L , "Manager"))
                .gender(Gender.MALE)
                .build();

        projectDTO = ProjectDTO.builder()
                .projectCode("Api1")
                .projectName("Api-ozzy")
                .assignedManager(userDTO)
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(5))
                .projectDetail("Api Test")
                .projectStatus(Status.OPEN)
                .build();
    }

    @Test
    public void givenNoToken_whenGetRequest() throws Exception {

        mvc.perform(MockMvcRequestBuilders
                .get("/api/v1/project"))
                .andExpect(status().is4xxClientError());

    }

    @Test
    public void givenToken_whenGetRequest() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                .get("/api/v1/project")
                .header("Authorization",token)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].projectCode").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].assignedManager.username").isNotEmpty());

    }

    @Test
    public void givenToken_createProject() throws Exception {

        mvc.perform(MockMvcRequestBuilders
                .post("/api/v1/project")
                .header("Authorization",token)
                .content(toJsonString(projectDTO))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));
    }

    @Test
    public void  givenToken_updateProject() throws Exception {
        projectDTO.setProjectName("Api-cydeo");

        mvc.perform(MockMvcRequestBuilders
                .put("/api/v1/project")
                .header("Authorization",token)
                .content(toJsonString(projectDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("message").value("Project is successfully updated"));
    }

    @Test
    public void givenToken_deleteProject() throws Exception {

        mvc.perform(MockMvcRequestBuilders
                .delete("/api/v1/project/"+projectDTO.getProjectCode())
                .header("Authorization",token)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    private static String toJsonString (final Object obj){
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS,false);
            objectMapper.registerModule(new JavaTimeModule());
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private static String makeRequest() {

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type", "password");
        map.add("client_id", "ticketing-app");
        map.add("client_secret", "WlwHN3r8oo4C3pHG0uyYqVm39P3K0BEb");
        map.add("username", "ozzy");
        map.add("password", "Abc1");
        map.add("scope", "openid");

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);

        ResponseEntity<ResponseDTO> response =
                restTemplate.exchange("http://localhost:8080/auth/realms/cydeo-dev/protocol/openid-connect/token",
                        HttpMethod.POST,
                        entity,
                        ResponseDTO.class);

        if (response.getBody() != null) {
            return response.getBody().getAccess_token();
        }

        return "";

    }


}
