package chessagents.agents.pieceagent.behaviours.turn.statebehaviours;

import chessagents.agents.pieceagent.behaviours.turn.PieceState;
import chessagents.agents.pieceagent.behaviours.turn.PieceTransition;
import chessagents.agents.pieceagent.events.TransitionEvent;
import chessagents.agents.pieceagent.PieceAgent;
import jade.core.behaviours.SimpleBehaviour;
import jade.util.Logger;

public abstract class PieceStateBehaviour extends SimpleBehaviour {

    private final PieceState pieceState;
    protected final Logger logger = Logger.getMyLogger(getClass().getName());
    protected TransitionEvent transitionEvent;

    /**
     * Constructor ensures that each piece state behaviour corresponds to a piece state enum value, and that
     * only PieceAgents execute this behaviour.
     *
     * @param pieceAgent pieceAgent that will execute this behaviour
     * @param pieceState state this behaviour handles
     */
    protected PieceStateBehaviour(PieceAgent pieceAgent, PieceState pieceState) {
        super(pieceAgent);
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
        transitionEvent = null;
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
        return transitionEvent != null;
    }

    /**
     * On end, each state behaviour needs to return the int value for the transition, and also
     * inform the agent of each event so it can update its personality, plan, and social understanding.
     *
     * @return ordinal value of the next transition to take
     */
    @Override
    public final int onEnd() {
        int transition = 0;

        if (transitionEvent != null) {
            ((PieceAgent) myAgent).experienceEvent(transitionEvent);
            transition = transitionEvent.getTransition().ordinal();
        }

        return transition;
    }

    /**
     * Logs the current state name
     */
    public final void logCurrentState() {
        logger.info("STATE: " + pieceState.name());
    }

    /**
     * Sets the event occurred at the end of the execution of this behaviour
     *
     * @param transitionEvent event
     */
    public final void setTransitionEvent(TransitionEvent transitionEvent) {
        this.transitionEvent = transitionEvent;
    }

    /**
     * Overloaded setter that creates the basic event using the given transition
     *
     * @param transition transition that will occur next
     */
    public final void setEvent(PieceTransition transition) {
        this.transitionEvent = new TransitionEvent(transition);
    }

}
