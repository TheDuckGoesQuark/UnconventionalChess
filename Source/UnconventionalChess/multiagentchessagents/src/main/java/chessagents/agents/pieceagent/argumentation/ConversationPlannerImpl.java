package chessagents.agents.pieceagent.argumentation;

import chessagents.agents.commonbehaviours.RequestGameAgentMove;
import chessagents.agents.pieceagent.PieceAgent;
import chessagents.agents.pieceagent.argumentation.actions.*;
import chessagents.ontology.schemas.actions.MakeMove;
import chessagents.util.RandomUtil;

import java.util.*;

public class ConversationPlannerImpl implements ConversationPlanner {

    /**
     * Agent that this planner is for
     */
    private final PieceAgent agent;
    /**
     * Discussions during each turn, with first move at first index
     */
    private final LinkedList<TurnDiscussion> turnDiscussions = new LinkedList<>();
    private final RandomUtil<ConversationAction> RANDOM_ACTION_CHOOSER = new RandomUtil<>();

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
            var action = chooseNextAction();
            conversationMessage = action.perform();
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

    private ConversationAction chooseNextAction() {
        var numberOfMessages = getLengthOfCurrentDiscussion();
        var setOfNextActions = new HashSet<ConversationAction>();

        // add the action to perform a move if theres one I can do
        var availableMoveResponses = getResponsesToAllMoves();
        var moveExistsThatICanPerform = containsMoveICanPerform(availableMoveResponses);
        if (moveExistsThatICanPerform) {
            setOfNextActions.add(new PerformMove());
        }

        if (numberOfMessages == 0) {
            // first message of turn so we can discuss previous suggestions
            setOfNextActions.add(new ProposeMove());
            setOfNextActions.add(new AskForProposals(agent));
        } else {
            var currentDiscussion = getCurrentDiscussion();
            if (currentDiscussion.proposalsCalledFor()) {
                setOfNextActions.add(new ProposeMove());
            } else {
                // react to previously proposed moves
                var response = getResponseToLastMoveDiscussed();
                switch (response.getOpinion()) {
                    case LIKE:
                    case DISLIKE:
                        setOfNextActions.add(new VoiceOpinion(response.getOpinion()));
                        setOfNextActions.add(new VoiceOpinionProposeAlternative(response.getOpinion()));
                        setOfNextActions.add(new VoiceOpinionWithJustification(response.getOpinion()));
                        break;
                    case NEUTRAL:
                        setOfNextActions.add(new Acknowledge());
                        setOfNextActions.add(new AskForProposals(agent));
                        setOfNextActions.add(new AcknowledgeAndAskForProposals());
                        break;
                }
            }
        }

        return RANDOM_ACTION_CHOOSER.chooseRandom(setOfNextActions);
    }

    private MoveResponse getResponseToLastMoveDiscussed() {
        var currentDiscussion = getCurrentDiscussion();
        var lastMoveDiscussed = currentDiscussion.getLastMoveDiscussed();
        var pieceContext = agent.getPieceContext();
        return pieceContext.getPersonality().getResponseToMoves(pieceContext.getMyPiece(), Collections.singleton(lastMoveDiscussed), pieceContext.getGameState()).iterator().next();
    }

    private boolean containsMoveICanPerform(Set<MoveResponse> moveResponses) {
        var myPos = agent.getPieceContext().getMyPiece().getPosition();
        return moveResponses.stream()
                .map(MoveResponse::getMove)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .anyMatch(m -> m.getSource().equals(myPos));
    }

    private ConversationMessage generateQuip() {
        // TODO
        return new ConversationMessage("lil quippy", agent.getAID());
    }

    private Set<MoveResponse> getResponsesToAllMoves() {
        var pieceContext = agent.getPieceContext();
        var gameState = pieceContext.getGameState();
        var allPossibleMoves = gameState.getAllLegalMoves();
        return pieceContext.getPersonality().getResponseToMoves(pieceContext.getMyPiece(), allPossibleMoves, gameState);
    }

    private TurnDiscussion getCurrentDiscussion() {
        return turnDiscussions.peekLast();
    }
}
