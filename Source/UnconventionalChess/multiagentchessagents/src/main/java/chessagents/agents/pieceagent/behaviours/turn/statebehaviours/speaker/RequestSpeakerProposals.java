package chessagents.agents.pieceagent.behaviours.turn.statebehaviours.speaker;

import chessagents.agents.pieceagent.PieceAgent;
import chessagents.agents.pieceagent.PieceContext;
import chessagents.agents.pieceagent.actions.PieceAction;
import chessagents.agents.pieceagent.actions.proposalsrequested.AskOtherPiecesForIdeas;
import chessagents.agents.pieceagent.actions.proposalsrequested.LetOtherPieceSuggest;
import chessagents.agents.pieceagent.behaviours.turn.PieceState;
import chessagents.agents.pieceagent.TurnContext;
import chessagents.agents.pieceagent.behaviours.turn.statebehaviours.PieceStateBehaviour;
import chessagents.util.RandomUtil;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RequestSpeakerProposals extends PieceStateBehaviour {

    public static final String SPEAKER_CONTRACT_NET_PROTOCOL = "SPEAKER_CONTRACT_NET_PROTOCOL";
    private final TurnContext turnContext;

    public RequestSpeakerProposals(PieceAgent pieceAgent, PieceContext pieceContext, TurnContext turnContext) {
        super(pieceContext, pieceAgent, PieceState.REQUEST_SPEAKER_PROPOSALS);
        this.turnContext = turnContext;
    }

    @Override
    public void action() {
        var actionSet = new HashSet<PieceAction>();
        var askOtherPiecesForIdeas = new AskOtherPiecesForIdeas(getMyPiece(), turnContext);
        var letOtherPiecesSuggest = new LetOtherPieceSuggest(getMyPiece(), turnContext);

        if (turnContext.getDebateCycles() < 1) actionSet.add(askOtherPiecesForIdeas);

        actionSet.add(letOtherPiecesSuggest);

        setChosenAction(new RandomUtil<PieceAction>().chooseRandom(actionSet));
    }
}
