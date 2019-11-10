package chessagents.agents.pieceagent.behaviours.turn.statebehaviours.speaker;

import chessagents.agents.pieceagent.PieceContext;
import chessagents.agents.pieceagent.behaviours.turn.TurnContext;
import chessagents.agents.pieceagent.behaviours.turn.PieceState;
import chessagents.agents.pieceagent.behaviours.turn.statebehaviours.PieceStateBehaviour;
import chessagents.agents.pieceagent.pieces.PieceAgent;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.MessageTemplate;
import jade.util.Logger;

import static chessagents.agents.pieceagent.behaviours.turn.PieceTransition.REQUESTED_TO_REMAIN_SPEAKER;
import static chessagents.agents.pieceagent.behaviours.turn.statebehaviours.speaker.RequestSpeakerProposals.SPEAKER_CONTRACT_NET_PROTOCOL;

public class RequestToRemainSpeaker extends PieceStateBehaviour {

    private final MessageTemplate mt = MessageTemplate.and(
            MessageTemplate.MatchProtocol(SPEAKER_CONTRACT_NET_PROTOCOL),
            MessageTemplate.MatchSender(myAgent.getAID())
    );
    private final PieceContext pieceContext;
    private final TurnContext turnContext;

    public RequestToRemainSpeaker(PieceAgent pieceAgent, PieceContext pieceContext, TurnContext turnContext) {
        super(pieceAgent, PieceState.REQUEST_TO_REMAIN_SPEAKER);
        this.pieceContext = pieceContext;
        this.turnContext = turnContext;
    }

    @Override
    public void action() {
        // Wait until received CFP from self to then propose self as speaker
        var message = myAgent.receive(mt);

        if (message != null) {
            turnContext.setCurrentMessage(message);
            var proposal = ((PieceAgent) myAgent).constructProposalToSpeak(message);
            myAgent.send(proposal);
            setEvent(REQUESTED_TO_REMAIN_SPEAKER);
        } else {
            block();
        }
    }
}
