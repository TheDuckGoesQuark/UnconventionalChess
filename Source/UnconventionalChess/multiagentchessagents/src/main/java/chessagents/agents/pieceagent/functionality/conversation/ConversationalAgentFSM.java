package chessagents.agents.pieceagent.functionality.conversation;

import chessagents.agents.pieceagent.PieceAgent;
import chessagents.agents.pieceagent.functionality.conversation.statebehaviours.*;
import jade.core.behaviours.FSMBehaviour;

import java.util.Arrays;

/**
 * FSM behaviour for maintaining the conversation state
 */
public class ConversationalAgentFSM extends FSMBehaviour {

    private ConversationContext conversationContext;

    public ConversationalAgentFSM(PieceAgent pieceAgent) {
        super(pieceAgent);
        this.conversationContext = new ConversationContext();
        var init = new Initial(pieceAgent, conversationContext);
        var speak = new Speak(pieceAgent, conversationContext);
        var startSpeakerElection = new StartSpeakerElection(pieceAgent, conversationContext);
        var chooseSpeaker = new ChooseSpeaker(pieceAgent, conversationContext);
        var listen = new Listen(pieceAgent, conversationContext);
        var waitForSpeakerResults = new WaitForSpeakerResults(pieceAgent, conversationContext);
        var waitForSpeakerCFP = new WaitForSpeakerCFP(pieceAgent, conversationContext);

        registerFirstState(init);
        registerTransition(init, speak, ConversationTransition.IS_SPEAKER);
        registerTransition(init, listen, ConversationTransition.NOT_SPEAKER);

        registerState(speak);
        registerTransition(speak, startSpeakerElection, ConversationTransition.SPOKE);

        registerState(listen);
        registerTransition(listen, waitForSpeakerResults, ConversationTransition.LISTENED);

        registerState(startSpeakerElection);
        registerTransition(startSpeakerElection, chooseSpeaker, ConversationTransition.STARTED_SPEAKER_ELECTION);

        registerState(waitForSpeakerCFP);
        registerTransition(waitForSpeakerResults, waitForSpeakerResults, ConversationTransition.ASKED_TO_SPEAK);

        registerState(chooseSpeaker);
        registerTransition(chooseSpeaker, waitForSpeakerResults, ConversationTransition.SPEAKER_CHOSEN);

        registerState(waitForSpeakerResults);
        registerTransition(waitForSpeakerResults, init, ConversationTransition.SPEAKER_CONFIRMED);
    }

    @Override
    public void reset() {
        // reset conversation context
        conversationContext.reset();
        super.reset();
    }

    public void registerState(ConversationStateBehaviour state) {
        super.registerState(state, state.getState().name());
    }

    public void registerFirstState(ConversationStateBehaviour state) {
        super.registerFirstState(state, state.getState().name());
    }

    public void registerTransition(ConversationStateBehaviour s1, ConversationStateBehaviour s2, ConversationTransition transition) {
        super.registerTransition(s1.getState().name(), s2.getState().name(), transition.ordinal());
    }

    public void registerTransition(ConversationStateBehaviour s1, ConversationStateBehaviour s2, ConversationTransition transition, ConversationState[] toBeReset) {
        var namesOfStatesToBeReset = Arrays.stream(toBeReset).map(ConversationState::name).toArray();
        super.registerTransition(s1.getState().name(), s2.getState().name(), transition.ordinal(), (String[]) namesOfStatesToBeReset);
    }
}
