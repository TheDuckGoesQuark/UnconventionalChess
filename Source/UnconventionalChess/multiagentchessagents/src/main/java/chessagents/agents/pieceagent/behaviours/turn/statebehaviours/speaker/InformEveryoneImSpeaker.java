package chessagents.agents.pieceagent.behaviours.turn.statebehaviours.speaker;

import chessagents.agents.pieceagent.PieceContext;
import chessagents.agents.pieceagent.actions.TellEveryoneImSpeakerAction;
import chessagents.agents.pieceagent.TurnContext;
import chessagents.agents.pieceagent.behaviours.turn.PieceState;
import chessagents.agents.pieceagent.behaviours.turn.statebehaviours.PieceStateBehaviour;
import chessagents.agents.pieceagent.PieceAgent;

public class InformEveryoneImSpeaker extends PieceStateBehaviour {

    private final TurnContext turnContext;

    public InformEveryoneImSpeaker(PieceAgent pieceAgent, PieceContext pieceContext, TurnContext turnContext) {
        super(pieceContext, pieceAgent, PieceState.INFORM_EVERYONE_IM_SPEAKER);
        this.turnContext = turnContext;
    }

    @Override
    public void action() {
        turnContext.setCurrentSpeaker(myAgent.getAID());
        setChosenAction(new TellEveryoneImSpeakerAction(getMyPiece(), turnContext.getCurrentMessage()));
    }
}
