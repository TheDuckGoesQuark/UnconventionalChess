package chessagents.agents.gameagent.behaviours.gameplay;

import chessagents.agents.commonbehaviours.SubscriptionInform;
import chessagents.ontology.ChessOntology;
import chessagents.ontology.schemas.concepts.PieceMove;
import jade.content.ContentElement;
import jade.content.abs.*;
import jade.content.lang.sl.SLVocabulary;
import jade.content.onto.BasicOntology;
import jade.content.onto.OntologyException;

/**
 *
 */
public class InformSubscribersOfMoves extends SubscriptionInform<PieceMove> {

    private final AbsIRE absIRE = new AbsIRE(SLVocabulary.IOTA);

    InformSubscribersOfMoves() {
        // construct IRE for left hand side
        var absVariableMove = new AbsVariable("Move", ChessOntology.MOVE_MADE_MOVE);
        absIRE.setVariable(absVariableMove);

        var absProp = new AbsPredicate(ChessOntology.MOVE_MADE);
        absProp.set(ChessOntology.MOVE_MADE_MOVE, absVariableMove);
        absIRE.setProposition(absProp);
    }

    @Override
    public ContentElement buildInformContents(PieceMove move) {
        AbsObject absMove = null;
        try {
            absMove = ChessOntology.getInstance().fromObject(move);
        } catch (OntologyException e) {
            logger.warning("Failed to put move into inform: " + e.getMessage());
        }

        var equals = new AbsPredicate(BasicOntology.EQUALS);

        equals.set(BasicOntology.EQUALS_LEFT, absIRE);
        equals.set(BasicOntology.EQUALS_RIGHT, absMove);

        return equals;
    }
}
