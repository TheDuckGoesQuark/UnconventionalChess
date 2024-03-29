package chessagents.ontology;

import chessagents.ontology.schemas.actions.BecomeSpeaker;
import chessagents.ontology.schemas.actions.CreateGame;
import chessagents.ontology.schemas.actions.MakeMove;
import chessagents.ontology.schemas.concepts.*;
import chessagents.ontology.schemas.predicates.*;
import com.github.bhlangonijr.chesslib.Piece;
import jade.content.Concept;
import jade.content.onto.BasicOntology;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.schema.*;

public class ChessOntology extends Ontology {

    public static final String ONTOLOGY_NAME = "Chess-Ontology";

    // Concepts
    public static final String POSITION = "Position";
    private static final String POSITION_COORDINATES = "Coordinates";

    public static final String MOVE = "Move";
    public static final String MOVE_SOURCE = "Source";
    public static final String MOVE_TARGET = "Target";

    public static final String COLOUR = "Colour";
    public static final String COLOUR_COLOUR = "Colour";

    public static final String GAME = "Game";
    public static final String GAME_ID = "GameId";

    public static final String PIECE = "Piece";
    public static final String PIECE_AGENT = "AgentAID";
    public static final String PIECE_TYPE = "Type";
    public static final String PIECE_COLOUR = "Colour";
    public static final String PIECE_POSITION = "Position";

    public static final String PIECE_CONFIGURATION = "Piece Configuration";
    public static final String PIECE_CONFIGURATION_STARTING_POSITION = "StartingPosition";
    public static final String PIECE_CONFIGURATION_NAME = "Name";
    public static final String PIECE_CONFIGURATION_PERSONALITY = "Personality";

    // Predicates
    public static final String CAN_CAPTURE = "Can Capture";
    public static final String CAN_CAPTURE_ATTACKER = "Attacker";
    public static final String CAN_CAPTURE_VICTIM = "Victim";

    public static final String IS_CAPTURED = "Is Captured";
    public static final String IS_CAPTURED_PIECE = "Piece";

    public static final String BEING_CREATED = "Being Created";
    public static final String BEING_CREATED_GAME = "Game";

    public static final String IS_READY = "Is Ready";
    public static final String IS_READY_GAME = "Game";

    public static final String IS_COLOUR = "Is Colour";
    public static final String IS_COLOUR_PIECE = "Piece";
    public static final String IS_COLOUR_COLOUR = "Colour";

    public static final String MOVE_MADE = "Move Made";
    public static final String MOVE_MADE_MOVE = "Move";

    public static final String IS_VALID_MOVE = "Is Valid Move";
    public static final String IS_VALID_MOVE_MOVE = "Move";

    public static final String IS_SPEAKER = "Is Speaker";
    public static final String IS_SPEAKER_AGENT = "Agent";

    public static final String SAID_TO = "Said To";
    public static final String SAID_TO_SPEAKER = "Speaker";
    public static final String SAID_TO_PHRASE = "Phrase";

    // Actions
    public static final String MAKE_MOVE = "Make Move";
    public static final String MAKE_MOVE_MOVE = "Move";

    public static final String CREATE_GAME = "Create Game";
    public static final String CREATE_GAME_GAME = "Game";
    public static final String CREATE_GAME_PIECE_CONFIGURATIONS = "PieceConfigurations";

    public static final String BECOME_SPEAKER = "Become Speaker";
    public static final String BECOME_SPEAKER_AGENT = "Agent";

    private static final ChessOntology instance = new ChessOntology();

