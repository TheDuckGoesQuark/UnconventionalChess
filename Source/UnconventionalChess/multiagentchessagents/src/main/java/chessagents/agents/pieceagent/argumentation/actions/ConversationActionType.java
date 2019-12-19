package chessagents.agents.pieceagent.argumentation.actions;

public enum ConversationActionType {

    ACKNOWLEDGE(Acknowledge.class.getSimpleName()),
    ACKNOWLEDGE_AND_ASK_FOR_PROPOSALS(AcknowledgeAndAskForProposals.class.getSimpleName()),
    PERFORM_MOVE(PerformMove.class.getSimpleName()),
    PROPOSE_MOVE(ProposeMove.class.getSimpleName()),
    VOICE_OPINION(VoiceOpinion.class.getSimpleName()),
    VOICE_OPINION_PROPOSE_ALTERNATIVE(VoiceOpinionProposeAlternative.class.getSimpleName()),
    VOICE_OPINION_WITH_JUSTIFICATION(VoiceOpinionWithJustification.class.getSimpleName());

    private final String className;

    ConversationActionType(String className) {
        this.className = className;
    }

    public String getClassName() {
        return className;
    }
}
