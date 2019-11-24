package chessagents.agents.pieceagent.behaviours.turn.statebehaviours.speaker;

import chessagents.agents.pieceagent.PieceAgent;
import chessagents.agents.pieceagent.PieceContext;
import chessagents.agents.pieceagent.actions.AskForProposalsAction;
import chessagents.agents.pieceagent.behaviours.turn.PieceState;
import chessagents.agents.pieceagent.behaviours.turn.TurnContext;
import chessagents.agents.pieceagent.behaviours.turn.statebehaviours.PieceStateBehaviour;

public class RequestSpeakerProposals extends PieceStateBehaviour {

    public static final String SPEAKER_CONTRACT_NET_PROTOCOL = "SPEAKER_CONTRACT_NET_PROTOCOL";
    private final TurnContext turnContext;

    public RequestSpeakerProposals(PieceAgent pieceAgent, PieceContext pieceContext, TurnContext turnContext) {
        super(pieceContext, pieceAgent, PieceState.REQUEST_SPEAKER_PROPOSALS);
        this.turnContext = turnContext;
    }

    @Override
    public void action() {
        setChosenAction(new AskForProposalsAction(getMyPiece(), turnContext));
    }
}
