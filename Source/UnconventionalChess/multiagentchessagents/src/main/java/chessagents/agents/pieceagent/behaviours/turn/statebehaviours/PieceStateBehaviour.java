package chessagents.agents.pieceagent.behaviours.turn.statebehaviours;

import chessagents.agents.pieceagent.PieceContext;
import chessagents.agents.pieceagent.behaviours.turn.PieceState;
import chessagents.agents.pieceagent.behaviours.turn.PieceTransition;
import chessagents.agents.pieceagent.events.TransitionEvent;
import chessagents.agents.pieceagent.PieceAgent;
import chessagents.agents.pieceagent.planner.PieceAction;
import jade.core.behaviours.SimpleBehaviour;
import jade.util.Logger;

public abstract class PieceStateBehaviour extends SimpleBehaviour {

    private final PieceState pieceState;
    protected final Logger logger = Logger.getMyLogger(getClass().getName());
    protected final PieceContext pieceContext;
    protected PieceAction pieceAction;

    /**
     * Constructor ensures that each piece state behaviour corresponds to a piece state enum value, and that
     * only PieceAgents execute this behaviour.
     *
     * @param pieceAgent pieceAgent that will execute this behaviour
     * @param pieceState state this behaviour handles
     */
    protected PieceStateBehaviour(PieceContext pieceContext, PieceAgent pieceAgent, PieceState pieceState) {
        super(pieceAgent);
        this.pieceContext = pieceContext;
        this.pieceState = pieceState;
    }

    @Override
    public PieceAgent getAgent() {
        return (PieceAgent) myAgent;
    }

    /**
     * On start, each state logs its name, and the event is cleared
     */
    @Override
    public final void onStart() {
        logCurrentState();
        pieceAction = null;
        initialiseState();
        super.onStart();
    }

    /**
     * Empty method that can be overridden to allow extending classes to reset any variables
     * on revisiting the state
     */
    protected void initialiseState() {

    }

    /**
     * State behaviour is likely complete once some event or outcome has been reached
     *
     * @return true if state behaviour is complete
     */
    @Override
    public final boolean done() {
        return pieceAction != null;
    }

    /**
     * On end, each state behaviour needs to return the int value for the transition, and also
     * perform the action on the given state
     *
     * @return ordinal value of the next transition to take
     */
    @Override
    public final int onEnd() {
        int transition = 0;

        if (pieceAction != null) {
            transition = pieceAction.getTransition().ordinal();
            pieceContext.performAction(pieceAction);
        }

        return transition;
    }

    /**
     * Logs the current state name
     */
    private void logCurrentState() {
        logger.info("STATE: " + pieceState.name());
    }

    /**
     * Sets the event occurred at the end of the execution of this behaviour
     *
     * @param pieceAction action to be performed during this state
     */
    public final void setChosenAction(PieceAction pieceAction) {
        this.pieceAction = pieceAction;
    }
}
