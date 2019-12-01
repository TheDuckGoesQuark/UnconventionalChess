package stacs.chessgateway.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import stacs.chessgateway.models.PersonalityTypes;
import stacs.chessgateway.services.PersonalityService;

@RestController
public class PersonalityController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PersonalityController.class);
    private final PersonalityService personalityService;

    @Autowired
    public PersonalityController(PersonalityService personalityService) {
        this.personalityService = personalityService;
    }

    @GetMapping()
    public PersonalityTypes getPersonalityTypes() {
        return personalityService.getAllPersonalities();
    }

}
