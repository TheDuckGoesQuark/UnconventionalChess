package chessagents.agents.pieceagent.argumentation.actions;

import chessagents.agents.pieceagent.PieceAgent;
import chessagents.agents.pieceagent.argumentation.ConversationMessage;
import chessagents.agents.pieceagent.personality.Trait;
import chessagents.util.RandomUtil;

public class Quip extends ConversationAction {

    private final PieceAgent agent;
    private static final RandomUtil<Trait> RANDOM_TRAIT_CHOOSER = new RandomUtil<>();

    public Quip(PieceAgent agent) {
        this.agent = agent;
    }

    @Override
    public ConversationMessage perform() {
        var trait = RANDOM_TRAIT_CHOOSER.chooseRandom(agent.getPieceContext().getPersonality().getTraits());
        var quip = trait.getRiGrammar().expandFrom(grammarTag());
        return new ConversationMessage(quip, agent.getAID());
    }
}
