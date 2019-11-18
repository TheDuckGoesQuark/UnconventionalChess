package chessagents.agents.pieceagent.planner.actions;

import chessagents.GameState;
import chessagents.agents.ChessMessageBuilder;
import chessagents.agents.pieceagent.PieceContext;
import chessagents.agents.pieceagent.behaviours.turn.PieceTransition;
import chessagents.agents.pieceagent.planner.PieceAction;
import chessagents.ontology.schemas.actions.MakeMove;
import chessagents.ontology.schemas.concepts.ChessPiece;
import chessagents.ontology.schemas.concepts.PieceMove;
import jade.content.lang.Codec;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.lang.acl.ACLMessage;

import java.util.Optional;
import java.util.stream.Collectors;

public class TellPieceToMoveAction extends PieceAction {
    private final PieceMove move;
    private final ChessPiece otherChessPiece;

    public TellPieceToMoveAction(ChessPiece actor, PieceMove move, ChessPiece otherChessPiece) {
        super(PieceTransition.TOLD_PIECE_TO_MOVE, "Tell piece to move", actor);
        this.move = move;
        this.otherChessPiece = otherChessPiece;
    }

    @Override
    public ChessPiece getActor() {
        return null;
    }

    @Override
    public Optional<PieceMove> getMove() {
        return Optional.of(move);
    }

    @Override
    public void perform(GameState gameState) {
        createRequestToMove(gameState.getPieceAtPosition(move.getSource()).get().getAgentAID(), move);
    }

    private void sendRequestMove(ACLMessage request) {
        var aids = getAllPieces().stream()
                .map(ChessPiece::getAgentAID)
                .collect(Collectors.toSet());

        // send to everyone so they know not to expect a CFP to speak
        aids.forEach(request::addReceiver);

        // tell actual recipient that they should also let everyone know of their response
        aids.forEach(request::addReplyTo);

        myAgent.send(request);
    }

    private ACLMessage createRequestToMove(AID movingPiece, PieceMove move) {
        var request = ChessMessageBuilder.constructMessage(ACLMessage.REQUEST);
        var makeMove = new MakeMove(move);
        var action = new Action(movingPiece, makeMove);

        try {
            myAgent.getContentManager().fillContent(request, action);
        } catch (Codec.CodecException | OntologyException e) {
            logger.warning("Failed to create make move request: " + e.getMessage());
        }

        return request;
    }
    /*
     * TODO READ THIS AND DO THIS YOU SCHMUK
     *
     * Each piece state transition behaviour call PieceAgent.chooseNextAction(Set<Action>):Action and provides a set
     * of possible next actions (i.e. choose x as speaker, tell x to move, make move x)
     *
     * Planner checks if any of the possible actions will help attain the goal, and returns whatever action
     * does so best.
     *
     * If none, random?
     *
     * Once the agent chooses its action (or another agent receives knowledge of the action that was chosen), they
     * call PieceAgent.performAction(Action):PieceTransition which will update their game state accordingly,
     * and provide the next transition as an outcome of the action.
     */
}
