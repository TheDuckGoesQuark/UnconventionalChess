package chessagents.agents.pieceagent.actions;

import chessagents.agents.pieceagent.PieceContext;
import chessagents.chess.GameState;
import chessagents.agents.ChessMessageBuilder;
import chessagents.agents.pieceagent.PieceAgent;
import chessagents.agents.pieceagent.behaviours.turn.PieceTransition;
import chessagents.ontology.schemas.actions.MakeMove;
import chessagents.ontology.schemas.concepts.ChessPiece;
import chessagents.ontology.schemas.concepts.PieceMove;
import jade.content.lang.Codec;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.lang.acl.ACLMessage;
import simplenlg.features.Feature;
import simplenlg.features.InterrogativeType;
import simplenlg.features.Tense;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static chessagents.agents.pieceagent.nlg.NLGUtil.NLG_FACTORY;
import static chessagents.agents.pieceagent.nlg.NLGUtil.REALISER;

public class TellPieceToMoveAction extends PieceAction {

    private static final List<String> OBJECTS = List.of("position");
    private static final List<String> VERB = List.of("move");
    private static final List<String> COMPLEMENTISERS = List.of("to", "that");
    private static final List<String> MODALS = List.of("can", "will");

    private PieceAgent actor;
    private final PieceMove move;
    private final ChessPiece otherChessPiece;

    public TellPieceToMoveAction(ChessPiece actor, PieceMove move, ChessPiece otherChessPiece) {
        super(PieceTransition.TOLD_PIECE_TO_MOVE, "Tell piece to move", actor, true);
        this.move = move;
        this.otherChessPiece = otherChessPiece;
    }

    @Override
    public GameState perform(PieceAgent actor, GameState gameState) {
        this.actor = actor;

        var request = createRequestToMove(otherChessPiece.getAgentAID(), move);

        sendRequestMove(gameState, request);

        // This action doesn't actually update the game state
        return gameState;
    }

    @Override
    public GameState getOutcomeOfAction(GameState gameState) {
        return gameState.makeMove(move);
    }

    /**
     * Sends the request to move to everyone so that all agents know that proposals to speak are not occuring,
     * and requests the agent told to move to reply-all with their response.
     *
     * @param gameState current game state
     * @param request   request to send
     */
    private void sendRequestMove(GameState gameState, ACLMessage request) {
        var aids = gameState.getAllPieces().stream()
                .map(ChessPiece::getAgentAID)
                .collect(Collectors.toSet());

        // send to everyone so they know not to expect a CFP to speak
        aids.forEach(request::addReceiver);

        // tell actual recipient that they should also let everyone know of their response
        aids.forEach(request::addReplyTo);

        actor.send(request);
    }

    private ACLMessage createRequestToMove(AID movingPiece, PieceMove move) {
        var request = ChessMessageBuilder.constructMessage(ACLMessage.REQUEST);
        var makeMove = new MakeMove(move);
        var action = new Action(movingPiece, makeMove);

        try {
            actor.getContentManager().fillContent(request, action);
        } catch (Codec.CodecException | OntologyException e) {
            logger.warning("Failed to create make move request: " + e.getMessage());
        }

        return request;
    }

    @Override
    public Optional<String> verbalise(PieceContext context) {
        var clause = NLG_FACTORY.createClause();

        clause.setSubject(otherChessPiece.getAgentAID().getLocalName());
        clause.setVerb(chooseRandom(VERB));
        var target = NLG_FACTORY.createNounPhrase(move.getTarget().getCoordinates());
        target.setDeterminer("to");
        clause.setObject(target);
        clause.setFeature(Feature.COMPLEMENTISER, chooseRandom(COMPLEMENTISERS));
        clause.setFeature(Feature.TENSE, Tense.FUTURE);

        if (randBool()) {
            clause.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.YES_NO);
            clause.setFeature(Feature.MODAL, chooseRandom(MODALS));
        }

        return Optional.ofNullable(REALISER.realiseSentence(clause));
    }
}
