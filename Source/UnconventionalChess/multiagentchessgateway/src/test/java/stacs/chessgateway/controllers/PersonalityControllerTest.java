package stacs.chessgateway.controllers;

import chessagents.agents.gatewayagent.messages.MessageType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import stacs.chessgateway.models.Message;
import stacs.chessgateway.models.PersonalityType;
import stacs.chessgateway.models.PersonalityTypes;
import stacs.chessgateway.services.GatewayService;
import stacs.chessgateway.services.PersonalityService;

import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(PersonalityController.class)
public class PersonalityControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private PersonalityService personalityService;

    @Test
    public void getPersonalityTypes() throws Exception {
        given(personalityService.getAllPersonalities())
                .willReturn(new PersonalityTypes(List.of(new PersonalityType("Cool"), new PersonalityType("NotCool"))));

        mvc.perform(get("/personalities")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.personalityTypes").isArray())
                .andExpect(jsonPath("$.personalityTypes[0].name").value("Cool"))
                .andExpect(jsonPath("$.personalityTypes[1].name").value("NotCool"));
    }
}