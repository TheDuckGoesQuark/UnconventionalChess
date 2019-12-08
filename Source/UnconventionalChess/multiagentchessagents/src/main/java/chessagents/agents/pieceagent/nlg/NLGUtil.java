package chessagents.agents.pieceagent.nlg;

import simplenlg.framework.NLGFactory;
import simplenlg.lexicon.Lexicon;
import simplenlg.realiser.english.Realiser;

public class NLGUtil {

    public static final Lexicon LEXICON = Lexicon.getDefaultLexicon();
    public static final NLGFactory NLG_FACTORY = new NLGFactory(LEXICON);
    public static final Realiser REALISER = new Realiser(LEXICON);

}
