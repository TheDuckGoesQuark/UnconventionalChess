package stacs.chessgateway.controllers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(ViewController.class)
public class ViewControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void getApp() throws Exception {
        mvc.perform(get("/index")
                .contentType(MediaType.TEXT_HTML_VALUE))
                .andExpect(status().isOk());
    }
}