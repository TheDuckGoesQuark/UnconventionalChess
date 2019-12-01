package stacs.chessgateway.services.impl;

import chessagents.agents.pieceagent.personality.Personality;
import chessagents.agents.pieceagent.personality.Trait;
import org.springframework.stereotype.Service;
import stacs.chessgateway.models.PersonalityType;
import stacs.chessgateway.models.PersonalityTypes;
import stacs.chessgateway.services.PersonalityService;

import java.util.Arrays;
import java.util.stream.Collectors;

@Service
public class PersonalityServiceImpl implements PersonalityService {

    private static final PersonalityTypes ALL_PERSONALITIES = new PersonalityTypes(
            Arrays.stream(Trait.values())
                    .map(Enum::name)
                    .map(PersonalityType::new)
                    .collect(Collectors.toList())
    );

    @Override
    public PersonalityTypes getAllPersonalities() {
        return ALL_PERSONALITIES;
    }
}
