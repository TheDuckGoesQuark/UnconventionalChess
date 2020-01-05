package chessagents.agents.pieceagent.argumentation;

import chessagents.agents.commonbehaviours.RequestGameAgentMove;
import chessagents.agents.pieceagent.PieceAgent;
import chessagents.agents.pieceagent.argumentation.discussionactions.*;
import chessagents.agents.pieceagent.argumentation.reactions.*;
import chessagents.ontology.schemas.actions.MakeMove;
import chessagents.ontology.schemas.concepts.Colour;
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

        // if not our turn, then we can react to last move or just say things
        if (!isMyTurnToGo()) {
            conversationMessage = notMyTurnAction();
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

            // if there are moves to respond to then we can do that too
            if (turnDiscussions.size() > 1) {
                setOfNextActions.addAll(reactToEnemyMoveActions());
            }
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
                        if (lastMessageSent.getMoveResponse().get().getOpinion() == Opinion.LIKE) {
                            setOfNextActions.add(new VoiceOpinionProposeCompromise(agent, currentDiscussion, response, lastMessageSent));
                        }
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

    private ConversationMessage notMyTurnAction() {
        if (turnDiscussions.size() == 1) {
            // nothing to react to yet
            return new Quip(agent, getCurrentDiscussion()).perform();
        } else {
            return RANDOM_ACTION_CHOOSER.chooseRandom(reactToOurMoveActions()).perform();
        }
    }

    /**
     * @return Produce a set of Conversation actions that are reactions to the move
     * we just made
     */
    private Set<ConversationAction> reactToOurMoveActions() {
        var actions = new HashSet<ConversationAction>();

        if (performedMoveWasCurrentlyBeingDiscussed()) {
            actions.add(new ReactLastMoveDiscussedPerformed(agent));
        } else if (performedMoveWasDiscussedAtSomePoint()) {
            actions.add(new ReactPreviouslyDiscussedMovePerformed(agent));
        } else {
            actions.add(new ReactUndiscussedMovePerformed(agent));
        }

        if (enemyPieceWasCaptured()) {
            actions.add(new ReactEnemyPieceCaptured(agent));
        }
        if (enemyPieceBecameThreatened()) {
            actions.add(new ReactEnemyPieceThreatened(agent));
        }
        if (friendlyPieceEscapedThreat()) {
            actions.add(new ReactFriendlyPieceEscaped(agent));
        }
        if (friendlyPieceBecameThreatened()) {
            actions.add(new ReactFriendlyPieceThreatenedOnOurMove(agent));
        }

        return actions;
    }

    private boolean friendlyPieceBecameThreatened() {
        var pieceContext = agent.getPieceContext();
        var history = pieceContext.getGameHistory();
        var previousState = history.getPreviousState();
        var currentState = pieceContext.getGameState();

        var myColour = pieceContext.getMyPiece().getColour();
        var previouslyThreatened = previousState.getThreatenedForColour(myColour);
        var nowThreatened = currentState.getThreatenedForColour(myColour);

        return nowThreatened.stream().anyMatch(p -> !previouslyThreatened.contains(p));
    }

    private boolean friendlyPieceEscapedThreat() {
        var pieceContext = agent.getPieceContext();
        var myColour = pieceContext.getMyPiece().getColour();
        return colourPieceEscapedThreat(myColour);
    }

    private boolean enemyPieceEscapedThreat() {
        var pieceContext = agent.getPieceContext();
        var enemyColour = pieceContext.getMyPiece().getColour().flip();
        return colourPieceEscapedThreat(enemyColour);
    }

    private boolean colourPieceEscapedThreat(Colour colour) {
        var pieceContext = agent.getPieceContext();
        var history = pieceContext.getGameHistory();
        var previousState = history.getPreviousState();
        var currentState = pieceContext.getGameState();

        var previouslyThreatened = previousState.getThreatenedForColour(colour);
        var nowThreatened = currentState.getThreatenedForColour(colour);

        return previouslyThreatened.stream().anyMatch(p -> !nowThreatened.contains(p));
    }

    private boolean enemyPieceBecameThreatened() {
        var pieceContext = agent.getPieceContext();
        var history = pieceContext.getGameHistory();
        var previousState = history.getPreviousState();
        var currentState = pieceContext.getGameState();

        var otherColour = pieceContext.getMyPiece().getColour().flip();
        // TODO change to check for new piece in set after move rather than just counting
        return previousState.getThreatenedForColour(otherColour).size()
                < currentState.getThreatenedForColour(otherColour).size();
    }

    private boolean enemyPieceWasCaptured() {
        var pieceContext = agent.getPieceContext();
        var history = pieceContext.getGameHistory();
        var previousState = history.getPreviousState();
        var currentState = pieceContext.getGameState();

        var otherColour = pieceContext.getMyPiece().getColour().flip();
        return previousState.getCapturedForColour(otherColour).size()
                < currentState.getCapturedForColour(otherColour).size();
    }

    private boolean performedMoveWasDiscussedAtSomePoint() {
        var performedMove = agent.getPieceContext().getGameHistory().getLastMove();
        var lastDiscussion = turnDiscussions.get(turnDiscussions.size() - 2);
        var discussedMoves = lastDiscussion.getPreviouslyDiscussedMoves();

        // start from two discussions prior
        for (int i = discussedMoves.size() - 2; i >= 0; i--) {
            if (performedMove.equals(discussedMoves.get(i))) return true;
        }

        return false;
    }

    private boolean performedMoveWasCurrentlyBeingDiscussed() {
        var performedMove = agent.getPieceContext().getGameHistory().getLastMove();
        var lastDiscussion = turnDiscussions.get(turnDiscussions.size() - 2);

        return performedMove.equals(lastDiscussion.getLastMoveDiscussed());
    }

    /**
     * @return Produce a set of Conversation actions that are reactions
     * to the move the enemy just made
     */
    private Set<ConversationAction> reactToEnemyMoveActions() {
        var actions = new HashSet<ConversationAction>();

        if (ourPieceWasCaptured()) {
            actions.add(new ReactFriendlyPieceCaptured(agent));
        }
        if (ourPieceBecameThreatened()) {
            actions.add(new ReactFriendlyPieceThreatenedOnTheirMove(agent));
        }
        if (enemyPieceEscapedThreat()) {
            actions.add(new ReactEnemyPieceEscaped(agent));
        }
        actions.add(new InsultEnemyMove(agent));
        actions.add(new ComplimentEnemyMove(agent));
        actions.add(new QuestionEnemyMove(agent));

        return actions;
    }

    private boolean ourPieceBecameThreatened() {
        var pieceContext = agent.getPieceContext();
        var history = pieceContext.getGameHistory();
        var previousState = history.getPreviousState();
        var currentState = pieceContext.getGameState();

        var myColour = pieceContext.getMyPiece().getColour().flip();
        return previousState.getThreatenedForColour(myColour).size()
                < currentState.getThreatenedForColour(myColour).size();
    }

    private boolean ourPieceWasCaptured() {
        var pieceContext = agent.getPieceContext();
        var history = pieceContext.getGameHistory();
        var previousState = history.getPreviousState();
        var currentState = pieceContext.getGameState();

        var myPiece = pieceContext.getMyPiece().getColour();
        return previousState.getCapturedForColour(myPiece).size()
                < currentState.getCapturedForColour(myPiece).size();
    }

    private Set<MoveResponse> getResponsesToAllMoves() {
        var pieceContext = agent.getPieceContext();
        var gameState = pieceContext.getGameState();
        var allPossibleMoves = gameState.getAllLegalMoves();
        return pieceContext.getPersonality().getResponseToMoves(pieceContext.getMyPiece(), allPossibleMoves, gameState);
    }

    private boolean isMyTurnToGo() {
        return agent.getPieceContext().isMyTurnToGo();
    }

    private TurnDiscussion getCurrentDiscussion() {
        return turnDiscussions.peekLast();
    }
}
