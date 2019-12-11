package chessagents.agents.pieceagent.actions.proposalsrequested;

import chessagents.agents.pieceagent.PieceContext;
import chessagents.agents.pieceagent.actions.PieceAction;
import chessagents.chess.GameState;
import chessagents.agents.ChessMessageBuilder;
import chessagents.agents.pieceagent.PieceAgent;
import chessagents.agents.pieceagent.behaviours.turn.PieceTransition;
import chessagents.agents.pieceagent.TurnContext;
import chessagents.ontology.schemas.concepts.ChessPiece;
import jade.lang.acl.ACLMessage;
import simplenlg.features.Feature;
import simplenlg.features.InterrogativeType;
import simplenlg.features.Tense;

import java.util.Optional;

import static chessagents.agents.pieceagent.behaviours.turn.statebehaviours.speaker.RequestSpeakerProposals.SPEAKER_CONTRACT_NET_PROTOCOL;
import static chessagents.agents.pieceagent.nlg.NLGUtil.NLG_FACTORY;
import static chessagents.agents.pieceagent.nlg.NLGUtil.REALISER;

public class AskOtherPiecesForIdeas extends PieceAction {

    private final TurnContext turnContext;

    public AskOtherPiecesForIdeas(ChessPiece actor, TurnContext turnContext) {
        super(PieceTransition.PROPOSALS_REQUESTED, "Ask other piece what we should do", actor, true);
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

    @Override
    public Optional<String> verbalise(PieceContext context) {
        var sentence = NLG_FACTORY.createClause();
        var we = NLG_FACTORY.createNounPhrase("us");
        var doVerb = NLG_FACTORY.createVerbPhrase("doing");

        sentence.setSubject(we);
        sentence.setVerb(doVerb);
        sentence.setFeature(Feature.TENSE, Tense.PRESENT);
        sentence.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHAT_OBJECT);
        sentence.setFeature(Feature.MODAL, "should");
        sentence.setFeature(Feature.CUE_PHRASE, "next");

        return Optional.of(REALISER.realiseSentence(sentence));
    }
}
