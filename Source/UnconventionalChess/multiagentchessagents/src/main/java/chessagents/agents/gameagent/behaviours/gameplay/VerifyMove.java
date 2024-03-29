package chessagents.agents.gameagent.behaviours.gameplay;

import chessagents.agents.gameagent.GameAgent;
import chessagents.agents.gameagent.GameAgentContext;
import chessagents.ontology.schemas.actions.MakeMove;
import chessagents.ontology.schemas.concepts.PieceMove;
import jade.content.lang.Codec;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.behaviours.DataStore;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.lang.acl.ACLMessage;
import jade.util.Logger;

import static chessagents.agents.gameagent.behaviours.gameplay.GamePlayTransition.*;
import static chessagents.agents.gameagent.behaviours.gameplay.HandleGame.MOVE_KEY;
import static chessagents.agents.gameagent.behaviours.gameplay.HandleGame.MOVE_MESSAGE_KEY;

public class VerifyMove extends OneShotBehaviour {

    private final Logger logger = Logger.getMyLogger(getClass().getName());
    private final GameAgentContext context;

    private GamePlayTransition nextTransition = null;

    VerifyMove(GameAgent myAgent, GameAgentContext context, DataStore dataStore) {
        super(myAgent);
        this.context = context;
        setDataStore(dataStore);
    }

    @Override
    public void action() {
        var message = (ACLMessage) getDataStore().get(MOVE_MESSAGE_KEY);

        try {
            var move = extractMove(message);
            getDataStore().put(MOVE_KEY, move);

            if (isValidMove(move)) {
                nextTransition = MOVE_VALID;
            } else {
                nextTransition = MOVE_INVALID;
            }
        } catch (Codec.CodecException | OntologyException | NotUnderstoodException e) {
            logger.warning("Failed to extract move from message: " + e.getMessage());
            logger.warning("Move: " + message.getContent());
            nextTransition = MOVE_NOT_UNDERSTOOD;
        }
    }

    private boolean isValidMove(PieceMove move) {
        return context.getGameState().isValidMove(move);
    }

    private PieceMove extractMove(ACLMessage message) throws Codec.CodecException, OntologyException, NotUnderstoodException {
        var action = (Action) myAgent.getContentManager().extractContent(message);
        var absAction = action.getAction();

        if (absAction instanceof MakeMove) {
            var makeMove = (MakeMove) absAction;
            return makeMove.getMove();
        } else {
            throw new NotUnderstoodException("Sent message not request to make move?");
        }
    }

    @Override
    public int onEnd() {
        return (nextTransition != null ? nextTransition : MOVE_NOT_UNDERSTOOD).ordinal();
    }
}
