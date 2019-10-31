package chessagents.agents.gameagent.behaviours.gameplay;

import chessagents.agents.gameagent.GameAgent;
import chessagents.agents.gameagent.GameContext;
import jade.util.Logger;

import static chessagents.agents.gameagent.behaviours.gameplay.GamePlayState.*;
import static chessagents.agents.gameagent.behaviours.gameplay.GamePlayTransition.*;

/**
 * Game management operates as an FSM, where each transition is determined by the return value from the onEnd() method
 * of each state behaviour.
 */
public class HandleGame extends GamePlayFSMBehaviour {

    final static String MOVE_MESSAGE_KEY = "_MOVE_MESSAGE";
    final static String MOVE_KEY = "_MOVE";

    private final Logger logger = Logger.getMyLogger(getClass().getName());

    public HandleGame(GameAgent gameAgent, GameContext context) {
        super(gameAgent);

        var informSubscribersOfMove = new InformSubscribersOfMoves(context);
        myAgent.addBehaviour(new HandleMoveSubscriptions((GameAgent) myAgent, context, informSubscribersOfMove));
        var dataStore = getDataStore();
        registerFirstState(new InitTurn((GameAgent) myAgent, context));
        registerState(new ElectLeaderAgent((GameAgent) myAgent, context), ELECT_LEADER_AGENT);
        registerState(new WaitForMove((GameAgent) myAgent, dataStore), WAIT_FOR_MOVE);
        registerState(new VerifyMove((GameAgent) myAgent, context, dataStore), VERIFY_MOVE);
        registerState(new RefuseMove((GameAgent) myAgent, dataStore), REFUSE_MOVE);
        registerState(new AgreeToMove((GameAgent) myAgent, dataStore), AGREE_TO_MOVE);
        registerState(new PerformMove((GameAgent) myAgent, context, dataStore), PERFORM_MOVE);
        registerState(new SendInformMoveMessage((GameAgent) myAgent, dataStore, informSubscribersOfMove), SEND_INFORM_MESSAGE);
        registerLastState(new EndGame());

        // init transitions
        registerTransition(INIT, ELECT_LEADER_AGENT, IS_AGENT_MOVE);
        registerTransition(INIT, END_GAME, GAME_COMPLETE);
        registerTransition(INIT, WAIT_FOR_MOVE, IS_HUMAN_MOVE);

        // elect transitions
        registerTransition(ELECT_LEADER_AGENT, WAIT_FOR_MOVE, LEADER_AGENT_CHOSEN);

        // wait for move transitions
        registerTransition(WAIT_FOR_MOVE, WAIT_FOR_MOVE, NO_MOVE_RECEIVED);
        registerTransition(WAIT_FOR_MOVE, VERIFY_MOVE, MOVE_RECEIVED);

        // verify move transitions
        registerTransition(VERIFY_MOVE, REFUSE_MOVE, MOVE_INVALID);
        registerTransition(VERIFY_MOVE, AGREE_TO_MOVE, MOVE_VALID);
        registerTransition(VERIFY_MOVE, WAIT_FOR_MOVE, MOVE_NOT_UNDERSTOOD);

        // refuse move transitions (reset wait for move step)
        registerTransition(REFUSE_MOVE, WAIT_FOR_MOVE, REFUSED_TO_MOVE, new GamePlayState[]{WAIT_FOR_MOVE});

        // agree to move transitions
        registerTransition(AGREE_TO_MOVE, PERFORM_MOVE, AGREED_TO_MOVE);

        // perform move transitions
        registerTransition(PERFORM_MOVE, SEND_INFORM_MESSAGE, PERFORMED_MOVE);

        // send inform move transitions (resets all once returned to initial state)
        registerTransition(SEND_INFORM_MESSAGE, INIT, SENT_MOVE_INFORM, GamePlayState.values());
    }
}
