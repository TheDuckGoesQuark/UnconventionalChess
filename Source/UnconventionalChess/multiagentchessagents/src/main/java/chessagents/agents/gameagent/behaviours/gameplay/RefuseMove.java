package chessagents.agents.gameagent.behaviours.gameplay;

import chessagents.agents.gameagent.GameAgent;
import chessagents.agents.gameagent.GameContext;
import chessagents.ontology.ChessOntology;
import chessagents.ontology.schemas.concepts.Move;
import chessagents.ontology.schemas.predicates.IsValidMove;
import jade.content.ContentElement;
import jade.content.ContentElementList;
import jade.content.abs.AbsPredicate;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLVocabulary;
import jade.content.onto.BasicOntology;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.DataStore;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.util.Logger;

import static chessagents.agents.gameagent.behaviours.gameplay.GamePlayTransition.REFUSED_TO_MOVE;
import static chessagents.agents.gameagent.behaviours.gameplay.HandleGame.MOVE_KEY;
import static chessagents.agents.gameagent.behaviours.gameplay.HandleGame.MOVE_MESSAGE_KEY;

public class RefuseMove extends OneShotBehaviour {

    private final Logger logger = Logger.getMyLogger(getClass().getName());

    RefuseMove(GameAgent myAgent, DataStore datastore) {
        super(myAgent);
        setDataStore(datastore);
    }

    @Override
    public void action() {
        try {
            var request = (ACLMessage) getDataStore().get(MOVE_MESSAGE_KEY);
            var reply = createRefusalReply(request);
            myAgent.send(reply);
        } catch (Codec.CodecException | OntologyException e) {
            logger.warning("Unable to construct refusal message." + e.getMessage());
        }
    }

    private ACLMessage createRefusalReply(ACLMessage request) throws Codec.CodecException, OntologyException {
        var reply = request.createReply();
        reply.setPerformative(ACLMessage.REFUSE);

        var contentElementList = new ContentElementList();
        contentElementList.add(getAction(request));
        contentElementList.add(constructNotValidMovePredicate());

        return reply;
    }

    private ContentElement constructNotValidMovePredicate() throws OntologyException {
        var not = new AbsPredicate(SLVocabulary.NOT);
        var move = (Move) getDataStore().get(MOVE_KEY);
        var validMove = new IsValidMove(move);
        var absValidMove = ChessOntology.getInstance().fromObject(validMove);
        not.set(SLVocabulary.NOT_WHAT, absValidMove);
        return not;
    }

    private ContentElement getAction(ACLMessage request) throws Codec.CodecException, OntologyException {
        return myAgent.getContentManager().extractContent(request);
    }

    @Override
    public int onEnd() {
        return REFUSED_TO_MOVE.ordinal();
    }
}
