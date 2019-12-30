package stacs.chessgateway.controllers;

import chessagents.agents.gatewayagent.messages.ChatMessage;
import chessagents.agents.gatewayagent.messages.MessageType;
import jade.lang.acl.MessageTemplate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import stacs.chessgateway.models.GameConfiguration;
import stacs.chessgateway.models.Message;
import stacs.chessgateway.models.PersonalityType;
import stacs.chessgateway.models.PieceConfig;
import stacs.chessgateway.services.GatewayService;

import java.time.Instant;
import java.util.HashMap;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(GameController.class)
public class GameControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private GatewayService gatewayService;

    @Test
    public void handleMoveMessage() {

    }

    @Test
    public void createGame() throws Exception {
        var gameConfiguration = new GameConfiguration();
        gameConfiguration.setHumanPlays(true);
        gameConfiguration.setHumanPlaysAsWhite(true);
        var pieceConfigs = new HashMap<String, PieceConfig>();
        pieceConfigs.put("a5", new PieceConfig("james", new PersonalityType("cool")));
        gameConfiguration.setPieceConfigs(pieceConfigs);
        var timestamp = Instant.ofEpochMilli(20);

        given(gatewayService.createGame(Mockito.any()))
                .willReturn(new Message<>(timestamp, MessageType.GAME_CONFIGURATION_MESSAGE, gameConfiguration));

        var body = "{" +
                "\"type\":\"" + MessageType.GAME_CONFIGURATION_MESSAGE.name() + "\"," +
                "\"timestamp\":\"" + timestamp.toString() + "\"," +
                "\"body\": " +
                "{" +
                "\"humanPlays\":\"true\"," +
                "\"humanPlaysAsWhite\":\"true\"," +
                "\"pieceConfigs\": " +
                "{" +
                "\"a5\": " +
                "{" +
                "\"name\":\"james\"," +
                "\"personality\": " +
                "{" +
                "\"name\":\"cool\"" +
                "}" +
                "}" +
                "}" +
                "}" +
                "}";

        mvc.perform(post("/game")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.type").value(MessageType.GAME_CONFIGURATION_MESSAGE.name()))
                .andExpect(jsonPath("$.timestamp").value(timestamp.toString()))
                .andExpect(jsonPath("$.body.humanPlays").value(true))
                .andExpect(jsonPath("$.body.humanPlaysAsWhite").value(true))
                .andExpect(jsonPath("$.body.pieceConfigs.a5.name").value("james"));
    }
}