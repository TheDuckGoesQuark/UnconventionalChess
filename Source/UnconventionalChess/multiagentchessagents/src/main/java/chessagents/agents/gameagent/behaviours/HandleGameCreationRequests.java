package chessagents.agents.gameagent.behaviours;

import chessagents.agents.gameagent.GameAgent;
import chessagents.agents.gameagent.GameStatus;
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
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.SimpleAchieveREResponder;
import jade.util.Logger;

/**
 *
 */
public class HandleGameCreationRequests extends SimpleAchieveREResponder {

    private static final Logger LOGGER = Logger.getMyLogger(HandleGameCreationRequests.class.getName());
    private static final MessageTemplate mt = MessageTemplate.MatchOntology(FIPANames.InteractionProtocol.FIPA_REQUEST);
    private GameStatus gameStatus = GameStatus.NOT_EXIST;

    public HandleGameCreationRequests(GameAgent gameAgent) {
        super(gameAgent, mt);
    }

    /**
     * Called when agent receives a request to create game.
     *
     * @param request request
     * @return response to request message (agree/reject)
     * @throws NotUnderstoodException
     * @throws RefuseException
     */
    @Override
    protected ACLMessage prepareResponse(ACLMessage request) throws NotUnderstoodException, RefuseException {
        LOGGER.info("Received request");
        var reply = request.createReply();
        var action = extractAction(request);
        Game game;

        switch (gameStatus) {
            case NOT_EXIST:
                LOGGER.info("Agreeing to request to create game");
                createAgreeResponse(action, reply);
                gameStatus = GameStatus.BEING_CREATED;
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
    @Override
    protected ACLMessage prepareResultNotification(ACLMessage request, ACLMessage response) throws FailureException {
        var resultReply = request.createReply();

        final Action action;
        try {
            action = extractAction(request);
        } catch (NotUnderstoodException e) {
            throw new FailureException(e.getACLMessage());
        }

        var game = ((CreateGame) action.getAction()).getGame();
        var result = ((GameAgent) myAgent).createGame(game);

        if (result.isPresent()) {
            createSuccessResponse(action, resultReply);
        } else {
            resultReply.setPerformative(ACLMessage.FAILURE);
            throw new FailureException(resultReply);
        }

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

        try {
            myAgent.getContentManager().fillContent(response, contentList);
        } catch (Codec.CodecException | OntologyException e) {
            throw new FailureException("Unable to create success response");
        }
    }
}
