package chessagents.agents.gameagent.behaviours.gameplay;

import chessagents.agents.gameagent.GameAgent;
import chessagents.agents.gameagent.GameContext;
import jade.util.Logger;

import static chessagents.agents.gameagent.behaviours.gameplay.GamePlayState.*;
import static chessagents.agents.gameagent.behaviours.gameplay.GamePlayTransition.*;

public class HandleGame extends GamePlayFSMBehaviour {

    private final Logger logger = Logger.getMyLogger(getClass().getName());
    private final GameContext context;

    public HandleGame(GameAgent gameAgent, GameContext context) {
        super(gameAgent);
        this.context = context;

        myAgent.addBehaviour(new HandleMoveSubscriptions((GameAgent) myAgent, context));

        registerFirstState(new InitTurn((GameAgent) myAgent, context));
        registerState(new ElectLeaderAgent(), ELECT_LEADER_AGENT);
        registerState(new WaitForMove(), WAIT_FOR_MOVE);
        registerState(new VerifyMove(), VERIFY_MOVE);
        registerState(new RefuseMove(), REFUSE_MOVE);
        registerState(new AgreeToMove(), AGREE_TO_MOVE);
        registerState(new PerformMove(), PERFORM_MOVE);
        registerState(new SendInformMoveMessage(), SEND_INFORM_MESSAGE);
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
