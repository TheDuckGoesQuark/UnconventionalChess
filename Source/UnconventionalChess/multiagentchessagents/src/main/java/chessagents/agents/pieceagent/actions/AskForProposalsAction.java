package chessagents.agents.pieceagent.actions;

import chessagents.GameState;
import chessagents.agents.ChessMessageBuilder;
import chessagents.agents.pieceagent.PieceAgent;
import chessagents.agents.pieceagent.behaviours.turn.PieceTransition;
import chessagents.agents.pieceagent.behaviours.turn.TurnContext;
import chessagents.ontology.schemas.concepts.ChessPiece;
import jade.lang.acl.ACLMessage;

import static chessagents.agents.pieceagent.behaviours.turn.statebehaviours.speaker.RequestSpeakerProposals.SPEAKER_CONTRACT_NET_PROTOCOL;

public class AskForProposalsAction extends PieceAction {
    private final TurnContext turnContext;

    public AskForProposalsAction(ChessPiece actor, TurnContext turnContext) {
        super(PieceTransition.PROPOSALS_REQUESTED, "Ask other piece what we should do", actor);
        this.turnContext = turnContext;
    }

    @Override
    public GameState perform(PieceAgent actor, GameState gameState) {
        requestProposals(actor, gameState);
        return gameState;
    }

    private void requestProposals(PieceAgent actor, GameState gameState) {
        var cfp = ChessMessageBuilder.constructMessage(ACLMessage.CFP);

        // send to everyone on my side (including myself!)
        gameState.getAllAgentPiecesForColour(gameState.getSideToGo())
                .stream()
                .map(ChessPiece::getAgentAID)
                .forEach(cfp::addReceiver);

        cfp.setProtocol(SPEAKER_CONTRACT_NET_PROTOCOL);

        // Store CFP
        turnContext.setCurrentMessage(cfp);

        actor.send(cfp);
    }

    @Override
    public GameState getOutcomeOfAction(GameState gameState) {
        return gameState;
    }
}
