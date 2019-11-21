package chessagents.agents.pieceagent.behaviours.turn.statebehaviours.speaker;

import chessagents.agents.pieceagent.PieceContext;
import chessagents.agents.pieceagent.actions.TellEveryoneImSpeakerAction;
import chessagents.agents.pieceagent.behaviours.turn.TurnContext;
import chessagents.agents.pieceagent.behaviours.turn.PieceState;
import chessagents.agents.pieceagent.behaviours.turn.statebehaviours.PieceStateBehaviour;
import chessagents.agents.pieceagent.PieceAgent;
import chessagents.ontology.schemas.actions.BecomeSpeaker;
import jade.content.OntoAID;
import jade.content.lang.Codec;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.content.onto.basic.Done;
import jade.core.AID;
import jade.lang.acl.ACLMessage;

public class InformEveryoneImSpeaker extends PieceStateBehaviour {

    private final TurnContext turnContext;

    public InformEveryoneImSpeaker(PieceAgent pieceAgent, PieceContext pieceContext, TurnContext turnContext) {
        super(pieceContext, pieceAgent, PieceState.INFORM_EVERYONE_IM_SPEAKER);
        this.turnContext = turnContext;
    }

    @Override
    public void action() {
        turnContext.setCurrentSpeaker(myAgent.getAID());
        setChosenAction(new TellEveryoneImSpeakerAction(getMyPiece(), turnContext.getCurrentMessage()));
    }
}
