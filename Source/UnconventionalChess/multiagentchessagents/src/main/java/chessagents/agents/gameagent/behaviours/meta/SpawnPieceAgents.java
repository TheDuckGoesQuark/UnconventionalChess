package chessagents.agents.gameagent.behaviours.meta;

import chessagents.agents.gameagent.GameAgent;
import chessagents.agents.gameagent.GameAgentContext;
import chessagents.agents.gameagent.GameCreationStatus;
import chessagents.agents.pieceagent.PieceAgent;
import chessagents.ontology.schemas.concepts.Colour;
import chessagents.ontology.schemas.concepts.ChessPiece;
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

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class SpawnPieceAgents extends SimpleBehaviour {

    // TODO make this variable in GUI
    private static final int MAX_DEBATE_CYCLES = 2;

    private enum CreationState {
        INIT, CREATING, CREATED
    }

    private static final Logger logger = Logger.getMyLogger(SpawnPieceAgents.class.getName());
    private final GameAgentContext context;
    private CreationState creationState = CreationState.INIT;

    public SpawnPieceAgents(GameAgent gameAgent, GameAgentContext context) {
        super(gameAgent);
        this.context = context;
    }

    private String generatePieceAgentName(String pieceType, String colour, String startingSquare) {
        return context.getGameContext().getGameId() + "-" + startingSquare + "-" + colour + "-" + pieceType;
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
        final Set<String> agentColours = new HashSet<>(2);

        // Determine colours to generate agents for
        if (!context.getGameProperties().isHumanPlays()) {
            agentColours.add(Colour.WHITE);
            agentColours.add(Colour.BLACK);
        } else {
            if (context.getGameProperties().isHumanPlaysAsWhite()) {
                agentColours.add(Colour.BLACK);
            } else {
                agentColours.add(Colour.WHITE);
            }
        }

        // build up sequence of spawn agent behaviours followed by an update to the game status
        var sequence = new SequentialBehaviour();

        for (String agentColour : agentColours) {
            Set<Behaviour> behaviours = getBehavioursForSpawningAllPiecesOnSide(agentColour);
            behaviours.forEach(sequence::addSubBehaviour);
        }

        sequence.addSubBehaviour(new OneShotBehaviour() {
            @Override
            public void action() {
                context.setGameCreationStatus(GameCreationStatus.READY);
            }
        });

        myAgent.addBehaviour(sequence);
    }

    @Override
    public boolean done() {
        return creationState == CreationState.CREATED;
    }

    private Set<Behaviour> getBehavioursForSpawningAllPiecesOnSide(String colour) {
        logger.info("Spawning agents for side " + colour);
        var board = context.getGameContext().getBoard();
        return board.getPositionsOfAllPiecesForColour(colour)
                .stream()
                .map(sq -> createSpawnPieceBehaviour(sq, colour, board.getPieceTypeAtSquare(sq)))
                .collect(Collectors.toSet());
    }

    private Behaviour createSpawnPieceBehaviour(String startingSquare, String colour, String type) {
        final String agentName = generatePieceAgentName(type, colour, startingSquare);
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
        createAgent.addArguments(startingSquare);
        createAgent.addArguments(colour);
        createAgent.addArguments(myAgent.getAID().getName());
        createAgent.addArguments(Integer.toString(context.getGameContext().getGameId()));
        createAgent.addArguments(Integer.toString(MAX_DEBATE_CYCLES));

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
                    var pieces = context.getGameContext().getAidToPiece();
                    var aid = new AID(agentName, AID.ISLOCALNAME);
                    var ontoAid = new OntoAID(aid.getLocalName(), AID.ISLOCALNAME);
                    var colourConcept = new Colour(colour);
                    var position = new Position(startingSquare);
                    pieces.put(aid, new ChessPiece(ontoAid, colourConcept, type, position));
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
}
