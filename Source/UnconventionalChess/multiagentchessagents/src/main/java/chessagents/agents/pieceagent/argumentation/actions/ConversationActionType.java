package chessagents.agents.pieceagent.argumentation.actions;

public enum ConversationActionType {

    ACKNOWLEDGE(Acknowledge.class.getName()),
    ACKNOWLEDGE_AND_ASK_FOR_PROPOSALS(AcknowledgeAndAskForProposals.class.getName()),
    PERFORM_MOVE(PerformMove.class.getName()),
    PROPOSE_MOVE(ProposeMove.class.getName()),
    VOICE_OPINION(VoiceOpinion.class.getName()),
    VOICE_OPINION_PROPOSE_ALTERNATIVE(VoiceOpinionProposeAlternative.class.getName()),
    VOICE_OPINION_WITH_JUSTIFICATION(VoiceOpinionWithJustification.class.getName());

    private final String className;

    ConversationActionType(String className) {
        this.className = className;
    }

    public String getClassName() {
        return className;
    }
}
