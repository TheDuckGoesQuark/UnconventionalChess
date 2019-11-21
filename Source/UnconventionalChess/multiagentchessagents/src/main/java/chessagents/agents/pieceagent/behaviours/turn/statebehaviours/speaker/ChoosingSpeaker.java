package chessagents.agents.pieceagent.behaviours.turn.statebehaviours.speaker;

import chessagents.agents.pieceagent.PieceContext;
import chessagents.agents.pieceagent.actions.PieceAction;
import chessagents.agents.pieceagent.behaviours.turn.PieceState;
import chessagents.agents.pieceagent.behaviours.turn.PieceTransition;
import chessagents.agents.pieceagent.behaviours.turn.TurnContext;
import chessagents.agents.pieceagent.behaviours.turn.statebehaviours.PieceStateBehaviour;
import chessagents.agents.pieceagent.PieceAgent;
import chessagents.agents.pieceagent.actions.ChoosePieceToSpeakAction;
import chessagents.ontology.schemas.concepts.ChessPiece;
import jade.core.AID;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static chessagents.agents.pieceagent.behaviours.turn.statebehaviours.speaker.RequestSpeakerProposals.SPEAKER_CONTRACT_NET_PROTOCOL;

public class ChoosingSpeaker extends PieceStateBehaviour {

    private final TurnContext turnContext;
    private final Set<ACLMessage> speakerProposals = new HashSet<>();
    private MessageTemplate mt = null;

    public ChoosingSpeaker(PieceAgent pieceAgent, PieceContext pieceContext, TurnContext turnContext) {
        super(pieceContext, pieceAgent, PieceState.CHOOSING_SPEAKER);
        this.turnContext = turnContext;
    }

    @Override
    protected void initialiseState() {
        speakerProposals.clear();

        var conversationID = turnContext.getCurrentMessage().getConversationId();

        mt = MessageTemplate.and(
                MessageTemplate.MatchProtocol(SPEAKER_CONTRACT_NET_PROTOCOL),
                MessageTemplate.and(
                        MessageTemplate.MatchPerformative(ACLMessage.PROPOSE),
                        MessageTemplate.MatchConversationId(conversationID)
                )
        );
    }

    @Override
    public void action() {
        if (receivedRequestFromEveryone()) {
            setChosenAction(getAgent().chooseAction(generateActionForEachSpeakerProposal()));

            // Clear speaker to avoid confusion in future states
            turnContext.setCurrentSpeaker(null);
        } else {
            // receive all messages for this protocol
            var message = myAgent.receive(mt);

            if (message != null) {
                speakerProposals.add(message);
            } else {
                block();
            }
        }
    }

    private Set<PieceAction> generateActionForEachSpeakerProposal() {
        var myPiece = getMyPiece();
        return speakerProposals.stream()
                .map(ACLMessage::getSender)
                .map(sender -> new ChoosePieceToSpeakAction(myPiece, pieceContext.getPieceForAID(sender).get(), speakerProposals))
                .collect(Collectors.toSet());
    }

    private boolean receivedRequestFromEveryone() {
        var numAgents = pieceContext.getGameState().getAllAgentPiecesForColour(getMyPiece().getColour()).size();
        logger.info("Received request to speak from " + speakerProposals.size() + "/" + numAgents + " of agents");
        return speakerProposals.size() == numAgents;
    }
}
