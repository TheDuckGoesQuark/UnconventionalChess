package chessagents.agents.pieceagent;

import chessagents.agents.pieceagent.personality.Trait;
import chessagents.chess.GameState;
import chessagents.agents.pieceagent.personality.Personality;
import chessagents.ontology.schemas.concepts.ChessPiece;
import chessagents.ontology.schemas.concepts.PieceConfiguration;
import chessagents.ontology.schemas.concepts.PieceMove;
import chessagents.ontology.schemas.concepts.Position;
import jade.content.OntoAID;
import jade.core.AID;
import lombok.Getter;
import lombok.Setter;

import java.util.Optional;

@Getter
@Setter
public class PieceContext {

    private final AID gameAgentAID;
    private final int maxDebateCycle;
    private final Personality personality;
    private final OntoAID myAID;
    private final GameHistory gameHistory;
    private String moveSubscriptionId = null;
    private GameState gameState;
    private int moveCounter = 0;

    public PieceContext(AID gameAgentAID) {
        this.gameAgentAID = gameAgentAID;
        gameHistory = null;
        personality = null;
        maxDebateCycle = 0;
        myAID = null;
    }

    public PieceContext(Position myPosition, AID myAID, int gameId, AID gameAgentAID, int maxDebateCycle, PieceConfiguration pieceConfiguration) {
        this.gameState = new GameState(gameId);
        this.gameAgentAID = gameAgentAID;
        this.maxDebateCycle = maxDebateCycle;
        this.personality = Personality.fromTrait(Trait.valueOf(pieceConfiguration.getPersonality()));
        this.myAID = new OntoAID(myAID.getName(), AID.ISGUID);

        // register this piece as an agent
        gameState.registerPieceAtPositionAsAgent(this.myAID, myPosition);
        gameHistory = new GameHistory();
        gameHistory.put(null, gameState);
    }

    public void makeMove(PieceMove move) {
        gameState = gameState.applyMove(move);
        gameHistory.put(move, gameState);
        moveCounter++;
    }

    /**
     * @return true if it is my side to go
     */
    public boolean isMyTurnToGo() {
        return gameState.isSideToGo(getMyPiece().getColour());
    }

    public ChessPiece getMyPiece() {
        return getPieceForAID(myAID).get();
    }

    public Optional<ChessPiece> getPieceForAID(AID aid) {
        return gameState.getAgentPieceWithAID(new OntoAID(aid.getName(), AID.ISGUID));
    }

    public int getTurnIndex() {
        return moveCounter;
    }
}
