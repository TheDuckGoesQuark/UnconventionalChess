package chessagents.agents.pieceagent.argumentation;

import chessagents.agents.commonbehaviours.RequestGameAgentMove;
import chessagents.agents.pieceagent.PieceAgent;
import chessagents.agents.pieceagent.behaviours.conversation.SendChatMessage;
import chessagents.ontology.schemas.actions.MakeMove;
import chessagents.ontology.schemas.concepts.PieceMove;
import chessagents.util.RandomUtil;

import java.util.*;
import java.util.stream.Collectors;

public class ConversationPlannerImpl implements ConversationPlanner {

    /**
     * Agent that this planner is for
     */
    private final PieceAgent agent;
    /**
     * Discussions during each turn, with first move at first index
     */
    private final LinkedList<TurnDiscussion> turnDiscussions = new LinkedList<>();
    private final RandomUtil<ConversationMessage> RANDOM_MESSAGE_CHOOSER = new RandomUtil<>();

    public ConversationPlannerImpl(PieceAgent pieceAgent) {
        this.agent = pieceAgent;
        startNewTurn();
    }

    @Override
    public void handleConversationMessage(ConversationMessage conversationMessage) {
        getCurrentDiscussion().recordMessage(conversationMessage);
    }

    @Override
    public void startNewTurn() {
        turnDiscussions.add(new TurnDiscussion());
    }

    @Override
    public int getLengthOfCurrentDiscussion() {
        return getCurrentDiscussion().getNumberOfMessages();
    }

    @Override
    public ConversationMessage produceMessage() {
        final ConversationMessage conversationMessage;

        // if not our turn, then we can react to last move
        if (!agent.getPieceContext().isMyTurnToGo()) {
            conversationMessage = generateQuip();
        } else {
            int numberOfMessages = getLengthOfCurrentDiscussion();
            if (numberOfMessages == 0) {
                conversationMessage = startDiscussion();
            } else {
                conversationMessage = continueDiscussion();
            }
        }

        // if choice of next message involves agreeing and performing the move,
        // then send the move to the game agent to be processed
        if (conversationMessage.movePerformed()) {
            var makeMove = new MakeMove(conversationMessage.getMoveResponse().get().getMove().get());
            var gameAgentAID = agent.getPieceContext().getGameAgentAID();
            agent.addBehaviour(new RequestGameAgentMove(makeMove, gameAgentAID));
        }

        // record our message
        handleConversationMessage(conversationMessage);

        return conversationMessage;
    }

    private ConversationMessage generateQuip() {
        return new ConversationMessage("lil quippy", agent.getAID());
    }

    private ConversationMessage startDiscussion() {
        var pieceContext = agent.getPieceContext();
        var gameState = pieceContext.getGameState();
        var allPossibleMoves = gameState.getAllLegalMoves();
        var responseToAllMoves = pieceContext.getPersonality().getResponseToMoves(pieceContext.getMyPiece(), allPossibleMoves, gameState);

        var options = new HashSet<ConversationMessage>();
        options.addAll(createProposalMessageForMoves(responseToAllMoves));
        options.addAll(createPerformMessageForMoves(responseToAllMoves));
        options.add(createAskForProposalMessage());

        return RANDOM_MESSAGE_CHOOSER.chooseRandom(options);
    }

    private ConversationMessage createAskForProposalMessage() {
        var moveResponse = MoveResponse.askForProposals();
        return new ConversationMessage("Any ideas what we should do next?", moveResponse, agent.getAID());
    }

    private ConversationMessage continueDiscussion() {
        // no proposals so far -> propose or perform move
        // else -> get last proposal and determine reaction
        return new ConversationMessage("another thing?", agent.getAID());
    }

    private Collection<ConversationMessage> createPerformMessageForMoves(Set<MoveResponse> moves) {
        return moves.stream()
                .map(MoveResponse::clone)
                .peek(move -> move.setPerformed(true))
                .map(moveResponse -> new ConversationMessage(createStatementFromMoveResponse(moveResponse), moveResponse, agent.getAID()))
                .collect(Collectors.toSet());
    }

    private Collection<ConversationMessage> createProposalMessageForMoves(Set<MoveResponse> moves) {
        return moves.stream()
                .map(MoveResponse::clone)
                .map(moveResponse -> new ConversationMessage(createStatementFromMoveResponse(moveResponse), moveResponse, agent.getAID()))
                .collect(Collectors.toSet());
    }

    private String createStatementFromMoveResponse(MoveResponse move) {
        return "waddup "+move.getMove().get().toString();
    }

    private TurnDiscussion getCurrentDiscussion() {
        return turnDiscussions.peekLast();
    }
}
