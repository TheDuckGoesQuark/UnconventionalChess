package chessagents.agents.pieceagent.behaviours.turn.statebehaviours.nonspeaker;

import chessagents.agents.pieceagent.PieceContext;
import chessagents.agents.pieceagent.actions.RequestToSpeakAction;
import chessagents.agents.pieceagent.TurnContext;
import chessagents.agents.pieceagent.behaviours.turn.PieceState;
import chessagents.agents.pieceagent.behaviours.turn.statebehaviours.PieceStateBehaviour;
import chessagents.agents.pieceagent.PieceAgent;

public class RequestToSpeak extends PieceStateBehaviour {
    private final TurnContext turnContext;

    public RequestToSpeak(PieceAgent pieceAgent, PieceContext pieceContext, TurnContext turnContext) {
        super(pieceContext, pieceAgent, PieceState.REQUEST_TO_SPEAK);
        this.turnContext = turnContext;
    }

    @Override
    public void action() {
        var cfp = turnContext.getCurrentMessage();
        setChosenAction(new RequestToSpeakAction(getMyPiece(), cfp));
    }
}
