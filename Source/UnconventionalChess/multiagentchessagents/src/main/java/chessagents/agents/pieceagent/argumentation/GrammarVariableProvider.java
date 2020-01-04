package chessagents.agents.pieceagent.argumentation;

public interface GrammarVariableProvider {

    String getMoveTarget();

    String getJustification();

    String getMovingPiece();

    String getAlternativeMoveTarget();

    String getAlternativeMovingPiece();

    String getAlternativeJustification();

    String getCapturedPiece();

    String getEscapingPiece();

    String getThreatenedPiece();

}
