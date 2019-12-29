package chessagents.agents.gameagent.behaviours.meta;

import chessagents.agents.gameagent.GameAgent;
import chessagents.agents.gameagent.GameAgentContext;
import chessagents.agents.gameagent.GameCreationStatus;
import chessagents.agents.pieceagent.PieceAgent;
import chessagents.ontology.schemas.concepts.Colour;
import chessagents.ontology.schemas.concepts.ChessPiece;
import chessagents.ontology.schemas.concepts.PieceConfiguration;
import chessagents.ontology.schemas.concepts.Position;
import jade.content.OntoAID;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.ContainerID;
import jade.core.behaviours.*;
import jade.domain.FIPANames;
import jade.domain.JADEAgentManagement.CreateAgent;
import jade.domain.JADEAgentManagement.JADEManagementOntology;
import jade.lang.acl.ACLMessage;
import jade.proto.AchieveREInitiator;
import jade.util.Logger;
import jade.wrapper.ControllerException;

import java.util.*;

public class SpawnPieceAgents extends SimpleBehaviour {

    // TODO make this variable in GUI
    private static final int MAX_DEBATE_CYCLES = 10;

    private enum CreationState {
        INIT, CREATING, CREATED
    }

    private static final Logger logger = Logger.getMyLogger(SpawnPieceAgents.class.getName());
    private final GameAgentContext context;
    private final Set<PieceConfiguration> pieceConfigs;
    private CreationState creationState = CreationState.INIT;

    public SpawnPieceAgents(GameAgent gameAgent, GameAgentContext context, Set<PieceConfiguration> pieceConfigs) {
        super(gameAgent);
        this.context = context;
        this.pieceConfigs = pieceConfigs;
    }

    private String generatePieceAgentName(String pieceType, Colour colour, Position startingSquare) {
        return pieceConfigs.stream().filter(p -> p.getStartingPosition().equals(startingSquare.getCoordinates())).findFirst().get().getName();
    }

    @Override
    public void action() {
        switch (creationState) {
            case INIT:
                addPieceCreationBehaviours();
                creationState = CreationState.CREATING;
                break;
            case CREATING:
                if (context.getGameCreationStatus() == GameCreationStatus.READY) {
                    creationState = CreationState.CREATED;
                } else {
                    block();
                }
                break;
        }
    }

    private void addPieceCreationBehaviours() {
        var sequence = new SequentialBehaviour();

        for (PieceConfiguration pieceConfig : pieceConfigs) {
            sequence.addSubBehaviour(createSpawnPieceBehaviour(pieceConfig));
        }

        sequence.addSubBehaviour(new OneShotBehaviour() {
            @Override
            public void action() {
                context.setGameCreationStatus(GameCreationStatus.READY);
            }
        });

        myAgent.addBehaviour(sequence);
    }

    private Behaviour createSpawnPieceBehaviour(PieceConfiguration pieceConfiguration) {
        return context.getGameState().getPieceAtPosition(new Position(pieceConfiguration.getStartingPosition()))
                .map(this::createSpawnPieceBehaviour).get();
    }

    private Behaviour createSpawnPieceBehaviour(ChessPiece piece) {
        final String agentName = generatePieceAgentName(piece.getType(), piece.getColour(), piece.getPosition());
        final String agentClassName = PieceAgent.class.getName();

        String containerName;
        try {
            containerName = myAgent.getContainerController().getContainerName();
        } catch (ControllerException e) {
            logger.warning("Unable to get current container name, using default. Reason: " + e.getMessage());
            containerName = "chess-container";
        }

        final CreateAgent createAgent = new CreateAgent();
        createAgent.setAgentName(agentName);
        createAgent.setClassName(agentClassName);
        createAgent.setContainer(new ContainerID(containerName, null));

        // add arguments for piece
        createAgent.addArguments(piece.getPosition().getCoordinates());
        createAgent.addArguments(piece.getColour().getColour());
        createAgent.addArguments(myAgent.getAID().getName());
        createAgent.addArguments(Integer.toString(context.getGameState().getGameId()));
        createAgent.addArguments(Integer.toString(MAX_DEBATE_CYCLES));
        // Ugly but it'll do, should really serialize these somehow
        createAgent.addArguments(pieceConfigs);

        final Action requestAction = new Action(myAgent.getAMS(), createAgent);
        final ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
        request.addReceiver(myAgent.getAMS());
        request.setOntology(JADEManagementOntology.getInstance().getName());

        myAgent.getContentManager().registerLanguage(new SLCodec(),
                FIPANames.ContentLanguage.FIPA_SL);

        myAgent.getContentManager().registerOntology(JADEManagementOntology.getInstance());

        request.setLanguage(FIPANames.ContentLanguage.FIPA_SL);
        request.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);

        Behaviour requestCreateAgentBehaviour = null;

        try {
            myAgent.getContentManager().fillContent(request, requestAction);
            requestCreateAgentBehaviour = new AchieveREInitiator(myAgent, request) {
                protected void handleInform(ACLMessage inform) {
                    logger.info("Agent " + agentName + " successfully created");
                    var aid = new AID(agentName, AID.ISLOCALNAME);
                    var ontoAid = new OntoAID(aid.getLocalName(), AID.ISLOCALNAME);
                    piece.setAgentAID(ontoAid);
                }

                protected void handleFailure(ACLMessage failure) {
                    logger.warning("Error creating agent " + agentName + ":" + failure.getContent());
                    // TODO inform user of failure, cancel game creation and cleanup
                }
            };
        } catch (Exception e) {
            logger.warning("Error creating agent " + agentName + ":" + e.getMessage());
        }

        return requestCreateAgentBehaviour;
    }

    @Override
    public boolean done() {
        return creationState == CreationState.CREATED;
    }

}
