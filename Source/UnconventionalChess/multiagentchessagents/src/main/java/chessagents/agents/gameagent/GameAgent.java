package chessagents.agents.gameagent;

import chessagents.agents.gameagent.behaviours.ProcessMove;
import chessagents.agents.pieceagent.*;
import chessagents.ontology.ChessOntology;
import com.github.bhlangonijr.chesslib.*;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.content.onto.basic.Action;
import jade.core.Agent;
import jade.core.ContainerID;
import jade.domain.FIPANames;
import jade.domain.JADEAgentManagement.CreateAgent;
import jade.domain.JADEAgentManagement.JADEManagementOntology;
import jade.lang.acl.ACLMessage;
import jade.proto.AchieveREInitiator;
import jade.util.Logger;
import jade.wrapper.ControllerException;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

public class GameAgent extends Agent {

    private Logger logger = Logger.getMyLogger(this.getClass().getName());
    private GameAgentProperties properties;
    private Codec codec = new SLCodec();
    private Ontology ontology = ChessOntology.getInstance();
    private Board board;

    @Override
    protected void setup() {
        super.setup();
        addBehaviour(new ProcessMove());
        properties = (GameAgentProperties) getArguments()[0];

        getContentManager().registerLanguage(codec);
        getContentManager().registerOntology(ontology);

        setupGame();
    }

    private void setupGame() {
        board = new Board();

        if (!properties.isHumanPlays()) {
            spawnPieceAgentsForSide(Side.WHITE);
            spawnPieceAgentsForSide(Side.BLACK);
        } else {
            if (properties.isHumanPlaysAsWhite()) {
                spawnPieceAgentsForSide(Side.BLACK);
            } else {
                spawnPieceAgentsForSide(Side.WHITE);
            }
        }
    }

    private void spawnPieceAgentsForSide(Side colour) {
        Stream.of(Square.values())
                .map(sq -> board.getPiece(sq))
                .filter(piece -> !piece.equals(Piece.NONE))
                .filter(piece -> piece.getPieceSide().equals(colour))
                .forEach(this::spawnPieceAgent);
    }

    private Optional<String> getPieceClassname(Piece piece) {
        switch (piece.getPieceType()) {
            case PAWN:
                return Optional.ofNullable(PawnAgent.class.getName());
            case KNIGHT:
                return Optional.ofNullable(KnightAgent.class.getName());
            case BISHOP:
                return Optional.ofNullable(BishopAgent.class.getName());
            case ROOK:
                return Optional.ofNullable(RookAgent.class.getName());
            case QUEEN:
                return Optional.ofNullable(QueenAgent.class.getName());
            case KING:
                return Optional.ofNullable(KingAgent.class.getName());
            case NONE:
            default:
                return Optional.empty();
        }
    }

    private void spawnPieceAgent(Piece piece) {
        final CreateAgent createAgent = new CreateAgent();
        final String agentName = piece.getPieceSide().name() + "-" + piece.getPieceType().name();
        final String agentClassName = getPieceClassname(piece)
                .orElseThrow(IllegalArgumentException::new);

        String containerName;
        try {
            containerName = getContainerController().getContainerName();
        } catch (ControllerException e) {
            logger.warning("Unable to get current container name, using default. Reason: " + e.getMessage());
            containerName = "chess-container";
        }

        createAgent.setAgentName(agentName);
        createAgent.setClassName(agentClassName);
        createAgent.setContainer(new ContainerID(containerName, null));
        Action requestAction = new Action(getAMS(), createAgent);
        ACLMessage request = new ACLMessage(ACLMessage.REQUEST);

        request.addReceiver(getAMS());
        request.setOntology(JADEManagementOntology.getInstance().getName());

        getContentManager().registerLanguage(new SLCodec(),
                FIPANames.ContentLanguage.FIPA_SL);

        getContentManager().registerOntology(JADEManagementOntology.getInstance());

        request.setLanguage(FIPANames.ContentLanguage.FIPA_SL);
        request.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
        try {
            getContentManager().fillContent(request, requestAction);
            addBehaviour(new AchieveREInitiator(this, request) {
                protected void handleInform(ACLMessage inform) {
                    logger.info("Agent " + agentName + "successfully created");
                }

                protected void handleFailure(ACLMessage failure) {
                    logger.warning("Error creating agent " + agentName + ".");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
