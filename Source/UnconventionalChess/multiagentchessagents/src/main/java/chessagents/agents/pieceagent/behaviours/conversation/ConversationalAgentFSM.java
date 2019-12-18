package chessagents.agents.pieceagent.behaviours.conversation;

import chessagents.agents.pieceagent.PieceAgent;
import chessagents.agents.pieceagent.argumentation.ConversationContext;
import chessagents.agents.pieceagent.behaviours.conversation.statebehaviours.*;
import jade.core.behaviours.FSMBehaviour;

/**
 * FSM behaviour for maintaining the conversation state
 */
public class ConversationalAgentFSM extends FSMBehaviour {

    private ConversationContext conversationContext;

    public ConversationalAgentFSM(PieceAgent pieceAgent) {
        super(pieceAgent);
        this.conversationContext = new ConversationContext(pieceAgent);

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
        registerTransition(listen, waitForSpeakerCFP, ConversationTransition.LISTENED);

        registerState(startSpeakerElection);
        registerTransition(startSpeakerElection, chooseSpeaker, ConversationTransition.STARTED_SPEAKER_ELECTION);

        registerState(waitForSpeakerCFP);
        registerTransition(waitForSpeakerCFP, waitForSpeakerResults, ConversationTransition.ASKED_TO_SPEAK);

        registerState(chooseSpeaker);
        registerTransition(chooseSpeaker, waitForSpeakerResults, ConversationTransition.SPEAKER_CHOSEN);

        registerState(waitForSpeakerResults);
        registerTransition(waitForSpeakerResults, init, ConversationTransition.SPEAKER_CONFIRMED, ConversationState.values());
        // reset every state on return to initial state, otherwise they dont execute
    }

    @Override
    public void reset() {
        // reset conversation context
        conversationContext.startNewTurn();
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
        var namesOfStatesToBeReset = new String[toBeReset.length];

        for (int i = 0; i < toBeReset.length; i++) {
            namesOfStatesToBeReset[i] = toBeReset[i].name();
        }

        super.registerTransition(s1.getState().name(), s2.getState().name(), transition.ordinal(), namesOfStatesToBeReset);
    }
}
