package chessagents.ontology.schemas.concepts;

import jade.content.Concept;
import jade.content.OntoAID;

public class Piece implements Concept {

    private OntoAID pieceAgent;

    public OntoAID getPieceAgent() {
        return pieceAgent;
    }

    public void setPieceAgent(OntoAID pieceAgent) {
        this.pieceAgent = pieceAgent;
    }
}
