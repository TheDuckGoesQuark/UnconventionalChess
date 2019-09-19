package stacs.chessgateway.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GameController {

    @GetMapping("/ping")
    public String ping() {
        return "Hello world";
    }

}
