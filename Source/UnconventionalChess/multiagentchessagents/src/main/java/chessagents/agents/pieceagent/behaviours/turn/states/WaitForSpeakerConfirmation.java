package chessagents.agents.pieceagent.behaviours.turn.states;

import chessagents.agents.pieceagent.PieceContext;
import chessagents.agents.pieceagent.behaviours.turn.TurnContext;
import chessagents.agents.pieceagent.behaviours.turn.fsm.PieceStateBehaviour;
import chessagents.agents.pieceagent.pieces.PieceAgent;
import jade.core.behaviours.Behaviour;

public class WaitForSpeakerConfirmation extends Behaviour implements PieceStateBehaviour {
    public WaitForSpeakerConfirmation(PieceAgent pieceAgent, PieceContext pieceContext, TurnContext turnContext) {
    }
}
