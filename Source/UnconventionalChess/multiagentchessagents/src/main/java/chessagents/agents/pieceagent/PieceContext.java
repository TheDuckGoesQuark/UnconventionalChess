package chessagents.agents.pieceagent;

import chessagents.chess.GameState;
import chessagents.agents.pieceagent.actions.PieceAction;
import chessagents.agents.pieceagent.personality.Personality;
import chessagents.ontology.schemas.concepts.ChessPiece;
import chessagents.ontology.schemas.concepts.Position;
import chessagents.util.RandomUtil;
import jade.content.OntoAID;
import jade.core.AID;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
public class PieceContext {

    private final AID gameAgentAID;
    private final int maxDebateCycle;
    private final Personality personality;
    private final OntoAID myAID;
    private String moveSubscriptionId = null;
    private GameState gameState;

    public PieceContext(AID gameAgentAID) {
        this.gameAgentAID = gameAgentAID;
        personality = null;
        maxDebateCycle = 0;
        myAID = null;
    }

    public ChessPiece getMyPiece() {
        return gameState.getAgentPieceWithAID(myAID).get();
    }

    public PieceContext(Position myPosition, AID myAID, int gameId, AID gameAgentAID, int maxDebateCycle) {
        this.gameState = new GameState(gameId);
        this.gameAgentAID = gameAgentAID;
        this.maxDebateCycle = maxDebateCycle;
        this.personality = Personality.random();
        this.myAID = new OntoAID(myAID.getName(), AID.ISGUID);

        // register this piece as an agent
        var myPiece = gameState.getPieceAtPosition(myPosition).get();
        myPiece.setAgentAID(this.myAID);
        gameState.registerPieceAsAgent(myPiece);
    }

    /**
     * @return true if it is my side to go
     */
    public boolean isMyTurnToGo() {
        return gameState.isSideToGo(getMyPiece().getColour());
    }

    /**
     * Performs the given action with the given piece as the actor, including all side effects such as sending messages
     * to other agents
     *
     * @param actor  piece agent thats performing the action
     * @param action action be performed
     */
    void performAction(PieceAgent actor, PieceAction action) {
        // have agent perform action and update this game state
        gameState = action.perform(actor, gameState);
    }

    PieceAction chooseAction(Set<PieceAction> possibleActions) {
        var scoredActions = personality.getResponseToActions(getMyPiece(), possibleActions, gameState)
                .entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> countApprovingResponses(e.getValue())));

        var maxScore = scoredActions.values().stream().max(Integer::compareTo).get();
        var maxScoringActions = scoredActions.entrySet().stream().filter(e -> e.getValue().equals(maxScore)).map(Map.Entry::getKey).collect(Collectors.toList());
        return new RandomUtil<PieceAction>().chooseRandom(maxScoringActions);
    }

    private int countApprovingResponses(Set<ActionResponse> responses) {
        return responses.stream()
                .map(ActionResponse::isApproveAction)
                .filter(approves -> approves)
                .map(approves -> 1)
                .reduce(0, Integer::sum);
    }

    public Optional<ChessPiece> getPieceForAID(AID aid) {
        return gameState.getAllAgentPieces().stream()
                .filter(p -> p.getAgentAID().getLocalName().equals(aid.getLocalName()))
                .findFirst();
    }
}
