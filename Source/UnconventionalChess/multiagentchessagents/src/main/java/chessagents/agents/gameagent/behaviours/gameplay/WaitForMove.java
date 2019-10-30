package chessagents.agents.gameagent.behaviours.gameplay;

import chessagents.agents.gameagent.GameAgent;
import chessagents.ontology.ChessOntology;
import jade.core.behaviours.DataStore;
import jade.core.behaviours.SimpleBehaviour;
import jade.domain.FIPANames;
import jade.lang.acl.MessageTemplate;

import static chessagents.agents.gameagent.behaviours.gameplay.GamePlayTransition.MOVE_RECEIVED;
import static chessagents.agents.gameagent.behaviours.gameplay.GamePlayTransition.NO_MOVE_RECEIVED;
import static chessagents.agents.gameagent.behaviours.gameplay.HandleGame.MOVE_MESSAGE_KEY;

public class WaitForMove extends SimpleBehaviour {

    private final MessageTemplate MT = MessageTemplate.and(
            MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST),
            MessageTemplate.MatchOntology(ChessOntology.ONTOLOGY_NAME)
    );
    private GamePlayTransition nextTransition = NO_MOVE_RECEIVED;

    WaitForMove(GameAgent myAgent, DataStore dataStore) {
        super(myAgent);
        setDataStore(dataStore);
    }

    @Override
    public void action() {
        var message = myAgent.receive(MT);

        if (message != null) {
            getDataStore().put(MOVE_MESSAGE_KEY, message);
            nextTransition = MOVE_RECEIVED;
        } else {
            block();
        }

    }

    @Override
    public boolean done() {
        return nextTransition == MOVE_RECEIVED;
    }

    @Override
    public void reset() {
        getDataStore().remove(MOVE_MESSAGE_KEY);
        super.reset();
    }

    @Override
    public int onEnd() {
        return nextTransition.ordinal();
    }
}
