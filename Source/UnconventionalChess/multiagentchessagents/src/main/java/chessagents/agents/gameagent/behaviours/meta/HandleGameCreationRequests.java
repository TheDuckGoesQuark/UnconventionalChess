package chessagents.agents.gameagent.behaviours.meta;

import chessagents.agents.gameagent.GameAgent;
import chessagents.GameContext;
import chessagents.agents.gameagent.GameAgentContext;
import chessagents.ontology.ChessOntology;
import chessagents.ontology.schemas.actions.CreateGame;
import chessagents.ontology.schemas.concepts.Game;
import chessagents.ontology.schemas.predicates.BeingCreated;
import jade.content.ContentElementList;
import jade.content.Predicate;
import jade.content.lang.Codec;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.content.onto.basic.Done;
import jade.content.onto.basic.TrueProposition;
import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.util.Logger;

import static chessagents.agents.gameagent.GameStatus.*;

/**
 *
 */
public class HandleGameCreationRequests extends SimpleBehaviour {

    private enum State {
        WAITING_FOR_MESSAGE,
        PREPARE_RESPONSE,
        SEND_RESPONSE,
        PREPARE_RESULT_NOTIFICATION,
        SEND_RESULT_NOTIFICATION,
        DONE
    }

    private static final Logger LOGGER = Logger.getMyLogger(HandleGameCreationRequests.class.getName());
    private static final String REQUEST_KEY = "_REQUEST";
    private static final String RESPONSE_KEY = "_RESPONSE";
    private static final String RESULT_NOTIFICATION_KEY = "_RESULT_NOTIFICATION";
    private static final MessageTemplate mt = MessageTemplate.and(
            MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST),
            MessageTemplate.MatchOntology(ChessOntology.ONTOLOGY_NAME)
    );

    private final GameAgentContext context;
    private State state = State.WAITING_FOR_MESSAGE;

    public HandleGameCreationRequests(Agent a, GameAgentContext context) {
        super(a);
        this.context = context;
    }

    /**
     * Called when agent receives a request to create game.
     *
     * @param request request
     * @return response to request message (agree/reject)
     * @throws NotUnderstoodException
     * @throws RefuseException
     */
    private ACLMessage prepareResponse(ACLMessage request) throws NotUnderstoodException, RefuseException {
        LOGGER.info("Received request");
        var reply = request.createReply();
        var action = extractAction(request);
        Game game;

        var gameStatus = context.getGameStatus();
        switch (gameStatus) {
            case NOT_EXIST:
                LOGGER.info("Agreeing to request to create game");
                createAgreeResponse(action, reply);
                game = ((CreateGame) (action.getAction())).getGame();
                ((GameAgent) myAgent).createGame(game);
                context.setGameStatus(BEING_CREATED);
                break;
            case BEING_CREATED:
                LOGGER.info("Can't create game, already being created");
                game = ((CreateGame) (action.getAction())).getGame();
                createRefuseResponse(action, reply, new BeingCreated(game));
                break;
            case READY:
                LOGGER.info("Can't create game, already created");
                createRefuseResponse(action, reply, new Done(action));
                break;
        }

        return reply;
    }

    /**
     * Creates a refuse response to perform the given action because of the given reason
     *
     * @param action action being refused
     * @param reply  reply message to populate
     * @param reason reason for not performing action
     * @throws RefuseException if unable to even create reply
     */
    private void createRefuseResponse(Action action, ACLMessage reply, Predicate reason) throws RefuseException {
        try {
            var contentList = new ContentElementList();
            contentList.add(action);
            contentList.add(reason);
            myAgent.getContentManager().fillContent(reply, contentList);
            reply.setPerformative(ACLMessage.REFUSE);
        } catch (Codec.CodecException | OntologyException e) {
            reply.setContent("Unable to create refuse message: " + e.getMessage());
            reply.setPerformative(ACLMessage.FAILURE);
            throw new RefuseException(reply);
        }
    }

    /**
     * Creates agree response to perform the given action
     *
     * @param action action being agreed to
     * @param reply  reply to populate
     * @throws RefuseException if unable to create agree message
     */
    private void createAgreeResponse(Action action, ACLMessage reply) throws RefuseException {
        try {
            var contentList = new ContentElementList();
            contentList.add(action);
            contentList.add(new TrueProposition());

            myAgent.getContentManager().fillContent(reply, contentList);

            reply.setPerformative(ACLMessage.AGREE);
        } catch (Codec.CodecException | OntologyException e) {
            reply.setContent("Unable to create agree response: " + e.getMessage());
            reply.setPerformative(ACLMessage.FAILURE);
            throw new RefuseException(reply);
        }
    }

    /**
     * Attempts to extract action being requested from message
     *
     * @param request request to extract action from
     * @return extracted action
     * @throws NotUnderstoodException if unable to parse action from message
     */
    private Action extractAction(ACLMessage request) throws NotUnderstoodException {
        final Action action;
        try {
            action = (Action) myAgent.getContentManager().extractContent(request);
        } catch (Codec.CodecException | OntologyException e) {
            throw new NotUnderstoodException(request);
        }
        return action;
    }

    /**
     * Called when an agree or no response is sent in reply to request.
     *
     * @param request  request made
     * @param response response sent to request
     * @return message informing requester of result of execution
     * @throws FailureException if execution failed
     */
    private ACLMessage prepareResultNotification(ACLMessage request, ACLMessage response) throws FailureException {
        var resultReply = request.createReply();

        final Action action;
        try {
            action = extractAction(request);
        } catch (NotUnderstoodException e) {
            throw new FailureException(e.getACLMessage());
        }

        createSuccessResponse(action, resultReply);

        return resultReply;
    }

    /**
     * Populates the response when action was successful
     *
     * @param a        action that has been successfully carried out
     * @param response message to populate
     * @throws FailureException if unable to populate message
     */
    private void createSuccessResponse(Action a, ACLMessage response) throws FailureException {
        var contentList = new ContentElementList();
        var done = new Done(a);
        contentList.add(done);
        response.setPerformative(ACLMessage.INFORM);

        try {
            myAgent.getContentManager().fillContent(response, contentList);
        } catch (Codec.CodecException | OntologyException e) {
            throw new FailureException("Unable to create success response");
        }
    }

    @Override
    public void action() {
        ACLMessage request = null, response = null, resultNotifcation = null;
        switch (this.state) {
            case WAITING_FOR_MESSAGE:
                if (receiveMessage()) {
                    LOGGER.info("Received request to create game");
                    state = State.PREPARE_RESPONSE;
                } else {
                    block();
                }
                break;
            case PREPARE_RESPONSE:
                request = (ACLMessage) getDataStore().get(REQUEST_KEY);
                try {
                    response = prepareResponse(request);
                } catch (NotUnderstoodException e) {
                    response = request.createReply();
                    response.setContent(e.getMessage());
                    response.setPerformative(ACLMessage.NOT_UNDERSTOOD);
                } catch (RefuseException e) {
                    response = request.createReply();
                    response.setContent(e.getMessage());
                    response.setPerformative(ACLMessage.REFUSE);
                } finally {
                    getDataStore().put(RESPONSE_KEY, response);
                    state = State.SEND_RESPONSE;
                }
                break;
            case SEND_RESPONSE:
                response = (ACLMessage) getDataStore().get(RESPONSE_KEY);
                if (response != null) {
                    myAgent.send(response);

                    if (response.getPerformative() == ACLMessage.AGREE) {
                        state = State.PREPARE_RESULT_NOTIFICATION;
                        myAgent.addBehaviour(new HandlePieceListRequests((GameAgent) myAgent, context));
                    } else {
                        state = State.WAITING_FOR_MESSAGE;
                    }
                } else {
                    state = State.WAITING_FOR_MESSAGE;
                }
                break;
            case PREPARE_RESULT_NOTIFICATION:
                var gameStatus = context.getGameStatus();
                if (gameStatus != READY) {
//                    block(); TODO this might fix inconsistent start behaviour
                    break;
                    // repeat until ready
                }

                request = (ACLMessage) getDataStore().get(REQUEST_KEY);
                response = (ACLMessage) getDataStore().get(RESPONSE_KEY);

                try {
                    resultNotifcation = prepareResultNotification(request, response);
                } catch (FailureException e) {
                    resultNotifcation = request.createReply();
                    resultNotifcation.setContent(e.getMessage());
                    resultNotifcation.setPerformative(ACLMessage.FAILURE);
                } finally {
                    getDataStore().put(RESULT_NOTIFICATION_KEY, resultNotifcation);
                    state = State.SEND_RESULT_NOTIFICATION;
                }
                break;
            case SEND_RESULT_NOTIFICATION:
                resultNotifcation = (ACLMessage) getDataStore().get(RESULT_NOTIFICATION_KEY);

                if (resultNotifcation != null) {
                    myAgent.send(resultNotifcation);
                }
                state = State.DONE;
                break;
        }
    }

    private boolean receiveMessage() {
        var request = myAgent.receive(mt);
        var requestReceived = request != null;
        if (requestReceived) {
            getDataStore().put(REQUEST_KEY, request);
        }
        return requestReceived;
    }

    @Override
    public boolean done() {
        return state == State.DONE;
    }
}