    private ChessOntology() {
        super(ONTOLOGY_NAME, BasicOntology.getInstance());

        try {
            // Concepts
            final var colourSchema = new ConceptSchema(COLOUR);
            colourSchema.add(COLOUR_COLOUR, (PrimitiveSchema) getSchema(BasicOntology.STRING));
            add(colourSchema, Colour.class);

            final var positionSchema = new ConceptSchema(POSITION);
            positionSchema.add(POSITION_COORDINATES, (PrimitiveSchema) getSchema(BasicOntology.STRING));
            add(positionSchema, Position.class);

            final var pieceSchema = new ConceptSchema(PIECE);
            // not all pieces will have an agent so this field may not be present
            pieceSchema.add(PIECE_AGENT, (ConceptSchema) getSchema(BasicOntology.AID), ObjectSchema.OPTIONAL);
            pieceSchema.add(PIECE_TYPE, (PrimitiveSchema) getSchema(BasicOntology.STRING));
            pieceSchema.add(PIECE_COLOUR, (ConceptSchema) getSchema(COLOUR));
            pieceSchema.add(PIECE_POSITION, (ConceptSchema) getSchema(POSITION));
            add(pieceSchema, ChessPiece.class);

            final var moveSchema = new ConceptSchema(MOVE);
            moveSchema.add(MOVE_SOURCE, (ConceptSchema) getSchema(POSITION));
            moveSchema.add(MOVE_TARGET, (ConceptSchema) getSchema(POSITION));
            add(moveSchema, PieceMove.class);

            final var gameSchema = new ConceptSchema(GAME);
            gameSchema.add(GAME_ID, (PrimitiveSchema) getSchema(BasicOntology.INTEGER));
            add(gameSchema, Game.class);

            final var pieceConfigurationSchema = new ConceptSchema(PIECE_CONFIGURATION);
            pieceConfigurationSchema.add(PIECE_CONFIGURATION_NAME, (PrimitiveSchema) getSchema(BasicOntology.STRING));
            pieceConfigurationSchema.add(PIECE_CONFIGURATION_PERSONALITY, (PrimitiveSchema) getSchema(BasicOntology.STRING));
            pieceConfigurationSchema.add(PIECE_CONFIGURATION_STARTING_POSITION, (PrimitiveSchema) getSchema(BasicOntology.STRING));
            add(pieceConfigurationSchema, PieceConfiguration.class);

            // Predicates
            final var canCaptureSchema = new PredicateSchema(CAN_CAPTURE);
            canCaptureSchema.add(CAN_CAPTURE_ATTACKER, getSchema(PIECE));
            canCaptureSchema.add(CAN_CAPTURE_VICTIM, getSchema(PIECE));
            add(canCaptureSchema, CanCapture.class);

            final var isCapturedSchema = new PredicateSchema(IS_CAPTURED);
            isCapturedSchema.add(IS_CAPTURED_PIECE, getSchema(PIECE));
            add(isCapturedSchema, IsCaptured.class);

            final var beingCreatedSchema = new PredicateSchema(BEING_CREATED);
            beingCreatedSchema.add(BEING_CREATED_GAME, getSchema(GAME));
            add(beingCreatedSchema, BeingCreated.class);

            final var isReadySchema = new PredicateSchema(IS_READY);
            isReadySchema.add(IS_READY_GAME, getSchema(GAME));
            add(isReadySchema, IsReady.class);

            final var isColourSchema = new PredicateSchema(IS_COLOUR);
            isColourSchema.add(IS_COLOUR_COLOUR, getSchema(COLOUR));
            isColourSchema.add(IS_COLOUR_PIECE, getSchema(PIECE));
            add(isColourSchema, IsColour.class);

            final var moveMadeSchema = new PredicateSchema(MOVE_MADE);
            moveMadeSchema.add(MOVE_MADE_MOVE, getSchema(MOVE));
            add(moveMadeSchema, MoveMade.class);

            final var isValidMoveSchema = new PredicateSchema(IS_VALID_MOVE);
            isValidMoveSchema.add(IS_VALID_MOVE_MOVE, getSchema(MOVE));
            add(isValidMoveSchema, IsValidMove.class);

            final var isSpeakerSchema = new PredicateSchema(IS_SPEAKER);
            isSpeakerSchema.add(IS_SPEAKER_AGENT, getSchema(BasicOntology.AID));
            add(isSpeakerSchema);

            final var saidToSchema = new PredicateSchema(SAID_TO);
            saidToSchema.add(SAID_TO_SPEAKER, getSchema(BasicOntology.AID));
            saidToSchema.add(SAID_TO_PHRASE, getSchema(BasicOntology.STRING));
            add(saidToSchema, SaidTo.class);

            // Actions
            final var makeMoveSchema = new AgentActionSchema(MAKE_MOVE);
            makeMoveSchema.add(MAKE_MOVE_MOVE, (ConceptSchema) getSchema(MOVE));
            add(makeMoveSchema, MakeMove.class);

            final var pieceConfigsSchema = new AggregateSchema(BasicOntology.SEQUENCE, (ConceptSchema) getSchema(PIECE_CONFIGURATION));
            final var createGameSchema = new AgentActionSchema(CREATE_GAME);
            createGameSchema.add(CREATE_GAME_GAME, (ConceptSchema) getSchema(GAME));
            createGameSchema.add(CREATE_GAME_PIECE_CONFIGURATIONS, pieceConfigsSchema);
            add(createGameSchema, CreateGame.class);

            final var becomeSpeakerSchema = new AgentActionSchema(BECOME_SPEAKER);
            becomeSpeakerSchema.add(BECOME_SPEAKER_AGENT, (ConceptSchema) getSchema(BasicOntology.AID));
            add(becomeSpeakerSchema, BecomeSpeaker.class);


        } catch (OntologyException e) {
            e.printStackTrace();
        }
    }

    public static ChessOntology getInstance() {
        return instance;
    }


}
