package chessagents.agents.pieceagent.argumentation;

import chessagents.agents.commonbehaviours.RequestGameAgentMove;
import chessagents.agents.pieceagent.PieceAgent;
import chessagents.agents.pieceagent.argumentation.actions.*;
import chessagents.ontology.schemas.actions.MakeMove;
import chessagents.ontology.schemas.concepts.PieceMove;
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
    private final RandomUtil<MoveResponse> RANDOM_RESPONSE_CHOOSER = new RandomUtil<>();

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
    public PieceMove getLastMoveDiscussed() {
        return getCurrentDiscussion().getLastMoveDiscussed();
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
        var currentDiscussion = getCurrentDiscussion();
        var availableMoveResponses = getResponsesToAllMoves();
        var moveExistsThatICanPerform = containsMoveICanPerform(availableMoveResponses);

        if (moveExistsThatICanPerform) {
            var performMove = new PerformMove(agent, currentDiscussion);
            // just perform move if we've been talking for too long
            if (agent.getPieceContext().getMaxDebateCycle() < currentDiscussion.getNumberOfMessages()) {
                return performMove;
                // otherwise, low probability (1/5) we perform move to extend discussion
            } else if (RandomUtil.nextInt(5) == 4) {
                setOfNextActions.add(performMove);
            }
        }

        if (numberOfMessages == 0) {
            // first message of turn so we can discuss previous suggestions
            setOfNextActions.add(new ProposeMove(agent, currentDiscussion));
            setOfNextActions.add(new ProposeMoveWithJustification(agent, currentDiscussion));
            setOfNextActions.add(new InitialAskForProposals(agent, currentDiscussion));
        } else {
            if (currentDiscussion.proposalsCalledFor()) {
                // allow agent to propose new or revisit previous move
                setOfNextActions.add(new ProposeMove(agent, currentDiscussion));
                if (currentDiscussion.getNumberOfMovesDiscussed() > 1) {
                    setOfNextActions.add(new RevisitMove(agent, currentDiscussion));
                }
            } else {
                // react to previously proposed moves
                var response = getResponseToLastMoveDiscussed();
                switch (response.getOpinion()) {
                    case LIKE:
                    case DISLIKE:
                        setOfNextActions.add(new VoiceOpinion(agent, currentDiscussion, response));
                        setOfNextActions.add(new VoiceOpinionProposeAlternative(agent, currentDiscussion, response));
                        setOfNextActions.add(new VoiceOpinionWithJustification(agent, currentDiscussion, response));

                        // we can try to offer a compromise that appeals to more of our values
                        var lastMessageSent = currentDiscussion.getLastMessageSent();
                        setOfNextActions.add(new VoiceOpinionProposeCompromise(agent, currentDiscussion, response, lastMessageSent));
                        break;
                    case NEUTRAL:
                        setOfNextActions.add(new Acknowledge(agent, currentDiscussion, response));
                        setOfNextActions.add(new AskForProposals(agent, currentDiscussion));
                        setOfNextActions.add(new AcknowledgeAndAskForProposals(agent, currentDiscussion, response));
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

        // choose random from our responses to this move
        try {
            var responses = pieceContext.getPersonality().getResponseToMoves(pieceContext.getMyPiece(), Collections.singleton(lastMoveDiscussed), pieceContext.getGameState());
            return RANDOM_RESPONSE_CHOOSER.chooseRandom(responses);
        } catch (IllegalArgumentException e) {
            System.out.println("bad times");
            return null;
        }
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
        return new Quip(agent).perform();
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
