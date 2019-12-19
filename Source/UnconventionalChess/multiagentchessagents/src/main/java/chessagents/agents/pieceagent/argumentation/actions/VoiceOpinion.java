package chessagents.agents.pieceagent.argumentation.actions;

import chessagents.agents.pieceagent.PieceAgent;
import chessagents.agents.pieceagent.argumentation.*;
import chessagents.agents.pieceagent.personality.Trait;
import chessagents.util.RandomUtil;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class VoiceOpinion implements ConversationAction {
    private final PieceAgent pieceAgent;
    private final TurnDiscussion turnDiscussion;
    private final MoveResponse response;

    @Override
    public String grammarTag() {
        return "<Voice" + response.getOpinion().name() + ">";
    }

    @Override
    public ConversationMessage perform() {
        // find trait that has value used, use its grammar to build statement
        var personality = pieceAgent.getPieceContext().getPersonality();
        var randomTraitChooser = new RandomUtil<Trait>();
        var reasoning = response.getReasoning();
        var traitResponsible = randomTraitChooser.chooseRandom(personality.getTraitsThatHaveValue(reasoning.getValue()));
        var move = response.getMove().get();
        var movingPiece = pieceAgent.getPieceContext().getGameState().getPieceAtPosition(move.getSource()).get().getAgentAID().getLocalName();
        var grammarVariableProvider = new GrammarVariableProviderImpl(move.getTarget().getCoordinates(), reasoning.getJustification(), movingPiece);
        return new ConversationMessage(traitResponsible.getRiGrammar().expandFrom(grammarTag(), grammarVariableProvider), response, pieceAgent.getAID());
    }
}
