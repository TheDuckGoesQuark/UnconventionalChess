package chessagents.ontology;

import chessagents.ontology.schemas.actions.CreateGame;
import chessagents.ontology.schemas.actions.MakeMove;
import chessagents.ontology.schemas.concepts.*;
import chessagents.ontology.schemas.predicates.*;
import jade.content.onto.BasicOntology;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.schema.*;

public class ChessOntology extends Ontology {

    public static final String ONTOLOGY_NAME = "Chess-Ontology";

    // Concepts
    public static final String PIECE = "Piece";
    public static final String PIECE_AGENT = "Agent";
    public static final String PIECE_TYPE = "Type";
    public static final String PIECE_COLOUR = "Colour";

    public static final String POSITION = "Position";
    private static final String POSITION_COORDINATES = "Coordinates";

    public static final String MOVE = "Move";
    public static final String MOVE_SOURCE = "Source";
    public static final String MOVE_TARGET = "Target";

    public static final String COLOUR = "Colour";
    public static final String COLOUR_COLOUR = "Colour";

    public static final String GAME = "Game";
    public static final String GAME_ID = "ID";

    // Predicates
    public static final String CAN_MOVE = "Can Move";
    public static final String CAN_MOVE_PIECE = "Piece";

    public static final String CAN_MAKE_MOVE = "Can Make Move";
    public static final String CAN_MAKE_MOVE_PIECE = "Piece";
    public static final String CAN_MAKE_MOVE_MOVE = "Move";

    public static final String CAN_CAPTURE = "Can Capture";
    public static final String CAN_CAPTURE_ATTACKER = "Attacker";
    public static final String CAN_CAPTURE_VICTIM = "Victim";

    public static final String IS_CAPTURED = "Is Captured";
    public static final String IS_CAPTURED_PIECE = "Piece";

    public static final String BEING_CREATED = "Being Created";
    public static final String BEING_CREATED_GAME = "Game";

    // Actions
    public static final String MAKE_MOVE = "Make Move";
    public static final String MAKE_MOVE_MOVE = "Move";

    public static final String CREATE_GAME = "Create Game";

    private static final ChessOntology instance = new ChessOntology();

    private ChessOntology() {
        super(ONTOLOGY_NAME, BasicOntology.getInstance());

        try {
            // Concepts
            final ConceptSchema pieceSchema = new ConceptSchema(PIECE);
            pieceSchema.add(PIECE_AGENT, (ConceptSchema) getSchema(BasicOntology.AID));
            pieceSchema.add(PIECE_TYPE, (PrimitiveSchema) getSchema(BasicOntology.STRING));
            pieceSchema.add(PIECE_COLOUR, (ConceptSchema) getSchema(COLOUR));
            add(pieceSchema, Piece.class);

            final ConceptSchema positionSchema = new ConceptSchema(POSITION);
            positionSchema.add(POSITION_COORDINATES, (PrimitiveSchema) getSchema(BasicOntology.STRING));
            add(positionSchema, Position.class);

            final ConceptSchema moveSchema = new ConceptSchema(MOVE);
            moveSchema.add(MOVE_SOURCE, (ConceptSchema) getSchema(POSITION));
            moveSchema.add(MOVE_TARGET, (ConceptSchema) getSchema(POSITION));
            add(moveSchema, Move.class);

            final ConceptSchema gameSchema = new ConceptSchema(GAME);
            gameSchema.add(GAME_ID, (ConceptSchema) getSchema(BasicOntology.INTEGER));
            add(gameSchema, Game.class);

            // Predicates
            final PredicateSchema canMoveSchema = new PredicateSchema(CAN_MOVE);
            canMoveSchema.add(CAN_MOVE_PIECE, getSchema(PIECE));
            add(canMoveSchema, CanMove.class);

            final PredicateSchema canCaptureSchema = new PredicateSchema(CAN_CAPTURE);
            canCaptureSchema.add(CAN_CAPTURE_ATTACKER, getSchema(PIECE));
            canCaptureSchema.add(CAN_CAPTURE_VICTIM, getSchema(PIECE));
            add(canCaptureSchema, CanCapture.class);

            final PredicateSchema isCapturedSchema = new PredicateSchema(IS_CAPTURED);
            isCapturedSchema.add(IS_CAPTURED_PIECE, getSchema(PIECE));
            add(isCapturedSchema, IsCaptured.class);

            final PredicateSchema beingCreatedSchema = new PredicateSchema(BEING_CREATED);
            beingCreatedSchema.add(BEING_CREATED_GAME, getSchema(GAME));
            add(beingCreatedSchema, BeingCreated.class);

            // Actions
            final AgentActionSchema makeMoveSchema = new AgentActionSchema(MAKE_MOVE);
            makeMoveSchema.add(MAKE_MOVE_MOVE, (ConceptSchema) getSchema(MOVE));
            add(makeMoveSchema, MakeMove.class);

            final AgentActionSchema createGameSchema = new AgentActionSchema(CREATE_GAME);
            createGameSchema.add(GAME, (ConceptSchema) getSchema(GAME));
            add(createGameSchema, CreateGame.class);

        } catch (OntologyException e) {
            e.printStackTrace();
        }
    }

    public static ChessOntology getInstance() {
        return instance;
    }


}
