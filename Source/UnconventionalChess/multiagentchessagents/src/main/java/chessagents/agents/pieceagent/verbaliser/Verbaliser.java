package chessagents.agents.pieceagent.verbaliser;

import chessagents.agents.pieceagent.PieceAgent;
import chessagents.agents.pieceagent.actions.PieceAction;
import chessagents.agents.pieceagent.behaviours.chat.SendChatMessage;
import simplenlg.framework.NLGFactory;
import simplenlg.lexicon.Lexicon;
import simplenlg.realiser.english.Realiser;

public class Verbaliser {

    private static final Lexicon LEXICON = Lexicon.getDefaultLexicon();
    private static final NLGFactory NLG_FACTORY = new NLGFactory(LEXICON);
    private static final Realiser REALISER = new Realiser(LEXICON);

    public void verbaliseActionForAgent(PieceAgent pieceAgent, PieceAction action) {
        var s1 = NLG_FACTORY.createSentence("I am " + action.getActionName());
        var output = REALISER.realiseSentence(s1);
        pieceAgent.addBehaviour(new SendChatMessage(output, pieceAgent.getAID(), pieceAgent.getContext().getGameAgentAID()));
    }
}
